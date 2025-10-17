/*
 * @since 2007-okt-31
 */
package se.csn.notmotor.ipl.model;

import java.util.ArrayList;
import java.util.List;

import se.csn.notmotor.ipl.MeddelandeSender;

/**
 * @author Jonas åhrnell - csn7821
 */
public class SandResultat extends KodText {

    private int handelsetyp;
    private List<Mottagare> mottagare;
    private MeddelandeSender sandare;

    public SandResultat(int handelsetyp, int kod, String text, MeddelandeSender sandare, Mottagare mott) {
        super(kod, text);
        this.handelsetyp = handelsetyp;
        this.sandare = sandare;
        mottagare = new ArrayList<Mottagare>();
        if (mott != null) {
            mottagare.add(mott);
        }
    }

    public Mottagare[] getMottagare() {
        return (Mottagare[]) mottagare.toArray(new Mottagare[0]);
    }

    public void addMottagare(Mottagare mott) {
        mottagare.add(mott);
    }

    public MeddelandeSender getSandare() {
        return sandare;
    }

    public void setSandare(MeddelandeSender sandare) {
        this.sandare = sandare;
    }

    public int getHandelsetyp() {
        return handelsetyp;
    }

    public void setHandelsetyp(int handelsetyp) {
        this.handelsetyp = handelsetyp;
    }

}
