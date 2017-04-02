# CustomerEventInviter
Event inviter is a pet application that invites customers within a certain distance to an event specified. The application code involves around processing gps co-ordinates and computing distances, with some key principles on distributing the data sets.

## COMPILE
Maven 3.x with Java 1.7
```
mvn clean install
```
    - from the top pom builds the model, geo-utils and the event-inviter applciation
    
## RUN
```    
java -jar event-inviter-<version>.jar 
```
    - will run the application with default args and resources (customer file) from the project
