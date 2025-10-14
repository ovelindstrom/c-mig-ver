/*
 * Skapad 2007-nov-29
 */
package se.csn.common.config;

import org.xml.sax.Attributes;

/**
 * @author Jonas åhrnell - csn7821
 * Interface för objekt som lyssnar till parsning av config.xml-filer.
 */
public interface ConfigXmlParseListener {
    
    public void start(String element, Attributes attrs);
    
    public void end(String element);

}
