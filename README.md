
![alt text](https://www.google.com/url?sa=i&url=https%3A%2F%2Fczechgames.com%2Ffor-press%2Fcodenames%2Findex.html&psig=AOvVaw2o-92rRrUd_C1o93V_OMNo&ust=1723226993128000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCJiOtfr-5YcDFQAAAAAdAAAAABAE)


# Codenames Java Edition

### Multiplayer videogame inspired by Vlaada Chvatil's (Chez Games®) boardgame



## Game features

- LAN Multiplayer (4 players)
- Saving game locally
- Fullscreen mode
- Cross platform (Java 17 installed)

## Technical features

- Continuous socket connection
- Local saving and loading (XML - JAXB)
- JNDI Configuration
- JavaFX (MVC pattern)
- Multithreading (used in sending and recieving client-server)
- Automatic code documentation generation to HTML using reflection
- Live chat using RMI
## Installation

This project has a server and client app. To test it out, start the server app first, and then run four client instances. They'll connect automatically to the local host and the port you've set in the JNDI.

Once all players have signed up, the game begins, following the rules of the original board game. Each round ends when one team wins, and you can keep playing until you decide to stop. The team with the most round wins wins overall.

## Screenshots
![Screenshot 2023-10-05 213405](https://github.com/L3N1X/Codenames/assets/67825601/1e37cac7-77f4-4a3b-bd29-0003c7edb349)
![Screenshot 2023-10-05 213858](https://github.com/L3N1X/Codenames/assets/67825601/c415d13b-d151-41ea-9e78-157d5efeba1c)
![Screenshot 2023-10-05 213958](https://github.com/L3N1X/Codenames/assets/67825601/03781e85-a7d2-42c5-899b-e2c37cbbcfe7)

    
## Tech Stack

**Client:** JavaFX (Java17), CSS

**Server:** Java17


## Authors

- [@L3N1X](https://www.github.com/l3n1x)


This is a beta application to show technical knowledge for a university class, but could be easily extended to be production ready. 
(Creating a game pool and then connecting to the server using a code should be added)
