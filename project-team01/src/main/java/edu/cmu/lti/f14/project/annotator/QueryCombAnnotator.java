package edu.cmu.lti.f14.project.annotator;

import java.util.ArrayList;
import java.util.List;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.TOP;

import util.Utils;
import edu.cmu.lti.oaqa.type.retrieval.AtomicQueryConcept;
import edu.cmu.lti.oaqa.type.retrieval.ComplexQueryConcept;
import edu.cmu.lti.oaqa.type.retrieval.QueryOperator;

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

}
