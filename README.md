# CharChat

CharChat is an application written in Java that uses a client-server architecture to allow users to chat with each 
other.The server is able to handle multiple clients at once and the clients are able to send messages to the server
which are then broadcast to all other clients. Each client that connects to the server is assigned a unique ID, the
server also keeps track of the clients connection status.

## Purpose

This was a research project for my own personal interest. I wanted to learn more about how sockets work and how they
can be used to create a client-server architecture while also learning more about multi-threading.

## Mockup Diagram
![image](https://user-images.githubusercontent.com/30878588/204161330-2608b4ee-8f7a-4c53-9bf9-8020982c8f43.png)

## How to Run

- build the project using `mvn clean install`
- in one terminal run the server using `java -jar target/CharChat-{version}.jar server`
- in another terminal run the client using `java -jar target/CharChat-{version}.jar client`
  - in the client user input from the console will be sent to the server and all other clients
  - enter `.` in the client to disconnect from the server
- Enjoy!
