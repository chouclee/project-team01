
package gov.nih.nlm.uts.webservice.semnet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getSemanticNetworkRelationLabelRelation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getSemanticNetworkRelationLabelRelation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ticket" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="semanticTypeId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="semanticRelationLabelId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="relatedSemanticTypeId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getSemanticNetworkRelationLabelRelation", propOrder = {
    "ticket",
    "version",
    "semanticTypeId",
    "semanticRelationLabelId",
    "relatedSemanticTypeId"
})
public class GetSemanticNetworkRelationLabelRelation {

    protected String ticket;
    protected String version;
    protected String semanticTypeId;
    protected String semanticRelationLabelId;
    protected String relatedSemanticTypeId;

    /**
     * Gets the value of the ticket property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTicket() {
        return ticket;
    }

    /**
     * Sets the value of the ticket property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTicket(String value) {
        this.ticket = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the semanticTypeId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSemanticTypeId() {
        return semanticTypeId;
    }

    /**
     * Sets the value of the semanticTypeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSemanticTypeId(String value) {
        this.semanticTypeId = value;
    }

    /**
     * Gets the value of the semanticRelationLabelId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSemanticRelationLabelId() {
        return semanticRelationLabelId;
    }

    /**
     * Sets the value of the semanticRelationLabelId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSemanticRelationLabelId(String value) {
        this.semanticRelationLabelId = value;
    }

    /**
     * Gets the value of the relatedSemanticTypeId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRelatedSemanticTypeId() {
        return relatedSemanticTypeId;
    }

    /**
     * Sets the value of the relatedSemanticTypeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRelatedSemanticTypeId(String value) {
        this.relatedSemanticTypeId = value;
    }

}
