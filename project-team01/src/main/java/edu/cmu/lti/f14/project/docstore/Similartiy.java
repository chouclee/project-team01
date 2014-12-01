package edu.cmu.lti.f14.project.docstore;

import java.util.HashMap;

import util.StanfordLemmatizer;

/**
 * Similarity function collection
 * @author zhouchel
 *
 */
public class Similartiy {
  public static final double k1 = 1.2;
  public static final double b = 0.75;
  public static final double k3 = 0.0;
  
  /**
   * Calculate document score using BM25 
   * @param stat
   * @param query
   * @param docId
   * @return
   */
  public static double BM25(CollectionStat stat, String query, int docId) {
    query = StanfordLemmatizer.process(query);
    String[] q = query.split("\\s+");
    HashMap<String, Integer> queryTermVec = new HashMap<String, Integer>();
    for (String term : q) {
      Integer tf = queryTermVec.get(term);
      if (tf == null)
        queryTermVec.put(term, 1);
      else
        queryTermVec.put(term, tf + 1);
    }
    double bm25Score = 0.0;
    double avgDocLength = (double)stat.collectionLength / stat.N;
    int docLength = stat.docInfoMap.get(docId).docLen;
    Integer tf, df, qtf;
    double RSJWeight, tfWeight, userWeight;
    for (String term : queryTermVec.keySet()) {
      tf = stat.docInfoMap.get(docId).tfMap.get(term);
      if (tf != null) {
        df = stat.docFreqMap.get(term);
        RSJWeight = Math.log((stat.N - df + 0.5)/(df + 0.5));
        
        tfWeight = (double) tf / (tf + k1 * (1 - b + b * docLength/avgDocLength));
        
        qtf = queryTermVec.get(term);
        userWeight = (k3 + 1)* qtf/ (k3 + qtf);
        
        bm25Score += RSJWeight * tfWeight * userWeight;
      }
    }  
    return bm25Score;
  }
  
  /**
   * Calculate document score using Cosine similarity
   * @param query
   * @param context
   * @return
   */
  public static double consineSim(String query, String context) {
    query = StanfordLemmatizer.process(query);
    String[] q = query.split("\\s+");
    HashMap<String, Integer> queryVector = new HashMap<String, Integer>();
    for (String term : q) {
      Integer tf = queryVector.get(term);
      if (tf == null)
        queryVector.put(term, 1);
      else
        queryVector.put(term, tf + 1);
    }
    
    context = StanfordLemmatizer.process(context);
    String[] c = query.split("\\s+");
    HashMap<String, Integer> docVector = new HashMap<String, Integer>();
    for (String term : c) {
      Integer tf = docVector.get(term);
      if (tf == null)
        docVector.put(term, 1);
      else
        docVector.put(term, tf + 1);
    }
    
    double cosine_similarity = 0.0;

    // TODO :: compute cosine similarity between two sentences
    double overlap = 0.0, normQuery = 0.0, normDoc = 0.0;
    for (String token : queryVector.keySet()) {
      normQuery += Math.pow(queryVector.get(token), 2);
      if (docVector.containsKey(token))
        overlap += queryVector.get(token) * docVector.get(token);
    }

    for (String token : docVector.keySet()) {
      normDoc += Math.pow(docVector.get(token), 2);
    }

    cosine_similarity = overlap / (Math.sqrt(normDoc) * Math.sqrt(normQuery));

    return cosine_similarity;
  }
}
