public class Main
{
   public static Game game;
   public static Display display;
   public enum players {
      X,
      O,
      Tie
   }

   public static void main(String[] args)
   {
      game = new Game();
      display = new Display("Quantum Tic-Tac-Toe", game);

      while (true) {
         display.repaint();
      }
   }

   public static void createNewGame() {
      display.getFrame().remove(display);
      display.getFrame().setVisible(false);
      display.getFrame().dispose();
      game = new Game();
      display = new Display("Quantum Tic-Tac-Toe", game);
   }
}