# Test-BCNC

### Purpose

Test-BCNC is an application that retrieves data from an external source and persist them in an own database. The
objective of this project is developing the initial technical test of BCNC Consulting Group.

### Project structure

This project is structured following the hexagonal architecture. It is divided in 3 principal folders:

* ``application``: This folder contains code related to the uses of cases, with will interact with the domain
elements.

* ``domain``: This folder contains the domain entities, the services, and the definition of the operations that must
be done by the infrastructure.

* ``infraestructure``: This folder contains the implementations for data access or persistence operations related to
the domain entities.

### Recommended prerequisites

For building and running the app you will need:

* Intellij
* Maven plugin.
* JDK 1.17

### Running locally

Using IntelliJ: ``Run -> Run Demo Application 'Test'``

Using command line: ``mvn spring-boot:run``

### Automated tests

The tests are contained in tests folder. It contains the following tests:
* There are integration tests, that uses an H2 database and a mocked external 
API provided by a mocked web server.
* There are unit tests, that uses mocked dependencies.

To execute test locally, run the following command: ``mvn test``

#### Coverage

When the ``mvn test`` is executed, it generates a report using the Jacoco plugin. It will be avaliable 
in ``/target/jacoco.exec``. In order to read it, you can use your IDE or other tool to read the binary content.

### Links of interest

[![Linkedin joelcb](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/joelcb)