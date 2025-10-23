# Migrationssteg

För att modernisera befintliga WASM-moduler och istället använda OpenLiberty som server, behöver ett antal steg göras.

Under moderniseringen av Notmotorn har jag tagit fram ett antal skript för att hjälpa till med att hantera och korrigera Java-filerna. Dessa finns i ModerniseringsVerktygsladan och om ett skript refereras till som `{MV}/` är det där filen finns. Ibland är det enklast att kopiera skripten till root-katalogen av projektet.

Innan och efter varje steg är det rekommenderat att göra en ny commit så att det enkelt går att diffa filerna för varje åtgärd.

## 1. Rengöring och preparering

### 1.1 Remsa ut Eclipse-filer

Det är rekommenderat att man undviker att versionshantera IDE-specifika filer när man anänder sig av Maven eller liknande projekthanterare. Moderna IDE:er hanterar att läsa projektet från Maven/Gradle.

Linux
```sh
find . \( -name '.project' -o -name '.classpath' -o -name '.factorypath' -o -type d -name '.settings' -o -type d -name '.metadata' \) -delete
```

Windows CMD behöver två kommandon
```cmd

FOR /R . %%f IN (.project .classpath .factorypath) DO IF EXIST "%%f" DEL /F /Q "%%f"

FOR /D /R . %%d IN (.settings .metadata) DO IF EXIST "%%d" RMDIR /S /Q "%%d"
```

### 1.2 Rensa ut eventuella .class-filer.

```sh
find . -name '*.class' -delete
```

Om det finns en övergripande POM som hanterar hela projektets moduler, kör en `mvn clean` också.

### 1.3 Fixa encoding-fel

Filerna i SVN har en blandning av UTF-8 och ISO-8859-1. Rekommendationen är att använda UTF-8 och det är vad som ska vara satt i pom-filerna.

För att rätta till det mesta finns 2 olika script.

`fix_encoding.py` itererar igenom alla filer och rättar till encodings.
`fix_maven_encoding_errors.sh` fångar upp resten, om det fortfarande finns några fel som Maven inte klarar av.

### 1.4 Rensa pre-package JavaDoc

I en massa filer hittar vi inledande JavaDoc liknande den här:

```java
/**
 * Skapad 2007-apr-23
 * @author Jonas �hrnell (csn7821)
 * 
 */
package se.csn.notmotor.admin;
```

Det här är tyvärr felaktigt användande av JavaDoc. Före `package` deklaration i en Java-fil får det bara finnas icke-kodrelaterade kommentarer.
Alltså måste dessa fixas till.

Det korrekta är att flytta det där det hör hemma, i class-javadoc, liknande det här:

```java
/*
 * Copyright 2007 CSN.
 */
package se.csn.exempel;

import se.csn.common.servlet.RunControl;

/**
 * Konstanter för Exempel State-maskinen.
 * 
 * @author Förnamn Efternamn (csn0000)
 * @since 1970-01-01
 * 
 */
public class Constants extends ExempelStateMachine {

    public Constants(){
        super(null, new RunControl());
    }
}
```

När man skriver en JavaDoc, allså börjar med `/**` så är det "kraftfullt rekommenderat" (vilket är Java-språk för "nästan obligatoriskt") att alltid ha med en kort beskrivande text.

Sedan föjer annotationerna i föjande ordning:
- @author
- @version
- @since
- @param (i samma ordning som de kommer i metodanropet)
- @return
- @throws or @exception (i samma ordning som de kommer i `throws`)
- @deprecated

I verktygslådan finns python-skriptet `{MV}/remove_pre_package.py` som är mycket mer "brutal" och helt enkelt bara tar bort allt som är före `package`.

## 2. Föräldrar-beroenden

I `se.csn:superpom:2.4` finns en del beroenden och konfigurationer som inte är optimala för modernisering och som visade sig ställa till det för OpenRewrite. Det är därför rekommenderat att man använder sig av en `se.csn:superpom:11.3` som "top-parent". Det finns en version för varje Java version.

I `{MV}/rewrite-pom.yml` finns ett recept, `se.csn.recipes.modernize.NewSuperPomParent` som kan användas för att sätta en ny parent.

Receptet `se.csn.recipes.modernize.UpdateProjectParent` kan användas för att snabbt sätta alla modulers parent till den version som finns i Build-modulen.

Strukturen på de flesta äldre projekt liknar följande:
- <projekt>Build - Bygger allt/det mesta, innehåller referenser till underliggande moduler, på samma nivå (<modules><module>../<projekt>SubModule</...>)
  - Detta är ett pom-projekt och ska vara parent till de projekt som är deklarerade i <modules>. Har `se.csn:superpom:3.0-SNAPSHOT` som parent.
  - Här definierar man med fördel de beroenden och versioner som gäller över alla modulerna. Detta görs i <dependencyManagement>.
  - Undvik att deklarera <dependencies> i denna 
- <projekt>Properties - De properties som används av olika moduler.
- <project>Jar - Gemensamma modeller, entiteter och utilities som delas mellan olika moduler 
- <project>Ipl... - Moduler för integrationsplatformen 
  - <project>IplWebb - Grafisk gränssnitt, WSDL-filer, SOAP services...
  - <project>IplEjb - Enterprise Java Beans för Ipl
  - <project>IplEar - Packar ihop Web, Ejb, Jar osv till en EAR-fil.
  - <project>IplClient - Klient för andra system att använda sig av. Använder sig av `se.csn:superpom:3.0-SNAPSHOT` som parent då den är "fristående".
- <project>Admin... - Moduler för administration av servicen.
  - <project>AdminWebb - Grafisk gränssnitt, WSDL-filer, SOAP services...
  - <project>AdminEar - Packar ihop Web, Ejb, Jar osv till en EAR-fil.
- <project>...TestClient

Ny modul i alla OpenLiberty-baserade projekt är:
- <project>Liberty - Innehåller OpenLiberty server-definitioner och filer för att bygga en OCI Image. Det skiljer inget i hur man sedan deployar sin image.
  - Ska innehålla en sektion som kopierar de binärer från EAR, WAR och JAR som behövs samt drar ner de beroenden som krävs av applikationerna och som inte tillhandahålls av OpenLiberty genom `<features>` i `server.xml`.

## 3. Omstrukturering av koden

## 3.1 Omstrukturering av källkodens source-foldrar

De projekt och moduler som analyserats har en mix av olika strukturer och om inte alla element finns exakt där de ska vara och pom.xml innehåller rätt mängd konfiguration så bygger det inte som det ska. Ett moderniserat projekt **ska** därför följa [Maven Standard Directory Layout](https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html)

Python-skriptet `{MV}/maven_structure_converter.py` kan hjälpa till med att flytta runt filer.

## 3.2 Modernisera POM-filerna.

Du kommer att behöva städa i pom-filerna en hel del.

För det finns det ett antal recept som manipulerar på XML-strukturer. De följer mönstret `se.csn.recipes.modernize.Drop<XML ELEMENT>` och är definierade i `{MV}/rewrite-pom.yml`.

- `se.csn.recipes.modernize.DropDevelopers`
- `se.csn.recipes.modernize.DropSourceDirectory`
- `se.csn.recipes.modernize.DropTestSourceDirectory`
- `se.csn.recipes.modernize.DropOutputDirectory`
- `se.csn.recipes.modernize.DropTestOutputDirectory`

```sh
mvn -U -N org.openrewrite.maven:rewrite-maven-plugin:run \
    -Drewrite.activeRecipes=se.csn.recipes.modernize.DropDevelopers,se.csn.recipes.modernize.DropSourceDirectory,se.csn.recipes.modernize.DropTestSourceDirectory,se.csn.recipes.modernize.DropOutputDirectory,se.csn.recipes.modernize.DropTestOutputDirectory
```

## 3.3 Updatering av Javafiler och JavaDoc

När man analyserar Javafiler med Open Rewrite behöver de vara Lossless Semantic Tree (LST) positiva. Så är inte alltid fallet. Det finns mer information i OPEN_REWRITE.md.

Nedan följer en del recept som hjälper en hel del.

## 3.3.1 Rensua ut felaktiga ISO-Latin inehåll från Java och Javadoc

```sh
mvn -U org.openrewrite.maven:rewrite-maven-plugin:run -Drewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-static-analysis:RELEASE -Drewrite.activeRecipes=org.openrewrite.staticanalysis.maven.MavenJavadocNonAsciiRecipe -Drewrite.exportDatatables=true
```

## 3.3.2 Ta bort trailing whitespaces från filer

```sh
mvn -U org.openrewrite.maven:rewrite-maven-plugin:run -Drewrite.activeRecipes=org.openrewrite.java.format.RemoveTrailingWhitespace -Drewrite.exportDatatables=true
```
## 3.3.3 Formatera alla Java filer

```sh
mvn -U org.openrewrite.maven:rewrite-maven-plugin:run -Drewrite.activeRecipes=org.openrewrite.java.format.AutoFormat -Drewrite.exportDatatables=true
```

## 3.3.4 Lös analys-fel.

Det mesta handlar om JavaDoc som är på fel plats eller icke komplett. Börja där. Finns beskrivet i OPEN_REWRITE.md

Nästa jag gör är att lägga till saknade @Overrides. Om metoden gör en @Overrides, så kommer LST-trädet att bli fel om det inte står.

```sh
mvn -U org.openrewrite.maven:rewrite-maven-plugin:run -Drewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-static-analysis:RELEASE -Drewrite.activeRecipes=org.openrewrite.staticanalysis.MissingOverrideAnnotation -Drewrite.exportDatatables=true --errors
```

Jag tycker också att man kan ersätta "Skapad" med annoteringen @since.

`find . -name '*.java' -print0 | xargs -0 sed -i '' 's/\* Skapad/* @since/g'`

### 3.3.5 Allmänt städande

Det finns ett litet trevligt recept i Open Rewrite, `org.openrewrite.staticanalysis.CodeCleanup` som fixar en del saker som SAST brukar påpeka.
```sh
mvn -U org.openrewrite.maven:rewrite-maven-plugin:run -Drewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-static-analysis:RELEASE -Drewrite.activeRecipes=org.openrewrite.staticanalysis.CodeCleanup  --errors
```

Om man vill ta bort alla `@author` taggar gör man det med.

```sh
mvn -U org.openrewrite.maven:rewrite-maven-plugin:run -Drewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-static-analysis:RELEASE -Drewrite.activeRecipes=org.openrewrite.staticanalysis.RemoveJavaDocAuthorTag -Drewrite.exportDatatables=true --errors
```

## 4. Kodmigrering

Nu börjar själva migreringen.

### 4.1 Decoupling

Det första man måste göra är att frikoppla sig från den nuvarande SuperPomen.

För det kan man använda sig av följande Rewrite i Build-projektet för att uppdatera alla moduler som eventuellt använder dem.

I exemplet har jag använd mig av 3.0-SNAPSHOT, men man ska använda sig av de Java-språknivå-beroende super-pommarna som finns i Nexus.


```yml
---
type: specs.openrewrite.org/v1beta/recipe
name: se.csn.recipes.modernize.AllRecipes
displayName: All Modernization Recipes
description: Applies all modernization recipes including changing to the new superpom, removing developers section and ordering imports.
recipeList:
  - se.csn.recipes.modernize.NewSuperPomParent
  - se.csn.recepes.modernize.NewNotmotorParent
  - se.csn.recipes.modernize.DropDevelopers
  - se.csn.recipes.modernize.AddJunit3TestDependency
  - org.openrewrite.staticanalysis.maven.MavenJavadocNonAsciiRecipe
  - org.openrewrite.staticanalysis.RemoveEmptyJavaDocParameters
  - org.openrewrite.java.cleanup.RemoveUnusedImports
  - org.openrewrite.java.format.AutoFormat
---
type: specs.openrewrite.org/v1beta/recipe
name: se.csn.recipes.modernize.NewSuperPomParent
displayName: Change to use the new superpom as parent
recipeList:
  - org.openrewrite.maven.ChangeParentPom:
      oldGroupId: se.csn
      oldArtifactId: superpom
      newVersion: 3.0-SNAPSHOT
---
type: specs.openrewrite.org/v1beta/recipe
name: se.csn.recipes.modernize.NewNotmotorParent
displayName: Make sure that all projects use the new Notmotor parent
recipeList:
  - org.openrewrite.maven.ChangeParentPom:
      oldGroupId: se.csn
      oldArtifactId: notmotor
      newVersion: 1.0-SNAPSHOT

---
type: specs.openrewrite.org/v1beta/recipe
name: se.csn.recipes.modernize.DropDevelopers
displayName: Delete <developers> section
description: Removes the <developers> section from Maven POM files.
recipeList:
  - org.openrewrite.xml.RemoveXmlTag:
      # The XPath expression to target the <developers> element anywhere in a POM file.
      xPath: /project/developers
      fileMatcher: '**/pom.xml'
---
type: specs.openrewrite.org/v1beta/recipe
name: se.csn.recipes.modernize.AddJunit3TestDependency
displayName: Add JUnit 3 Test Dependency
description: Adds the classic JUnit 3 dependency with test scope to pom.xml.
recipeList:
  - org.openrewrite.maven.AddDependency:
      # Required parameters for the dependency coordinate
      groupId: junit
      artifactId: junit
      version: 3.8.1 # Specify the version you need
      # Optional parameter to set the scope
      scope: test
      # We only want to add JUnit 3 if the project is using it
      onlyIfUsing: junit.framework.*
---
type: specs.openrewrite.org/v1beta/recipe
name: se.csn.recipes.modernize.FixByteArrayDataSourceImport
displayName: Change ByteArrayDataSource import from apache.soap to javax.mail
description: Changes import from org.apache.soap.util.mime.ByteArrayDataSource to javax.mail.util.ByteArrayDataSource
recipeList:
  - org.openrewrite.java.ChangeType:
      oldFullyQualifiedTypeName: org.apache.soap.util.mime.ByteArrayDataSource
      newFullyQualifiedTypeName: javax.mail.util.ByteArrayDataSource
```


För att köra dessa:

```sh
mvn -U org.openrewrite.maven:rewrite-maven-plugin:run -Drewrite.activeRecipes=se.csn.recipes.modernize.AllRecipes`
```


## 4.2 Modernisera till Java 11

```sh
mvn -U org.openrewrite.maven:rewrite-maven-plugin:run -Drewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-migrate-java:RELEASE -Drewrite.activeRecipes=org.openrewrite.java.migrate.Java8toJava11 -Drewrite.exportDatatables=true --errors
```


# 5 Migrera till OpenLiberty

Nu ska vi börja packa om applikationen.

Jag kör denna från det nya Liberty-projektet, men det går att köra varifrån som helst. 

Börja med att ladda ner [Migration Toolkit for Application Binaries](https://www.ibm.com/support/pages/migration-toolkit-application-binaries).

Direktlänk till [AppScannerIntaller.jar](https://public.dhe.ibm.com/ibmdl/export/pub/software/websphere/wasdev/downloads/wamt/ApplicationBinaryTP/binaryAppScannerInstaller.jar) 
och kör `java -jar binaryAppScannerInstaller.jar`. Acceptera licensen och standard installationsfolder.

Du får nu en `wamt/` folder med filerna `binaryAppScanner.jar` och `scanner.properties`.
Documentationen för Scannern finns på https://www.ibm.com/docs/en/wamt?topic=migration-toolkit-application-binaries

Man kan använda sig av CLI options men det kan vara enklare att använda `scanner.properties` för
definera vad man vill göra.  Använd `--output=<fil-att-skriva-rapporten-till>`.

Ett exempel på att analysera en .ear-fil.

```sh
java -jar wamt/binaryAppScanner.jar target/notmotor-ipl-ear-0.1-SNAPSHOT.ear --all --targetAppServer=openLiberty --targetJava=java11 --targetJavaEE=ee7 --output=ipl.ear_Migration.html
```

De viktiga elementen här är:
`--targetAppServer=openLiberty` - vilket är mål-app-servern.
`--targetJava=java11`           - vilken Java-version tänker vi bygga mot
`--targetJavaEE=ee7`            - vilken Enterprise Edition vi har som mål. Här måste man skilja på `targetJavaEE` och `targetJakartaEE`.

Nu kommer du att få en rapport som berättar hur man migrerar till OpenLiberty. Den är väldetaljerad och ger även en OpenRewrite recept rekommendation.
Denna är dock för version rewrite-maven-plugin:6.9.0 av plugin och rewrite-migrate-java:3.10.0. Nuvarande är rewrite-maven-plugin:6.21.1 och rewrite-migrate-java:3.19.0.

De flesta som inte hittas i listan är migrerade till `org.openrewrite.java.migrate.UpgradeToJava11`














TODO: 
Senare
## 4.3 Migrera till Commons Lang 3

```sh
mvn -U org.openrewrite.maven:rewrite-maven-plugin:run -Drewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-apache:RELEASE -Drewrite.activeRecipes=org.openrewrite.apache.commons.lang.UpgradeApacheCommonsLang_2_3 -Drewrite.exportDatatables=true
```

## Log4j til Log4j2

org.openrewrite.java.logging.log4j.Log4j1ToLog4j2