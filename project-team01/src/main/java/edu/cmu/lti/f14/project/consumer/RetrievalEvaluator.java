/**
 * 
 * @author KangHuang, Chouchen Li 2014/11/11
 * 
 */
package edu.cmu.lti.f14.project.consumer;

import static java.util.stream.Collectors.toList;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import json.gson.RetrievalResult;
import json.gson.Snippet;
import json.gson.TestQuestion;
import json.gson.TestSet;
import json.gson.TestYesNoQuestion;
import json.gson.TestYesNoSet;
import json.gson.Triple;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.TOP;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.apache.uima.util.ProcessTrace;

import util.RetrievalMeasures;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.cmu.lti.oaqa.type.answer.Answer;
import edu.cmu.lti.oaqa.type.input.Question;
import edu.cmu.lti.oaqa.type.retrieval.ConceptSearchResult;
import edu.cmu.lti.oaqa.type.retrieval.Document;
import edu.cmu.lti.oaqa.type.retrieval.TripleSearchResult;

/**
 * Evaluate retrieved concepts, documents, triples. 
 * @author kangh
 *
 */
public class RetrievalEvaluator extends CasConsumer_ImplBase {

	/**
	 * Name of configuration parameter that be set to the path of output
	 * file(optional)
	 */
	public static final String PARAM_OUTPUT = "outputFile";
	List<Question> goldout;
	HashMap<String, TestYesNoQuestion> goldSet = new HashMap<String, TestYesNoQuestion>();
	String outputPath;
	List<Double[]> precision;
	List<Double[]> recall;
	List<Double[]> fmeasure;
	List<Double[]> avgPrecision;
	List<RetrievalResult> retrievalRes;
	int count = 0;
	ArrayList<ArrayList<String>> test = new ArrayList<ArrayList<String>>();

	/**
	 * Read goldAnswer into hashmap and initialize ArrayList storing metrics
	 */
	public void initialize() throws ResourceInitializationException {
		outputPath = (String) getUimaContext().getConfigParameterValue(
				PARAM_OUTPUT);
		if (outputPath == null) {
			throw new ResourceInitializationException(
					ResourceInitializationException.RESOURCE_DATA_NOT_VALID,
					new Object[] { PARAM_OUTPUT });
		}
		// String goldPath = "src/main/resources/data/BioASQ-SampleData1B.json";
		String goldPath = ((String) getUimaContext().getConfigParameterValue(
				"goldFile")).trim();
		List<TestYesNoQuestion> goldAnswer;
		goldAnswer = Lists.newArrayList();
		Object value = goldPath;

		/*
		 * if (goldPath != null && goldPath.length() != 0) { goldAnswer =
		 * TestSet.load(getClass().getResourceAsStream(goldPath)).stream()
		 * .collect(toList());
		 */
		if (String.class.isAssignableFrom(value.getClass())) {
			goldAnswer = TestYesNoSet
					.load(getClass().getResourceAsStream(
							String.class.cast(value))).stream()
					.collect(toList());
		} else if (String[].class.isAssignableFrom(value.getClass())) {
			goldAnswer = Arrays
					.stream(String[].class.cast(value))
					.flatMap(
							path -> TestYesNoSet.load(
									getClass().getResourceAsStream(path))
									.stream()).collect(toList());
		}
		// trim question texts
		goldAnswer
				.stream()
				.filter(input -> input.getBody() != null)
				.forEach(
						input -> input.setBody(input.getBody().trim()
								.replaceAll("\\s+", " ")));
		//System.out.println("concepts");
	//	System.out.println(goldAnswer.get(1).getConcepts());
		 for(TestYesNoQuestion q : goldAnswer){
			 goldSet.put(q.getId(), q);
		 }
		 precision = new ArrayList<Double[]>();
		 recall = new ArrayList<Double[]>();
		 fmeasure = new ArrayList<Double[]>();
		 retrievalRes = new ArrayList<RetrievalResult>();
		 avgPrecision = new ArrayList<Double[]>();
  }


	/**
	 * Fetch three items: concept, document and triples from correspond
	 * annotation. Also find gold answer with same queryId, Then use static
	 * methods in util.RetrievalMeasures.java to calculate metrics then store
	 * them into ArrayList respectively.
	 */
	// @Override
	public void processCas(CAS aCas) throws ResourceProcessException {

		JCas jcas;
		try {
			jcas = aCas.getJCas();
		} catch (CASException e) {
			throw new ResourceProcessException(e);
		}
		try {
			jcas = aCas.getJCas();
		} catch (CASException e) {
			throw new ResourceProcessException(e);
		}

		FSIndex<?> QuestionIndex = jcas.getAnnotationIndex(Question.type);
		Iterator<?> QuestionIter = QuestionIndex.iterator();
		Question question = (Question) QuestionIter.next();

		FSIterator<TOP> conceptIter = jcas.getJFSIndexRepository()
				.getAllIndexedFS(ConceptSearchResult.type);

		Map<Integer, String> conceptMap = new TreeMap<Integer, String>();
		while (conceptIter.hasNext()) {
			ConceptSearchResult cpt = (ConceptSearchResult) conceptIter.next();
			conceptMap.put(cpt.getRank(), cpt.getUri());
		}

		FSIterator<TOP> docIter = jcas.getJFSIndexRepository().getAllIndexedFS(
				Document.type);
		Map<Integer, String> docMap = new TreeMap<Integer, String>();
		while (docIter.hasNext()) {
			// count++;
			Document doc = (Document) docIter.next();
			docMap.put(doc.getRank(), doc.getUri());
		}
		// System.out.println(docMap.values().size());
		FSIterator<TOP> triIter = jcas.getJFSIndexRepository().getAllIndexedFS(
				TripleSearchResult.type);
		Map<Integer, Triple> triMap = new TreeMap<Integer, Triple>();
		while (triIter.hasNext()) {
			TripleSearchResult trp = (TripleSearchResult) triIter.next();
			// It conflict with gson.Triple
			edu.cmu.lti.oaqa.type.kb.Triple temp = trp.getTriple();
			triMap.put(
					trp.getRank(),
					new Triple(temp.getSubject(), temp.getPredicate(), temp
							.getObject()));
		}
		// System.out.println("dasdad:" + docMap.size());
		List<String> myConcepts = new ArrayList<String>(conceptMap.values());
		List<String> myDocs = new ArrayList<String>(docMap.values());
		List<Triple> myTriples = new ArrayList<Triple>(triMap.values());
		List<Snippet> mySnippets = new ArrayList<Snippet>();// 
		 FSIterator<TOP> ansIter = jcas.getJFSIndexRepository().getAllIndexedFS(Answer.type);
		 Answer exactAnswer = (Answer) ansIter.next();
		retrievalRes.add(new RetrievalResult(question.getId(), question
				.getText(), myConcepts, myDocs, myTriples, mySnippets,
				exactAnswer.getText(), goldSet.get(question.getId()).getExactAnswer()));
		List<String> goldDocs = new ArrayList<String>();
		List<String> goldConcepts = new ArrayList<String>();
		List<Triple> goldTriples = new ArrayList<Triple>();

		String queryId = question.getId();
		// System.out.println(goldSet.containsKey(queryId));
		if (goldSet.containsKey(queryId)) {
			goldConcepts = goldSet.get(queryId).getConcepts();
			goldDocs = goldSet.get(queryId).getDocuments();
			List<Triple> tempTriples = goldSet.get(queryId).getTriples();
			if (tempTriples != null) {
				for (Triple tri : tempTriples) {
					goldTriples.add(new Triple(tri.getO(), tri.getP(), tri
							.getS()));
				}
			}
		} // test.add((ArrayList<String>) goldDocs);
			// System.out.println("******" + myDocs.size());
			// RetrievalMeasures evaluator = new RetrievalMeasures();
		Double[] precisions = new Double[3];
		Double[] recalls = new Double[3];
		Double[] fscores = new Double[3];
		Double[] avgP = new Double[3];
		RetrievalMeasures.type = 0;
		precisions[0] = RetrievalMeasures.precision(goldDocs, myDocs);
		RetrievalMeasures.type = 1;
		precisions[1] = RetrievalMeasures.precision(goldConcepts, myConcepts);
		RetrievalMeasures.type = 2;
		precisions[2] = RetrievalMeasures.precision(goldTriples, myTriples);
		precision.add(precisions);
		RetrievalMeasures.type = 0;
		recalls[0] = RetrievalMeasures.recall(goldDocs, myDocs);
		RetrievalMeasures.type = 1;
		recalls[1] = RetrievalMeasures.recall(goldConcepts, myConcepts);
		RetrievalMeasures.type = 2;
		recalls[2] = RetrievalMeasures.recall(goldTriples, myTriples);
		recall.add(recalls);
		RetrievalMeasures.type = 0;
		fscores[0] = RetrievalMeasures.f1Measure(precisions[0], recalls[0]);
		RetrievalMeasures.type = 1;
		fscores[1] = RetrievalMeasures.f1Measure(precisions[1], recalls[1]);
		RetrievalMeasures.type = 2;
		fscores[2] = RetrievalMeasures.f1Measure(precisions[2], recalls[2]);
		fmeasure.add(fscores);
		avgP[0] = RetrievalMeasures.avgPreision(goldDocs, myDocs);
		avgP[1] = RetrievalMeasures.avgPreision(goldConcepts, myConcepts);
		avgP[2] = RetrievalMeasures.avgPreision(goldTriples, myTriples);
		avgPrecision.add(avgP);
	}

	@Override
	public void destroy() {

	}

	/**
	 * When whole pipeline completes, calculate unordered metrics: precisions,
	 * recalls and F1-score and ordered metrics: MAP,GMAP. Output metrics result
	 * to console and write retrieval result into Json format files.
	 */
	@SuppressWarnings("resource")
	@Override
	public void collectionProcessComplete(ProcessTrace arg0)
			throws ResourceProcessException, IOException {
		super.collectionProcessComplete(arg0);
		// for (ArrayList<String> t : test)
		// System.out.println("!!!!!!!" + t.get(0));
		Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting()
				.create();
		// String jsonOutput = gson.toJson(retrievedAnswers);
		File resultFile = new File(outputPath);
		
		BufferedWriter fWriter = new BufferedWriter(new FileWriter(resultFile));
		for (RetrievalResult rr : retrievalRes) {
			fWriter.write(gson.toJson(rr));
		}
		fWriter.close();

		int length = avgPrecision.size();
		Double map[] = new Double[3];
		Double gmap[] = new Double[3];

		map = RetrievalMeasures.MAP(avgPrecision);

		gmap = RetrievalMeasures.GMAP(avgPrecision);

		System.out.println("*************************");
		for (int i = 0; i < length; i++) {
			System.out.println("\n\nQuery" + i + ":");
			Double output[] = new Double[3];
			System.out.print("  precision:");
			output = precision.get(i);
			for (int j = 0; j < output.length; j++)
				System.out.print(output[j] + "\t");
			System.out.print("\n  recall:");
			output = recall.get(i);
			for (int j = 0; j < output.length; j++)
				System.out.print(output[j] + "\t");
			System.out.print("\n  fmeasure:");
			output = fmeasure.get(i);
			for (int j = 0; j < output.length; j++)
				System.out.print(output[j] + "\t");
		}
		System.out.println("\n*************************");
		System.err.println("oh shit!!!!!!!!!!!");
		System.out.println("There are " + length + " Queries:\n");
		System.out.print("\nMAP:");
		for (int j = 0; j < map.length; j++)
			System.out.print(map[j] + "\t");
		System.out.print("\nGMAP:");
		for (int j = 0; j < gmap.length; j++)
			System.out.print(gmap[j] + "\t");
		System.out.print("\nfinalPrecision:");
		for (int j = 0; j < gmap.length; j++)
			System.out.print(RetrievalMeasures.overallTP[j] * 1.0
					/ RetrievalMeasures.overallRetrieved[j] + "\t");
		System.out.print("\nfinalRecall:");
		for (int j = 0; j < gmap.length; j++)
			System.out.print(RetrievalMeasures.overallTP[j] * 1.0
					/ RetrievalMeasures.overallGold[j] + "\t");
		System.out.println();
	}
}
