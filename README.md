# be-crawler

This program is developed to crawl news articles for HashTagBe project.

The program is managed by apache Maven project management tool.
It is a web application based on Spring MVC framework.

## Requirements

- JDK 1.7+
- Maven 3.0+

## How to build

1. Adjust the configuration files in `${src code directory}/src/main/resources`

   - Crawler configuration: `config/config.properties`
     - crawl interval time
     - XML configuration directory
     Make sure the `all_sites.xml` file and site specific xml files
     (for example `theguardian_articles.xml`) are located in the
     specified directory.

   - Setup database running `mvn sql:execute`

   - Configure logger in `config/log4j.xml` file
     - currently there are `<param name="File" value="logs/debug.log" />`
       nodes, you can modify `value` attribute to change the default
       log file disk location.

1. Run `mvn clean package`.

## How to run

1. Run `bin/webapp` or `bin/webapp.bat` to start the web
   application. Use the `PORT` environment variable to define on which
   port start the embedded Tomcat. By default it uses `8088`.
   You can also user the `foreman` gem or the `bin/shoreman` script.
   In this case the default port is `5000`.

1. Visit the project web homepage at `http://localhost:$PORT/` in order
   to start the daemon crawler thread.

1. In order to:
   - get article list result:
   `http://localhost:$PORT/article/getArticleList.do?startTime=${start time value}&endTime=${end time value}`
   - get article list count:
   `http://localhost:$PORT/article/getArticleCount.do?startTime=${start time value}&endTime=${end time value}`
   - export article list to Excel:
   `http://localhost:$PORT/article/exportToExcel.do`

## How to add crawling tasks

Add a configuration file in `src/main/resources/config`.
Have a look at the `articles_template.xml` and the other XML files.
