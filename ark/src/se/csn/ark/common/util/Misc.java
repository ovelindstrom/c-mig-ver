/*
 * Created on 2007-apr-26
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package se.csn.ark.common.util;

import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

import se.csn.ark.common.util.logging.Log;

/**
 * @author csk4135
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Misc {
	private static Log log = Log.getInstance(Misc.class);


    /**
     * 
     */
    public Misc() {
        super();
        // TODO Auto-generated constructor stub
    }

    public static String getClassplace(Object aObject) {
    	Class cls = aObject.getClass();
        ProtectionDomain pDomain = cls.getProtectionDomain();
        CodeSource cSource = pDomain.getCodeSource();
        URL loc = cSource.getLocation();  // file:/c:/almanac14/examples/
        
        log.debug(aObject.getClass().getName() + "**************LOCATION*************");
        log.debug(loc);
        log.debug("**************LOCATION*************");
        return loc.toString();
    }
}
