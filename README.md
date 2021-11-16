# messagingApp

This was one of my early projects and it is not very well made - but it got me thinking about a lot of different interesting topics.

Structure:

Client <--> Server <--> Database

1. TLS encryption is implemented with self-signed certificates.
2. The custom protocol is stateful and it allows one tcp connection to multiplex many requests and match them with corresponding response. This allows for one connection to handle many requests before delivering a response.
3. Hashing and salting is done with BCrypt.
4. Database connections are reused (pooled) - this is done with the library commons-pool2-2.9.0
5. Authentication is implemented using username + hash(password+salt). 
6. The system has access control, it is implemented using a role system: Member/Commander/Admin.
7. The client is using the MVC pattern and was created using JavaFX.
