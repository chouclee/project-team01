package edu.cmu.lti.f14.project.annotator;

import java.util.ArrayList;
import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.TOP;
import org.apache.uima.resource.ResourceInitializationException;

import util.TypeFactory;

import com.google.common.collect.Lists;

import edu.cmu.lti.oaqa.type.retrieval.AtomicQueryConcept;
import edu.cmu.lti.oaqa.type.retrieval.ConceptSearchResult;
import edu.cmu.lti.oaqa.type.retrieval.Passage;

public class AnswerExtractor extends JCasAnnotator_ImplBase {

  
  @Override
  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    super.initialize(aContext);
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
      if(query == null || query.isEmpty()){
        continue;
      }
      
      List<String> nes = Lists.newArrayList();
      


      // select entities from query
      List<String> selectedNEs = selectEntities(nes, atomicQuestion);
      

      
      // iterating between passage
      int v = 0;
      FSIterator<TOP> pIter = aJCas.getJFSIndexRepository().getAllIndexedFS(Passage.type);
      ArrayList<Double> weight = new ArrayList<>();
      while(pIter.hasNext() && ++v < 10){
        double score = 0.0;
        Passage p = (Passage) pIter.next();
        String sections = p.getBeginSection();
        if (sections.equals("title"))
          score += 0.5;
        else {
          String[] section = sections.split("\\.");
          if ((section[1]).equals("0"))
             score += 1.0;
          else score+= 0.2;
        }
        
        score += p.getScore() * 2;
        score += 1.0 / p.getRank();
        weight.add(score);
        System.out.println(p.getText());
        System.out.println(score);
      }
   /*   for (FeatureStructure fs : aJCas.getAnnotationIndex(Passage.type)) {
        Passage passage = (Passage) fs;
        String text = passage.getText();
        // extract answer
        System.out.println(text);
        if (++v >= 10)
          break;
      }*/
      
      
      // evaluate 
      
      // create Answer
      //TypeFactory.createAnswer(aJCas, selectedNEs);
      
      
      
    }
    

    
  }

  private List<String> selectEntities(List<String> nes, AtomicQueryConcept atomicQuestion) {
    // TODO Auto-generated method stub
    return null;
  }

}
