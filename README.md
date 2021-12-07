## Incoming HTTP requests authorizer through method-level annotation with Spring Boot

### Typical scenario

Useful when your microservices architecture already implements an authorization token validation service and filters forbidden HTTP requests through a gateway, but needs a lightweight way to implement authorization mechanism at method-level on every internal services (clients) trough a simple annotation, without having to rely on relatively heavy libraries (like Spring Security, Oauth stuff, etc)

### Maven dependency

To utilize this library as a maven dependency add to your project's pom:

```xml
<dependencies>
   <dependency>
      <groupId>com.ciros</groupId>
      <artifactId>authorizer</artifactId>
      <version>0.5.6</version>
   </dependency>
</dependencies>

<repositories>
   <repository>
      <id>ciros88-authorizer</id>
      <url>https://github.com/ciros88/authorizer/raw/mvn-repo</url>
      <snapshots>
         <enabled>true</enabled>
         <updatePolicy>always</updatePolicy>
      </snapshots>
   </repository>
</repositories>
```
