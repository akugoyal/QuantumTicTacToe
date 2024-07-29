Run Main.java to begin game.

## Game Rules
1. Each 3x3 grid is a "universe"
2. Get three in a row to win the universe
3. Once a universe has been won, it cannot be won again
4. You can create a new universe at the start of your turn
5. You must play in the oldest unfilled universe
6. The game ends when all universes have been won or all universes are full
7. When you place a piece, it is flipped about the center of the universe for each subsequent universe except the last one. The last universe mirrors the one before it
8. If the piece you just placed intersects with another one of your own pieces, that square 
   collapses, your move doesn't progress to the universes after the one that collapsed, and your 
   turn ends.
9. If the piece you just placed intersects with one of the opponent's pieces, the opponent's 
   move is overwritten with your move. This doesn't change the history of who won in that universe.
10. When you create a new universe, the last move which reached the last universe is copied to the 
    new universe.