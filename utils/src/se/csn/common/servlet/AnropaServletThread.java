/**
 * Skapad 2007-mar-09
 * @author Jonas åhrnell (csn7821)
 *
 */
package se.csn.common.servlet;

import se.csn.ark.common.util.logging.Log;


/**
 * Klass som försöker anropa en URL tills den lyckas.
 * Använd denna klass för att kicka igång en servlettråd
 * vid startup.
 * För att en servlet ska skapa en "giltig" tråd (en tråd
 * som befinner sig i rätt kontext för resurshantering i
 * WAS:en) så måste den anropas från en annan tråd, typiskt
 * i init-metoden i en annan servlet.
 * Problemet är när init() går så har inte WAS:en öppnat
 * webhostarna -> anropet misslyckas. Därav behovet för
 * denna lösning.
 */
public class AnropaServletThread implements Runnable {
    
    private Log log = Log.getInstance(AnropaServletThread.class);
    private int sovtid;
    private int antalForsok;
    private String url;
    /**
     * 
     * @param url Den URL som ska anropas.
     * @param antalForsok Hur många anropsförsök som ska göras. Om ett negativt tal anges
     * så görs oändligt antal försök. 
     * @param sovtidMillisMellanForsok Hur många millisekunder tråden ska sova mellan försöken.
     */
    public AnropaServletThread(String url, int antalForsok, int sovtidMillisMellanForsok) {
        this.url = url;
        this.antalForsok = antalForsok;
        this.sovtid = sovtidMillisMellanForsok;
        Thread t = new Thread(this);
        t.start();
    }
    
    public void run() {
        int cnt = 0;

        while(true) {
		    if(cnt == antalForsok) {
		        log.debug("Gjort " + antalForsok + " försök, avbryter.");
		        break;
		    }
		    cnt++;

		    try {
				Thread.sleep(sovtid);
				log.debug("Anropar url");
			    if(ServletUtils.anropaServletAsynkront(url)) {
			        log.debug("Lyckades anropa " + url);
			    } else {
			        log.debug("Lyckades ansluta till " + url + ", men fick inte HTTP 200 tillbaka.");
			    }
			    break;
			} catch(Throwable t) {
			    log.debug("Anropet misslyckades, försöker igen");
			}
		}
    }


}
