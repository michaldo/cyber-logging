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

Spring usage
------------





