# messagingApp

This was one of my early projects and it is not very well made - but it got me thinking about a lot of different interesting topics. At the time I had only taken two basic courses in Java programming. 


The goal with the project was to have a secure way to authenticate users and enable them to message each other without exposing their IP to an other users. A subgoal was to implement a role system in which certain roles could use features based on their role.

When i worked on this project I really struggled due to the fact that the connections were stateful - and this caused all kinds of different challenges. 

Structure
==
Client <--> Server <--> Database

1. TLS encryption is implemented with self-signed certificates
2. A custom self-made protocol on top of TCP is used. It is stateful and it allows one tcp connection to multiplex many requests and match them with corresponding response. This allows for one connection to handle many requests before delivering a response (sounds a lot more fancy then what it actually is).
3. Hashing and salting is done with BCrypt.
4. Database connections are reused (pooled) - this is done with the library commons-pool2-2.9.0
5. Authentication is implemented using username + hash(password+salt) - credentials stored in a MySQL database.
6. The system has access control, it is implemented using a role system: Member/Commander/Admin.
7. The graphical client is using the MVC pattern and was created using JavaFX.
8. Creating/signing certificates is done using keytool and openssl
9. AWS was used for the IT infrastructure. The infrastructure was designed and deployed as code using Terraform and is provided in the folder [here](https://github.com/Schyllert/messagingApp/tree/master/terraform%20%20-%20aws%20infrastructure)



Overview of AWS
==


![alt text](https://github.com/Schyllert/messagingApp/blob/master/AWS-infrastructure.png)

Client login-page
==

![alt text](https://github.com/Schyllert/messagingApp/blob/master/loginscreen.png)
