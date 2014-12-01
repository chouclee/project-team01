package edu.cmu.lti.f14.project.annotator;

import java.awt.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.jcas.cas.TOP;
import org.apache.uima.resource.ResourceInitializationException;

import util.TypeFactory;
import util.Utils;
import edu.cmu.lti.oaqa.bio.bioasq.services.GoPubMedService;
import edu.cmu.lti.oaqa.bio.bioasq.services.LinkedLifeDataServiceResponse;
import edu.cmu.lti.oaqa.type.kb.Triple;
import edu.cmu.lti.oaqa.type.retrieval.AtomicQueryConcept;
import edu.cmu.lti.oaqa.type.retrieval.ComplexQueryConcept;
import edu.cmu.lti.oaqa.type.retrieval.TripleSearchResult;


/**
 * Retrieve Triples
 * @author xzhan
 *
 */
public class QueryTripleAnnotator extends JCasAnnotator_ImplBase {
  GoPubMedService goService;
    
    /**
     * Initialize the PubMedService
     * 
     * @param UimaContext
     * 
     */
    public void initialize(UimaContext aContext) throws ResourceInitializationException {
      super.initialize(aContext);
      
      try {
        goService = new GoPubMedService("project.properties");
      } catch (ConfigurationException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();     
      }
      
      
  }
    /**
     * LinkedLifeDataServiceResponse
     * to return triple result
     * 
     */
    @Override
    public void process(JCas aJCas) throws AnalysisEngineProcessException {
      // TODO Auto-generated method stub
      FSIterator<TOP> iter = aJCas.getJFSIndexRepository().getAllIndexedFS(ComplexQueryConcept.type);
      while(iter.hasNext()){
        String result = "";
        ComplexQueryConcept cqc = (ComplexQueryConcept) iter.next();
        FSList fslist = cqc.getOperatorArgs();
        ArrayList<AtomicQueryConcept> arraylist = Utils.fromFSListToCollection(fslist, AtomicQueryConcept.class);
        
        // content in the query of each AtomicQuery
        String queryText = arraylist.get(0).getText();
      
        // This one can return document type answer
        LinkedLifeDataServiceResponse.Result linkedLifeDataResult = null;
        try {
          linkedLifeDataResult = goService
                .findLinkedLifeDataEntitiesPaged(queryText, 0);
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        // System.out.println("LinkedLifeData: " + linkedLifeDataResult.getEntities().size());
        // rank
        int rank = 0;
        if (linkedLifeDataResult.getEntities() == null){
        	continue;
        }
        for (LinkedLifeDataServiceResponse.Entity entity : linkedLifeDataResult.getEntities()) {
          for (LinkedLifeDataServiceResponse.Relation relation : entity.getRelations()) {
            String pred = relation.getPred();
            String sub = relation.getSubj();
            String obj = relation.getObj();
            
            Triple triple = TypeFactory.createTriple(aJCas, sub, pred, obj);
            
            triple.addToIndexes();
            // triple's super type
            TripleSearchResult tripleSearchResult = new TripleSearchResult(aJCas);
            tripleSearchResult.setTriple(triple);
            tripleSearchResult.setRank(rank);
            tripleSearchResult.setQueryString(queryText);
            tripleSearchResult.addToIndexes();
            
          }
        }
      }
    }
}