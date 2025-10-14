/**
 * Skapad 2007-feb-05
 * @author Jonas Öhrnell (csn7821)
 * 
 */
package se.csn.common.util.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Enklast möjliga cache: en minimal wrapper runt en HashMap.
 * 
 * Skapad 2007-feb-05
 * @author Jonas Öhrnell (csn7821)
 */
public class SimpleCache implements Cache {
    
    private Map map;
    
    public SimpleCache() {
        map = new HashMap();
    }
    
    public void clear() {
        map.clear();
    }
    
    public Object get(Object key) {
        return map.get(key);
    }

    public void put(Object key, Object value) {
        map.put(key, value);
    }
}
