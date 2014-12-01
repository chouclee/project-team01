
package gov.nih.nlm.uts.webservice.content;

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
@WebServiceClient(name = "UtsWsContentControllerImplService", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", wsdlLocation = "https://uts-ws.nlm.nih.gov/services/nwsContent?wsdl")
public class UtsWsContentControllerImplService
    extends Service
{

    private final static URL UTSWSCONTENTCONTROLLERIMPLSERVICE_WSDL_LOCATION;
    private final static WebServiceException UTSWSCONTENTCONTROLLERIMPLSERVICE_EXCEPTION;
    private final static QName UTSWSCONTENTCONTROLLERIMPLSERVICE_QNAME = new QName("http://webservice.uts.umls.nlm.nih.gov/", "UtsWsContentControllerImplService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("https://uts-ws.nlm.nih.gov/services/nwsContent?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        UTSWSCONTENTCONTROLLERIMPLSERVICE_WSDL_LOCATION = url;
        UTSWSCONTENTCONTROLLERIMPLSERVICE_EXCEPTION = e;
    }

    public UtsWsContentControllerImplService() {
        super(__getWsdlLocation(), UTSWSCONTENTCONTROLLERIMPLSERVICE_QNAME);
    }

    public UtsWsContentControllerImplService(WebServiceFeature... features) {
        super(__getWsdlLocation(), UTSWSCONTENTCONTROLLERIMPLSERVICE_QNAME, features);
    }

    public UtsWsContentControllerImplService(URL wsdlLocation) {
        super(wsdlLocation, UTSWSCONTENTCONTROLLERIMPLSERVICE_QNAME);
    }

    public UtsWsContentControllerImplService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, UTSWSCONTENTCONTROLLERIMPLSERVICE_QNAME, features);
    }

    public UtsWsContentControllerImplService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public UtsWsContentControllerImplService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns UtsWsContentController
     */
    @WebEndpoint(name = "UtsWsContentControllerImplPort")
    public UtsWsContentController getUtsWsContentControllerImplPort() {
        return super.getPort(new QName("http://webservice.uts.umls.nlm.nih.gov/", "UtsWsContentControllerImplPort"), UtsWsContentController.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns UtsWsContentController
     */
    @WebEndpoint(name = "UtsWsContentControllerImplPort")
    public UtsWsContentController getUtsWsContentControllerImplPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://webservice.uts.umls.nlm.nih.gov/", "UtsWsContentControllerImplPort"), UtsWsContentController.class, features);
    }

    private static URL __getWsdlLocation() {
        if (UTSWSCONTENTCONTROLLERIMPLSERVICE_EXCEPTION!= null) {
            throw UTSWSCONTENTCONTROLLERIMPLSERVICE_EXCEPTION;
        }
        return UTSWSCONTENTCONTROLLERIMPLSERVICE_WSDL_LOCATION;
    }

}
