Notifieringsmotorn
OM PROJEKTET
Läs Driftdok och systembeskrivning, finns på G:. Notifieringsmotorn levererades
som en del av brevärendesystemet, men konstruerades av arkitektenheten.

BYGGE
Notifieringsmotorn består av två ear-filer: notmotor-ipl och notmotor-iw. Dessa byggs med Maven.


1. Installera Maven.
Det finns en installer på http://csn24int/maven/NSIS/maven_2.0.8_csn_mod.exe, ladda hem och kör.

Maven är ett open source-verktyg som kan kompilera och paketera kod, skapa
rapporter mm. Lite dokumentation finns på Wikin, här:
http://wiki.csnnet.int/Wiki-sidor/mavenhandbok.aspx
Maven-projektets hemsida är: http://maven.apache.org/

När Maven är installerat bör du testa installationen genom att starta en
kommandotolk (cmd) och köra kommandot

mvn -v

Du bör få svaret
Maven version: 2.0.8 eller senare
Java version: 1.5.0_14

Observera att JAVA_HOME måste vara satt till t.ex. C:\Utvpgm\jdk1.5.0_14
Lägg även till %JAVA_HOME%\bin till path-variabeln.


2. Checka ut koden.
Välj ut eller skapa en katalog som koden ska checkas ut till, exempelvis
C:\java\build\notmotor. Denna katalog blir din byggkatalog.

Kör scriptet "getFromHarvest.bat" (följer med maven-installationen).
Detta batscript samlar in nödvändiga användaruppgifter och anropar
programmet hco, Harvest CheckOut, som i sin tur plockar ut en läskopia
av all kod i det angivna projektet till den sökväg du angav.
Välj att checka ut projektet: Notmotor

När scriptet är klart bör du ha följande kataloger i din byggkatalog:
NotmotorAdminEAR
NotmotorAdminWebb
NotmotorBuild
NotmotorIplClient
NotmotorIplEjb
NotmotorIplWebb
NotmotorIplEAR
NotmotorJar


3. Kör Maven.
Gå in i den nyss uthämtade katalogen NotmotorBuild i din byggkatalog
och kör kommandot

mvn package

Nu kommer Maven att kompilera all kod, köra enhetstester och bygga de
två EAR-filerna. Första gången du kör scriptet måste Maven hämta hem
ett antal dependencies så det kommer att ta en stund (några minuter).
Därefter bör ett bygge ta runt en minut.

När bygget är klart har maven skapat två EAR-filer:
<byggkatalog>/NotmotorAdminEAR/target/notmotor-admin-ear-SNAPSHOT.ear
<byggkatalog>/NotmotorIplEAR/target/notmotor-ipl-ear-SNAPSHOT.ear


4. Distribuera ear-filer och installationsanvisning till filservern
Kör scriptet dist.bat (följer med maven-installationen).

Scriptet kommer att
-skapa en ny katalog under \\csnutv\pubopen\Wint\dist\notmotor
-kopiera dit notmotor-admin-ear och notmotor-ipl-ear, med katalognamnet som en extra del i namnet
-kopiera in installationsanvisningen i katalogen, tillsammans med xsl-schemat

