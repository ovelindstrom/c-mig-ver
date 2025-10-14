/*
 * Created on 2004-okt-21
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package se.csn.common.fam;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * @author csn7478
 * @version 1.01
 *
 */
public class DTOSignedDocument implements Serializable {
    private final static String SEPARATOR = "#¤";
	private byte[] data;
	private String ocrdata;
	private String attributes;
	private String xslfilnamn;
	private String blankett;
	private String programnamn;

	public DTOSignedDocument() {
	}
	
    
    /**
     * Det dokumentdata som ska sparas
     */
    public byte[] getData() {
        return data;
    }
    public void setData(byte[] data) {
        this.data = data;
    }
    
    /**
     * @param Data i strängformat
     */
    public void setData(String s) {
        if((s == null) || (s.length() == 0)) {
            data = new byte[0];
        } else {
            data = s.getBytes();
        }
    }
    public String getDataAsString() {
        return new String(data);
    }
    
    private Map toMap(String data) {
        Map map = new HashMap();
        if((data == null) || (data.length() == 0)) {
            return map;
        }
        String[] arr = data.split(SEPARATOR);
        for(int i = 0; i < arr.length; i += 2) {
            map.put(arr[i], arr[i+1]);
        }
        return map;
    }
    
    private String toString(Map map) {
        StringBuffer sb = new StringBuffer();
        for(Iterator it = map.keySet().iterator();it.hasNext();) {
            String key = (String)it.next();
            sb.append(key);
            sb.append(SEPARATOR);
            sb.append((String)map.get(key));
            sb.append(SEPARATOR);
        }
        if(sb.length() > 0) {
            sb.delete(sb.length()-SEPARATOR.length(), sb.length());
        }
        return sb.toString();
    }
    
    /** 
     * OCR-data som används för att simulera en inskanning från pappersdokument.
     * OCR-data lagras som en sträng på formatet varde1=name1#varde2=varde2#....
     */
    public void clearOcrdata() {
        ocrdata = "";
    }
    
    public void removeOcrParam(String key) {
        Map map = toMap(ocrdata);
        map.remove(key);
        ocrdata = toString(map);
        
    }
    public void addOcrParam(String param, String verde) {
        Map map = toMap(ocrdata);
        map.put(param, verde);
        ocrdata = toString(map);
    }
    public String getOcrParam(String param) {
       Map map = toMap(ocrdata);
       return (String)map.get(param);
    }
    /**
     * @return En strängrepresentation av ocrdatat
     */
    public String getOcrdataStrang() {
        return getTCLLista(toMap(ocrdata));
    }
    public void setOcrdata(Map ocrdata) {
        this.ocrdata = toString(ocrdata);
    }
    
    /**
     * @return En TCL-lista på formatet {{param1}{verde1} {param2}{verde2}...}
     */
    public String getTCLLista(Map tuples) {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        for(Iterator it = tuples.keySet().iterator(); it.hasNext(); ) {
            String param = (String)it.next();
            String verde = (String)tuples.get(param);
            sb.append("{");
            sb.append(param);
            sb.append("} {");
            sb.append(verde);
            sb.append("} ");
        }
        if(sb.length() > 1) {
            sb.delete(sb.length()-1, sb.length());
        }
        sb.append("}");
        return sb.toString();
    }
    
    
    
    /** 
     * Attribut är data som inte påverkar batch-uppdateringen, men som ändå ska sparas.
     * Attribut består av key-value-par, strängar.
     */
    public void clearAttributLista() {
        attributes = "";
    }
    public void removeAttribut(String key) {
        Map map = toMap(attributes);
        map.remove(key);
        attributes = toString(map);
    }
    public void addAttribut(String param, String verde) {
        Map map = toMap(attributes);
        map.put(param, verde);
        attributes = toString(map);
    }
    public String getAttribut(String param) {
        Map map = toMap(attributes);
       return (String)map.get(param);
    }
    /**
     * @return En strängrepresentation av ocrdatat
     */
    public String getAttributStrang() {
        return getTCLLista(toMap(attributes));
    }
    public void setAttributLista(Map attributes) {
        this.attributes = toString(attributes);
    }

    public String getXslfilnamn() {
        return xslfilnamn;
    }
    public void setXslfilnamn(String xslfilnamn) {
        this.xslfilnamn = xslfilnamn;
    }

    public String getBlankett() {
        return blankett;
    }
    public void setBlankett(String blankett) {
        this.blankett = blankett;
    }
    
    public String getProgramnamn() {
        return programnamn;
    }
    public void setProgramnamn(String programnamn) {
        this.programnamn = programnamn;
    }
    

    public String getAttributes() {
        return attributes;
    }
    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }
    public String getOcrdata() {
        return ocrdata;
    }
    public void setOcrdata(String ocrdata) {
        this.ocrdata = ocrdata;
    }
}