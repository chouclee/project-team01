package edu.cmu.lti.f14.project.annotator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.jcas.cas.TOP;
import org.apache.uima.resource.ResourceInitializationException;

import util.Utils;
import com.aliasi.tokenizer.TokenizerFactory;

import edu.cmu.lti.f14.project.docstore.CollectionStat;
import edu.cmu.lti.oaqa.type.retrieval.AtomicQueryConcept;
import edu.cmu.lti.oaqa.type.retrieval.ComplexQueryConcept;
import edu.cmu.lti.oaqa.type.retrieval.Document;

/**
 * Use BM25 retrieval algorithm to re-rank all retrieved documents.
 * @author zhouchel
 *
 */
public class DocRerank extends JCasAnnotator_ImplBase {
  
  private CollectionStat stat;

  /**
   * Perform initialization logic. Initialize the service.
   * 
   * @param aContext
   *          the UimaContext object
   */
  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    super.initialize(aContext);
    //this.stat = QueryDocAnnotator.stat;
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

    //TfIdfDistance tfIdf = new TfIdfDistance(REFINED_TKFACTORY);
    //tfIdf.handle(queryWOOp);

    List<Document> docList = new ArrayList<Document>();
    while (DocIter.hasNext()) {
      Document doc = (Document) DocIter.next();
      if (doc.getText() == null || doc.getText().trim().length() == 0) {
        continue;
      }

      //tfIdf.handle(doc.getText());

      docList.add(doc);
    }

    for (int i = 0; i < docList.size(); ++i) {
      Document doc = docList.get(i);
      //double sim = tfIdf.proximity(queryWOOp, docList.get(i).getText());
      double bm25 = edu.cmu.lti.f14.project.docstore.Similartiy.BM25(
              QueryDocAnnotator.stat, queryWOOp, Integer.parseInt(doc.getDocId()));
      doc.setScore(bm25);
    }

    Collections.sort(docList, new DocSimComparator());
    for (int i = 0; i < docList.size(); ++i) {
      docList.get(i).setRank(i);
    }
  }

  /**
   * Read file through stream LPDictExactNERAnnotator
   * 
   * @param filePath
   *          the file path
   * @return the string of the file
   * @throws ResourceInitializationException
   */
  private String getFileAsStream(String filePath) throws ResourceInitializationException {
    StringBuilder sb = new StringBuilder();
    try {
      InputStream is = DocRerank.class.getClassLoader().getResourceAsStream(filePath);

      BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));

      String line = br.readLine();
      while (line != null) {
        sb.append(line);
        sb.append("\n");
        line = br.readLine();
      }
      br.close();
    } catch (Exception ex) {
      System.out.println("[Error]: Look Below.");
      ex.printStackTrace();
      System.out.println("[Error]: Look Above.");
      throw new ResourceInitializationException();
    }

    String content = sb.toString();
    return content;
  }
  
  /**
   * Comparator for document similarity
   * @author zhouchel
   *
   */
  class DocSimComparator implements Comparator<Document> {
    @Override
    public int compare(Document lhs, Document rhs) {
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
