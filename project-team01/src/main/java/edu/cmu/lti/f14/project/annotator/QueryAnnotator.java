package edu.cmu.lti.f14.project.annotator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.lucene.analysis.Analyzer.TokenStreamComponents;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.tartarus.snowball.ext.PorterStemmer;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.jcas.tcas.Annotation;

import util.StanfordLemmatizer;
import util.Utils;
import edu.cmu.lti.oaqa.type.input.Question;
import edu.cmu.lti.oaqa.type.retrieval.AtomicQueryConcept;
import edu.cmu.lti.oaqa.type.retrieval.ComplexQueryConcept;

import util.LuceneConfig;
import edu.cmu.lti.oaqa.type.input.Question;

/**
 * Remove stopwords, stem sentences.
 * @author smart moon
 *
 */
public class QueryAnnotator extends JCasAnnotator_ImplBase {
	// //////
	private BufferedReader br = null;
	private HashSet<String> strset = new HashSet<String>();
	public static LuceneConfig analyzer = new LuceneConfig();
	static {
		analyzer.setLowercase(true);
		analyzer.setStopwordRemoval(true);
		analyzer.setStemmer(LuceneConfig.StemmerType.KSTEM);
	}
	///add configuration file

	public void initialize(UimaContext aContext)
			throws ResourceInitializationException {
		super.initialize(aContext);
		String samplefile = (String) aContext
				.getConfigParameterValue("STOPWORDS_FILE");
		InputStream is = QueryAnnotator.class.getClassLoader()
				.getResourceAsStream(samplefile);
		System.out.println(is == null);

		try {
			br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			String str = null;
			while ((str = br.readLine()) != null) {
				strset.add(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// //////
	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

		FSIterator<Annotation> iter = jcas.getAnnotationIndex().iterator();
	    if (iter.isValid() && iter.hasNext()) {
	      //iter.moveToNext();
	      Question question = (Question) iter.next();
	      
	      AtomicQueryConcept atomicQueryConcept = new AtomicQueryConcept(jcas);
	      // set the whole query into the text
	      
	      // original text
	      String originalText = question.getText();
	      atomicQueryConcept.setOriginalText(originalText);
	     
	      // eliminate punctuation, stopwords
	      String textHandled = originalText.replace("?", "").replace(".", "").replace(",", "").replace("\"", "");
	      atomicQueryConcept.setText(StanfordLemmatizer.stemText(textHandled).trim());
	      
	      atomicQueryConcept.addToIndexes();
	      
	      // add atomic to complex
	      ComplexQueryConcept complexQueryConcept = new ComplexQueryConcept(jcas);
	      
	      // fs
	      FSList fs = new FSList(jcas);
	      // arraylist
	      ArrayList<AtomicQueryConcept> tokenList = new ArrayList<AtomicQueryConcept>();
	      tokenList.add(atomicQueryConcept);
	      
	      // from arraylist to fs     Change in the Utils
	      fs = Utils.fromCollectionToFSList(jcas,tokenList);
	      complexQueryConcept.setOperatorArgs(Utils.fromCollectionToFSList(jcas, tokenList)); 
	      complexQueryConcept.addToIndexes();
	      
	    }

	}

	

	public static void main(String[] args) {
		QueryAnnotator qa = new QueryAnnotator();
		String text = "a weakness little boy or and in Chinese has more than 10 years old, WHERE could we found him?";
		String text1 = "I \'ve got a brand new combine harvester, and I \'m giving you the key";
		String ret = qa.removeStopWords(text);
		System.out.println(ret);

	}

	// /////////////
	private String removeStopWords(String text) {
		TokenStreamComponents comp = analyzer.createComponents("",
				new StringReader(text));
		TokenStream tokenstream = comp.getTokenStream();
		CharTermAttribute charTermAttribute = tokenstream
				.addAttribute(CharTermAttribute.class);
		try {
			tokenstream.reset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<String> tokens = new ArrayList<String>();
		try {
			while (tokenstream.incrementToken()) {
				String term = charTermAttribute.toString();
				tokens.add(term);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String ret = "";
		// weak PS weakness
		// porter is stronger than KSTEM
		final PorterStemmer stemmer = new PorterStemmer();

		System.out.println(stemmer.getCurrent());
		for (String token : tokens) {
			stemmer.setCurrent(token);
			stemmer.stem();
			ret += stemmer.getCurrent() + " ";
		}
		// why not use the given stem function??? to compare the result
		return ret;
	}

	// stem first or remove first??
	private String removeStopWordsByDic(String text) {
		List<String> res = new ArrayList<String>();
		for (String s : text.split("\\s+")) {
			if (!strset.contains(s)) {
				res.add(s);
			}
		}
		String ret = "";
		for (String token : res) {
			ret += token + " ";
		}
		return ret;
	}
}
