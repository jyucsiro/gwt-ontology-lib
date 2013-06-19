GWT Ontology UI Toolkit
=======================
This contains Java libraries for creating GWT Web Applications.

Author: Jonathan Yu
Date: 18/06/2013
Contact: jonathan.yu@csiro.au

Pre-requisites:
---------------
- Java 1.6 +
- Maven 4.0

Modules available
-----------------
a) eis-utils: utility code for i/o, uris, http
b) ontology-lib: OWL-API, Jena, POJOs for interacting with OWL/RDF ontologies
c) ontology-gwt-svc: GWT RPC code for interfacing with the ontology mediator implementation as a service
d) ontology-gwt-widgets: Ontology-driven UI widgets 
e) ontology-ui-demo: Demo GWT Web Application for examples of Ontology-Driven UI

Quick start
-----------
1. Perform `mvn install` on each module in the following order:
- eis-utils
- ontology-lib
- ontology-gwt-svc
- ontology-gwt-widgets
- ontology-ui-demo

2. From the ontology-ui-demo module, you can run GWT Web Application in Dev mode
$ mvn gwt:run

3. From the ontology-ui-demo module and compile the WAR
$ mvn package

4. Deploy the generated war in the target dir into the webapps dir of a Tomcat 6+ server