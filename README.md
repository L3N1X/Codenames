
![Logo](https://camo.githubusercontent.com/39a3912db83336e97ef3b8f5e4290525fbd2a90c8d4ab0c6cff0bf9ffc1874de/68747470733a2f2f63646e2e646973636f72646170702e636f6d2f6174746163686d656e74732f3739373232343831383736333130343331372f313032363134393732393139343735343036382f636f64656e616d65732d6772616469656e742e706e67)


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
    
## Tech Stack

**Client:** JavaFX (Java17), CSS

**Server:** Java17


## Authors

- [@L3N1X](https://www.github.com/l3n1x)


This is a beta application to show technical knowledge for a university class, but could be easily extended to be production ready. 
(Creating a game pool and then connecting to the server using a code should be added)
