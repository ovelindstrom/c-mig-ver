/*
 * Skapad 2007-nov-29
 */
package se.csn.common.config;

import java.util.Date;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author Jonas Öhrnell - csn7821
 */
public class ResourceStatus {

    public static final int OK = 0, 
	WARNING = 1,
	ERROR = 2;
    
    private String problem, fix;
    private int status;
    private Date checkTime;
    
    /**
     * Skapar ny ResourceStatus med status OK och standardtexter. 
     */
    public ResourceStatus() {
        this("Inga problem.", "Behövs inte.", OK);
    }
    
    public ResourceStatus(String problem, String fix, int status) {
        this.status = status;
        this.problem = problem; 
        this.fix = fix;
        checkTime = new Date();
    }
    
    /**
     * @return En beskrivning av problemet med denna resurs. Ska vidarebefordra den 
     *         information som användes för att testa resursen.
     * 		   Om resursen fungerar returneras null.
     */
    public String getProblem() {
        return problem;
    }
    
    /**
     * @return En beskrivning av eventuell åtgärd för att få denna resurs att fungera. 
     *         Om resursen fungerar returneras null.
     */
    public String getFix() {
        return fix;
    }
    
    public int getStatus() {
        return status;
    }
    
    public Date getCheckTime() {
        return checkTime;
    }
    
    /**
     * @return Returnerar det allvarligaste resultatet av de två.
     */
    public static ResourceStatus mostSevere(ResourceStatus rs1, ResourceStatus rs2) {
        int[] order = new int[]{ResourceStatus.ERROR, ResourceStatus.WARNING, ResourceStatus.OK};
        for(int i = 0; i < order.length; i++) {
            if(rs1.status == order[i]) return rs1;
            if(rs2.status == order[i]) return rs2;
        }
        throw new IllegalArgumentException("Okända resultatkoder: " + rs1.status + "," + rs2.status);
    }
    
    public boolean equals(Object o) {
        if(o == null) return false;
        if(!(o instanceof ResourceStatus)) return false;
        ResourceStatus rs = (ResourceStatus)o;
        return 	(rs.status == status) && 
        		(rs.problem == problem) && 
        		(rs.fix == fix);
    }
    
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    
}
