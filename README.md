# mrules-jboss-test
Testing [MRules](https://mrules.xyz) in a JBoss environment with Arquilian

To launch :

* Clone the repository
* Put the licence file in the src/test/resources directory
* Execute `mvn test` with the JAVA_HOME environment variable pointing to a JDK 7

Or, just execute `mvn test -Dmrules.licence.file=/path/to/mrules.licence` to force use  of an external licence file, in which case you don't need to put it in the src/test/resources directory.
