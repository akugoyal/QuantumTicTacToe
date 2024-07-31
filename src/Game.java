import java.util.ArrayList;

public class Game
{
   private ArrayList<Universe> universes;
   private Main.players turn;
   private int oScore;
   private int xScore;
   private int activeUniverse;
   private int lastMoveX;
   private int lastMoveY;
   private Main.players lastMoveTurn;
   private boolean universeCreated;

   public Game() {
      universes = new ArrayList<Universe>();

      for (int i = 0; i < Constants.NUM_INIT_UNIVERSES; i++) {
         universes.add(new Universe());
      }

      activeUniverse = 0;
      turn = Constants.BEGINNING_PLAYER;
      lastMoveX = -1;
      lastMoveY = -1;
      lastMoveTurn = null;
      universeCreated = false;
   }

   public Game(ArrayList<Universe> universes, Main.players turn, int oScore, int xScore, int activeUniverse, int lastMoveX, int lastMoveY, Main.players lastMoveTurn, boolean universeCreated) {
      this.universes = universes;
      this.turn = turn;
      this.oScore = oScore;
      this.xScore = xScore;
      this.activeUniverse = activeUniverse;
      this.lastMoveX = lastMoveX;
      this.lastMoveY = lastMoveY;
      this.lastMoveTurn = lastMoveTurn;
      this.universeCreated = universeCreated;
   }

   public ArrayList<Universe> getAllUniverses() {
      return universes;
   }

   public int getNumUniverses() {
      return universes.size();
   }

   public boolean gameOver() {
      if (activeUniverse >= getNumUniverses()) {
         return true;
      }

      for (Universe u : universes) {
         if (!u.won() && !u.filled()) {
            return false;
         }
      }

      return true;
   }

   public Universe getUniverse(int ind) {
      return universes.get(ind);
   }

   public int move(int x, int y, boolean minimaxxing) {
      int middleInd = ((Constants.UNIVERSE_SIZE * Constants.UNIVERSE_SIZE - 1) / 2);
      int reflectedInd = 2 * middleInd - (x * Constants.UNIVERSE_SIZE + y);
      int reflectedX = reflectedInd / Constants.UNIVERSE_SIZE;
      int reflectedY = reflectedInd % Constants.UNIVERSE_SIZE;

      boolean collapsed = false;
      int score = 0;
      for (int i = getIndOfActiveUniverse(); i < getNumUniverses() - 1; i++) {
         if ((i - getIndOfActiveUniverse()) % 2 == 0) {
            int s = getUniverse(i).move(x, y, turn);
            collapsed = s == 0;
            score += s;
         } else {
            int s = getUniverse(i).move(reflectedX, reflectedY, turn);
            collapsed = s == 0;
            score += s;
         }

         if (collapsed) {
            break;
         }
      }
      if (!collapsed) {
         if ((getNumUniverses() - 2 - getIndOfActiveUniverse()) % 2 == 0) {
            score += getUniverse(getNumUniverses() - 1).move(x, y, turn);
            lastMoveX = x;
            lastMoveY = y;
            lastMoveTurn = turn;
         } else {
            score += getUniverse(getNumUniverses() - 1).move(reflectedX, reflectedY, turn);
            lastMoveX = reflectedX;
            lastMoveY = reflectedY;
            lastMoveTurn = turn;
         }
      }

      oScore = 0;
      xScore = 0;
      for (Universe u : universes) {
         boolean alrWon = u.won();
         u.updateWinner();
         if (u.won()) {
            if (u.getWinner() == Main.players.X) {
               xScore++;
            } else if (u.getWinner() == Main.players.O) {
               oScore++;
            }
         }
         if (u.won() && !alrWon && u.getWinner() == Main.players.O) {
            score += 3;
         }
      }

//      System.out.println("X: " + xScore);
//      System.out.println("O: " + oScore);
//      System.out.println();

      while (!gameOver() && getActiveUniverse().filled()) {
         activeUniverse++;
      }
      if (turn == Main.players.X) {
         turn = Main.players.O;
      }
      else {
         turn = Main.players.X;
      }

      universeCreated = false;

      if (Constants.PLAY_RANDOM && turn == Main.players.O) {
         int m = (int) (Math.random() * 15);
         while (m >= 9) {
            createNewUniverse();
            m = (int) (Math.random() * 10);
         }
         int a = m / Constants.UNIVERSE_SIZE;
         int b = m % Constants.UNIVERSE_SIZE;
         while (m == 9 || getActiveUniverse().getSquare(a, b) != null)
         {
            m = (int) (Math.random() * 10);
            a = m / Constants.UNIVERSE_SIZE;
            b = m % Constants.UNIVERSE_SIZE;
         }
         move(a, b, false);
      } else if (Constants.PLAY_MINIMAX && turn == Main.players.O && !minimaxxing) {
         SmartPlayer sp = new SmartPlayer(this);
         Move m = sp.nextMove();
         if (m.getCreateNew()) {
            createNewUniverse();
         }
         move(m.getX(), m.getY(), false);
      }

      if (gameOver()) {
//         System.out.println("GAME OVER");
      }

      return score;
   }

   public Universe getActiveUniverse() {
      return universes.get(activeUniverse);
   }

   public int getIndOfActiveUniverse() {
      return activeUniverse;
   }

   public void createNewUniverse() {
      if (!gameOver())
      {
         if (!universeCreated)
         {
            if (getNumUniverses() >= Constants.NUM_MAX_UNIVERSES)
            {
               if (getUniverse(0).filled()) {
                  removeUniverse(0);
               }
            }
            Universe u = new Universe();
            if (lastMoveX != -1)
            {
               u.move(lastMoveX, lastMoveY, lastMoveTurn);
            }
            universes.add(u);
            universeCreated = true;
         }
      }
   }

   public Main.players getTurn() {
      return turn;
      //hi
   }

   public Main.players getWinner() {
      if (!gameOver()) {
         return null;
      }
      return (xScore > oScore) ? Main.players.X : (xScore == oScore) ? Main.players.Tie :
            Main.players.O;
   }

   public int getXScore() {
      return xScore;
   }

   public int getOScore() {
      return oScore;
   }

   public void removeUniverse(int ind) {
      universes.remove(ind);
      if (ind < activeUniverse) {
         activeUniverse--;
      }
   }

   public String getXOScoresAsString() {
      return "X: " + getXScore() + ", O: " + getOScore();
   }

   public Game myClone() {
      ArrayList<Universe> unis = new ArrayList<Universe>();
      for (Universe i : universes) {
         unis.add(i.myClone());
      }

      return new Game(unis, turn, oScore, xScore, activeUniverse, lastMoveX, lastMoveY, lastMoveTurn, universeCreated);
   }
}
