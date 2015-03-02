This program is developed to crawl news articles for HashTagBe project.

The program is managed by apache Maven project management tool. It is a web 
application based on Spring MVC framework.

To deploy this program, please follow the steps described below:

1, Make sure the required environment is ready in your computer:
	a) JDK1.7+
	b) Maven3.0+
	c) Apache Tomcat7.0+
	d) Mysql5.0+

2, Pull the src codes from the specified Github repository to your local directory.

3, Modify the associated configuration files which located in 
   ${src code directory}/src/main/resources/ dir, mainly including DB and log configuration
   parameters.
	a) Open src/main/resources/config/config.properties file, modify crawler related xml 
	   configuration file directory, and the crawl interval time. Make sure the all_sites.xml
 	   file and site specific xml file(for example, the sample guardian_articles.xml) is located
	   in the specified file directory. 
	b) Switch to src/main/resources/mybatis directory, execute database.sql statements to
	   create crawler database and associated tables.
	c) Open src/main/resources/mybatis/jdbc.properties file, modify DB username/password
	   according to your DB configurations. 
	d) Open src/main/resources/config/log4j.xml file, there are "<param name="File" value="logs/debug.log" />"
	   nodes, you can modify "value" attribute to change the default log file disk location.  

4, Switch to your local src codes directory, execute the command: mvn clean package.
   You should see a lot of compiling and packaging related information printed out. 

5, After mvn command finished, switch to the maven target directory, you should find a 
   packaged war file. 

6, Copy the war file to your tomcat webapps directory, then startup the tomcat. 

7, Visit the project web hompage, for example: http://localhost:8080/HashtagCrawler/, 
   a welcome page should be displayed in your browser. Make sure you visit the home page
   URL of this application, otherwise the daemon crawler thread can not be started.          
