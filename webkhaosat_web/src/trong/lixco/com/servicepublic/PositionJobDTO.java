/**
 * PositionJobDTO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package trong.lixco.com.servicepublic;

public class PositionJobDTO  implements java.io.Serializable {
    private java.lang.String code;

    private java.lang.String codeDepart;

    private boolean disable;

    private long id;

    private java.lang.String name;

    public PositionJobDTO() {
    }

    public PositionJobDTO(
           java.lang.String code,
           java.lang.String codeDepart,
           boolean disable,
           long id,
           java.lang.String name) {
           this.code = code;
           this.codeDepart = codeDepart;
           this.disable = disable;
           this.id = id;
           this.name = name;
    }


    /**
     * Gets the code value for this PositionJobDTO.
     * 
     * @return code
     */
    public java.lang.String getCode() {
        return code;
    }


    /**
     * Sets the code value for this PositionJobDTO.
     * 
     * @param code
     */
    public void setCode(java.lang.String code) {
        this.code = code;
    }


    /**
     * Gets the codeDepart value for this PositionJobDTO.
     * 
     * @return codeDepart
     */
    public java.lang.String getCodeDepart() {
        return codeDepart;
    }


    /**
     * Sets the codeDepart value for this PositionJobDTO.
     * 
     * @param codeDepart
     */
    public void setCodeDepart(java.lang.String codeDepart) {
        this.codeDepart = codeDepart;
    }


    /**
     * Gets the disable value for this PositionJobDTO.
     * 
     * @return disable
     */
    public boolean isDisable() {
        return disable;
    }


    /**
     * Sets the disable value for this PositionJobDTO.
     * 
     * @param disable
     */
    public void setDisable(boolean disable) {
        this.disable = disable;
    }


    /**
     * Gets the id value for this PositionJobDTO.
     * 
     * @return id
     */
    public long getId() {
        return id;
    }


    /**
     * Sets the id value for this PositionJobDTO.
     * 
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }


    /**
     * Gets the name value for this PositionJobDTO.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this PositionJobDTO.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PositionJobDTO)) return false;
        PositionJobDTO other = (PositionJobDTO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.code==null && other.getCode()==null) || 
             (this.code!=null &&
              this.code.equals(other.getCode()))) &&
            ((this.codeDepart==null && other.getCodeDepart()==null) || 
             (this.codeDepart!=null &&
              this.codeDepart.equals(other.getCodeDepart()))) &&
            this.disable == other.isDisable() &&
            this.id == other.getId() &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getCode() != null) {
            _hashCode += getCode().hashCode();
        }
        if (getCodeDepart() != null) {
            _hashCode += getCodeDepart().hashCode();
        }
        _hashCode += (isDisable() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += new Long(getId()).hashCode();
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PositionJobDTO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://servicepublic.com.lixco.trong/", "positionJobDTO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("code");
        elemField.setXmlName(new javax.xml.namespace.QName("", "code"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codeDepart");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codeDepart"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("disable");
        elemField.setXmlName(new javax.xml.namespace.QName("", "disable"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
