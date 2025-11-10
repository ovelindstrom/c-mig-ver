package se.csn.webservice.bas.notmotor.skicka;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Vector;

import javax.xml.namespace.QName;
import javax.xml.rpc.Service;
import javax.xml.rpc.encoding.SerializerFactory;

import org.apache.axis.AxisEngine;
import org.apache.axis.AxisFault;
import org.apache.axis.NoEndPointException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Stub;
import org.apache.axis.constants.Style;
import org.apache.axis.constants.Use;

import org.apache.axis.description.OperationDesc;
import org.apache.axis.description.ParameterDesc;
import org.apache.axis.encoding.DeserializerFactory;
import org.apache.axis.encoding.ser.ArrayDeserializerFactory;
import org.apache.axis.encoding.ser.ArraySerializerFactory;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;
import org.apache.axis.encoding.ser.EnumDeserializerFactory;
import org.apache.axis.encoding.ser.EnumSerializerFactory;
import org.apache.axis.encoding.ser.SimpleDeserializerFactory;
import org.apache.axis.encoding.ser.SimpleListDeserializerFactory;
import org.apache.axis.encoding.ser.SimpleListSerializerFactory;
import org.apache.axis.encoding.ser.SimpleSerializerFactory;
import org.apache.axis.soap.SOAPConstants;
import org.apache.axis.utils.JavaUtils;

public class SkickaSOAPStub extends Stub implements Skicka_PortType {
    private Vector cachedSerClasses = new Vector();
    private Vector cachedSerQNames = new Vector();
    private Vector cachedSerFactories = new Vector();
    private Vector cachedDeserFactories = new Vector();

    static OperationDesc[] _operations;

    static {
        _operations = new OperationDesc[3];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1() {
        OperationDesc oper;
        ParameterDesc param;
        oper = new OperationDesc();
        oper.setName("skickaMeddelande");
        param = new ParameterDesc(new QName("http://webservice.csn.se/bas/notmotor/skicka", "skickaMeddelandeRequest"), ParameterDesc.IN, new QName("http://webservice.csn.se/bas/notmotor/skicka", "DTOMeddelande"), DTOMeddelande.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("http://webservice.csn.se/bas/notmotor/skicka", "DTONotifieringResultat"));
        oper.setReturnClass(DTONotifieringResultat.class);
        oper.setReturnQName(new QName("http://webservice.csn.se/bas/notmotor/skicka", "skickaMeddelandeResponse"));
        oper.setStyle(Style.DOCUMENT);
        oper.setUse(Use.LITERAL);
        _operations[0] = oper;

        oper = new OperationDesc();
        oper.setName("hamtaMeddelande");
        param = new ParameterDesc(new QName("http://webservice.csn.se/bas/notmotor/skicka", "hamtaMeddelandeRequest"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("http://webservice.csn.se/bas/notmotor/skicka", "DTOMeddelande"));
        oper.setReturnClass(DTOMeddelande.class);
        oper.setReturnQName(new QName("http://webservice.csn.se/bas/notmotor/skicka", "hamtaMeddelandeResponse"));
        oper.setStyle(Style.DOCUMENT);
        oper.setUse(Use.LITERAL);
        _operations[1] = oper;

        oper = new OperationDesc();
        oper.setName("taBortMeddelande");
        param = new ParameterDesc(new QName("http://webservice.csn.se/bas/notmotor/skicka", "taBortMeddelandeRequest"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("http://webservice.csn.se/bas/notmotor/skicka", "DTONotifieringResultat"));
        oper.setReturnClass(DTONotifieringResultat.class);
        oper.setReturnQName(new QName("http://webservice.csn.se/bas/notmotor/skicka", "taBortMeddelandeResponse"));
        oper.setStyle(Style.DOCUMENT);
        oper.setUse(Use.LITERAL);
        _operations[2] = oper;

    }

    public SkickaSOAPStub() throws AxisFault {
        this(null);
    }

    public SkickaSOAPStub(URL endpointURL, Service service) throws AxisFault {
        this(service);
        super.cachedEndpoint = endpointURL;
    }

    public SkickaSOAPStub(Service service) throws AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service) super.service).setTypeMappingVersion("1.2");
        Class cls;
        QName qName;
        QName qName2;
        Class beansf = BeanSerializerFactory.class;
        Class beandf = BeanDeserializerFactory.class;
        Class enumsf = EnumSerializerFactory.class;
        Class enumdf = EnumDeserializerFactory.class;
        Class arraysf = ArraySerializerFactory.class;
        Class arraydf = ArrayDeserializerFactory.class;
        Class simplesf = SimpleSerializerFactory.class;
        Class simpledf = SimpleDeserializerFactory.class;
        Class simplelistsf = SimpleListSerializerFactory.class;
        Class simplelistdf = SimpleListDeserializerFactory.class;
        qName = new QName("http://webservice.csn.se/bas/notmotor/skicka", "DTOAvsandare");
        cachedSerQNames.add(qName);
        cls = DTOAvsandare.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new QName("http://webservice.csn.se/bas/notmotor/skicka", "DTOBilaga");
        cachedSerQNames.add(qName);
        cls = DTOBilaga.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new QName("http://webservice.csn.se/bas/notmotor/skicka", "DTOMeddelande");
        cachedSerQNames.add(qName);
        cls = DTOMeddelande.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new QName("http://webservice.csn.se/bas/notmotor/skicka", "DTOMeddelandeHandelse");
        cachedSerQNames.add(qName);
        cls = DTOMeddelandeHandelse.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new QName("http://webservice.csn.se/bas/notmotor/skicka", "DTOMottagare");
        cachedSerQNames.add(qName);
        cls = DTOMottagare.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new QName("http://webservice.csn.se/bas/notmotor/skicka", "DTONotifieringResultat");
        cachedSerQNames.add(qName);
        cls = DTONotifieringResultat.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

    }

    protected Call createCall() throws RemoteException {
        try {
            Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0;i < cachedSerFactories.size();i++) {
                        Class cls = (Class) cachedSerClasses.get(i);
                        QName qName =
                            (QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            Class sf = (Class)
                                cachedSerFactories.get(i);
                            Class df = (Class)
                                cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        } else if (x instanceof SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                cachedSerFactories.get(i);
                            DeserializerFactory df = (DeserializerFactory)
                                cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new AxisFault("Failure trying to get the Call object", _t);
        }
    }

    @Override
    public DTONotifieringResultat skickaMeddelande(DTOMeddelande parameters) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://webservice.csn.se/bas/notmotor/skicka/skickaMeddelande");
        _call.setEncodingStyle(null);
        _call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("", "skickaMeddelande"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[]{parameters});

            if (_resp instanceof RemoteException) {
                throw (RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (DTONotifieringResultat) _resp;
                } catch (java.lang.Exception _exception) {
                    return (DTONotifieringResultat) JavaUtils.convert(_resp, DTONotifieringResultat.class);
                }
            }
        } catch (AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    @Override
    public DTOMeddelande hamtaMeddelande(long parameters) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://webservice.csn.se/bas/notmotor/skicka/hamtaMeddelande");
        _call.setEncodingStyle(null);
        _call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("", "hamtaMeddelande"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[]{Long.valueOf(parameters)});

            if (_resp instanceof RemoteException) {
                throw (RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (DTOMeddelande) _resp;
                } catch (java.lang.Exception _exception) {
                    return (DTOMeddelande) JavaUtils.convert(_resp, DTOMeddelande.class);
                }
            }
        } catch (AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    @Override
    public DTONotifieringResultat taBortMeddelande(long parameters) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://webservice.csn.se/bas/notmotor/skicka/taBortMeddelande");
        _call.setEncodingStyle(null);
        _call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("", "taBortMeddelande"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[]{Long.valueOf(parameters)});

            if (_resp instanceof RemoteException) {
                throw (RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (DTONotifieringResultat) _resp;
                } catch (java.lang.Exception _exception) {
                    return (DTONotifieringResultat) JavaUtils.convert(_resp, DTONotifieringResultat.class);
                }
            }
        } catch (AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

}
