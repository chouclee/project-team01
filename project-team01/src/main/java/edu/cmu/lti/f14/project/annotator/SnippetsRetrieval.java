package edu.cmu.lti.f14.project.annotator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.uima.UIMARuntimeException;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.jcas.cas.TOP;
import org.apache.uima.resource.ResourceInitializationException;

import util.Utils;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunking;
import com.aliasi.sentences.MedlineSentenceModel;
import com.aliasi.sentences.SentenceChunker;
import com.aliasi.sentences.SentenceModel;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.TokenizerFactory;

import edu.cmu.lti.f14.project.docstore.CollectionStat;
import edu.cmu.lti.oaqa.type.retrieval.AtomicQueryConcept;
import edu.cmu.lti.oaqa.type.retrieval.ComplexQueryConcept;
import edu.cmu.lti.oaqa.type.retrieval.Document;
import edu.cmu.lti.oaqa.type.retrieval.Passage;

/**
 * Retrieve snippt from documents
 * @author zhouchel
 *
 */
public class SnippetsRetrieval extends JCasAnnotator_ImplBase {

  private static final TokenizerFactory TOKENIZER_FACTORY = IndoEuropeanTokenizerFactory.INSTANCE;

  private static final SentenceModel SENTENCE_MODEL = new MedlineSentenceModel();

  private static final SentenceChunker SENTENCE_CHUNKER = new SentenceChunker(TOKENIZER_FACTORY,
          SENTENCE_MODEL);

  @Override
  public void initialize(org.apache.uima.UimaContext aContext)
          throws ResourceInitializationException {
    super.initialize(aContext);
  }

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    FSIterator<TOP> DocIter = aJCas.getJFSIndexRepository().getAllIndexedFS(Document.type);
    FSIterator<TOP> queryIter = aJCas.getJFSIndexRepository().getAllIndexedFS(
            ComplexQueryConcept.type);

    ComplexQueryConcept query = (ComplexQueryConcept) queryIter.next();
    FSList fslist = query.getOperatorArgs();
    ArrayList<AtomicQueryConcept> arraylist = Utils
      .fromFSListToCollection(fslist, AtomicQueryConcept.class);

    // content in the query of each AtomicQuery
    String queryWOOp = arraylist.get(0).getText();


    List<Document> docList = new ArrayList<Document>();
    while (DocIter.hasNext()) {
      Document doc = (Document) DocIter.next();
      if (doc.getText() == null || doc.getText().trim().length() == 0) {
        continue;
      }

      docList.add(doc);
    }
    List<Passage> res = getPassageList(docList, aJCas);
    for (Passage p : res) {
      p.setScore(edu.cmu.lti.f14.project.docstore.Similartiy.consineSim(queryWOOp, p.getText()));
    }
    Collections.sort(res, new SnippetSimComparator());
    int v= 1;
    for (Passage p : res) {
      p.setRank(v++);
      p.addToIndexes();
    }
    if (res.size()!=0)
      System.out.println(res.get(0).getText());
  }


  private List<Passage> getPassageList(List<Document> docs, JCas jcas) {
    List<Passage> res = new ArrayList<Passage>();
    for (Document doc : docs) {
      // create a passage based on the title of doc
      Passage p = new Passage(jcas);
      p.setBeginSection("title");
      p.setEndSection("title");
      p.setTitle(doc.getTitle());
      p.setDocId(doc.getDocId());
      p.setUri(doc.getUri());
      p.setOffsetInBeginSection(0);
      p.setOffsetInEndSection(doc.getTitle().length());
      p.setText(doc.getTitle());
      res.add(p);
      // deal with sentence in the body/abstract
      String[] sections = doc.getText().split("\n");
      for (int i = 0; i < sections.length; ++i) {
        String text = sections[i];
        Chunking chunking = SENTENCE_CHUNKER.chunk(text.toCharArray(), 0, text.length());
        Set<Chunk> sentences = chunking.chunkSet();
        String slice = chunking.charSequence().toString();
        for (Chunk sentence : sentences) {
          int start = sentence.start();
          int end = sentence.end();
          p = new Passage(jcas);
          p.setBeginSection("sections." + i);
          p.setEndSection("sections." + i);
          p.setTitle(doc.getTitle());
          p.setDocId(doc.getDocId());
          p.setUri(doc.getUri());
          p.setOffsetInBeginSection(start);
          p.setOffsetInEndSection(end);
          p.setText(slice.substring(start, end));
          res.add(p);
        }
      }
    }
    return res;
  }
  
  class SnippetSimComparator implements Comparator<Passage> {
    @Override
    public int compare(Passage lhs, Passage  rhs) {
      if (lhs.getScore() < rhs.getScore()) {
        return 1;
      } else if (lhs.getScore() > rhs.getScore()) {
        return -1;
      } else {
        return lhs.getRank() - rhs.getRank();
      }
    }
  }
}
