# be-crawler

This program is developed to crawl news articles for HashTagBe project.

The program is managed by apache Maven project management tool.
It is a web application based on Spring MVC framework.

# How to build and run

To deploy this program, please follow the steps described below:

1. Make sure the required environment is ready in your computer:
   - JDK 1.7+
   - Maven 3.0+
   - Mysql 5.0+

2. Get the source codes from Github repository to your local directory.

3. Modify the associated configuration files located in the
   `${src code directory}/src/main/resources` directory, mainly including
   database and log configuration parameters.
   - edit `src/main/resources/config/config.properties` file, modify crawler
     related xml configuration file directory, and the crawl interval time.
     Make sure the `all_sites.xml` file and site specific xml files
     (for example the sample `guardian_articles.xml`) are located in
     the specified directory.
   - in `src/main/resources/mybatis` directory, execute `database.sql`
     statements to create crawler database and associated tables.
   - edit `src/main/resources/mybatis/jdbc.properties` file to modify
     database username/password according to your database configuration.
   - edit `src/main/resources/config/log4j.xml` file, there are
     `<param name="File" value="logs/debug.log" />` nodes, you can modify
     `value` attribute to change the default log file disk location.

4. Switch to your local source code directory, execute the command
   `mvn clean package`.
   You should see a lot of compiling and packaging related information
   printed out.

5. Run `target/bin/webapp` or `target/bin/webapp.bat` to start the web
   application. Use the `PORT` environment variable to define on which
   port start the embedded Tomcat. By default it uses 8088.
   Alternatively use `foreman start`, which will use port 5000.

6. Visit the project web homepage at `http://localhost:8088/`.
   to start the daemon crawler thread.

7. In order to:
   - get article list result:
   `http://localhost:8088/article/getArticleList.do?startTime=${start time value}&endTime=${end time value}`
   - get article list count:
   `http://localhost:8088/article/getArticleCount.do?startTime=${start time value}&endTime=${end time value}`
   - export article list to Excel:
   `http://localhost:8088/article/exportToExcel.do`
