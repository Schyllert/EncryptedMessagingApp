# messagingApp

This was one of my early projects and it is not very well made - but it got me thinking about a lot of different interesting topics.

The goal with the project was to have a secure way to authenticate users and enable them to message each other without exposing their IP to an other users. A subgoal was to implement a role system in which certain roles could use features based on their role.

Structure:

Client <--> Server <--> Database

1. TLS encryption is implemented with self-signed certificates.
2. A custom self-made protocol on top of TCP is used. It is stateful and it allows one tcp connection to multiplex many requests and match them with corresponding response. This allows for one connection to handle many requests before delivering a response.
3. Hashing and salting is done with BCrypt.
4. Database connections are reused (pooled) - this is done with the library commons-pool2-2.9.0
5. Authentication is implemented using username + hash(password+salt) - credentials stored in a MySQL database.
6. The system has access control, it is implemented using a role system: Member/Commander/Admin.
7. The graphical client is using the MVC pattern and was created using JavaFX.
8. Creating/signing certificates is done using keytool and openssl
