# README

## Running with maven
```bash
mvn spring-boot:run
```

## Test scenario

### Create user 'one' and 'two'
Note: body data is not needed here, beacuse only email is used, and it is passed as query parameter
```bash
curl -H "Content-Type: application/json" -X POST --data '{"email":"one@mycompany.com"}' http://localhost:8080/users
curl -H "Content-Type: application/json" -X POST --data '{"email":"two@mycompany.com"}' http://localhost:8080/users
```

### Search for artist by term
Search for "prodigy"
```bash
curl -X GET "http://localhost:8080/artists/search?term=prodigy"
```

Search for "aha"
```bash
curl -X GET "http://localhost:8080/artists/search?term=aha"
```
### Set user favorite artist
For user 'one' to 'prodigy'
```bash
curl -X POST "http://localhost:8080/users/1/favorite-artist?artistId=26871"
```

For user 'two' to 'aha'
```bash
curl -X POST "http://localhost:8080/users/2/favorite-artist?artistId=3491"
```

### Set user favorite artist top 5 albums
For user 1
```curl
curl -X GET "http://localhost:8080/users/1/favorite-artist-top-albums"
```

For user 2
```curl
curl -X GET "http://localhost:8080/users/2/favorite-artist-top-albums"
```

