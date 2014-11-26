package util;

import java.io.IOException;

import edu.cmu.lti.oaqa.bio.bioasq.services.GoPubMedService;
import edu.cmu.lti.oaqa.bio.bioasq.services.OntologyServiceResponse;
import edu.cmu.lti.oaqa.bio.bioasq.services.OntologyServiceResponse.*;

import java.util.ArrayList;
import java.util.List;

public class ConcpetWebService {
  public static List<Finding> getConceptWebService(GoPubMedService service, 
          String text) {
    List<Finding> result = new ArrayList<Finding>();
    try {
      OntologyServiceResponse.Result diseaseOntologyResult = service
              .findDiseaseOntologyEntitiesPaged(text, 0);
      result.addAll(diseaseOntologyResult.getFindings());
    } catch (Exception e) {
      
    }
    
    OntologyServiceResponse.Result geneOntologyResult;
    try {
      geneOntologyResult = service
              .findGeneOntologyEntitiesPaged(text, 0, 10);
      result.addAll(geneOntologyResult.getFindings());
    } catch (Exception e) {
      // TODO Auto-generated catch block
      //e.printStackTrace();
    }
    
      
    OntologyServiceResponse.Result jochemResult;
    try {
      jochemResult = service.findJochemEntitiesPaged(text, 0);
      result.addAll(jochemResult.getFindings());
    } catch (Exception e) {
      // TODO Auto-generated catch block
      //e.printStackTrace();
    }
    
    
    OntologyServiceResponse.Result meshResult;
    try {
      meshResult = service.findMeshEntitiesPaged(text, 0);
      result.addAll(meshResult.getFindings());
    } catch (Exception e) {
      // TODO Auto-generated catch block
      //aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaae.printStackTrace();
    }
    
    
    OntologyServiceResponse.Result uniprotResult;
    try {
      uniprotResult = service.findUniprotEntitiesPaged(text, 0);
      result.addAll(uniprotResult.getFindings());
    } catch (IOException e) {
      // TODO Auto-generated catch block
      //e.printStackTrace();
    }
       
    return result;
  }
}
