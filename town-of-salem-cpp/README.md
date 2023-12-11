# town-of-salem

~ july 2023
a little town of salem - like game based on my assignments from a university course


In this game players participate, each with a role. The roles are: 1) Citizen 2) Doctor 3) Gangster.
In the beginning of the game a role and a name is assigned to each player using a txt file. There is one doctor, one
gangster and the rest are citizens. Tha game is split into 2 phases, day and night.
In the night phase, the citizens are “asleep“ and the gangster selects which player has to leave (lose).
Then, the doctor decides which player to save from the gangster. If the doctor selects the same player
that the gangster chose, the player is saved and he does not lose. At the end of the night it is announced
which player lost (if a player has lost).
In the day phase, the players vote one of the rest players to quit the game. The player with the most
votes loses.
The cycle of day and night repeats until the gangster loses or all the players lose.
If the gangster loses while there are still players left the citizens win. If there are two players left and
one of them is the gangster, the gangster wins.
