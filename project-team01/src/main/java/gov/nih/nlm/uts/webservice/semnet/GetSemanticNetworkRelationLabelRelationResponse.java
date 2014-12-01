
package gov.nih.nlm.uts.webservice.semnet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getSemanticNetworkRelationLabelRelationResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getSemanticNetworkRelationLabelRelationResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://webservice.uts.umls.nlm.nih.gov/}semanticNetworkRelationLabelRelationDTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getSemanticNetworkRelationLabelRelationResponse", propOrder = {
    "_return"
})
public class GetSemanticNetworkRelationLabelRelationResponse {

    @XmlElement(name = "return")
    protected SemanticNetworkRelationLabelRelationDTO _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link SemanticNetworkRelationLabelRelationDTO }
     *     
     */
    public SemanticNetworkRelationLabelRelationDTO getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link SemanticNetworkRelationLabelRelationDTO }
     *     
     */
    public void setReturn(SemanticNetworkRelationLabelRelationDTO value) {
        this._return = value;
    }

}
