# Car Parks

**Goal**\
Find closest cark parks to current location

**Provided data for task**
* [All car parks in Poitiers](https://data.grandpoitiers.fr/api/records/1.0/search/?dataset=mobilite-parkings-grand-poitiers-donnees-metiers&rows=1000&facet=nom_du_parking&facet=zone_tarifaire&facet=statut2&facet=statut3)
* [Car parks with indication of capatity and free places in Poitiers](https://data.grandpoitiers.fr/api/records/1.0/search/?dataset=mobilites-stationnement-des-parkings-en-temps-reel&facet=nom)

**Time spent per task:**\
5 days

**Things that could be added, but were not due to lack of time:**
1) Testing
2) Pagination
3) Dockerfile

**Description of solution:**
1) As we can expect that there'll be lots of queries for finding car parks, I've decided to create database for storing car parks information, because retrieving data from there will be done faster than getting data every time by URL from another service
2) Used database is ***MongoDb*** as in provided already built in functionality for finding closes points to specified location. So it'll allow to perform search faster and more efficiently
3) I've added different synchronization policies (STATIC, EVERY_1_MINUTE). That will allow to get some data only once and some once in a minute. 
4) Why EVERY_1_MINUTE ? Because provided dataset of car park, where we can see free places, is updated every one minute
5) For allowing the logic work not only for Poitiers datasets, but also for other cities and datasets ***citi-loader.yml*** file is created for field mapping and specification of desired datasets to be used

**Stack:**\
Java 17, Spring Boot, MongoDB, Maven, Lombok, Swagger, Docker, Git

**Swagger**\
http://localhost:8080/swagger-ui/index.html
