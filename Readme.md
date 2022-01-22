# Students Information and Profile System

This is a new project that I'll use to learn and update my knowledge about the latest version of Spring Cloud to build microservices.

In my previous project, I got excellent results developing a TO-DO application using a microservices architecture with Spring Boot, Spring Cloud, and Docker compose. Still, I faced some problems that I'll try to solve better using different modules and tools.

I'll implement a tracking system for students' profiles that receive training from an IT company. The system will keep track of the activity of individual students and their performance during the training. 

There are some new things that I want to learn and practice with this project:
1. **Spring WebFlux**. I used Spring MVC in my last project, but since then, I've been practicing Spring WebFlux in separate projects, but I feel confident enough to use it in a large project.
2. **Spring Cloud Stream**. For message-based microservices.
3. **Test containers**. To startup databases when running integrations tests.
4. **Spring Cloud Gateway**. To hide microservices behind an Edge Server. In the last project, I used AWS Gateway Loadbalancer, but I want to try Spring's native Gateway.
5. **Spring Authorization Server**. The last time I used plain Spring Security, it worked but was painful when the number of microservices grew.
6. **Hashicorp vault**. To manage secrets. Last time I did not have time to incorporate it. I used Spring Cloud config and all the configuration was in a GitHub's private repo, but now I don't want to depend on GitHub.
7. **Keycloak**. As Identity and Access Manager.
8. **GraphQL**. I think that no further explanation is needed 😁.
9. **Resilience4j**. I used this the last time. I liked it.
10. **Zipkin** and **Spring Cloud Sleuth**. I used this the last time as distributed tracing tools.
11. **Kubernetes**. I've been using it a little bit, but now I want to use it in a large system to deploy the Microservices. I want to replace the Eureka server with the built-in support in Kubernetes for service discovery, based on Kubernetes Service objects and the kube-proxy runtime component. From here, I'll see how can I use Kubernetes as an alternative to other Spring Cloud Services.
12. **Helm**. To package and configure microservices for deployment in Kubernetes for different runtime environments, such as test and production environments.
13. **Istio**. To create a service mesh to improve observability and management and further improve the microservice landscape's resilience, security, traffic management, and observability.
14. **EFK** (Elasticsearch, Fluentd, and Kibana). The last time I used ELK, now I want to see how different EFK is. 
15. **Prometheus** and **Grafana**. The last time I used InfluxDB and Grafana, I think that Prometheus has a better integration with Spring Boot.
16. **Spring Native**. I want to try the underlying GraalVM Native Image builder to create Spring-based microservices compiled to native code. Compared to using the regular Java Virtual Machine, this will result in microservices that can start up almost instantly.
17. **GitHub Actions**. I've always used Jenkins, now I want to see if there is any additional benefit using GitHub native actions.
18. **[Miro](https://miro.com/)**. To model and diagram processes.
19. **[Structurizr](https://structurizr.com/help)**. To model components. I'm a big fan of the C4 model but have never tried Structurizr, I wouldn't say I like the user interface, but I'll give it a chance.
20. **[Ilograph](https://www.ilograph.com/)**. To document tech specs and the internal structure of some components. It is going to be the first time that I use it, so I don't really know what to spect.
21. **ClickUp**. I've been using Jira for Project Management for the last, maybe, ten years; I want to give ClickUp a chance and see if it really works as well as the ads say.


## Tech Stack
This is the project's current tech stack:

| Tool / Framework                                                                                                                                                                     | Version              | Use                                      | References |
|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------|------------------------------------------|------------|
| <img align="left" alt="Java" width="40" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" />      Java                                                 | 17                   | Main programming language                |            |
| <img align="left" alt="Spring" width="40" src="https://raw.githubusercontent.com/github/explore/8ab0be27a8c97992e4930e630e2d68ba8d819183/topics/spring/spring.png" />    Spring Boot | 2.6.3                | Main development framework               |   https://spring.io/projects/spring-boot         |
| <img align="left" alt="Spring" width="40" src="https://raw.githubusercontent.com/github/explore/8ab0be27a8c97992e4930e630e2d68ba8d819183/topics/spring/spring.png" />  Spring Cloud  | 2021.0.x aka Jubilee | For all the Cloud related infrastructure |      https://spring.io/projects/spring-cloud      |
| <img align="left" alt="Spring" width="40" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/gradle/gradle-plain.svg" /> Gradle                                                 | 7.3                  | Build automation tool                    |  https://docs.gradle.org/current/userguide/structuring_software_products.html |
| <img align="left" alt="Spring" width="40" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/docker/docker-original.svg" /> Docker                                              | -                    | To run the microservices as containers   |
