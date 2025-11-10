# Properties Configuration for Notmotor Liberty

## Overview

Properties files are packaged into separate JAR files and deployed to Liberty's `lib/global/` directory, making them available on the classpath for all applications.

## Structure

### IPL (Integration Platform) Properties
Used by **notmotoriplear.ear**:
- `ipl-ark-properties.jar` - Contains ark.properties and arkitektur.properties from GemensammaProperties/build/IPL/
- `ipl-notmotor-properties.jar` - Contains notmotor-ipl.properties from NotmotorProperties/build/IPL/
- `ipl-log-properties.jar` - Contains log4j properties from NotmotorProperties/build/IPL/properties/log/

### IW (Internal Web) Properties  
Used by **notmotoradminear.ear**:
- `iw-ark-properties.jar` - Contains ark.properties and arkitektur.properties from GemensammaProperties/build/IW/
- `iw-notmotor-properties.jar` - Contains notmotor.properties from NotmotorProperties/build/IW/
- `iw-log-properties.jar` - Contains log4j properties from NotmotorProperties/build/IW/properties/log/

## How It Works

1. **Build Phase**: Maven jar plugin creates separate JAR files for each property set
2. **Package Phase**: Maven antrun plugin copies JARs to `${server.config.dir}/lib/global/`
3. **Runtime**: Liberty's `globalLib` library (defined in server.xml) includes all `*.jar` from lib/global/
4. **Application Access**: Both EARs reference `globalLib` via `commonLibraryRef` in their classloader config
5. **ResourceBundle Loading**: Java's ResourceBundle.getBundle() finds properties files in the classpath

## Benefits

✅ **Centralized**: Single source of truth for properties  
✅ **Environment-specific**: IPL and IW properties are separated  
✅ **No WAR rebuilds**: Update properties without repackaging applications  
✅ **Standard approach**: Follows Liberty's shared library pattern  
✅ **Classpath available**: Works with ResourceBundle and direct file loading  

## Maven Configuration

The configuration is in `NotmotorLiberty/pom.xml`:

```xml
<!-- Creates property JARs -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-jar-plugin</artifactId>
    <executions>
        <execution>
            <id>create-ipl-ark-properties-jar</id>
            ...
        </execution>
        <!-- More executions for IW, notmotor, log properties -->
    </executions>
</plugin>

<!-- Copies JARs to Liberty -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-antrun-plugin</artifactId>
    ...
</plugin>
```

## Server.xml Configuration

```xml
<!-- Global library includes all JARs from lib/global -->
<library id="globalLib" apiTypeVisibility="+third-party">
    <fileset dir="${server.config.dir}/lib/global" includes="*.jar"/>
</library>

<!-- Both EARs use the global library -->
<enterpriseApplication id="notmotoriplear" location="notmotoriplear.ear">
    <classloader commonLibraryRef="globalLib" apiTypeVisibility="+third-party"/>
</enterpriseApplication>

<enterpriseApplication id="notmotoradminear" location="notmotoradminear.ear">
    <classloader commonLibraryRef="globalLib" apiTypeVisibility="+third-party"/>
</enterpriseApplication>
```

## Troubleshooting

### Properties Not Found

1. Check JAR files exist: `ls -la target/liberty/wlp/usr/servers/notmotorn/lib/global/`
2. Verify JAR contents: `jar tf target/liberty/wlp/usr/servers/notmotorn/lib/global/ipl-notmotor-properties.jar`
3. Check Liberty logs for classloader errors

### Wrong Properties Loaded

Ensure the correct application uses the correct properties:
- **notmotoriplear** → IPL properties (notmotor-ipl.properties)
- **notmotoradminear** → IW properties (notmotor.properties)

### Updating Properties

1. Edit source files in `NotmotorProperties/build/` or `GemensammaProperties/build/`
2. Rebuild: `mvn clean package` in NotmotorLiberty
3. Liberty dev mode will auto-detect changes and reload

## Source Locations

- **IPL Properties**: `NotmotorProperties/build/IPL/properties/`
- **IW Properties**: `NotmotorProperties/build/IW/properties/`
- **IPL Ark**: `GemensammaProperties/build/IPL/properties/arkitektur/`
- **IW Ark**: `GemensammaProperties/build/IW/properties/arkitektur/`
