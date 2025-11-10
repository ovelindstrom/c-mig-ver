# Förslag: Ersätt Quartz med EJB Timer Service

## Problembeskrivning

Den nuvarande Quartz-schemaläggaren har problem med classloader-isolering i Liberty:
- Quartz-trådar kan inte komma åt properties från `globalLib`
- Properties de använder måste in i jar-filen
- Properties måste dupliceras på flera ställen
- Externt beroende (quartz-1.6.0.jar) ökar komplexiteten
- Inte fullt integrerad med Java EE-containerns livscykel
- IBM Migration Tool flaggar detta beroende som **kritiskt** att ersätta

## Rekommenderad lösning: EJB Timer Service

### Varför EJB Timers?

✅ **Native Java EE 7-integration** - Inbyggd i Liberty, inga externa beroenden  
✅ **Korrekt classloader-kontext** - Körs i EJB-containern med full tillgång till resurser  
✅ **Transaktionsstöd** - Automatisk transaktionshantering  
✅ **Persistenta timers** - Överlever serveromstarter  
✅ **Enkel migrering** - Liknande programmeringsmodell som Quartz  
✅ **Container-hanterad livscykel** - Automatisk uppstädning och felhantering  

### Nuvarande Quartz-arkitektur

```
package se.csn.notmotor.ipl.servlets;

SkickaMotorServlet (init)
  └─> Quartz SchedulerFactory
      └─> JobDetail (Notmotor.class)
          └─> Trigger (omedelbar, upprepande)
              └─> Notmotor.execute(JobExecutionContext)
```

**Berörda filer:**
- `NotmotorIplWebb/src/main/java/se/csn/notmotor/ipl/servlets/SkickaMotorServlet.java` - Startar Quartz-schemaläggaren
- `NotmotorJar/src/main/java/se/csn/notmotor/ipl/Notmotor.java` - Implementerar `org.quartz.Job`

### Föreslagen EJB Timer-arkitektur

```
@Startup @Singleton NotmotorSchedulerBean
  └─> @PostConstruct initializeTimers()
      └─> TimerService.createTimer(config)
          └─> @Timeout executeNotmotor(Timer)
              └─> Notmotor.init(url, runControl)
```

## Implementeringsplan

### Steg 1: Skapa EJB Timer Service Bean

**Ny fil:** `NotmotorIplEjb/src/main/java/se/csn/ipl/notmotor/timer/NotmotorSchedulerBean.java`

```java
package se.csn.notmotor.ipl.timer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import se.csn.ark.common.util.Properties;
import se.csn.ark.common.util.logging.Log;
import se.csn.common.servlet.RunControl;
import se.csn.notmotor.ipl.Notmotor;

/**
 * EJB Timer Service för schemaläggning av Notmotor-instanser.
 * Ersätter Quartz-schemaläggaren med native Java EE timer-funktionalitet.
 * 
 * @since 2025-11-06
 */
@Singleton
@Startup
public class NotmotorSchedulerBean {
    
    private Log log = Log.getInstance(NotmotorSchedulerBean.class);
    
    @Resource
    private TimerService timerService;
    
    private RunControl runControl = new RunControl();
    
    @PostConstruct
    public void initializeTimers() {
        log.info("Initialiserar Notmotor EJB Timer Service");
        
        try {
            // Läs autostart-konfiguration
            Properties props = Properties.getInstance("notmotor-ipl");
            int autostart = props.getIntProperty("notmotor.autostart", 0);
            
            if (autostart > 0) {
                log.info("Autostart aktiverad - startar " + autostart + " Notmotor-instans(er)");
                
                // Hämta notmotor URL från properties
                String notmotorUrl = props.getProperty("notmotor.url", 
                    "https://localhost:9443/NotmotorIPL/SkickaMotorServlet");
                
                // Skapa timer för varje autostart-instans
                for (int i = 0; i < autostart; i++) {
                    TimerConfig timerConfig = new TimerConfig();
                    timerConfig.setInfo("Notmotor-" + i + "-" + System.currentTimeMillis());
                    timerConfig.setPersistent(false); // Ej persistent för omedelbar körning
                    
                    // Schemalägg omedelbar körning (1 sekunds fördröjning för att undvika kollision)
                    timerService.createSingleActionTimer(1000L * (i + 1), timerConfig);
                    
                    log.info("Schemalade Notmotor-instans " + i);
                }
            } else {
                log.info("Autostart inaktiverad - inga Notmotor-instanser startade");
            }
            
        } catch (Exception e) {
            log.error("Misslyckades initiera Notmotor-timers", e);
        }
    }
    
    @Timeout
    public void executeNotmotor(Timer timer) {
        String timerInfo = (String) timer.getInfo();
        log.info("Kör Notmotor-timer: " + timerInfo);
        
        try {
            Properties props = Properties.getInstance("notmotor-ipl");
            String url = props.getProperty("notmotor.url", 
                "https://localhost:9443/NotmotorIPL/SkickaMotorServlet");
            
            // Skapa och initiera Notmotor-instans
            Notmotor notmotor = new Notmotor();
            notmotor.init(url, runControl);
            
            log.info("Notmotor-instans slutförd: " + timerInfo);
            
        } catch (Exception e) {
            log.error("Fel vid körning av Notmotor-timer: " + timerInfo, e);
        }
    }
}
```
--- OBS! Inte 100% funktionellt ännu, men man kan använda templaten nedan ---
Det finns ett recept skapat i projektet `ModerniseringsVerktyg`.

Lägg till i rewrite.yml för EJB-projektet (Byt ut Notmotor mot din egen modul. `moduleName` med gemener.):

```yml
---
type: specs.openrewrite.org/v1beta/recipe
name: se.csn.modernize.quartz.NotmotorCreateSchedulerBean
displayName: Create Notmotor Scheduler Bean
description: Creates a EJB Scheduler Bean for Notmotor Processing
recipeList:
  - se.csn.modernize.quartz.CreateSchedulerBean:
      moduleName: notmotor 
```

Lägg till i `pom:build.plugins.plugin.rewrite-maven-plugin`:
```xml
...
<configuration>
   <activeRecipes>
        <recipe>sse.csn.modernize.quartz.NotmotorCreateSchedulerBean</recipe>
    </activeRecipes>
</configuration>
 <dependencies>
    <dependency>
    <groupId>se.csn.modernize</groupId>
    <artifactId>rewrite-recipes</artifactId>
    <version>0.1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```


### Steg 2: Modifiera Notmotor.java

Ta bort Quartz-beroendet från `Notmotor.java`:

```java
package se.csn.notmotor.ipl;

// TA BORT: import org.quartz.Job;
// TA BORT: import org.quartz.JobExecutionContext;
// TA BORT: import org.quartz.JobExecutionException;

/**
 * Huvudklassen, ingångsklassen för notifieringsmotorn.
 */
public class Notmotor extends NotmotorBase /* TA BORT: implements Job */ {
    
    // TA BORT denna metod (behövs inte längre):
    // @Override
    // public void execute(JobExecutionContext context) throws JobExecutionException
    
    // Behåll den befintliga init(String, RunControl)-metoden
    public void init(String anropadURL, RunControl runControl) {
        // ... befintlig implementation ...
    }
    
    // ... resten av klassen oförändrad ...
}
```

### Steg 3: Uppdatera SkickaMotorServlet.java

Förenkla servleten till enbart hälsokontroll (ingen Quartz-schemaläggning):

```java
package se.csn.notmotor.ipl.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import se.csn.ark.common.util.logging.Log;
import se.csn.common.servlet.RunControl;

/**
 * Servlet för Notmotor-hälsokontroller.
 * Schemaläggning hanteras nu av NotmotorSchedulerBean EJB.
 */
public class SkickaMotorServlet extends HttpServlet {
    
    private Log log = Log.getInstance(SkickaMotorServlet.class);
    private RunControl runControl = new RunControl();
    
    @Override
    public void init() throws ServletException {
        super.init();
        log.debug("init");
        // Schemaläggning sker nu automatiskt via @Startup EJB
        // Ingen manuell Quartz-setup behövs
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        // Hälsokontroll-endpoint
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().println("<html><body>");
        response.getWriter().println("<h1>Notmotor Status</h1>");
        response.getWriter().println("<p>Schemaläggning via EJB Timer Service</p>");
        response.getWriter().println("<p>Status: " + runControl.getStatus() + "</p>");
        response.getWriter().println("</body></html>");
    }
    
    @Override
    public void destroy() {
        log.info("SkickaMotorServlet avslutas...");
        super.destroy();
    }
}
```

### Steg 4: Ta bort Quartz-beroende

**NotmotorBuild/pom.xml** - Ta bort från dependencyManagement:

```xml
<!-- TA BORT DETTA:
<dependency>
    <groupId>opensymphony</groupId>
    <artifactId>quartz</artifactId>
    <version>1.6.0</version>
</dependency>
-->
```

**NotmotorLiberty/pom.xml** - Ta bort från copyDependencies:

```xml
<!-- TA BORT DETTA:
<dependency>
    <groupId>opensymphony</groupId>
    <artifactId>quartz</artifactId>
</dependency>
-->
```

Recept kan med fördel skrivas för att göra flera av ändringarna.


## Fördelar med denna lösning

### 1. **Classloader-problemet löst**
- EJB-containern har full tillgång till `globalLib`
- Properties automatiskt tillgängliga via ResourceBundle
- Ingen speciell classloader-konfiguration behövs

### 2. **Förenklade beroenden**
- Ta bort quartz-1.6.0.jar (gammal, ej underhållen)
- Färre JAR-filer i lib/global
- Färre potentiella CVE-sårbarheter

### 3. **Bättre container-integration**
- Automatisk livscykelhantering
- Transaktionsgränser
- JMX-övervakning via Liberty
- Korrekt avstängningshantering

### 4. **Konfiguration**
- Samma properties (`notmotor.autostart`, `notmotor.url`)
- Inga kodändringar i affärslogiken i `Notmotor.init()`
- Transparent för resten av applikationen

## Migreringssteg

1. ✅ Skapa `NotmotorSchedulerBean.java` i NotmotorIplEjb
2. ✅ Uppdatera `Notmotor.java` - ta bort Quartz-interface
3. ✅ Förenkla `SkickaMotorServlet.java` - ta bort Quartz-schemaläggare
4. ✅ Ta bort Quartz-beroenden från POM-filer
5. ✅ Testa autostart-beteende
6. ✅ Verifiera att properties-laddning fungerar
7. ✅ Ta bort quartz JAR från lib/global

## Testplan

1. **Verifiera att @Startup bean laddas:**
   - Kontrollera Liberty-loggar för "Initialiserar Notmotor EJB Timer Service"
   
2. **Verifiera properties-laddning:**
   - Ska se "Laddar ResourceBundle: notmotor-ipl.properties"
   - Inget mer "MissingResourceException"

3. **Verifiera timer-körning:**
   - Kontrollera loggar för "Kör Notmotor-timer: Notmotor-0-..."
   - Verifiera att affärslogik körs

4. **Testa autostart=0:**
   - Ska se "Autostart inaktiverad"
   - Inga timers skapade

## Alternativ: MicroProfile @Schedule (framtida förbättring)

För mer avancerad schemaläggning, överväg MicroProfile Fault Tolerance:

```java
@ApplicationScoped
public class NotmotorScheduler {
    
    @Schedule(cron = "0 */5 * * * ?") // Var 5:e minut
    public void scheduledExecution() {
        // Kör Notmotor
    }
}
```

**Kräver:** `microProfile-3.0` eller senare feature i server.xml

## Återställningsplan

Om problem uppstår:
1. Behåll Quartz JAR-filer i git-historik
2. Återställ commits för steg 1-3
3. Återställ Quartz-beroenden

## Uppskattad arbetsinsats

- Utveckling: 2-3 timmar
- Testning: 1-2 timmar
- Totalt: En halv dag

## Rekommendation

**Gå vidare med EJB Timer Service-ersättning.** Det är standard Java EE-approach, eliminerar classloader-problemet och förenklar arkitekturen. Migreringen är okomplicerad och låg risk.

---

## Sammanfattning av ändringar

### Filer som ska skapas:
- `NotmotorIplEjb/src/main/java/se/csn/notmotor/ipl/timer/NotmotorSchedulerBean.java` (ny EJB)

### Filer som ska modifieras:
- `NotmotorJar/src/main/java/se/csn/notmotor/ipl/Notmotor.java` (ta bort `implements Job`)
- `NotmotorIplWebb/src/main/java/se/csn/notmotor/ipl/servlets/SkickaMotorServlet.java` (förenkla)
- `NotmotorBuild/pom.xml` (ta bort Quartz-beroende)
- `NotmotorLiberty/pom.xml` (ta bort Quartz-beroende)

### Filer som kan tas bort (efter verifiering):
- `lib/global/quartz-1.6.0.jar` (inte längre behövd)

### Ingen påverkan på:
- Properties-konfiguration (oförändrad)
- Affärslogik i Notmotor-klassen (oförändrad)
- Databas- eller MQ-konfiguration (oförändrad)
