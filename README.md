# Java Utilities 

## Features
- Cryptography utilities
- JDBC utilities (Named parameters, Array parameters, LIKE_ANY operator, etc.)
- Record framework
- DI framework
- Type formatters/parsers with localization
- Java bean validators
- GSON adapters
- JSON Web Token
- Geography utilities
- ToStringBuilder
- 400+ Unit tests

## Installation

### Maven
```XML
<dependency>
    <groupId>com.appslandia</groupId>
    <artifactId>appslandia-common</artifactId>
    <version>{LATEST_VERSION}</version>
</dependency>
```

### Gradle
```
dependencies {
   compile 'com.appslandia:appslandia-common:{LATEST_VERSION}'
}
```

## Sample Usage
### ToString Builder
```java
// Print all fields
System.out.println( new ToStringBuilder().toString(any_object));
```
### JDBC Named Parameters
```java
Sql sql = new Sql("SELECT * FROM User WHERE status=:status");

try (StatementImpl stat = new StatementImpl(connection, sql)) {
  stat.setInt("status", 1); // Named parameter
  //
  stat.executeQuery();
}
```
### JDBC Named Array Parameters
```java
Sql sql = new Sql("SELECT * FROM User WHERE userType IN :types");

try (StatementImpl stat = new StatementImpl(connection, sql)) {
  stat.setIntArray("types", new int[] {1,2,3});
  //
  stat.executeQuery();
}
```
### JDBC LIKE_ANY
```java
Sql sql = new Sql("SELECT * FROM User WHERE name LIKE_ANY :names");

try (StatementImpl stat = new StatementImpl(connection, sql)) {
  stat.setLikeAny("names", new String[] {"a, "b"}); // name LIKE '%a%' OR name LIKE '%b%'
  //
  stat.executeQuery();
}
```
### System.getProperty & getenv
```java
 String password = SYS.resolveExpr("${db.password, env.DB_PASSWORD:default_password}")
 // resolving order:  System.getProperty("db.password"),  System.getenv("DB_PASSWORD"), default_password
```
### Geography
```java
 GeoLocation loc = new GeoLocation(lat, long);
 GeoLocation loc_east = loc.move(Direction.EAST, 10, DistanceUnit.MILE);
 //
 double distanceInMiles = loc.distanceTo(loc_east, DistanceUnit.MILE); // ~10 miles
```
### JWT
```java
  // JwtProcessor
  JwtProcessor processor = new JwtProcessor().setIssuer("Issuer1");

  // GsonProcessor or your JsonProcessor
  processor.setJsonProcessor(new GsonProcessor().setBuilder(JwtGson.newGsonBuilder()));
  
  processor.setJwtSigner(new JwtSigner().setAlg("HS256")
  				.setSigner(new MacDigester().setAlgorithm("HmacSHA256").setSecret("secret")));

  JwtHeader header = processor.newHeader();
  JwtPayload payload = processor.newPayload().setExpiresIn(1, TimeUnit.DAYS);

  // Serialize
  String jwt = processor.toJwt(new JwtToken(header, payload));
  
  // Deserialize
  JwtToken token = processor.parseJwt(jwt);
  token.getHeader(); token.getPayload();
```
## Questions?
Please feel free to contact me if you have any questions or comments.
Email: haducloc13@gmail.com

## License
This code is distributed under the terms and conditions of the [MIT license](LICENSE).
