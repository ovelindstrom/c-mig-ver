/**
 * SkickaSOAPSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package se.csn.webservice.bas.notmotor.skicka;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axis.description.FaultDesc;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.description.ParameterDesc;
import org.apache.axis.wsdl.Skeleton;

public class SkickaSOAPSkeleton implements Skicka_PortType, Skeleton {
    private Skicka_PortType impl;
    private static Map _myOperations = new Hashtable();
    private static Collection _myOperationsList = new ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static List getOperationDescByName(java.lang.String methodName) {
        return (List) _myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        OperationDesc _oper;
        FaultDesc _fault;
        ParameterDesc[] _params;
        _params = new ParameterDesc []{
            new ParameterDesc(new QName("http://webservice.csn.se/bas/notmotor/skicka", "skickaMeddelandeRequest"), ParameterDesc.IN, new QName("http://webservice.csn.se/bas/notmotor/skicka", "DTOMeddelande"), DTOMeddelande.class, false, false), 
        };
        _oper = new OperationDesc("skickaMeddelande", _params, new QName("http://webservice.csn.se/bas/notmotor/skicka", "skickaMeddelandeResponse"));
        _oper.setReturnType(new QName("http://webservice.csn.se/bas/notmotor/skicka", "DTONotifieringResultat"));
        _oper.setElementQName(new QName("", "skickaMeddelande"));
        _oper.setSoapAction("http://webservice.csn.se/bas/notmotor/skicka/skickaMeddelande");
        _myOperationsList.add(_oper);
        if (_myOperations.get("skickaMeddelande") == null) {
            _myOperations.put("skickaMeddelande", new ArrayList());
        }
        ((List) _myOperations.get("skickaMeddelande")).add(_oper);
        _params = new ParameterDesc []{
            new ParameterDesc(new QName("http://webservice.csn.se/bas/notmotor/skicka", "hamtaMeddelandeRequest"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
        };
        _oper = new OperationDesc("hamtaMeddelande", _params, new QName("http://webservice.csn.se/bas/notmotor/skicka", "hamtaMeddelandeResponse"));
        _oper.setReturnType(new QName("http://webservice.csn.se/bas/notmotor/skicka", "DTOMeddelande"));
        _oper.setElementQName(new QName("", "hamtaMeddelande"));
        _oper.setSoapAction("http://webservice.csn.se/bas/notmotor/skicka/hamtaMeddelande");
        _myOperationsList.add(_oper);
        if (_myOperations.get("hamtaMeddelande") == null) {
            _myOperations.put("hamtaMeddelande", new ArrayList());
        }
        ((List) _myOperations.get("hamtaMeddelande")).add(_oper);
        _params = new ParameterDesc []{
            new ParameterDesc(new QName("http://webservice.csn.se/bas/notmotor/skicka", "taBortMeddelandeRequest"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
        };
        _oper = new OperationDesc("taBortMeddelande", _params, new QName("http://webservice.csn.se/bas/notmotor/skicka", "taBortMeddelandeResponse"));
        _oper.setReturnType(new QName("http://webservice.csn.se/bas/notmotor/skicka", "DTONotifieringResultat"));
        _oper.setElementQName(new QName("", "taBortMeddelande"));
        _oper.setSoapAction("http://webservice.csn.se/bas/notmotor/skicka/taBortMeddelande");
        _myOperationsList.add(_oper);
        if (_myOperations.get("taBortMeddelande") == null) {
            _myOperations.put("taBortMeddelande", new ArrayList());
        }
        ((List) _myOperations.get("taBortMeddelande")).add(_oper);
    }

    public SkickaSOAPSkeleton() {
        this.impl = new SkickaSOAPImpl();
    }

    public SkickaSOAPSkeleton(Skicka_PortType impl) {
        this.impl = impl;
    }

    @Override
    public DTONotifieringResultat skickaMeddelande(DTOMeddelande parameters) throws RemoteException
    {
        DTONotifieringResultat ret = impl.skickaMeddelande(parameters);
        return ret;
    }

    @Override
    public DTOMeddelande hamtaMeddelande(long parameters) throws RemoteException
    {
        DTOMeddelande ret = impl.hamtaMeddelande(parameters);
        return ret;
    }

    @Override
    public DTONotifieringResultat taBortMeddelande(long parameters) throws RemoteException
    {
        DTONotifieringResultat ret = impl.taBortMeddelande(parameters);
        return ret;
    }

}
