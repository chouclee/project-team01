package edu.cmu.lti.f14.project.docstore;

import java.util.HashMap;

import util.StanfordLemmatizer;

public class Similartiy {
  public static final double k1 = 1.2;
  public static final double b = 0.75;
  public static final double k3 = 0.0;
  
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
}
