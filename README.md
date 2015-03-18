# be-crawler

This program is developed to crawl news articles for HashTagBe project.

The program is managed by apache Maven project management tool.
It is a web application based on Spring MVC framework.

## Requirements

- JDK 1.7+
- Maven 3.0+
- Mysql 5.0+

## How to build

1. Adjust the configuration files in `${src code directory}/src/main/resources`

   - Crawler configuration: `config/config.properties`
     - crawl interval time
     - XML configuration directory
     Make sure the `all_sites.xml` file and site specific xml files
     (for example `theguardian_articles.xml`) are located in the
     specified directory.

   - Setup database
     - edit `mybatis/jdbc.properties` file to match
       your database
     - execute `mybatis/database.sql` script to
       create the database and tables.

   - Configure logger in `config/log4j.xml` file
     - currently there are `<param name="File" value="logs/debug.log" />`
       nodes, you can modify `value` attribute to change the default
       log file disk location.

1. Run `mvn clean package`.

## How to run

1. Run `target/bin/webapp` or `target/bin/webapp.bat` to start the web
   application. Use the `PORT` environment variable to define on which
   port start the embedded Tomcat. By default it uses `8088`.
   Alternatively use `foreman start`, which will use port `5000`.

1. Visit the project web homepage at `http://localhost:8088/` in order
   to start the daemon crawler thread.

1. In order to:
   - get article list result:
   `http://localhost:8088/article/getArticleList.do?startTime=${start time value}&endTime=${end time value}`
   - get article list count:
   `http://localhost:8088/article/getArticleCount.do?startTime=${start time value}&endTime=${end time value}`
   - export article list to Excel:
   `http://localhost:8088/article/exportToExcel.do`

## How to add crawling tasks

TODO
