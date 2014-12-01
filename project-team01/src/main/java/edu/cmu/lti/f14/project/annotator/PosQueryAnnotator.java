package edu.cmu.lti.f14.project.annotator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

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
import util.UmlsService;

public class PosQueryAnnotator extends JCasAnnotator_ImplBase {
	private PosTagNamedEntityRecognizer mRecognizer;

	/**
	 * Initialize a POS tag named entity recognizer.
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
			 String operator=" ";
			for (Map.Entry<Integer, Integer> entry : begin2end.entrySet()) {
				// Create an atomic query first
			        String text=queString.substring(entry.getKey(), entry.getValue());
			        ret=ret+text+operator;
			}
			String sym=ret.substring(0,ret.length()-operator.length());
			ato.setText(sym);
			System.err.println("text:"+sym);
	        ato.addToIndexes();
		}
	}
	private String getSym(String mQuery){
		UmlsService umlsService = UmlsService.getInstance();
	    String[] strs=mQuery.split(" ");
	    List<String> nouns =new ArrayList<String>();
	    for(String s:strs){
	    	nouns.add(s);
	    }
	    String synonyms = nouns
	            .stream()
	            .map(s1 -> umlsService
	                            .getSynonyms(s1)
	                            .stream()
	                            .limit(5)
	                            .map(i -> '"' + i + '"')
	                            .collect(Collectors.joining(" OR ")))
	            .map(s2 -> '(' + s2 + ')')
	            .collect(Collectors.joining(" AND "));
		return synonyms;
	}

}
