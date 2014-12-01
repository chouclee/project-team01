package edu.cmu.lti.f14.project.annotator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.TOP;
import org.apache.uima.resource.ResourceInitializationException;

import util.TypeFactory;

import com.google.common.collect.Lists;

import edu.cmu.lti.f14.project.reader.QuestionReader;
import edu.cmu.lti.oaqa.type.answer.Answer;
import edu.cmu.lti.oaqa.type.retrieval.AtomicQueryConcept;
import edu.cmu.lti.oaqa.type.retrieval.Passage;

/**
 * Extract yes or no exact answer from snippets.
 * @author zhouchel
 *
 */
public class AnswerExtractor extends JCasAnnotator_ImplBase {
  private static BufferedReader in = null;
  private HashMap<String, Integer> dic;

  @Override
  /**
   * Provides access to external resources (other than the CAS)<br>
   * Load parameters configuration from file paramConfig
   * 
   * @param aContext
   *          provides UIMA resources with all access to external resources (other than the CAS)
   *          
   * @see AnalysisComponent#initialize(UimaContext)
   */
  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    super.initialize(aContext);
    String mFilePath = ((String) aContext.getConfigParameterValue("dictionary")).trim();
    InputStream is = QuestionReader.class.getClassLoader().getResourceAsStream(mFilePath);
    dic = new HashMap<String, Integer>();
    try {
      in = new BufferedReader(new InputStreamReader(is, "utf-8"));
      String strs=null;
      while((strs=in.readLine())!=null){
        String[] str = strs.split("\\s+");
        dic.put(str[0], Integer.parseInt(str[1]));
      }
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub

    FSIterator<TOP> iter = aJCas.getJFSIndexRepository().getAllIndexedFS(AtomicQueryConcept.type);

    while (iter.isValid() && iter.hasNext()) {
      AtomicQueryConcept atomicQuestion = (AtomicQueryConcept) iter.next();
      // text has been add AND
      String query = atomicQuestion.getText();

      // if query is null
      if (query == null || query.isEmpty()) {
        continue;
      }

      List<String> nes = Lists.newArrayList();

      // select entities from query
      List<String> selectedNEs = selectEntities(nes, atomicQuestion);

      // iterating between passage
      int v = 0;
      FSIterator<TOP> pIter = aJCas.getJFSIndexRepository().getAllIndexedFS(Passage.type);
      ArrayList<Double> weight = new ArrayList<>();
      while (pIter.hasNext() && ++v < 5) {
        //double score = 0.0;
        Passage p = (Passage) pIter.next();
        String text = p.getText();
        String[] terms = text.split("\\s+");
        Integer w = null;
        Double negOrPos = 0.0;
        System.out.println(text);
        for (String term : terms) {
          w = dic.get(term);
          if (w != null) {
            negOrPos += w;
            //System.err.println("Positive/Negative: " + w);
          }
        }
        String sections = p.getBeginSection();
        if (sections.equals("title"))
          negOrPos += 1;
        else {
          String[] section = sections.split("\\.");
          if ((section[1]).equals("0"))
            negOrPos += 2.0;
          else
            negOrPos += 0.0;
        }

        negOrPos += p.getScore() * 5;
        //score += 1.0 / p.getRank();
        weight.add(negOrPos);
        System.out.println(negOrPos);
      }
      int yes = 0, no = 0;
      double threshold = 5.5;
      for (Double w : weight) {
        if (w > threshold) {
          yes++;
        }
        else 
          no++;
      }
      String ans;
      if (yes > no)
        ans = "yes";
      else
        ans = "no";   
      /*
       * for (FeatureStructure fs : aJCas.getAnnotationIndex(Passage.type)) { Passage passage =
       * (Passage) fs; String text = passage.getText(); // extract answer System.out.println(text);
       * if (++v >= 10) break; }
       */

      // evaluate

      // create Answer
      Answer answer = TypeFactory.createAnswer(aJCas, ans);
      answer.addToIndexes();
      System.out.println(ans);
    }

  }

  private List<String> selectEntities(List<String> nes, AtomicQueryConcept atomicQuestion) {
    // TODO Auto-generated method stub
    return null;
  }

}
