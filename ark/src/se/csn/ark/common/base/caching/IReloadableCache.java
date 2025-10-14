package se.csn.ark.common.base.caching;

/**
 * @author Vincent Wong, Iocore
 * @since 2002-08-27, v1.0
 * @version 1.0
 */
	public interface IReloadableCache {
	
	/** 
	 * Denna metod tvingar en cache att ladda om sig. Det är tillåtet för implementationer
	 * att endast tömma sig i detta läge (dvs inte ladda in ny information omgående), 
	 * under förutsättning att de vid användning omärkligt för konsumenten
	 * successivt laddar om sig - cachen får inte uppträda som om den var tömd efter detta anrop.
     * @throws CacheReloadException gick ej att ladda om
	 */
	public void reload() throws CacheReloadException;
	
}

