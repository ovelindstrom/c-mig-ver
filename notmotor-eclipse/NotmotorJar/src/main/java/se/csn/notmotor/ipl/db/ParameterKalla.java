/**
 * @since 2007-mar-13
 * @author Jonas åhrnell (csn7821)
 * 
 */
package se.csn.notmotor.ipl.db;

import java.util.Map;

import se.csn.notmotor.ipl.model.Setting;


public interface ParameterKalla {

    String getStringParam(String namn);

    String getStringParam(String namn, String defaultVarde);

    void setStringParam(String namn, String varde);

    int getIntParam(String namn, int defaultVarde);

    void setIntParam(String namn, int varde);

    long getLongParam(String namn, long defaultVarde);

    void setLongParam(String namn, long varde);

    void setBooleanParam(String namn, boolean varde);

    boolean getBooleanParam(String namn, boolean defaultVarde);

    String getDescription(String namn);

    void reload();

    /**
     * Returnerar en map av namn-vardepar.
     */
    Map getStringParamMap();

    Setting[] getSettings();
}
