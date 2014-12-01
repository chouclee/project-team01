package edu.cmu.lti.f14.project.reader;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import json.JsonCollectionReaderHelper;
import json.gson.Question;
import json.gson.TestSet;
import json.gson.TrainingSet;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

/**
 * Collection reader to read in all questions 
 * @author smart moon
 *
 */
public class QuestionReader extends CollectionReader_ImplBase {
	private final String PARAM_INFILEPATH="INFILE";
	private String mFilePath;
	List<Question> inputs;
	private int mCurrentIndex;
	 /**
	   * @see org.apache.uima.collection.CollectionReader_ImplBase#initialize()
	   */
	public void initialize() throws ResourceInitializationException {
	    mFilePath = ((String) getConfigParameterValue(PARAM_INFILEPATH)).trim();
	    mCurrentIndex = 0;

	    Object value = mFilePath;
		if (String.class.isAssignableFrom(value.getClass())) {
			inputs = TrainingSet
					.load(getClass().getResourceAsStream(
							String.class.cast(value))).stream()
					.collect(toList());
		} else if (String[].class.isAssignableFrom(value.getClass())) {
			inputs = Arrays
					.stream(String[].class.cast(value))
					.flatMap(
							path -> TestSet.load(
									getClass().getResourceAsStream(path))
									.stream()).collect(toList());
		}
		// trim question texts
				inputs.stream()
						.filter(input -> input.getBody() != null)
						.forEach(
								input -> input.setBody(input.getBody().trim()
										.replaceAll("\\s+", " ")));//here we need to remove stop words, and end marker
				//System.out.println(inputs.get(0).getConcepts());
	  }
	
	@Override
	public void getNext(CAS aCAS) throws IOException, CollectionException {
		// TODO Auto-generated method stub
		JCas jcas;
	    try {
	      jcas = aCAS.getJCas();
	    } catch (CASException e) {
	      throw new CollectionException(e);
	    }

	    // open input stream to file
	    Question qt = inputs.get(mCurrentIndex++);
	    
	    JsonCollectionReaderHelper.addQuestionToIndex(qt, "", jcas);
	}

	@Override
	public boolean hasNext() throws IOException, CollectionException {
		// TODO Auto-generated method stub
		return mCurrentIndex<inputs.size();
	}

	@Override
	public Progress[] getProgress() {
		// TODO Auto-generated method stub
		System.out.println("getProgress()...." + mCurrentIndex);
		return new Progress[] { new ProgressImpl(mCurrentIndex, inputs.size(), Progress.ENTITIES) };
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
