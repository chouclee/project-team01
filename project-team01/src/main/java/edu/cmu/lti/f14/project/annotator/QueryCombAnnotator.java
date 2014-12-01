package edu.cmu.lti.f14.project.annotator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.TOP;

import util.UmlsService;
import util.Utils;

import com.google.common.collect.Lists;

import edu.cmu.lti.oaqa.type.retrieval.AtomicQueryConcept;
import edu.cmu.lti.oaqa.type.retrieval.ComplexQueryConcept;
import edu.cmu.lti.oaqa.type.retrieval.QueryOperator;

/**
 * Add query operator, expand atomic query into complex query.
 * @author smart moon
 *
 */
public class QueryCombAnnotator extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		// TODO Auto-generated method stub
		FSIterator<TOP> it = aJCas.getJFSIndexRepository().getAllIndexedFS(AtomicQueryConcept.type);

	    // Put the atomic queries in a list.
	    ArrayList<AtomicQueryConcept> terms = new ArrayList<AtomicQueryConcept>();

	    // Generate a query operator.
	    QueryOperator op = new QueryOperator(aJCas);
	    op.setName("AND");

	    // Create the whole query strings
//	    String wholeWithOp = "";
//	    String wholeWithoutOp = "";

	    while (it.hasNext()) {
	      AtomicQueryConcept term = (AtomicQueryConcept) it.next();
	      terms.add(term);
//	      System.out.println("term:"+term);
	    }
	    // Create the complex query.
	    ComplexQueryConcept query = new ComplexQueryConcept(aJCas);
	    query.setOperatorArgs(Utils.fromCollectionToFSList(aJCas, terms));
	    query.setOperator(op);
//	    System.err.println(wholeWithOp);
//	    System.err.println(wholeWithoutOp);
//	    query.setWholeQueryWithOp(wholeWithOp);
//	    query.setWholeQueryWithoutOp(wholeWithoutOp);
	    query.addToIndexes();
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
	 private String queryExpand(String originalQuery) {
		    UmlsService umlsService = UmlsService.getInstance();
		    String[] strs=originalQuery.split(" ");
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

		  /**
		   * Build NGrams from Unigram string.
		   *
		   * @param s Original text
		   * @param N Number of consecutive words in a gram
		   * @return A list of N-Grams
		   */
		  private List<String> buildGrams(String s, int N) {
		    List<String> ngrams = Lists.newArrayList();
		    String grams[] = s.split(" ");
		    for (int n = 0; n <= N - 1; ++n) {
		      for (int i = 0; i < grams.length - n; ++i) {
		        StringBuilder gramBuilder = new StringBuilder(grams[i]);
		        for (int j = i; j < i + n; ++j) {
		          gramBuilder.append(" ").append(grams[j + 1]);
		        }
		        String gram = gramBuilder.toString();
		        if (gram.isEmpty() || gram.charAt(0) == ' ')
		          continue;
		        ngrams.add(gram);
		      }
		    }
		    return ngrams;
		  }

}
