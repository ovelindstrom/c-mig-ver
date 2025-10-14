package se.csn.ark.common.base.genproxy;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Ulf, Peter, Vincent
 * @since 
 * @version 1
 *
 */
public class GenProxyActionListener implements ActionListener {
	
	private boolean errorOccurred = false;
	private ActionEvent event;
	
	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae) {
		this.event = ae;
		errorOccurred = true;
		
	}
	
	/**
	 * @return true om fel har rapporterats till lysnaren
	 */
	public boolean errorHasOccurred() {
		return errorOccurred;
	}
	
    /**
     * @return den händelse som har rapporterats
     */
	public ActionEvent getActionEvent() {
		return event;
	}

}

