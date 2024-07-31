import java.awt.*;
import java.util.*;
/**
 * Minimax algorithm that looks Constants.MINIMAX_DEPTH moves deep to determine the best move.
 *
 * +3 for winning a universe
 * +2 for overriding a piece
 * +1 for placing a piece
 */
public class SmartPlayer
{
   public Game currentGame;
   public ArrayList<Game> futureGames;

   public SmartPlayer(Game game) {
      currentGame = game;
      futureGames = new ArrayList<Game>();
      futureGames.add(game);
   }

   public ArrayList<Move> getAllMoves(Game game) {
      ArrayList<int[]> m = new ArrayList<int[]>();
      for (int i = 0; i < Constants.UNIVERSE_SIZE; i++) {
         for (int j = 0; j < Constants.UNIVERSE_SIZE; j++) {
            if (game.getActiveUniverse().getSquare(i, j) == null) {
               m.add(new int[] {i, j});
            }
         }
      }

      ArrayList<Move> moves = new ArrayList<Move>();
      for (int[] i : m) {
         moves.add(new Move(i[0], i[1], false));
         moves.add(new Move(i[0], i[1], true));
      }
      return moves;
   }

   public Move nextMove() {
      ArrayList<Move> all = getAllMoves(currentGame);
//      System.out.println("nexting");
//      for (Move i : all)
//      {
//         System.out.println(i);
//      }
      int[] scores = new int[all.size()];

      int max = Integer.MIN_VALUE;
      for (int i = 0; i < all.size(); i++)
      {
         Game g = currentGame.myClone();
         Move m = all.get(i);
         System.out.println("Move " + i + " - " + m);
         if (m.getCreateNew()) {
            g.createNewUniverse();
         }
         int s = g.move(m.getX(), m.getY(), true);
         s += valueOfMeanestResponse(Constants.MINIMAX_DEPTH - 1, g);
//         System.out.println(m);
//         System.out.println(s);
         scores[i] = s;
         if (s > max)
         {
            max = s;
         }
      }


//      System.out.println(Arrays.toString(scores));
      ArrayList<Move> bestMoves = new ArrayList<Move>();
      for (int i = 0; i < all.size(); i++)
      {
         if (scores[i] == max)
         {
            bestMoves.add(all.get(i));
         }
      }

      for (Move i : bestMoves)
      {
         System.out.println(i);
      }
      System.out.println();
      int ind = (int) (Math.random() * bestMoves.size());
      return bestMoves.get(ind);
//      return new int[] {1, 1};
   }

   private int valueOfMeanestResponse(int moves, Game game)
   {
      if (moves <= 0)
      {
         return 0;
      }

      ArrayList<Move> all = getAllMoves(game);
//      System.out.println("moves: " + moves);
      int worst = 0;
      for (Move i : all)
      {
//         System.out.println(game.getTurn());
         Game g = game.myClone();
         if (i.getCreateNew()) {
            g.createNewUniverse();
         }
         int s = g.move(i.getX(), i.getY(), true);
         s += valueOfBestMove(moves - 1, g);
         worst = Integer.max(s, worst);
         System.out.println(i.toString() + s);
      }

      return -worst;
   }

   private int valueOfBestMove(int moves, Game game)
   {
      if (moves <= 0)
      {
         return 0;
      }
      ArrayList<Move> all = getAllMoves(game);
//      System.out.println("besting");

      int max = Integer.MIN_VALUE;
      for (int i = 0; i < all.size(); i++)
      {
         Game g = game.myClone();
         Move m = all.get(i);
         if (m.getCreateNew()) {
            g.createNewUniverse();
         }
         int s = g.move(m.getX(), m.getY(), true);
         s += valueOfMeanestResponse(moves - 1, g);
         if (s > max)
         {
            max = s;
         }
      }

      return max;
   }
}
