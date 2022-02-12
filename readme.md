# Instructions to run the application

## Details
This application has below specifications
1. Uses in-memory H2 database, to view in console please follow below link for instructions
    ```https://www.baeldung.com/spring-boot-h2-database```
2. The DB has no user and passwords.
3. This application also uses Java in-built caching methodologies on the ```User``` resource.
4. To select appropriate caching please modify the application.properties file and in the ```eviction.method``` select the appropriate methodology.
   ```aidl
    Accepted values for eviction.method are :
   - LRU --> (Least recently used)
   - LFU --> (Least frequently used)
   - MRU --> (Most Recently used)
   - MFU --> (Most frequently used

5. This application runs on default port for tomcat - 8080, under tests , this application includes a basic collection of postman to trigger the requests and get and post ```User```  resource.
6. This application will also load 5 dummy records at the application start, so it's not mandatory to craete the ```User``` resource before accessing one, and is ready to go.