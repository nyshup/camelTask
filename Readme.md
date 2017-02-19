# Solution

### Build
* mvn clean package

### Configuration

#### Services configuration
Configuration is saved in "configuration" module and shared between all other modules.
To change parameters "application.properties" file should be edited.
After changes all artifacts should be rebuilded: 
    * mvn clean package
    
#### Important: 
Parameter "output.file.dir" should be set with *absolute* path 
to directory where http service will save files. 

#### ActiveMQ configuration for integration test
Coniguration file:
   * integrationTest/src/test/resources/activemq.xml

### Run
* http service
    * java -jar http-service-1.0-SNAPSHOT.jar 
* jms service
    * java -jar jms-service-1.0-SNAPSHOT.jar

### Integration test
Integration test is placed in module integrationTest. 
Before running test next services will be started automatically:
   * http service
   * jms service
   * ActiveMQ  

Test will be run on maven's phase "integration-test"
    * To run build with integration test: mvn verify

