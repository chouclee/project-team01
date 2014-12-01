
package gov.nih.nlm.uts.webservice.finder;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "UtsWsFinderControllerImplService", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", wsdlLocation = "https://uts-ws.nlm.nih.gov/services/nwsFinder?wsdl")
public class UtsWsFinderControllerImplService
    extends Service
{

    private final static URL UTSWSFINDERCONTROLLERIMPLSERVICE_WSDL_LOCATION;
    private final static WebServiceException UTSWSFINDERCONTROLLERIMPLSERVICE_EXCEPTION;
    private final static QName UTSWSFINDERCONTROLLERIMPLSERVICE_QNAME = new QName("http://webservice.uts.umls.nlm.nih.gov/", "UtsWsFinderControllerImplService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("https://uts-ws.nlm.nih.gov/services/nwsFinder?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        UTSWSFINDERCONTROLLERIMPLSERVICE_WSDL_LOCATION = url;
        UTSWSFINDERCONTROLLERIMPLSERVICE_EXCEPTION = e;
    }

    public UtsWsFinderControllerImplService() {
        super(__getWsdlLocation(), UTSWSFINDERCONTROLLERIMPLSERVICE_QNAME);
    }

    public UtsWsFinderControllerImplService(WebServiceFeature... features) {
        super(__getWsdlLocation(), UTSWSFINDERCONTROLLERIMPLSERVICE_QNAME, features);
    }

    public UtsWsFinderControllerImplService(URL wsdlLocation) {
        super(wsdlLocation, UTSWSFINDERCONTROLLERIMPLSERVICE_QNAME);
    }

    public UtsWsFinderControllerImplService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, UTSWSFINDERCONTROLLERIMPLSERVICE_QNAME, features);
    }

    public UtsWsFinderControllerImplService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public UtsWsFinderControllerImplService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns UtsWsFinderController
     */
    @WebEndpoint(name = "UtsWsFinderControllerImplPort")
    public UtsWsFinderController getUtsWsFinderControllerImplPort() {
        return super.getPort(new QName("http://webservice.uts.umls.nlm.nih.gov/", "UtsWsFinderControllerImplPort"), UtsWsFinderController.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns UtsWsFinderController
     */
    @WebEndpoint(name = "UtsWsFinderControllerImplPort")
    public UtsWsFinderController getUtsWsFinderControllerImplPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://webservice.uts.umls.nlm.nih.gov/", "UtsWsFinderControllerImplPort"), UtsWsFinderController.class, features);
    }

    private static URL __getWsdlLocation() {
        if (UTSWSFINDERCONTROLLERIMPLSERVICE_EXCEPTION!= null) {
            throw UTSWSFINDERCONTROLLERIMPLSERVICE_EXCEPTION;
        }
        return UTSWSFINDERCONTROLLERIMPLSERVICE_WSDL_LOCATION;
    }

}
