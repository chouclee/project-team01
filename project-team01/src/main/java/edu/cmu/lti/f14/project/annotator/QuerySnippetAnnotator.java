package edu.cmu.lti.f14.project.annotator;
  
/**
 * 
 * 
 */


import edu.cmu.lti.oaqa.type.answer.CandidateAnswerVariant;
import edu.cmu.lti.oaqa.type.retrieval.Document;
import edu.cmu.lti.oaqa.type.retrieval.Passage;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import util.TypeFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

/**
 * Retrieve snippet from documents
 * @author xzhan
 *
 */
public class QuerySnippetAnnotator extends JCasAnnotator_ImplBase {

    
  public String URL_PRE= "http://gold.lti.cs.cmu.edu:30002/pmc/";
  
  private static final CloseableHttpClient http = HttpClients.createDefault();
  

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
  // TODO Auto-generated method stub
  
    Collection<Document> documents = JCasUtil.select(aJCas, Document.class);
     
    // get id, filter not null url 
    List<String> pmids = documents.stream().map(Document::getDocId).filter(Objects::nonNull).collect(toList());
     
    for(Document doc : documents){
      String pmid = doc.getDocId();
//      // full url   
      String url = URL_PRE + pmid;
     // String url = doc.getUri();
      
      // store query sentence
      String query = doc.getQueryString();
      
      String [] queryArray = query.split("\\s+"); 
      
      // storing query vector
      Map<String, Integer> queryVector = new HashMap<String, Integer>();
      // store
      for(String str : queryArray){
        if(queryVector.get(str) != null){
          queryVector.put(str, queryVector.get(str) + 1);
        }
        else{
          queryVector.put(str, 1);
        }
        
      }
      
      
      /**********************get snippet*************************/
      
      // get httpget
      HttpGet httpGet = new HttpGet(url);
      String article = "";
      try(CloseableHttpResponse response = http.execute(httpGet)){
        HttpEntity entity = response.getEntity();
        if (entity != null) {
          BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent())); 
          String line = "";
          
          // read the whole article from url
          while((line = br.readLine()) != null){
            article += line;
          }
        } else {
          article = doc.getText();
        }
        
        // split the whole article into each sentence
        // replace ? ! with . to divide into different sentence
        
        String stopArticle = article.replace("!", ".").replace("?", ".");
          
        String [] sentence = stopArticle.split("\\.");
        
        Map<Integer, Map<String, Integer>> vec = new HashMap<Integer, Map<String, Integer>>();
        
        Map<Integer, Double> similarityMap = new HashMap<Integer, Double>();
        
        // store the id of the sentence with max
        int maxId = 0;
        
        // max similarity
        double simi = 0.0;
        
        // calculate each vector of each sentence and store into map
        for(int i = 0; i < sentence.length; i++){
          String [] words = sentence[i].replace(",", "").replace(":", "").replace("'s", "").replace("\"", "")
                  .replace("--"," ").replace("-", " ").replace(";", "").split("\\+");
          
          // store the vector of each sentence in passage
          Map<String, Integer> docVector = new HashMap<String, Integer>();
          // store the vector
          for(String str : words){
            if(docVector.get(str) != null){
              docVector.put(str, docVector.get(str) + 1);
            }
            else{
              docVector.put(str, 1);
            }
          }
          
          double similarity = computeCosineSimilarity(queryVector, docVector);
          similarityMap.put(i, similarity);
          
          if(similarity > simi){
            simi = similarity;
            maxId = i;
          }
          
        }
        
        // calculate the start and the stop position of each passage
        int start = stopArticle.indexOf(sentence[maxId]);
        int end = sentence[maxId].length() + 1;
        
        Passage passage = TypeFactory.createPassage(aJCas, url, doc.getScore(), sentence[maxId], doc.getRank(), query, "", new ArrayList<CandidateAnswerVariant>(), doc.getTitle(), doc.getDocId(), start, end, "sections.0", "sections.0", "");
        
        passage.addToIndexes();
        
      }catch(IOException e){
        e.printStackTrace();
      }
    }
  }
  
  /**
   * compute the cosine similarity.
   * <p>
   * 
   * 
   * @return cosine_similarity
   */
  private double computeCosineSimilarity(Map<String, Integer> queryVector,
    Map<String, Integer> docVector) {
    double cosine_similarity=0.0, numerator = 0.0, denominator1 = 0.0, denominator2 = 0.0; 
    int temp1, temp2;
    // TODO :: compute cosine similarity between two sentences
    
      
    if ((queryVector.isEmpty()) || (docVector.isEmpty())) // if no word exist  
    {  
        return 0.0;  
    }  
      
    Iterator queryIter = queryVector.entrySet().iterator();
    while(queryIter.hasNext()){
      Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)queryIter.next();
      String key = entry.getKey();
      temp1 = entry.getValue();
        
      if(docVector.get(key) == null){
        temp2 = 0;
      }else{
        temp2 = docVector.get(key);
      }
        
      docVector.remove(key);
      numerator += temp1 * temp2;  
      denominator1 += temp1 * temp1;  
      denominator2 += temp2 * temp2;
        
    }  
      
    Iterator docIter = docVector.entrySet().iterator();
      
    while(docIter.hasNext()){
      Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)docIter.next();
  
      temp2 = entry.getValue();
        
      denominator2 += temp2 * temp2; 
      
    }
    cosine_similarity = numerator / (Math.sqrt(denominator1 * denominator2));
    return cosine_similarity;
  }
}
