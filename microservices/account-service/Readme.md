# Account Service

## REST API

| Endpoint	      | Method   | Req. body    | Status | Resp. body     | Description    		   	           |
|:---------------:|:--------:|:------------:|:------:|:--------------:|:--------------------------------|
| `/`       | `GET`    |              | 200    |          |              |
| `/`       | `POST`   |  | 200    |           |                                |

## Useful Commands

| Gradle Command	         | Description                                   |
|:---------------------------|:----------------------------------------------|
| `./gradlew bootRun`        | Run the application.                          |
| `./gradlew build`          | Build the application.                        |
| `./gradlew test`           | Run tests.                                    |
| `./gradlew bootJar`        | Package the application as a JAR.             |
| `./gradlew bootBuildImage` | Package the application as a container image. |

After building the application, you can also run it from the Java CLI:

```bash
java -jar build/libs/account-service-0.0.1-SNAPSHOT.jar
```