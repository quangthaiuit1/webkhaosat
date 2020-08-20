/**
 * DepartNoticeRelaServicePublicServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package trong.lixco.com.account.servicepublics;

import general.StaticPath;

public class DepartNoticeRelaServicePublicServiceLocator extends org.apache.axis.client.Service implements trong.lixco.com.account.servicepublics.DepartNoticeRelaServicePublicService {

    public DepartNoticeRelaServicePublicServiceLocator() {
    }


    public DepartNoticeRelaServicePublicServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public DepartNoticeRelaServicePublicServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for DepartNoticeRelaServicePublicPort
    private java.lang.String DepartNoticeRelaServicePublicPort_address = StaticPath.getPath()+"/account_service/DepartNoticeRelaServicePublic";

    public java.lang.String getDepartNoticeRelaServicePublicPortAddress() {
        return DepartNoticeRelaServicePublicPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String DepartNoticeRelaServicePublicPortWSDDServiceName = "DepartNoticeRelaServicePublicPort";

    public java.lang.String getDepartNoticeRelaServicePublicPortWSDDServiceName() {
        return DepartNoticeRelaServicePublicPortWSDDServiceName;
    }

    public void setDepartNoticeRelaServicePublicPortWSDDServiceName(java.lang.String name) {
        DepartNoticeRelaServicePublicPortWSDDServiceName = name;
    }

    public trong.lixco.com.account.servicepublics.DepartNoticeRelaServicePublic getDepartNoticeRelaServicePublicPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(DepartNoticeRelaServicePublicPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getDepartNoticeRelaServicePublicPort(endpoint);
    }

    public trong.lixco.com.account.servicepublics.DepartNoticeRelaServicePublic getDepartNoticeRelaServicePublicPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            trong.lixco.com.account.servicepublics.DepartNoticeRelaServicePublicServiceSoapBindingStub _stub = new trong.lixco.com.account.servicepublics.DepartNoticeRelaServicePublicServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getDepartNoticeRelaServicePublicPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setDepartNoticeRelaServicePublicPortEndpointAddress(java.lang.String address) {
        DepartNoticeRelaServicePublicPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (trong.lixco.com.account.servicepublics.DepartNoticeRelaServicePublic.class.isAssignableFrom(serviceEndpointInterface)) {
                trong.lixco.com.account.servicepublics.DepartNoticeRelaServicePublicServiceSoapBindingStub _stub = new trong.lixco.com.account.servicepublics.DepartNoticeRelaServicePublicServiceSoapBindingStub(new java.net.URL(DepartNoticeRelaServicePublicPort_address), this);
                _stub.setPortName(getDepartNoticeRelaServicePublicPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("DepartNoticeRelaServicePublicPort".equals(inputPortName)) {
            return getDepartNoticeRelaServicePublicPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://servicepublics.account.com.lixco.trong/", "DepartNoticeRelaServicePublicService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://servicepublics.account.com.lixco.trong/", "DepartNoticeRelaServicePublicPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("DepartNoticeRelaServicePublicPort".equals(portName)) {
            setDepartNoticeRelaServicePublicPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
