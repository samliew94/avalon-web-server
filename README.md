## Server Side implementation of Avalon Board Game (Web):

1. Server implemented via Spring Boot 2.7.2 (Java 8)
1. Server runs at port 80. Can be changed in resources/application.properties
2. 5 < totalPlayers <= 10
3. Oberon and Mordred are inactive by default (host can change this in lobby settings)
4. First user to access is the Host.
5. Only Hosts can start the game.