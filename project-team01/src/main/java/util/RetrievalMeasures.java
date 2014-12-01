package util;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;

public class RetrievalMeasures {
  /**
   * Calculate Precision
   * @param goldStand
   * @param ret
   * @return
   */
  public static int[] overallGold = new int[3];
  public static int[] overallTP = new int[3];
  public static int[] overallRetrieved = new int[3];
  public static int type;
  public static<T> double precision(List<T> goldStand,List<T> ret ) {
    if (goldStand == null || ret.size() == 0 ||goldStand.size() == 0)
      return 0.0;
    HashSet<T> gold = new HashSet<T>(goldStand);
    int positiveTrue = 0;
    for (T retrieved : ret) {
      if (gold.contains(retrieved)) {
        positiveTrue++; // find one in gold standard output true
        overallTP[type]++;
      }
    }
    overallRetrieved[type] += ret.size();
    return (double)positiveTrue/ret.size();
  }
  
  /**
   * Calculate Recall
   * @param goldStand
   * @param ret
   * @return
   */
  public static<T> double recall(List<T> goldStand,List<T> ret ) {
    if (goldStand == null || ret.size() == 0 ||goldStand.size() == 0)
      return 0.0;
    HashSet<T> gold = new HashSet<T>(goldStand);
    int positiveTrue = 0;
    for (T retrieved : ret) {
      if (gold.contains(retrieved)) {
        positiveTrue++; // find one in gold standard output true 
        //overallTP[type]++;
      }
    }
    overallGold[type] += goldStand.size();
    return (double)positiveTrue/goldStand.size();
  }
  
  public static double f1Measure(double precision, double recall) {
    if (precision == 0 || recall == 0)
      return 0.0;
    return 2*precision*recall/ (precision + recall);
  }
  
  /**
   * Calculate Average Precision
   * @param goldStand
   * @param ret
   * @return
   */
  public static<T> double avgPreision(List<T> goldStand,List<T> ret){
    if (goldStand == null || goldStand.size() == 0 || ret.size() == 0) 
      return 0.0;
    ArrayList<Double> p = new ArrayList<>();
    HashSet<T> gold = new HashSet<T>(goldStand);
    int relevantCount = 0; // number of relevant items
    T retrieved;
    for (int i = 0; i < ret.size(); i++) {
      retrieved = ret.get(i);
      if (gold.contains(retrieved)) {
        relevantCount++;
        p.add((double)relevantCount/(i+1)); // add P@n
      }
    }
    if (p.size() == 0)
      return 0.0;
    else {
      double sum = 0;
      for (Double precision : p) {
        sum += precision;
      }
      return sum/p.size();
    }
  }
  
  /**
   * Calculate Mean Average Precision
   * @param ap
   * @return
   */
  public static<T> Double[] MAP(List<Double[]> avgPrecision) {
	  Double map[] = {0.0,0.0,0.0};
	  Double length = new Double(avgPrecision.size());
	  for (Double[] ap : avgPrecision){
	    	for (int i = 0; i < ap.length; i++){
	    		map[i] += ap[i] / length;
	    		//gmap[i] *= (ap[i] + epslon);
	    	}
	    }
    return map;
  }
  
  /**
   * Calculate Geometric Mean Average
   * @param ap
   * @return
   */
  public static<T> Double[] GMAP(List<Double[]> avgPrecision) {
	  Double gmap[] = {1.0,1.0,1.0};
	  Double epslon = 1e-3;
	  Double length = new Double(avgPrecision.size());
	  for (Double[] ap : avgPrecision){
	    	for (int i = 0; i < ap.length; i++)
	    		gmap[i] *= (ap[i] + epslon);
	  }
	  for (int i = 0; i < gmap.length; i++){
	    	gmap[i] = Math.pow(gmap[i], 1./length);
	    }
	  return gmap;
  }
}
