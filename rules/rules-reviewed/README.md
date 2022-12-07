Rules Reviewed
===============

This directory contains default set of Windup rules distributed with MTA.
The name 'rules-reviewed' came from historic event of rewriting Windup 1.0 rules to Windup 2.0.

Directory structure
-----------

The directory structure is:

    <target-tech>/<source-tech>

For instance,

    eap7/eap5and6

contains rules that migrate from EAP 5 and 6 to EAP 7.

The 'api-jars' directory contains jars used to determine which API is provided by the target technology.

Technology Usage
-----------

The rules located under the `technology-usage` directory are meant to discovery which technologies are used in the
application.
They are then displayed in the report under the "Technologies" tab that shows the technologies occurrences.
It is an overview of what technologies are found in given project or a set of projects.
They are ordered by family type:

* View: technologies that are used to display the content of the application (e.g. JSF, JSP, HTML, etc.)
* Connect: technologies that are used to connect to external systems (e.g. JMS, JAX-RS, etc.)
* Store: technologies related to data storage (e.g. JPA, Hibernate, etc.)
* Sustain: technologies that are used to maintain the application (e.g. JMX, JNDI, etc.)
* Execute: technologies that are used to execute the application (e.g. EJB, CDI, etc.)

These rules are then categorized either in "Jakarta EE" (technologies that are managed by the server, such as Servlets,
EJBs, JPA) or "Embedded" (technologies external to the server that are embedded in the application, such as Spring).
Below two tables to show some examples of the mapping between the technology and the category.

Jakarta EE
-----------

| View          | Connect                | Store               | Sustain                | Execute           |
|---------------|------------------------|---------------------|------------------------|-------------------|
| Applet        | Common Annotations     | Bean Validation     | EJB                    | CDI               |
| JBoss Web XML | EAR Deployment         | JDBC                | File system logging    | Java EE Batch     |
| JNLP          | EJB XML                | JDBC datasources    | Java EE JACC           | Java EE Batch API |
| JSF Page      | Entity Bean            | JDBC XA datasources | JTA                    | Java EE JSON-P    |
| JSP Page      | Java EE JAXB           | JPA entities        | Security Realm         |                   |
| Web XML File  | Java EE JAXR           | JPA named queries   | Socket handler logging |                   |
| WebSocket     | JAX-RS                 | JPA XML             | Web Session            |                   |
|               | JAX-WS                 | Persistence units   |                        |                   |
|               | JBoss EJB XML          |                     |                        |                   |
|               | JCA                    |                     |                        |                   |
|               | JMS Connection Factory |                     |                        |                   |
|               | JMS Queue              |                     |                        |                   |
|               | JMS Topic              |                     |                        |                   |
|               | JNA                    |                     |                        |                   |
|               | JNI                    |                     |                        |                   |
|               | JSON-B                 |                     |                        |                   |
|               | Mail                   |                     |                        |                   |
|               | Management EJB         |                     |                        |                   |
|               | Message Driven Bean    |                     |                        |                   |
|               | RMI                    |                     |                        |                   |
|               | Servlet                |                     |                        |                   |
|               | Stateful EJB           |                     |                        |                   |
|               | Stateless EJB          |                     |                        |                   |
|               | Web Services Metadata  |                     |                        |                   |

Embedded
-----------

| View              | Connect                 | Store                | Sustain                  | Execute               |
|-------------------|-------------------------|----------------------|--------------------------|-----------------------|
| AngularFaces      | 0MQ Client              | Apache HBase Client  | Acegi Security           | Apache Aries          |
| Apache Tapestry   | ActiveMQ library        | Apache Ignite        | Apache Commons Validator | Apache Geronimo       |
| CSS               | Amazon SQS Client       | Cache API            | Apache Flume             | Apache Hadoop         |
| Eclipse RCP       | AMQP Client             | Cassandra Client     | Arquillian               | Apache Karaf          |
| FreeMarker        | Axis                    | Coherence            | Atomikos JTA             | AspectJ               |
| Grails            | Camel Messaging Client  | Derby Driver         | Bouncy Castle            | Camel                 |
| GWT               | CXF                     | Dynacache            | Commons Logging          | Camunda               |
| HTML              | HornetQ Client          | EclipseLink          | Cucumber                 | CDI                   |
| ICEfaces          | HTTP Client             | ehcache              | DbUnit                   | Cloudera              |
| JavaFX            | JBossMQ client          | H2 Driver            | EasyMock                 | Dagger                |
| JavaScript        | Jersey                  | Hazelcast            | Geronimo JTA             | Drools                |
| JFreeChart        | OpenWS                  | Hibernate            | GlassFish JTA            | Easy Rules            |
| JMustache         | RabbitMQ Client         | Hibernate Cfg        | Guava Testing            | Elasticsearch         |
| JSF               | Resource Adapter        | Hibernate Mapping    | Hamcrest                 | Eureka                |
| JSTL              | RocketMQ Client         | Hibernate OGM        | HttpUnit                 | Feign                 |
| Liferay           | Spring Messaging Client | HSQLDB Driver        | Java Transaction API     | Google Guice          |
| LiferayFaces      | WebSphere EJB           | infinispan           | JBoss logging            | Istio                 |
| MyFaces           | WSDL                    | JBoss Cache          | JBoss Transactions       | Javax Inject          |
| OpenFaces         | XFire                   | JCache               | JSecurity                | JBPM                  |
| Oracle ADF        |                         | Memcached            | JUnit                    | Jetty                 |
| Play              |                         | Microsoft SQL Driver | KumuluzEE JTA            | Kibana                |
| Portlet           |                         | MongoDB Client       | Log4J                    | Liferay               |
| PrimeFaces        |                         | MySQL Driver         | Logback                  | Logstash              |
| RichFaces         |                         | Oracle DB Driver     | Logging Utils            | MapR                  |
| Seam              |                         | PostgreSQL Driver    | Mockito                  | Micrometer            |
| Spring Boot Flo   |                         | Redis                | Narayana Arjuna          | Mule                  |
| Spring MVC        |                         | Spring Boot Cache    | Nuxeo JTA/JCA            | Neo4j                 |
| Spring Web        |                         | Spring Data          | OAUTH                    | Oracle Forms          |
| Struts            |                         | Spring Data JPA      | OpenSAML                 | PicoContainer         |
| Swing             |                         | SQLite Driver        | OW2 JTA                  | Quartz                |
| SWT               |                         |                      | OWASP ESAPI              | ServiceMix            |
| Thymeleaf         |                         |                      | PicketLink               | Spark                 |
| Vaadin            |                         |                      | PowerMock                | Spring                |
| Velocity          |                         |                      | Properties               | Spring Batch          |
| WebLogic Web XML  |                         |                      | REST Assured             | Spring Boot           |
| WebSphere Web XML |                         |                      | SAML                     | Spring Cloud Function |
| Wicket            |                         |                      | Shiro                    | Spring DI             |
|                   |                         |                      | SLF4J                    | Spring Integration    |
|                   |                         |                      | Spring Boot Actuator     | Spring Scheduled      |
|                   |                         |                      | Spring Cloud Config      | Spring Shell          |
|                   |                         |                      | Spring JMX               | Swagger               |
|                   |                         |                      | Spring Properties        | TensorFlow            |
|                   |                         |                      | Spring Security          | Tomcat                |
|                   |                         |                      | Spring Test              | Weld                  |
|                   |                         |                      | Spring Transactions      | Zipkin                |
|                   |                         |                      | SSL                      |                       |
|                   |                         |                      | TestNG                   |                       |
|                   |                         |                      | Unitils                  |                       |
|                   |                         |                      | WSS4J                    |                       |
|                   |                         |                      | XMLUnit                  |                       |
