package se.csn.ark.common.dal;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import org.apache.soap.util.xml.Deserializer;
import org.apache.soap.util.xml.Serializer;

import se.csn.ark.common.CsnApplicationException;
import se.csn.ark.common.CsnSystemException;

/**
 * Gränssnitt för web service access.
 * 
 * @author K-G Sjöström - AcandoFrontec
 * @since 20040907
 * @version 1 skapad
 *
 */
public interface CsnDAOWebService extends CsnDataAccessObject {

    /**
     * @param url url för webservice endpoint
     */
    public abstract void setEndPoint(URL url);




    /**
     * @return url för webservice endpoint
     * @throws MalformedURLException om url-sträng är felaktig
     */
    public abstract URL getEndPoint() throws MalformedURLException;




    /**
     * @return svar från webservice
     * @throws CsnApplicationException webservice-metoden felar
     * @throws CsnSystemException webservice-anropet felar
     */
    public abstract Object sendRequest() throws CsnApplicationException, CsnSystemException;




    /**
     * @param parameters parametrar till webservice-metoden
     * @return svar från webservice
     * @throws CsnApplicationException webservice-metoden felar
     * @throws CsnSystemException webservice-anropet felar
     */
    public abstract Object sendRequest(Vector parameters)
        throws CsnApplicationException, CsnSystemException;




    /**
     * Lägger till mappning för en viss klass
     * Exempel
     * addMapType("http://common.ark.csn.se/", "DTOException",
     *      se.csn.ark.common.DTOException.class);
     * 
     * @param nameSpaceURI uri
     * @param nameSpaceLocalPart namespace
     * @param theClass klassen som mappas
     */
    public abstract void addMapType(String nameSpaceURI, String nameSpaceLocalPart, Class theClass);




    /**
     * Lägger till mappning för en viss klass
     * 
     * @param nameSpaceURI uri
     * @param nameSpaceLocalPart namespace
     * @param theClass klassen som mappas
     * @param useSerializer true om XSDAnyTypeSerializer ska användas vid
     * serialisering av klassen
     * @param useDeserializer true om XSDAnyTypeSerializer ska användas vid
     * återskapande av klassen
     */
    public abstract void addMapType(
        String nameSpaceURI,
        String nameSpaceLocalPart,
        Class theClass,
        boolean useSerializer,
        boolean useDeserializer);




    /**
     * Lägger till mappning för en viss klass
     * 
     * @param nameSpaceURI uri
     * @param nameSpaceLocalPart namespace
     * @param theClass klassen som mappas
     * @param ser används för att serialisera klassen
     * @param deSer används för att återskapa klassen
     */




    public abstract void addMapType(
        String nameSpaceURI,
        String nameSpaceLocalPart,
        Class theClass,
        Serializer ser,
        Deserializer deSer);

}
