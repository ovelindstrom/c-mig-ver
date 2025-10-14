package se.csn.notmotor.ipl;

/**
 * Interface.
 */
public interface SkickaMeddelandeServices extends MeddelandeServicesBase {

	/**
	 * @return true om minst ett mail skickades
	 */
	boolean skickaMeddelande();
}
