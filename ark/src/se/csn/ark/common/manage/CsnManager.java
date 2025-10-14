package se.csn.ark.common.manage;

/**
 * Gränssnitt för att hantera en eller flera hanterbara funktioner
 * som en sammanhållen tjänst. Används för callback från funktionerna.
 * 
 * @author K-G Sjöström - AcandoFrontec
 * @since 20050413
 * @version 1 skapad
 *
 */
public interface CsnManager {

	/**
	 * Funktionen/tjänsten initierad.
	 * 
	 * @param managable Den aktuella funktionen/tjänsten.
	 */
	public void initiated(CsnManagable managable);

	/**
	 * Funktionen/tjänsten startad.
	 * 
	 * @param managable Den aktuella funktionen/tjänsten.
	 */
	public void started(CsnManagable managable);
	
	/**
	 * Funktionen/tjänsten stoppad.
	 * 
	 * @param managable Den aktuella funktionen/tjänsten.
	 */
	public void stopped(CsnManagable managable);
	
	/**
	 * Funktionen/tjänsten stängd.
	 * 
	 * @param managable Den aktuella funktionen/tjänsten.
	 */
	public void closed(CsnManagable managable);

	/**
	 * Funktionen/tjänsten går inte att hantera.
	 * 
	 * @param managable Den aktuella funktionen/tjänsten.
	 * 
	 * @param ume Anledningen till att det har spårat ut.
	 */
	public void unManagable(CsnManagable managable, UnManagableException ume);
	
}
