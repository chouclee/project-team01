package edu.cmu.lti.f14.project.annotator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.jcas.cas.TOP;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import util.Utils;
import util.TypeFactory;
import edu.cmu.lti.f14.project.docstore.CollectionStat;
import edu.cmu.lti.oaqa.bio.bioasq.services.GoPubMedService;
import edu.cmu.lti.oaqa.bio.bioasq.services.PubMedSearchServiceResponse;
import edu.cmu.lti.oaqa.type.answer.CandidateAnswerVariant;
import edu.cmu.lti.oaqa.type.retrieval.AtomicQueryConcept;
import edu.cmu.lti.oaqa.type.retrieval.ComplexQueryConcept;
import edu.cmu.lti.oaqa.type.retrieval.Document;

/**
 * Retrieve documents
 * @author xzhan
 *
 */
public class QueryDocAnnotator extends JCasAnnotator_ImplBase {
  GoPubMedService goService;
  
  public static CollectionStat stat;
  /**
   * Initialize the PubMedService
   * 
   * @param UimaContext
   * 
   */
  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    super.initialize(aContext);
    stat = new CollectionStat();
    try {
      goService = new GoPubMedService("project.properties");
    } catch (ConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();     
    }
    
    
  }

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    FSIterator<TOP> iter = aJCas.getJFSIndexRepository().getAllIndexedFS(ComplexQueryConcept.type);
    while(iter.hasNext()){
      ComplexQueryConcept cqc = (ComplexQueryConcept) iter.next();
      FSList fslist = cqc.getOperatorArgs();
      ArrayList<AtomicQueryConcept> arraylist = Utils.fromFSListToCollection(fslist, AtomicQueryConcept.class);
      
      // content in the query of each AtomicQuery
      String queryText = arraylist.get(0).getText();
    System.err.println("**"+arraylist.get(0).getText());
    
      // This one can return document type answer
      PubMedSearchServiceResponse.Result pubmedResult = null;
      try {
        pubmedResult = goService.findPubMedCitations(queryText, 0);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        System.out.println("PubMedResult exception!");
      }
      
      // return a list of documents
      if (pubmedResult != null) {
        List<PubMedSearchServiceResponse.Document> docList = pubmedResult.getDocuments();
        // rank
        int rank = 0;
        // iterate through the whole documents
        for(PubMedSearchServiceResponse.Document doc : docList){  
          // docid
          String docID = doc.getPmid();
          // docURI
          String uri = "http://www.ncbi.nlm.nih.gov/pubmed/" + docID;
          // Title
          String title = doc.getTitle();
          
          String abs = doc.getDocumentAbstract();
          // new a document 
          Document document = TypeFactory.createDocument(aJCas, uri, 0.0, abs, rank, queryText, "", 
                  new ArrayList<CandidateAnswerVariant>(), title, docID);
          
          stat.addDoc(document);
          
          document.addToIndexes();
          
          rank++;
          
        }
      }  
    }
  }
}