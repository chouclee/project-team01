package util;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import edu.cmu.lti.oaqa.bio.bioasq.services.GoPubMedService;
import edu.cmu.lti.oaqa.bio.bioasq.services.OntologyServiceResponse;
import edu.cmu.lti.oaqa.bio.bioasq.services.OntologyServiceResponse.*;

import java.util.ArrayList;
import java.util.List;

public class ConcpetWebService {
  public static List<Finding> getConceptWebService(GoPubMedService service, 
          String text) throws ClientProtocolException, IOException {
    List<Finding> result = new ArrayList<Finding>()
            ;
    OntologyServiceResponse.Result diseaseOntologyResult = service
            .findDiseaseOntologyEntitiesPaged(text, 0);
    result.addAll(diseaseOntologyResult.getFindings());
    
    OntologyServiceResponse.Result geneOntologyResult = service
            .findGeneOntologyEntitiesPaged(text, 0, 10);
    result.addAll(geneOntologyResult.getFindings());
    
    OntologyServiceResponse.Result jochemResult = service.findJochemEntitiesPaged(text, 0);
    result.addAll(jochemResult.getFindings());
    
    OntologyServiceResponse.Result meshResult = service.findMeshEntitiesPaged(text, 0);
    result.addAll(meshResult.getFindings());
    
    OntologyServiceResponse.Result uniprotResult = service.findUniprotEntitiesPaged(text, 0);
    result.addAll(uniprotResult.getFindings());
    
    return result;
  }
}
