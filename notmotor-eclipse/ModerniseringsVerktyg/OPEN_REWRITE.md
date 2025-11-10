# Att arbeta med Open Rewrite

OpenRewrite är den öppna versionen av Moderne, men saknar det UI och Builder som Moderne DevCenter erbjuder.

Den version som vi använder oss av körs som en Maven Plugin med GAV
```xml
<plugin>
	<groupId>org.openrewrite.maven</groupId>
	<artifactId>rewrite-maven-plugin</artifactId>
	<version>6.21.1</version>
<plugin>
```

## Recept

Det finns drygt 3000 community recept att tillgå och som är uppdelade i ett antal moduler. Rewrite Maven Plugin lägger till många av dem, men inte alla. Alla recept hittar man på https://docs.openrewrite.org/recipes.

### Olika typer av recept

I OpenRewrite skiljer man på ett antal olika typer av recept. Dessa är:
1. Imperativa recept: Använder `Visitor`-mönstret för att traversera koden med Lossless Semantic Tree (LST) för att se till att kod följer vissa mönster. Ett exempel är att lägga till `final` modifierare på locala variabler som aldrig blir ändrade.
2. Refaster Template recept: För enkla och repetitive "sök och ersätt" som att byta ut `StringUtil.equals(...)` till `Objects.equals(...)`. Inte alltid LST.
3. Deklarativa recept: Recept som med hjälp av YAML configuration ändrar på något. T.ex tar bort XML-element, byter ut versioner av ett Maven-dependency.
4. Scanning recept: Recept som samlar ihop information från olika filer och skapar tabeller för andra recept att använda. Används t.ex. för att hitta alla pom-filer för analys.

## Receptlådan

För att se alla recept som finns tillgängliga kan man använda `mvn rewrite:discover`. Vill man ha detaljer så använder man `-Ddetail=true`.

Så för att få information om ett recept, i detalj `./mvnw rewrite:discover -Ddetail=true -Drecipe=org.openrewrite.java.format.AutoFormat`

Det som följer med i "rewrite-all", som är en Moderne faciliterad och kontrollerad lista med recept som har skickats in till OpenRewrite, är följande:
- Core - Basrecept för att t.ex. flytta filer, döpa om filer, hitta dupliceringar samt en del basrecept som används av andra recept.
- Java - Allmäna byggstenar för att manipulera på Javakod. 
    - org.openrewrite:rewrite-java`
    - Moderniseringar - Moduler för att uppdatera till en viss Java-version, från 6 till 25.
        - org.openrewrite:rewrite-migrate-java - samlar alla
        - org.openrewrite:rewrite-java-8
        - org.openrewrite:rewrite-java-11
        - org.openrewrite:rewrite-java-17
        - org.openrewrite:rewrite-java-21
        - org.openrewrite:rewrite-java-25
    - Specialområden:
        - Lombok
            - org.openrewrite:rewrite-java-lombok
- Maven - För att manipulera eller hantera pom.xml
    - org.openrewrite:rewrite-maven
- *ML - Manipulera på *ML-filer
    - org.openrewrite:rewrite-xml
    - org.openrewrite:rewrite-toml
    - org.openrewrite:rewrite-yaml
    - org.openrewrite:rewrite-json (ja, jag räknar json som en ML)
- Properties
    - org.openrewrite:rewrite-properties

Alla recept är POJOs och distribueras som jar-filer och man kan alltså låsa ner även olika versioner av recept och miljöer eller excludera de man inte vill ha med.

Vissa recept finns inte i "rewrite-all" utan man behöver lägga till dessa som beroenden till pluginet.

Exempelvis om man vill köra något av [Liberty recepten](https://docs.openrewrite.org/recipes/java/liberty) för att migrera från WASM till Liberty.

```xml
<build>
    <plugins>
      <plugin>
        <groupId>org.openrewrite.maven</groupId>
        <artifactId>rewrite-maven-plugin</artifactId>
        <version>6.21.0</version>
        <configuration>
          <exportDatatables>true</exportDatatables>
          <activeRecipes>
            <recipe>org.openrewrite.java.liberty.MigrateFromWebSphereToLiberty</recipe>
          </activeRecipes>
        </configuration>
        <dependencies>
          <dependency>
            <groupId>org.openrewrite.recipe</groupId>
            <artifactId>rewrite-liberty</artifactId>
            <version>1.22.0</version>
          </dependency>
        </dependencies>
      </plugin>
    </plugins>
  </build>
```



## Hur man kör recept

Det finns ett flertal olika sätt att köra recept på. Det två vi kommer att primärt använda oss av är:
- Maven POM
- Maven CLI

### Maven POM

Det här är det vanligaste och enklaste sättet att köra recept. Det passar sig utmärkt att lägga bygget i en egen eller egna profiler, beroende på vad man vill göra. Rewrite-plugin är bunden till Mavens livscykelfas `process-test-classes` (vilket ibland kan ställa till det men mer om det senare).

Om man kör t.ex. `mvn package` så kommer det recept som man definerat i plugin.configuration att köras. Perfekt för att köra style-guides, formatering och liknande. Inte lika kul att köra stora modifieringar.

!! Använd profiler !!

Ett exempel på en profil som tar bort icke använda importer och modifierar Pom-filerna så de följer Best Practice. Den använder sig även av Googles Java Style Guide för att formatera Java-koden.

```xml
<profiles>
		<profile>
			<id>styling</id>
			<build>
				<plugins>
					<plugin>
						<groupId>org.openrewrite.maven</groupId>
						<artifactId>rewrite-maven-plugin</artifactId>
						<version>6.21.0</version>
						<configuration>
							<exportDatatables>true</exportDatatables>
							<activeRecipes>
								<recipe>org.openrewrite.java.RemoveUnusedImports</recipe>
								<recipe>org.openrewrite.maven.BestPractices</recipe>
							</activeRecipes>
							<activeStyles>
								<style>org.openrewrite.java.GoogleJavaFormat</style>
							</activeStyles>
						</configuration>
					</plugin>
				</plugins>
			</build>
		</profile>
```

Nu kan vi köra denna med följande kommando:
```sh
mvn rewrite:dryRun -Pstyling
```

Notera att vi här gör en `dryRun`, d.v.s vi ändrar inget. I `/target/rewrite/` hittar du en patch-fil och information om vilka filer som skulle bli påverkade.

För att köra på riktigt: `mvn rewrite:run`.

#### Konfigurera Deklarativa recept

Många recept kräver att man deklarerar hur de ska användas med hjälp av en YAML-fil. Rewrite kommer att leta efter en fil som heter `rewrite.yml` och även eventuell `checkstyle.xml` fil.

Genom att skapa `rewrite.yml` filen i det projekt som du kör rewrite så behöver du inte göra något speciellt.

Ett exempel för att hitta `System.out.println(..)`
```yml
---
type: specs.openrewrite.org/v1beta/recipe
name: se.csn.recipes.example.FindMethodsExample
displayName: Find method usages example
recipeList:
  - org.openrewrite.java.search.FindMethods:
      methodPattern: java.io.PrintStream println(..)
```

Nu kan man använda namnet som är deklarerat i receptet i pom.xml.

```xml
<configuration>
    <activeRecipes>
        <activeRecipe>se.csn.recipes.example.FindMethodsExample</activeRecipe>
	</activeRecipes>
</configuration>
```

Resultatet kommer ut i patch-filen.

### Multipla recept i samma fil

Det går utmärkt. Det är t.o.m rekommenderat.

Det går också att skapa olika namngivna filer som t.ex `rewrite-pom.yml` som samlar alla saker vi vill göra med våra POMar. Med denna kan jag nu köra bara `se.csn.recipes.modernize.Pom` som sedan aktiverar resten av recepten. Klart enklare på en kommandorad.

Man konfigurerar vilken fil man ska använda med `-DconfigLocation` eller `<configLocation>`.

```xml
<configuration>
    <activeRecipes>
        <activeRecipe>se.csn.recipes.modernize.Pom</activeRecipe>
	</activeRecipes>
	<configLocation>${maven.multiModuleProjectDirectory}/rewrite-pom.yml</configLocation>
</configuration>
```

```yml
---
type: specs.openrewrite.org/v1beta/recipe
name: se.csn.recipes.modernize.Pom
displayName: All POM Modernization Recipes
description: Applies all modernization recipes including changing to the new superpom, removing developers section, adding JUnit 3 dependency.
recipeList:
  - se.csn.recipes.modernize.pom.NewSuperPomParent
  - se.csn.recipes.modernize.pom.UpdateProjectParent
  - se.csn.recipes.modernize.pom.DropDevelopers
  - se.csn.recipes.modernize.pom.AddJunit3TestDependency
  - se.csn.recipes.modernize.pom.UpdateNotmotorJarVersion
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
name: se.csn.recipes.modernize.pom.UpdateProjectParent
displayName: Make sure that all projects use the new NotmotorBuild as parent
recipeList:
  - org.openrewrite.maven.ChangeParentPom:
      oldGroupId: se.csn
      oldArtifactId: notmotor
      newVersion: 1.0-SNAPSHOT

---
type: specs.openrewrite.org/v1beta/recipe
name: se.csn.recipes.modernize.pom.DropDevelopers
displayName: Delete <developers> section
description: Removes the <developers> section from Maven POM files.
recipeList:
  - org.openrewrite.xml.RemoveXmlTag:
      # The XPath expression to target the <developers> element anywhere in a POM file.
      xPath: /project/developers
      fileMatcher: '**/pom.xml'
---
type: specs.openrewrite.org/v1beta/recipe
name: se.csn.recipes.modernize.pom.AddJunit3TestDependency
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
name: se.csn.recipes.modernize.pom.UpdateNotmotorJarVersion
displayName: Update notmotorjar dependency version to 2.1-SNAPSHOT
description: Updates the version of se.csn.notmotor:notmotorjar dependency to 2.1-SNAPSHOT
recipeList:
  - org.openrewrite.maven.ChangeDependencyGroupIdAndArtifactId:
      oldGroupId: se.csn.notmotor
      oldArtifactId: notmotorjar
      newVersion: 2.1-SNAPSHOT
```

## Direkt på kommandoraden

Ibland vill man bara köra något enstaka recept. T.ex. så kan vi söka efter `System.out.println()` på kommandoraden med:

```sh
mvn rewrite:run -Drewrite.activeRecipes=org.openrewrite.java.search.FindMethods -Drewrite.options="methodPattern=java.io.PrintStream println(..)" -Drewrite.exportDatatables=true
```

Det här sätter ` /*~~>*/` före alla hittade rader så man enkelt kan leta efter dem.

Även recept som är definierade i `rewrite.xml` kan anropas.

### Hoppa över steg i bygget

Rewrite är bundet till `process-test-classes`, vilket ibland kan ställa till det, om det inte går att t.ex. kompilera eller om det finns fel i ett test. Det är därför viktigt att det går att göra `mvn package` innan man gör en `mvn rewrite:run | dryRun`.

Om man ändå behöver köra rewrite, t.ex. för att rätta till saker som går fel, kan man välja att hoppa över de olika stegen. I Maven finns följande:
- maven.main.skip=true - hoppa över Main-delen (det som förut hette compile)
- maven.test.skip=true - hoppa över Test
- checkstyle.skip=true - hoppa över checkstyle

## Parsningbarhet

Lossless Semantic Tree (LST) är kärnan i Rewrite och innebär att man säkrar att varje aspekt av javafilen, så som kommentarer, whitespace och foramttering. Ingen information försvinner när man analyserar klasserna.

Men, det ställer till det när det finns fel i vad som är skrivet i .java filerna och vad som är parsningsbart. T.ex förväntar sig rewrites Javakod-analysatorn att sådant som korrekt JavaDoc. Betrakta all JavaDoc som om det vore en HTML-sida och använda korrekta formaterings-taggar för ditt innehåll.

```
[INFO] Project [NotmotorJar] Parsing source files
[WARNING] There were problems parsing some source files
```

Vanliga fel som ställer till det för parsningen är:

**Whitespace-fel**

I många filer så har det smugit in sig tab-tecken, och ibland felaktigt. Det bästa, för parsningen, är att alltid använda sig av space. Börja alltid med att formatera om din javafil.




**Felaktig ordning på JavaDoc-elementen**

Fel
```java
/**
 * @since 2007-mar-23
 * 
 * Beskrivning av klassen.
 */
```

Korrekt
```java
/**
* Beskrivning av klassen.
 * 
 * @since 2007-mar-23
 */
```

** @throws utan throws, params utan param**

Dubbe-fel
```java
/**
* @param milliseconds Det antal millisekunder som tråden ska sova
* @throws RuntimeException om sovandet misslyckades
*/
public void sleepTick();
```

Korrekt
```java
/**
* Tickar till sleep. Kan kasta RuntimeExceptions men vi hanterar inte Unchecked exceptions.
*/
public void sleepTick();
```

**Inkomplett JavaDoc**

Inte helt rätt...
```java
/**
* Eftersom ett webb-gui kan gå in och modifiera tillstånd medan vi gör något
* måste vi kontrollera att vi fortfarande befinner oss i samma tillstånd som
* vi trodde innan vi går till ett nytt.
*/
public boolean makeTransition(int fromState, int intoState);
```

Rätt
```java
/**
	 * Eftersom ett webb-gui kan gå in och modifiera tillstånd medan vi gör något
	 * måste vi kontrollera att vi fortfarande befinner oss i samma tillstånd som
	 * vi trodde innan vi går till ett nytt.
	 * 
	 * @param fromState det tillstånd vi trodde vi befann oss i
	 * @param intoState det tillstånd vi vill gå in i
	 * @return true om vi fortfarande befinner oss i fromState och övergången
	 *         därför kunde göras; false om vi inte längre befinner oss i fromState
	 *         och övergången därför inte kunde göras
	 */
	public boolean makeTransition(int fromState, int intoState);
  ```



