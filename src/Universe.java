

public class Universe
{
   private Main.players[][] universe;
   private Main.players winner;
   private boolean tied;

   public Universe() {
      universe = new Main.players[Constants.UNIVERSE_SIZE][Constants.UNIVERSE_SIZE];

      for (int i = 0; i < Constants.UNIVERSE_SIZE; i++) {
         universe[i] = new Main.players[Constants.UNIVERSE_SIZE];

         for (int j = 0; j < Constants.UNIVERSE_SIZE; j++) {
            universe[i][j] = null;
         }
      }
      winner = null;
      tied = false;
   }

   public Universe(Main.players[][] universe, Main.players winner, boolean tied) {
      this.universe = universe;
      this.winner = winner;
      this.tied = tied;
   }

   public Main.players getSquare(int x, int y) {
      return universe[x][y];
   }

   public int[] getDimensions() {
      return new int[] {universe.length, universe[0].length};
   }

   /**
    * returns 0 if collapsed, 2 if overridden, 1 if placed
    * @param x
    * @param y
    * @param p
    * @return
    */
   public int move(int x, int y, Main.players p) {
      if (universe[x][y] == p) {
         return 0;
      }
      if (universe[x][y] != null)
      {
         universe[x][y] = p;
         return (won()) ? 0 : 2;
      }
      universe[x][y] = p;
      return 1;
   }

   public boolean updateWinner() {
      if (winner == null && !tied) {
         //Check columns
         boolean winnerFound = true;
         for (int i = 0; i < Constants.UNIVERSE_SIZE; i++) {
            winnerFound = true;
            for (int j = 1; j < Constants.UNIVERSE_SIZE; j++) {
               if (universe[i][j] != universe[i][j - 1] || universe[i][j] == null || universe[i][j - 1] == null) {
                  winnerFound = false;
                  break;
               }
            }
            if (winnerFound) {
               winner = universe[i][0];
               return true;
            }
         }

         //Check rows
         winnerFound = true;
         for (int i = 0; i < Constants.UNIVERSE_SIZE; i++) {
            winnerFound = true;
            for (int j = 1; j < Constants.UNIVERSE_SIZE; j++) {
               if (universe[j][i] != universe[j - 1][i] || universe[j][i] == null || universe[j - 1][i] == null) {
                  winnerFound = false;
                  break;
               }
            }
            if (winnerFound) {
               winner = universe[0][i];
               return true;
            }
         }

         //Check diagonals
         winnerFound = true;
         for (int i = 1; i < Constants.UNIVERSE_SIZE; i++) {
            if (universe[i][i] != universe[i - 1][i - 1] || universe[i][i] == null || universe[i - 1][i - 1] == null) {
               winnerFound = false;
               break;
            }
         }
         if (winnerFound) {
            winner = universe[0][0];
            return true;
         }
         winnerFound = true;
         for (int i = 1; i < Constants.UNIVERSE_SIZE; i++) {
            if (universe[i][Constants.UNIVERSE_SIZE - i - 1] !=
                  universe[i - 1][Constants.UNIVERSE_SIZE - i] || universe[i][Constants.UNIVERSE_SIZE - i - 1] == null || universe[i - 1][Constants.UNIVERSE_SIZE - i] == null) {
               winnerFound = false;
               break;
            }
         }
         if (winnerFound) {
            winner = universe[0][Constants.UNIVERSE_SIZE - 1];
         }
      }
      if (!tied && filled() && winner == null) {
         tied = true;
      }
      return false;
   }

   public boolean won() {
      return winner != null;
   }

   public boolean filled() {
      for (int i = 0; i < Constants.UNIVERSE_SIZE; i++) {
         for (int j = 0; j < Constants.UNIVERSE_SIZE; j++) {
            if (universe[i][j] == null) {
               return false;
            }
         }
      }

      return true;
   }

   public Main.players getWinner() {
      return winner;
   }

   public Universe myClone() {
      int x = getDimensions()[0];
      int y = getDimensions()[1];
      Main.players[][] uni = new Main.players[x][y];
      for (int i = 0; i < x; i++) {
         for (int j = 0; j < y; j++) {
            uni[i][j] =  universe[i][j];
         }
      }
      return new Universe(uni, winner, tied);
   }
}
