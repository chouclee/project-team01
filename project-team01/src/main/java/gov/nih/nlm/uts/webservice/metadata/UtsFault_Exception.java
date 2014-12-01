
package gov.nih.nlm.uts.webservice.metadata;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "UtsFault", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/")
public class UtsFault_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private UtsFault faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public UtsFault_Exception(String message, UtsFault faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public UtsFault_Exception(String message, UtsFault faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: gov.nih.nlm.uts.webservice.metadata.UtsFault
     */
    public UtsFault getFaultInfo() {
        return faultInfo;
    }

}
