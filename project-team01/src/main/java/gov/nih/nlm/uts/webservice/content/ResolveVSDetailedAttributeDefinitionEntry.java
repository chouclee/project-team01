
package gov.nih.nlm.uts.webservice.content;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for resolveVSDetailedAttributeDefinitionEntry complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="resolveVSDetailedAttributeDefinitionEntry">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ticket" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entry" type="{http://webservice.uts.umls.nlm.nih.gov/}attributeDefinitionEntryDTO" minOccurs="0"/>
 *         &lt;element name="psf" type="{http://webservice.uts.umls.nlm.nih.gov/}psf" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resolveVSDetailedAttributeDefinitionEntry", propOrder = {
    "ticket",
    "entry",
    "psf"
})
public class ResolveVSDetailedAttributeDefinitionEntry {

    protected String ticket;
    protected AttributeDefinitionEntryDTO entry;
    protected Psf psf;

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
     * Gets the value of the entry property.
     * 
     * @return
     *     possible object is
     *     {@link AttributeDefinitionEntryDTO }
     *     
     */
    public AttributeDefinitionEntryDTO getEntry() {
        return entry;
    }

    /**
     * Sets the value of the entry property.
     * 
     * @param value
     *     allowed object is
     *     {@link AttributeDefinitionEntryDTO }
     *     
     */
    public void setEntry(AttributeDefinitionEntryDTO value) {
        this.entry = value;
    }

    /**
     * Gets the value of the psf property.
     * 
     * @return
     *     possible object is
     *     {@link Psf }
     *     
     */
    public Psf getPsf() {
        return psf;
    }

    /**
     * Sets the value of the psf property.
     * 
     * @param value
     *     allowed object is
     *     {@link Psf }
     *     
     */
    public void setPsf(Psf value) {
        this.psf = value;
    }

}
