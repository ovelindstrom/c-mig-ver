/**
 * Skapad 2007-apr-23
 * @author Jonas Öhrnell (csn7821)
 * 
 */
package se.csn.common.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public abstract class ResourceBase implements Resource {

    protected String name, type;
    protected ResourceStatus status;
    protected List dependencies, dependants;
    
    public ResourceBase(String name, String type, Resource dependency) {
        this.name = name;
        this.type = type;
        dependencies = new ArrayList();
        dependants = new ArrayList();
        
        addDependency(dependency);
    }
    
    public ResourceBase(String name, String type) {
        
    }
    
    public String getName() {
        return name;
    }
    
    public String getType() {
        return type;
    }
    
    public String getXML(String indentation) {
        StringBuffer buf = new StringBuffer();
        buf.append("<resource name=\"");
        buf.append(getName());
        buf.append("\" type=\"");
        buf.append(getType());
        buf.append("\" classname=\"");
        buf.append(getClassName());
        buf.append(">\n");

        appendStatus(buf, indentation);
        
        if(hasDependencies()) {
            List children = getDependencies();
            for(Iterator it = children.iterator(); it.hasNext();) {
                Resource child = (Resource)it.next();
                buf.append(ResourceBase.indentLines(child.getXML(indentation), indentation));
            }
        }
        
        buf.append("</resource>\n");
        return buf.toString();
    }
    
    protected void appendStatus(StringBuffer buf, String indentation) {
        if(status == null) {
            return;
        }
        if(status.getProblem() != null) {
            buf.append(indentation);
            buf.append("<problem text=\"");
            buf.append(status.getProblem());
            buf.append("\"/>\n");
        }
        
        if(status.getFix() != null) {
            buf.append(indentation);
            buf.append("<fix text=\"");
            buf.append(status.getFix());
            buf.append("\"/>\n");
        }
    }
    
    public static String indentLines(String input, String indent) {
        if((input == null) || (indent == null)) {
            return input;
        }
        String[] lines = input.split("\\n");
        StringBuffer buf = new StringBuffer();
        for(int i = 0; i < lines.length; i++) {
            if(i > 0) {
                buf.append("\n");
            }
            buf.append(indent);
            buf.append(lines[i]);
        }
        if(input.endsWith("\n")) {
            buf.append("\n");
        }
        return buf.toString();
    }
    
    public ResourceStatus getStatus() {
        return status;
    }
    
    public ResourceStatus check() {
        ResourceStatus rs = doCheck();
        status = rs;
        return status;
    }
    
    /**
     * Implementera denna metod i subklasserna.
     * @return
     */
    public abstract ResourceStatus doCheck();
    
    /**
     * Gör rekursivt trädanrop på metoden check() och returnerar det allvarligaste 
     * utfallet. Detta anrop kommer alltså att uppdatera status för alla dependencies.
     * @return
     */
    public ResourceStatus checkDependencies() {
        if(!hasDependencies()) {
            
        }

        ResourceStatus rs = new ResourceStatus(null, null, ResourceStatus.OK);
        List children = getDependencies();
        for(Iterator it = children.iterator(); it.hasNext();) {
            Resource child = (Resource)it.next();
            rs = ResourceStatus.mostSevere(rs, child.check());
        }
        return rs;
    }
    
    
    public boolean hasDependencies() {
        return (getDependencies()!= null) && (getDependencies().size() > 0);
    }

    public void addDependency(Resource dependency) {
        if((dependency == null) || (dependencies.contains(dependency))) {
            return;
        }
        dependencies.add(dependency);
        dependency.addDependant(this);
    }
    
    public List getDependencies() {
        return dependencies;
    }
    
    public void removeDependency(Resource dependency) {
        if(dependency == null) {
            return;
        }
        dependencies.remove(dependency);
        dependency.removeDependant(this);
    }
    

    
    /* (non-Javadoc)
     * @see se.csn.common.config.Resource#addDependant(se.csn.common.config.Resource)
     */
    public void addDependant(Resource dependant) {
        if((dependant == null) || (dependants.contains(dependant))) {
            return;
        }
        dependants.add(dependant);
    }
    
    /* (non-Javadoc)
     * @see se.csn.common.config.Resource#getDependants()
     */
    public List getDependants() {
        return dependants;
    }
    /* (non-Javadoc)
     * @see se.csn.common.config.Resource#hasDependants()
     */
    public boolean hasDependants() {
        return (getDependants() != null) && (getDependants().size() > 0);
    }

    /* (non-Javadoc)
     * @see se.csn.common.config.Resource#removeDependant(se.csn.common.config.Resource)
     */
    public void removeDependant(Resource dependant) {
        if(dependant == null) {
            return;
        }
        dependants.remove(dependant);
    }
    
    public String getClassName() {
        return this.getClass().getName();
    }
}
