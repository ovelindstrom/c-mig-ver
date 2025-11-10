# Setup

- install eclipse - https://www.eclipse.org/downloads/packages/release/2020-06/r
- package/install the EAR projects
- place the generated archives in the `\poc\csn-liberty\app\dropin`


## Misc

- nedostajuci lib:
- manually add j2ee to local maven - https://repo.maven.apache.org/maven2/milyn/j2ee/1.4/
`mvn install:install-file -Dfile=j2ee.jar -DgroupId=javax.j2ee -DartifactId=j2ee -Dversion=1.4 -Dpackaging=jar`