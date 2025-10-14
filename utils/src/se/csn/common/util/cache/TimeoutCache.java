/**
 * Skapad 2007-feb-05
 * @author Jonas Öhrnell (csn7821)
 * 
 */
package se.csn.common.util.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Cache som tar bort objekt som är för gamla. 
 * Livslängden bestäms
 * 
 * Skapad 2007-feb-05
 * @author Jonas Öhrnell (csn7821)
 */
public class TimeoutCache implements Cache {
    
    class TimestampReference {
        long timestamp;
        Object object;
        
        public TimestampReference(Object object) {
            this.object = object;
            timestamp = System.currentTimeMillis();
        }
        
        /**
         * @param livstid i millisekunder. Om lifetime = -1 så antas objektet aldrig dö.
         * @return true om objektet är för gammalt, false annars.
         */
        boolean isTooOld(long lifetime) {
            if(lifetime == -1) {
                return false;
            }
            return (System.currentTimeMillis() > (timestamp + lifetime));
        }
    }
    
    private Map objects;
    private long lifetimeInMilliseconds;
    
    public TimeoutCache(long lifetimeInMilliseconds) {
        this.lifetimeInMilliseconds = lifetimeInMilliseconds;
        objects = new HashMap();
    }

    /** 
     * @see Cache.clear
     */
    public void clear() {
        objects.clear();
    }
    
    /**
     * @see Cache.get
     * @return null om objektet är för gammalt
     */
    public Object get(Object key) {
        if(key == null) return null;
        Object value = objects.get(key);
        if(value == null) return null;
        
        TimestampReference tr = (TimestampReference)value;
        if(tr.isTooOld(lifetimeInMilliseconds)) {
            objects.remove(key);
            return null;
        }
        return tr.object;
    }
    
    /**
     * @see Cache.put
     */
    public void put(Object key, Object value) {
        // Kolla om det finns någon referens:
        Object val = objects.get(key);
        if(val == null) {
            TimestampReference tr = new TimestampReference(value);
            objects.put(key, tr);
        } else {
            TimestampReference tr = (TimestampReference)val;
            tr.object = value;
        }
    }
    
    /**
     * Rensar bort alla objekt som är för gamla ur mappen. 
     * Kan vara ett sätt att hålla ner minnesförbrukning.  
     */
    public void purgeTimedoutObjects() {
        List toRemove = new ArrayList();
        for(Iterator it = objects.keySet().iterator(); it.hasNext();) {
            Object key = it.next();
            TimestampReference tr = (TimestampReference)objects.get(key);
            if(tr.isTooOld(lifetimeInMilliseconds)) {
                toRemove.add(key);
            }
        }
        for(Iterator it = toRemove.iterator();it.hasNext();) {
            objects.remove(it.next());
        }
    }
    
    /**
     * @return Cachelivstid i millisekunder (!)
     */
    public long getLifetimeInMilliseconds() {
        return lifetimeInMilliseconds;
    }
    
    /**
     * @param lifetimeInMilliseconds livstid i millisekunder
     */
    public void setLifetimeInMilliseconds(long lifetimeInMilliseconds) {
        this.lifetimeInMilliseconds = lifetimeInMilliseconds;
    }
}
