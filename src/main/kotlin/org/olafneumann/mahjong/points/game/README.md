# Data Model

The data model consists of the following types:

 - `Session`

   A `session` collects all information that is required to compute a `player`s total points and determine, who is the overall winner. A match defines the players that play against each other.

   A `session` consists of several `round`s.

   In a complete `session` of Mah Jong might take a number of hours to complete. Once each player has been East wind, South becomes the prevailing wind. Once South is finished, the prevailing wind becomes West and finally North. The session ends when each player has played as the prevailing North wind. [^1] 

   Obviously, it isn't necessary to complete a session - playing a set number of rounds or to a target score is just as good.

 - `Round`
   
   A `round` is an actual game play that starts with building the wall and ends with one player calling Mahjong (or a draw, if the end of the wall is reached).

   Such a `round` contains the information, who is the winner and how many points each player reached.

 - `Player`

   A `player` is the person that is taking part in the match.

The `session` will keep track, which wind is prevailing and which player has which seat wind.

This information is enough to compute the winds for each round and also to compute the final scoring of the session.

[^1]: https://www.mastersofgames.com/rules/mah-jong-rules.htm
