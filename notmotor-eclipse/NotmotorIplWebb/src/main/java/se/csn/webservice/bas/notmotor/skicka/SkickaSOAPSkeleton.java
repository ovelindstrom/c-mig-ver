/**
 * SkickaSOAPSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package se.csn.webservice.bas.notmotor.skicka;

public class SkickaSOAPSkeleton implements se.csn.webservice.bas.notmotor.skicka.Skicka_PortType, org.apache.axis.wsdl.Skeleton {
    private se.csn.webservice.bas.notmotor.skicka.Skicka_PortType impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List) _myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc []{
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "skickaMeddelandeRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "DTOMeddelande"), se.csn.webservice.bas.notmotor.skicka.DTOMeddelande.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("skickaMeddelande", _params, new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "skickaMeddelandeResponse"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "DTONotifieringResultat"));
        _oper.setElementQName(new javax.xml.namespace.QName("", "skickaMeddelande"));
        _oper.setSoapAction("http://webservice.csn.se/bas/notmotor/skicka/skickaMeddelande");
        _myOperationsList.add(_oper);
        if (_myOperations.get("skickaMeddelande") == null) {
            _myOperations.put("skickaMeddelande", new java.util.ArrayList());
        }
        ((java.util.List) _myOperations.get("skickaMeddelande")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc []{
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "hamtaMeddelandeRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("hamtaMeddelande", _params, new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "hamtaMeddelandeResponse"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "DTOMeddelande"));
        _oper.setElementQName(new javax.xml.namespace.QName("", "hamtaMeddelande"));
        _oper.setSoapAction("http://webservice.csn.se/bas/notmotor/skicka/hamtaMeddelande");
        _myOperationsList.add(_oper);
        if (_myOperations.get("hamtaMeddelande") == null) {
            _myOperations.put("hamtaMeddelande", new java.util.ArrayList());
        }
        ((java.util.List) _myOperations.get("hamtaMeddelande")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc []{
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "taBortMeddelandeRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("taBortMeddelande", _params, new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "taBortMeddelandeResponse"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://webservice.csn.se/bas/notmotor/skicka", "DTONotifieringResultat"));
        _oper.setElementQName(new javax.xml.namespace.QName("", "taBortMeddelande"));
        _oper.setSoapAction("http://webservice.csn.se/bas/notmotor/skicka/taBortMeddelande");
        _myOperationsList.add(_oper);
        if (_myOperations.get("taBortMeddelande") == null) {
            _myOperations.put("taBortMeddelande", new java.util.ArrayList());
        }
        ((java.util.List) _myOperations.get("taBortMeddelande")).add(_oper);
    }

    public SkickaSOAPSkeleton() {
        this.impl = new se.csn.webservice.bas.notmotor.skicka.SkickaSOAPImpl();
    }

    public SkickaSOAPSkeleton(se.csn.webservice.bas.notmotor.skicka.Skicka_PortType impl) {
        this.impl = impl;
    }

    @Override
    public se.csn.webservice.bas.notmotor.skicka.DTONotifieringResultat skickaMeddelande(se.csn.webservice.bas.notmotor.skicka.DTOMeddelande parameters) throws java.rmi.RemoteException
    {
        se.csn.webservice.bas.notmotor.skicka.DTONotifieringResultat ret = impl.skickaMeddelande(parameters);
        return ret;
    }

    @Override
    public se.csn.webservice.bas.notmotor.skicka.DTOMeddelande hamtaMeddelande(long parameters) throws java.rmi.RemoteException
    {
        se.csn.webservice.bas.notmotor.skicka.DTOMeddelande ret = impl.hamtaMeddelande(parameters);
        return ret;
    }

    @Override
    public se.csn.webservice.bas.notmotor.skicka.DTONotifieringResultat taBortMeddelande(long parameters) throws java.rmi.RemoteException
    {
        se.csn.webservice.bas.notmotor.skicka.DTONotifieringResultat ret = impl.taBortMeddelande(parameters);
        return ret;
    }

}
