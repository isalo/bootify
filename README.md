# Hub

This app was created with Bootify.io - tips on working with the code [can be found here](https://bootify.io/next-steps/).

## Development

Update your local database connection in `application.yml` or create your own `application-local.yml` file to override
settings for development.

During development it is recommended to use the profile `local`. In IntelliJ `-Dspring.profiles.active=local` can be
added in the VM options of the Run Configuration after enabling this property in "Modify options". For this multi-module
project you have to select the highest module `hub-web.main` as the classpath.

Lombok must be supported by your IDE. For IntelliJ install the Lombok plugin and enable annotation processing -
[learn more](https://bootify.io/next-steps/spring-boot-with-lombok.html).

In addition to the Spring Boot application, the DevServer must also be started - for this
[Node.js](https://nodejs.org/) version 22 is required. On first usage and after updates the dependencies have to be installed:

```
npm install
```

The DevServer can be started as follows:

```
npm run devserver
```

Using a proxy the whole application is now accessible under `localhost:3000`. All changes to the templates and JS/CSS
files are immediately visible in the browser.

## Testing requirements

To run the tests and build, [Docker](https://www.docker.com/get-started/) must be available on the current system. Due
to the reuse flag, the container will not shut down after the tests. It can be stopped manually if needed.

Frontend unit tests can be executed with `npm run test`.

## Build

The application can be tested and built using the following command:

```
gradlew clean build
```

Node.js is automatically downloaded using the `gradle-node-plugin` and the final JS/CSS files are integrated into the jar.

Start your application with the following command - here with the profile `production`:

```
java -Dspring.profiles.active=production -jar ./hub-web/build/libs/hub-web-0.0.1-SNAPSHOT.jar
```

If required, a Docker image can be created with the Spring Boot plugin. Add `SPRING_PROFILES_ACTIVE=production` as
environment variable when running the container.

```
gradlew bootBuildImage --imageName=com.cykor/hub
```

## Further readings

* [Gradle user manual](https://docs.gradle.org/)  
* [Spring Boot reference](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)  
* [Spring Data JPA reference](https://docs.spring.io/spring-data/jpa/reference/jpa.html)
* [Learn React](https://react.dev/learn)
* [Webpack concepts](https://webpack.js.org/concepts/)  
* [npm docs](https://docs.npmjs.com/)  
* [Tailwind CSS](https://tailwindcss.com/)  
