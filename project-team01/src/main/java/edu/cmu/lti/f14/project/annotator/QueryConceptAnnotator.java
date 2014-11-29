package edu.cmu.lti.f14.project.annotator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.http.client.ClientProtocolException;
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
import util.ConcpetWebService;
import edu.cmu.lti.oaqa.bio.bioasq.services.OntologyServiceResponse.*;

import java.util.List;

import edu.cmu.lti.oaqa.bio.bioasq.services.GoPubMedService;
import edu.cmu.lti.oaqa.bio.bioasq.services.OntologyServiceResponse;
import edu.cmu.lti.oaqa.type.kb.Concept;
import edu.cmu.lti.oaqa.type.retrieval.AtomicQueryConcept;
import edu.cmu.lti.oaqa.type.retrieval.ComplexQueryConcept;
import edu.cmu.lti.oaqa.type.retrieval.ConceptSearchResult;

public class QueryConceptAnnotator extends JCasAnnotator_ImplBase {

	GoPubMedService goService;

	/**
	 * Initialize the PubMedService
	 * 
	 * @param UimaContext
	 * 
	 */
	public void initialize(UimaContext aContext)
			throws ResourceInitializationException {
		super.initialize(aContext);

		try {
			goService = new GoPubMedService("project.properties");
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * use service to get answers
	 * 
	 * @see org.apache.uima.analysis_component.JCasAnnotator_ImplBase#process(org.apache.uima.jcas.JCas)
	 */
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		// TODO Auto-generated method stub
		FSIterator<TOP> iter = aJCas.getJFSIndexRepository().getAllIndexedFS(
				ComplexQueryConcept.type);
		while (iter.hasNext()) {
			// String result = "";
			ComplexQueryConcept cqc = (ComplexQueryConcept) iter.next();
			FSList fslist = cqc.getOperatorArgs();
			ArrayList<AtomicQueryConcept> arraylist = Utils
					.fromFSListToCollection(fslist, AtomicQueryConcept.class);

			// content in the query of each AtomicQuery
			String queryText = arraylist.get(0).getText();
//			OntologyServiceResponse.Result uniprotResult = null;
			List<Finding> result = null;
//			try {
//				uniprotResult = goService
//						.findJochemEntitiesPaged(queryText, 0);//change to mesh
//			} catch (ClientProtocolException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			result = ConcpetWebService.getConceptWebService(goService, queryText);
//			 OntologyServiceResponse.Result meshresult = null;
//			 try {
//			 meshresult = goService.findMeshEntitiesPaged(queryText, 0);
//			 } catch (IOException e) {
//			 // TODO Auto-generated catch block
//			 e.printStackTrace();
//			 }
			// store the rank of each answer
			int rank = 0;
			// store the concept name
			String conceptLabel = "";
			if (result != null) {
				for (OntologyServiceResponse.Finding finding : result) {
					// if (uniprotResult != null) {
					// System.out.println("meshresult not null!");
					// for (OntologyServiceResponse.Finding finding : meshresult
					// .getFindings()) {
					conceptLabel = finding.getConcept().getLabel();
					Concept concept = new Concept(aJCas);
					concept.setName(conceptLabel);
					concept.addToIndexes();
					ConceptSearchResult conceptSearchResult = new ConceptSearchResult(aJCas);
					conceptSearchResult.setConcept(concept);
					conceptSearchResult.setText(finding.getConcept().getLabel());
//					System.err.println(finding.getConcept().getLabel());
					conceptSearchResult.setUri(finding.getConcept().getUri());
					conceptSearchResult.setScore(finding.getScore());
					conceptSearchResult.setRank(rank);
					conceptSearchResult.setQueryString(queryText);
					conceptSearchResult.addToIndexes();
					rank++;
				}
			}
		}

	}

}
