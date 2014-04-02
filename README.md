cyber-logging
=============

Cyber-logging is tool to log Java method input and output just by single annotation. 

Although using annotation for logging is typical, cyber-logging simplifies effort get full-feature log content.

Simple example:
--------------- 

    @Logged
    public int add(int a, @NotLogged int b) {
        return a + b;
    }

Logged information:
---------------
* Entering MyBean.add with [2,2]
* Exiting MyBean.add with 4

cyber-logging benefits
---------------
1. By default objects are logged in JSON format. This is best profit, because full object content is visible in log without any additional effort.
2. Way how objects are written to log can be customized (save object as JSON, use **toString()** or own implementation)
3. **@NotLogged** excludes from logging fragile, heavy, trivial or just unwanted methods and/or parameters
4. Possible enhance logs with extra information, for example session id, username, to help tracking concrete flow
5. Uniform usage for POJO, Spring and EJB (as much as possible)

POJO usage
----------

Manually build proxy over target object to catch method calls and trigger logging. Example:

    MyBean target = new MyBean();
    IMyBean proxy = LoggedProxy.createProxy(target, IMyBean.class);
    proxy.foo();

LoggedProxy applies default logging policy. For custom policy, use own implementation. [I'm an inline-style link](https://www.google.com)

In order to use cyber-logging with pojo object the object must implement interface with methods for logging. This requirement may be burdensome. Need of interfaces may be 
replaced by using cglib. Please open issue in github to request cglib option

EJB usage
----------

Annotate EJB on class or method level with annotaton **com.cybercom.logging.ejb.Logged**. 

    @Logged
    public class MyBean...

For custom logging policy, use own annotation and interceptor. [Example](https://github.com/michaldo/cyber-logging-demo/tree/master/demo-logging-ejb/src/test/java/com/cybercom/demo/logging/ejb/custom)

Spring usage
----------

1. Activate aspects in you application
2. Define and configure bean **com.cybercom.logging.spring.LoggedSpringAspect**

[Example](https://github.com/michaldo/cyber-logging-demo/blob/master/demo-logging-spring/src/test/resources/spring-beans.xml)

For custom logging policy, Spring not need custom implementation, because policy can be configured in Spring descriptor

#### Minimal Spring configuration

Add to your pom repository 

	<repositories>		
      <repository>
      <snapshots>
        <enabled>true</enabled>
      </snapshots>
      <id>nex</id>
      <name>nex</name>
      <url>https://oss.sonatype.org/content/groups/public</url>
    </repository>		
	</repositories>
	
Add to your pom dependency

    <dependency>
	  <groupId>com.cybercom.logging</groupId>
	  <artifactId>logging-spring</artifactId>
	  <version>0.0.1-SNAPSHOT</version>
    </dependency>	

Configure the bean

    <aop:aspectj-autoproxy proxy-target-class="true" />
    <bean class="com.cybercom.logging.spring.LoggedSpringAspect"/>

Log level configuration
-----

Cyber-logging use [slf4j](http://www.slf4j.org/) facade for logging. Level is debug. To see logged data configure
**com.cybercom.logging.core.EntryExitMethodLogger** on level DEBUG 


Troubleshooting
----------

#### Nothing is logged
Add to the following line to your code:

    org.slf4j.LoggerFactory.getLogger("com.cybercom.logging.core.EntryExitMethodLogger").debug("Check logging");
    
If you see message *Check logging* in your logs then log configuration is OK and problem is somewhere else
Otherwise fix log configuration

#### I have error "error the @within pointcut expression is only supported at Java 5 compliance level or above"

see http://stackoverflow.com/questions/15678417/error-when-using-aspectj-aop-with-java-7


Configuration
-----

See Javadoc for EntryExitMethodLogger

Examples
--------

See [demo](https://github.com/michaldo/cyber-logging-demo)

Development notes
-------
In order to have cyber-logging in one Maven artifact, each artifact contains all required classes. (Simply, **logging-ejb** contains all classes from **logging-core**).
To achieve the goal, child modules (**logging-ejb**, **logging-spring**) include source folder of **logging-core**. But it is not handled by Eclipse plugin for Maven.

To work with cyber-logging with Eclipse I import all projects and add dependency to **logging-core** manually



