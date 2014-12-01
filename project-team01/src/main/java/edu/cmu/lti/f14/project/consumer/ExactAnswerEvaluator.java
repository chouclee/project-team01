
package edu.cmu.lti.f14.project.consumer;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import json.gson.RetrievalResult;
import json.gson.TestQuestion;
import json.gson.TestSet;
import json.gson.TestYesNoQuestion;
import json.gson.TestYesNoSet;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.apache.uima.util.ProcessTrace;

import com.google.common.collect.Lists;

import edu.cmu.lti.oaqa.type.answer.Answer;
import edu.cmu.lti.oaqa.type.input.Question;

public class ExactAnswerEvaluator extends CasConsumer_ImplBase{
	//public static final String PARAM_OUTPUT = "outputFile";
	  List<Question> goldout;
	  HashMap<String, TestYesNoQuestion> goldSet = new HashMap<String, TestYesNoQuestion>();
	  String outputPath;
	  int exactAnswerTP = 0;
	  int allAnswer = 0;
	 @SuppressWarnings("unchecked")
	public void initialize() throws ResourceInitializationException {
	/*	  outputPath = (String) getUimaContext().getConfigParameterValue(PARAM_OUTPUT);
		  if (outputPath == null){
			  throw new ResourceInitializationException(ResourceInitializationException.RESOURCE_DATA_NOT_VALID,
					  new Object[]{PARAM_OUTPUT});
		  }*/
		  //String goldPath = "src/main/resources/data/BioASQ-SampleData1B.json";
		  String goldPath = ((String) getUimaContext().getConfigParameterValue("goldFile")).trim();
		  List<TestYesNoQuestion> goldAnswer;
		  goldAnswer = Lists.newArrayList();
		  Object value = goldPath;
	      
	    /*if (goldPath != null && goldPath.length() != 0) {
	      goldAnswer = TestSet.load(getClass().getResourceAsStream(goldPath)).stream()
	              .collect(toList());*/
			if (String.class.isAssignableFrom(value.getClass())) {
	      goldAnswer = (List<TestYesNoQuestion>) TestYesNoSet.load(getClass().
				        getResourceAsStream(String.class.cast(value))).
				        stream().collect(toList());
			} else if (String[].class.isAssignableFrom(value.getClass())) {
				goldAnswer = (List<TestYesNoQuestion>) Arrays.stream(String[].class.cast(value)).flatMap(
								path -> TestYesNoSet.load(getClass().getResourceAsStream(path)).
								stream()).collect(toList());
			}
			// trim question texts
			goldAnswer.stream()
					.filter(input -> input.getBody() != null)
					.forEach(
							input -> input.setBody(input.getBody().trim()
									.replaceAll("\\s+", " ")));
			System.out.println("concepts");
			System.out.println(goldAnswer.get(1).getConcepts());
			 for(TestYesNoQuestion q : goldAnswer){
				 goldSet.put(q.getId(), q);
			 }
	  }

	@Override
	public void processCas(CAS aCas) throws ResourceProcessException {
		// TODO Auto-generated method stub
		
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
	    
	 /*   FSIndex<?> AnswerIndex = jcas.getAnnotationIndex(Answer.type);
	    Iterator<?> AnswerIter = AnswerIndex.iterator();
	    Answer answer = (Answer) AnswerIter.next();
	    String exactAnswer = answer.getText();
	    */
	    String goldAnswer = goldSet.get(question.getId()).getExactAnswer().trim().toLowerCase();
	    
	    if (goldAnswer.matches("yes")){
	    	exactAnswerTP++;
	    }
	    allAnswer++;
	}
	
	 public void collectionProcessComplete(ProcessTrace arg0) throws ResourceProcessException,    IOException{
		 super.collectionProcessComplete(arg0); 
		 System.out.println(exactAnswerTP);
		 System.out.println(allAnswer);
		 System.out.println("=============================");
		 System.out.println("ExactAnswerPrecision:  " +  exactAnswerTP * 1.0 / allAnswer);
		 System.out.println("=============================");
	 }
	    

}
