package se.csn.notmotor.admin;

import se.csn.common.servlet.RunControl;
import se.csn.notmotor.ipl.SkickaMeddelandeStateMachine;


public class Constants extends SkickaMeddelandeStateMachine {
    // TODO: Fundera över ett bättre sätt att kommunicera konstanter.
    // Denna koppling blir för osynlig
    public Constants() {
        super(null, new RunControl());
    }
}
