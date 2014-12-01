package edu.cmu.lti.f14.project.docstore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import util.StanfordLemmatizer;
import edu.cmu.lti.oaqa.type.retrieval.Document;

/**
 * Calculate statistics of all retrieved documents 
 * @author zhouchel
 *
 */
public class CollectionStat {
  public Map<String, Integer> ctfMap;
  public Map<String, List<Integer>> invList;
  public Map<Integer, DocInfo> docInfoMap;
  public Map<String, Integer> docFreqMap;
  //public List<Integer> docList;
  public int N; // corpus size
  public long collectionLength;
  
  /**
   * Statistics for a single document
   * @author zhouchel
   *
   */
  class DocInfo {
    public String url;
    public int docId;
    public String title;
    public int docLen;
    public Map<String, Integer> tfMap;
    public DocInfo(Document d) {
      tfMap = new HashMap<String, Integer>();
      url = d.getUri();
      docId = Integer.parseInt(d.getDocId());
      title = d.getTitle();
      String text = d.getText();
      text = StanfordLemmatizer.process(text);
      text = text.replace(".", "");
      if (text != "") {
        String[] terms = text.split("\\s+");
        docLen = terms.length;
        Integer tf;
        for (String term : terms) {
          tf = tfMap.get(term);
          if (tf != null)
            tfMap.put(term, tf + 1);
          else
            tfMap.put(term, 1);
        }
      }
    }
  }
  
  public CollectionStat() {
    ctfMap = new HashMap<String, Integer>();
    //docList = new ArrayList<DocInfo>();
    invList = new HashMap<String, List<Integer>>();
    docInfoMap = new HashMap<Integer, DocInfo>();
    docFreqMap = new HashMap<String, Integer>();
    N = 0;
    collectionLength = 0;
  }
  
  /**
   * Add a document to collection
   * @param doc
   */
  public void addDoc(Document doc) {
    N++;
    DocInfo docInfo = new DocInfo(doc);
    docInfoMap.put(docInfo.docId, docInfo);
    //docList.add(docInfo);
    
    Integer ctf, tf, df;
    for (String term : docInfo.tfMap.keySet()) {
      List<Integer> docList = invList.get(term);
      if (docList == null) 
        docList = new ArrayList<Integer>();
      docList.add(docInfo.docId);
      invList.put(term, docList);
      
      ctf = ctfMap.get(term);
      tf = docInfo.tfMap.get(term);
      collectionLength += tf;
      if (ctf != null)
        ctfMap.put(term, ctf + tf );
      else
        ctfMap.put(term, tf);
      
      df = docFreqMap.get(term);
      if (df == null)
        docFreqMap.put(term, 1);
      else
        docFreqMap.put(term, df + 1);
    }
  }
}
