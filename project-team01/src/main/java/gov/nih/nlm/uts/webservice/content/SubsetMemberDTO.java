
package gov.nih.nlm.uts.webservice.content;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for subsetMemberDTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="subsetMemberDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://webservice.uts.umls.nlm.nih.gov/}sourceDataDTO">
 *       &lt;sequence>
 *         &lt;element name="attributeCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="subsetHandle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "subsetMemberDTO", propOrder = {
    "attributeCount",
    "subsetHandle"
})
@XmlSeeAlso({
    RelationSubsetMemberDTO.class,
    SourceConceptSubsetMemberDTO.class,
    AtomSubsetMemberDTO.class
})
public class SubsetMemberDTO
    extends SourceDataDTO
{

    protected int attributeCount;
    protected String subsetHandle;

    /**
     * Gets the value of the attributeCount property.
     * 
     */
    public int getAttributeCount() {
        return attributeCount;
    }

    /**
     * Sets the value of the attributeCount property.
     * 
     */
    public void setAttributeCount(int value) {
        this.attributeCount = value;
    }

    /**
     * Gets the value of the subsetHandle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubsetHandle() {
        return subsetHandle;
    }

    /**
     * Sets the value of the subsetHandle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubsetHandle(String value) {
        this.subsetHandle = value;
    }

}
