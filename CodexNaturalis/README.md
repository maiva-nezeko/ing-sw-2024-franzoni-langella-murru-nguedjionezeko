# Software engineering project
The final project of Software Engineering, course of Computer Science Engineering held at Politecnico di Milano during the academic year 2023/2024, presents the implementation of a distributed system which plays a vital role in the organization of the game.
The following game gives the possibility to the client (player) to play one single session, while the server is able to manage multiple matches/game.

<br/> **Group**: PSP-39

**The team**: 
- [Edoardo Murru](https://github.com/EdoMurru)
- [Darelle Maiva Nguedjio Nezeko](https://github.com/maiva-nezeko)
- [Giovanni Langella](https://github.com/GioLange2002)
- [Davide Franzoni](https://github.com/elfr4nz0)

## Implemented features
| Feature               | Status |
|-----------------------|--------|
| UML(high level)       |   ✅   |
| Model(complete rules) |   ✅   |
| Networking            |   ✅   |
| Controller            |   ✅   |
| TUI                   |   ✅   |
| GUI                   |   ✅   |
| Persistence           |   ✅   |
| Multiple Games        |   ✅   |
| Connection resilience |   ✅   |


## Testing

| Package |  Class  |  Covereges(lines) |
|---------|---------|-------------------|
|  Mainclasses |     Game    |  94%  |
|  Mainclasses |      MultipleGameManager   |  90%  |
|    UpdateClasses     |      TableManager   | 87%  |
|   Utility   |     PersistanceManager    | 87% |
|  Table   |     Deck    | 100% |
|  Table  |     Player    | 100% |
| Table |    Table     | 98% |
| Cards |              | 97% |

## Requirements
In order to successfully execute the .jar applications, which can be found here, both Windows and Java 21 are required.
If you wish to compile the project independently, it is necessary to download the GitHub repository:
Afterward, it can be compiled using your IDE of choice, provided with the required dependencies, java-21-openjdk and javafx-sdk-21.0.1.

### HOW TO OPEN
Download or alternatively open the ‘CodexNaturalis’ folder ONLY with the preferred IDE. This step is essential to be able to run the code without folder source errors.

### HOW TO PLAY

*Before running for  first time:* Download one of the fonts from ‘CodexNaturalis/src/main/resources/Fonts’ folder. 
Install the font by opening the .ttf file and follow the instructions on screen – for Windows, simply click the install button after opening the font. 



In order to start a Game, a Server needs to be started through running Server.jar (or ServerMain.java class if you want to run on code). Then, you can start connecting a Client to the Server by running Client.jar (or App.java class if directly on source code). 
Reminder: All Clients and the Server need to share the same internet  connection, whether via cable or Wi-Fi.
The console will pop up and you will be asked to input the IP address of the hosting Server, then press enter. After that you can choose Socket or RMI – by pressing  “1” or “2” followed by enter key – and then  GUI or TUI – still by pressing "1”or “2” followed by enter key.
Now you can follow GUI or TUI guide depending on your interface choices!


**GUI GUIDE**

A new window should open after the console displays ‘Constructing Window’ and by clicking on it you should see the Game Main Menu.

*Join & Create*

By clicking on the “New Game” button, a pop up will allow you to type your username and if there are no games in the Server or they all have already added the right amount of players, the Scene will change to “Choose number of player” and you will be asked to click on the desired number of players so to Create a new Game.
Otherwise, you will simply Join an existing game without selecting the number of players.

⚠ DON’T press enter after typing your username, instead simply press the OK button.

You will wait until all have join. When that happens, the scene will change and you will be dealt your cards!
After the match has begun
Your personal Cards are in the bottom right of the screen; initially you are dealt 6 Cards: 3 Cards you can place on the PlayBoard on the right, 2 Goal Cards and 1 Starting Card on the left.
On the top of the screen, a banner will help you navigate your turn, here’s the list of possible moves and how to act on them:

-	CHOOSE GOAL CARD: press (left click) on one of the Goal Cards on your Hand to select it.
 
-	PLACE STARTING CARD: you can right click on the starting Card in your Hand if you want to flip it, before left clicking on it to place it.

-	PLAY: you can right click on a Playable Card in your Hand if you want to flip it, before left clicking on it to select it. To place it down, you need to also click on the desired space after selecting the Card. If your Card can be played in the desired space, you should now see your Card on the PlayBoard. Otherwise the Board will remain unchanged and you can try a different Card or a different position.

-	DRAW: the scene changes and you can no longer see the cards you already played. You can click one of the Decks or Public Cards to draw from them. The Card is automatically added to your Hand cards.

-	SPECTATING: no actions available, it’s not your turn. You can see the current player’s cards.

You can scale the Cards on the PlayBoard at any given time by pressing N (down) or M (up). Pressing U will manually update your view. You can flip any Playable Card on your Hand by right clicking on it.

*Reconnect*

If for any reason while you were playing you were disconnected from your Game, you can re-run a Client (App.java) and follow the previously provided instructions.

When you reach the Main Menu, press the “Reconnect” button. You can type in the dialogue window your old username, press OK, then you can type your old port number provided when Joining.

⚠ DON’T press enter after typing your username or the port, instead simply press the OK button.

If the information is correct and the Server is available, you should be able to get back to how you left the Game! 
*Server crashing*
Attempt a reconnection. You will need your previous port along with your username.


**TUI GUIDE**

You will remain in console. Follow the instructions on screen.
Once you are able to Join or Create a Game, your PlayBoard – where you will see your placed Cards will be first thing being rendered, followed by the rest of the Table on the left: your personal Cards in the first two columns, the Public cards on the next two columns. 
Finally on the right you have all the possible instructions to follow. At the bottom of the Table a small guide will help you recognize what phase of your tun you are in.

Type any of the provided commands, but keep the next instructions in mind that in order to correctly.

*Choose a Goal Card:* press “8” for the top Goal Card, “9” for the bottom Goal Card.

*Place Starting Card:* press “r” to select the Starting Card before flipping it with “f”, or simply place it by typing “s”.

*Play:*
1.	Select a space in your hand between “q”, “w”, “e” that represent the first Card in the first row of the Table, the second Card on the first row and the first Card on the second row, respectively. You can press now the enter key.
2.	If you want , once the Card has been selected you can flip it with “f”.
3.	Press “p” to signal the desire to place the Card.
4.	Go to your PlayBoard so you can see the available spaces’ numbers and type the one where you would like to place your Card, before pressing the enter key.
If the Card can be played in the desired space, you will now be in the Draw phase. Otherwise you will have to try again.
*Draw:* the places you can draw from are listed from left to right, bottom to left; “0” for the Gold Deck, “1” for the Resource Deck, then “2”, “3”, “4” and “5” for the shown Cards on the last two columns of the Table.

### HOW TO TEST
Before running the test class, make sure the ‘SavedGames’ folder in ‘resources’ is empty otherwise you will not be able to properly run ‘Utility Test’ -> ‘TestPersistance’.  

