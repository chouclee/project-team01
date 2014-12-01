package edu.cmu.lti.f14.project.annotator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.TOP;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import com.aliasi.spell.JaccardDistance;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.TokenizerFactory;

import edu.cmu.lti.oaqa.bio.bioasq.services.GoPubMedService;
import edu.cmu.lti.oaqa.type.retrieval.AtomicQueryConcept;
import edu.cmu.lti.oaqa.type.retrieval.ComplexQueryConcept;
import edu.cmu.lti.oaqa.type.retrieval.Document;

public class rankAjustment extends JCasAnnotator_ImplBase {
	TokenizerFactory tokenizerFactory;
    JaccardDistance jaccard;

    public void initialize(UimaContext aContext) throws ResourceInitializationException {
        super.initialize(aContext);
       tokenizerFactory = IndoEuropeanTokenizerFactory.INSTANCE;
       jaccard = new JaccardDistance(tokenizerFactory);
    }
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		// TODO Auto-generated method stub
		 FSIterator<TOP> docIter = aJCas.getJFSIndexRepository().getAllIndexedFS(Document.type);
		 FSIterator<TOP> queryIter = aJCas.getJFSIndexRepository().getAllIndexedFS(AtomicQueryConcept.type);
		 AtomicQueryConcept query = (AtomicQueryConcept) queryIter.next();
		 String refinedQuery =  query.getText();
		 ArrayList<Document> docList = new  ArrayList<Document>();
		 while(docIter.hasNext()){
			 Document singleDoc = (Document) docIter.next();
		//	 if (singleDoc != null){
				 singleDoc.setScore(jaccard.proximity(refinedQuery, singleDoc.getText()));
				 docList.add(singleDoc);
		//	 }
		 }
		 Collections.sort(docList, new docSimilarityComparator());
		 int rank = 0;
		 for (Document doc : docList){
			 doc.setScore(rank++);
		 }
		 
	}
	
	class docSimilarityComparator implements Comparator<Document>{

		@Override
		public int compare(Document doc1, Document doc2) {
			// TODO Auto-generated method stub
			 if (doc1.getScore() < doc2.getScore()){
				 return -1;
			 }
			 else if(doc1.getScore() > doc2.getScore()){
				 return 1;
			 }
			 else{
				 return 0;
			 }
		} 
		
	}

}
