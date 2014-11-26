package edu.cmu.lti.f14.project.annotator;

import java.util.Map;
import java.util.TreeMap;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import edu.cmu.lti.oaqa.type.input.Question;
import edu.cmu.lti.oaqa.type.retrieval.AtomicQueryConcept;
import util.PosTagNamedEntityRecognizer;

public class PosQueryAnnotator extends JCasAnnotator_ImplBase {
	private PosTagNamedEntityRecognizer mRecognizer;

	/**
	 * Initialize a POS tag named entity recognizer.
	 * 
	 * @see org.apache.uima.analysis_component.AnalysisComponent_ImplBase#initialize(UimaContext)
	 */
	@Override
	public void initialize(UimaContext aContext)
			throws ResourceInitializationException {
		try {
			mRecognizer = new PosTagNamedEntityRecognizer();
		} catch (ResourceInitializationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		// TODO Auto-generated method stub
		FSIterator<Annotation> iter = aJCas.getAnnotationIndex(Question.type)
				.iterator();

		if (iter.isValid() && iter.hasNext()) {
			// Get the question first
			Question question = (Question) iter.next();
			String queString = question.getText();
			TreeMap<Integer, Integer> begin2end = (TreeMap<Integer, Integer>) mRecognizer
					.getGeneSpans(queString);
			 AtomicQueryConcept ato = new AtomicQueryConcept(aJCas);
			 String ret="";
			for (Map.Entry<Integer, Integer> entry : begin2end.entrySet()) {
				// Create an atomic query first
			        String text=queString.substring(entry.getKey(), entry.getValue());
			        ret=ret+text+" AND ";
			}
			ato.setText(ret.substring(0, ret.length()-5));
			System.err.println("text:"+ret.substring(0, ret.length()-5));
	        ato.addToIndexes();
		}
	}

}
