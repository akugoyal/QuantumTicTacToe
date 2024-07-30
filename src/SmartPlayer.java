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

   public ArrayList<int[]> getAllMoves(Game game) {
      ArrayList<int[]> moves = new ArrayList<int[]>();
      for (int i = 0; i < Constants.UNIVERSE_SIZE; i++) {
         for (int j = 0; j < Constants.UNIVERSE_SIZE; j++) {
            if (game.getActiveUniverse().getSquare(i, j) == null) {
               moves.add(new int[] {i, j});
            }
         }
      }
      moves.add(new int[] {-1, -1});
      return moves;
   }

   public int[] nextMove() {
      ArrayList<int[]> all = getAllMoves(currentGame);
      int[] scores = new int[all.size()];

      int max = Integer.MIN_VALUE;
      for (int i = 0; i < all.size(); i++)
      {
         Game g = currentGame.clone();
         int s = g.move(all.get(i)[0], all.get(i)[1]);
         s += valueOfMeanestResponse(3, g);
         scores[i] = s;
         if (s > max)
         {
            max = s;
         }
      }

      ArrayList<int[]> bestMoves = new ArrayList<int[]>();
      for (int i = 0; i < all.size(); i++)
      {
         if (scores[i] == max)
         {
            bestMoves.add(all.get(i));
         }
      }

      int ind = (int) (Math.random() * bestMoves.size());
      return bestMoves.get(ind);
   }

   private int valueOfMeanestResponse(int moves, Game game)
   {
      if (moves <= 0)
      {
         return score();
      }

      ArrayList<int[]> all = getAllMoves(game);

      int worst = score();
      for (Move i : all)
      {
         getBoard().executeMove(i);
         int s = valueOfBestMove(moves - 1);
         worst = Integer.min(s, worst);
         getBoard().undoMove(i);
      }

      return worst;
   }

   private int valueOfBestMove(int moves)
   {
      if (moves <= 0)
      {
         return score();
      }
      ArrayList<Move> all = getBoard().allMoves(getColor());
      int max = Integer.MIN_VALUE;
      for (int i = 0; i < all.size(); i++)
      {
         getBoard().executeMove(all.get(i));
         int s = valueOfMeanestResponse(moves - 1);
         if (s > max)
         {
            max = s;
         }
         getBoard().undoMove(all.get(i));
      }

      return max;
   }
}
