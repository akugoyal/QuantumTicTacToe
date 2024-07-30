import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.util.*;

/**
 * A graphical component for displaying the contents of a binary tree.
 * The following methods are designed for interacting with the display:
 * Default constructor TreeDisplay()
 * displayTree(TreeNode someTree)
 * visit(TreeNode someNode)
 * The displayTree method creates the initial display of the tree.  It clears the component
 * and paints the tree
 * The visit method changes the background color of the node defined in the parameter
 * sample Useage:
 * TreeDisplay display = new TreeDisplay();
 * display.displayTree(someTree);
 * display.visit(someNode);
 *
 * @author DaveF
 * @author RichardP
 * @version 102613
 */
public class Display extends JComponent implements ActionListener
{
   public static final int WINDOW_HEIGHT = 650;
   public static final int WINDOW_WIDTH = 700;
   public static final int ADDITIONAL_CONTROLS_WIDTH = 100;
   public static final int ADDITIONAL_CONTROLS_HEIGHT = 100;
   public static final int ADDITIONAL_MESSAGE_WIDTH = 0;
   public static final int ADDITIONAL_MESSAGE_HEIGHT = 100;
   public static final int REFRESH_PERIOD = 100; //Window update rate in milliseconds
   public static final int X_OFFSET = 150;
   public static final int Y_OFFSET = 0;
   public static final int BORDER_MARGIN = 10;
   public static final int GRID_MARGIN = 20;
   public static final int MIN_GRID_WIDTH = 50;
   public static final Color X_COLOR = Color.RED;
   public static final Color O_COLOR = Color.BLUE;
   public static final Color TIE_COLOR = Color.GREEN;
   public static final double kFONT_SIZE = 1.0;
   public static final Font ARIAL_40 = new Font("Arial", Font.PLAIN, 40);
   public static final Stroke DEFAULT_STROKE = new BasicStroke();
   public static final int ACTIVE_PAD = 5;
   public static final int ACTIVE_THICKNESS = 3;
   public static final Color ACTIVE_COLOR = Color.GREEN;

   //the game being displayed
   private Game game;
   private JFrame frame;
   private ArrayList<JButton> buttons;
   private JButton newUniverseButton;

   //creates a frame with a new Display component.
   //(constructor returns the Display component--not the frame).
   public Display(String title, Game g)
   {
      game = g;
      //create surrounding frame
      frame = new JFrame(title);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setLocation(X_OFFSET, Y_OFFSET);
      //add the Display component to the frame
      frame.getContentPane().add(this);
      frame.setFont(ARIAL_40);
      //show frame
      frame.pack();

      buttons = new ArrayList<JButton>();
      for (int i = 0; i < 9; i++) {
         JButton button = new JButton();
         button.setBounds(BORDER_MARGIN, BORDER_MARGIN,
               BORDER_MARGIN + 10,
               BORDER_MARGIN + 10);
         button.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
         button.setFont(ARIAL_40);
         button.addActionListener(this);
         buttons.add(button);
         frame.add(button, BorderLayout.CENTER);
      }

      newUniverseButton = new JButton();
      newUniverseButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
      newUniverseButton.setFont(new Font("Arial", Font.PLAIN, 12));
      newUniverseButton.setText("New Universe");
      newUniverseButton.addActionListener(this);
      frame.add(newUniverseButton, BorderLayout.CENTER);

      frame.setVisible(true);

      java.util.Timer timer = new java.util.Timer();
      TimerTask task = new TimerTask()
      {
         public void run()
         {
            Display.this.repaint();
         }
      };
      timer.schedule(task, 0, REFRESH_PERIOD);
   }

   //Gets the size of the game window
   public Dimension getPreferredSize()
   {
      int xDim = WINDOW_WIDTH + ADDITIONAL_CONTROLS_WIDTH + ADDITIONAL_MESSAGE_WIDTH;
      int yDim = WINDOW_HEIGHT + ADDITIONAL_CONTROLS_HEIGHT + ADDITIONAL_MESSAGE_HEIGHT;
      return new Dimension(xDim, yDim);
   }

   //called whenever the Display must be drawn on the screen
   public void paint(Graphics g)
   {
      if (game != null)
      {
         Graphics2D g2 = (Graphics2D) g;
         g2.setFont(ARIAL_40);

         newUniverseButton.setBounds(WINDOW_WIDTH + BORDER_MARGIN,
               BORDER_MARGIN,
               ADDITIONAL_CONTROLS_WIDTH - BORDER_MARGIN,
               ADDITIONAL_CONTROLS_HEIGHT - BORDER_MARGIN);

         int maxNumInRow =
               (WINDOW_WIDTH - 2 * BORDER_MARGIN) / (MIN_GRID_WIDTH + GRID_MARGIN);
         int numGrids = game.getNumUniverses();
         int numRows = numGrids / maxNumInRow + 1;
         int tempWidth =
               (WINDOW_WIDTH - (numGrids - 1) * GRID_MARGIN -
                     2 * BORDER_MARGIN) / numGrids;
         int gridWidth = Math.max(tempWidth, MIN_GRID_WIDTH);

         int counter = 0;
         boolean done = false;
         for (int i = 0; i < numRows; i++) {
            if (done) {
               break;
            }
            for (int j = 0; j < maxNumInRow; j++) {
               int x =
                     BORDER_MARGIN + j * (gridWidth + GRID_MARGIN);
               int y =
                     BORDER_MARGIN + i * (gridWidth + GRID_MARGIN);

               boolean active = game.getIndOfActiveUniverse() == counter;
               drawUniverse(g2, x, y, gridWidth, active, game.getAllUniverses().get(counter));
               counter++;
               if (counter >= numGrids) {
                  done = true;
                  break;
               }
            }
         }

         if (game.gameOver())  {
            Main.players winner = game.getWinner();
            if (winner == Main.players.X) {
               setMessage("X WINS, " + game.getXOScoresAsString(), ARIAL_40, g2, X_COLOR);
            } else if (winner == Main.players.O){
               setMessage("O WINS, " + game.getXOScoresAsString(), ARIAL_40, g2, O_COLOR);
            } else {
               setMessage("TIE, " + game.getXOScoresAsString(), ARIAL_40, g2, TIE_COLOR);
            }
         }
         else if (game.getTurn() == Main.players.X) {
            String msg = game.getXOScoresAsString() + ", X's turn";
            setMessage(msg, ARIAL_40, g2, X_COLOR);
         } else {
            String msg = game.getXOScoresAsString() + ", O's turn";
            setMessage(msg, ARIAL_40, g2, O_COLOR);
         }
      }
   }

   private void drawUniverse(Graphics2D g2, int x, int y, int gridWidth,
                             boolean active, Universe universe)
   {
      int[][] gridLines = new int[][] {
            {x + gridWidth / Constants.UNIVERSE_SIZE, y, x + gridWidth / Constants.UNIVERSE_SIZE, y + gridWidth},
            {x + 2 * gridWidth / Constants.UNIVERSE_SIZE, y, x + 2 * gridWidth / Constants.UNIVERSE_SIZE, y + gridWidth},
            {x, y + gridWidth / Constants.UNIVERSE_SIZE, x + gridWidth, y + gridWidth / Constants.UNIVERSE_SIZE},
            {x, y + 2 * gridWidth / Constants.UNIVERSE_SIZE, x + gridWidth, y + 2 * gridWidth / Constants.UNIVERSE_SIZE}
      };

      if (active) {
         g2.setStroke(new BasicStroke(ACTIVE_THICKNESS));
         g2.setColor(ACTIVE_COLOR);
         g2.drawRect(x - ACTIVE_PAD, y - ACTIVE_PAD, gridWidth + 2 * ACTIVE_PAD,
               gridWidth + 2 * ACTIVE_PAD);
      }

      g2.setStroke(DEFAULT_STROKE);
      g2.setColor(Color.BLACK);
      for (int[] line : gridLines) {
         if (universe.won())
         {
            g2.setColor((universe.getWinner() == Main.players.X) ? X_COLOR : O_COLOR);
         }
         g2.drawLine(line[0], line[1], line[2], line[3]);
      }

      for (int i = 0; i < Constants.UNIVERSE_SIZE; i++) {
         for (int j = 0; j < Constants.UNIVERSE_SIZE; j++)
         {
            Main.players square = universe.getSquare(i, j);
            int startX = x + i * gridWidth / Constants.UNIVERSE_SIZE;
            int startY = y + j * gridWidth / Constants.UNIVERSE_SIZE;

            if (active)
            {
               JButton button = buttons.get(i * Constants.UNIVERSE_SIZE + j);
               button.setBounds(startX, startY, gridWidth / Constants.UNIVERSE_SIZE, gridWidth / Constants.UNIVERSE_SIZE);

               if (square == null)
               {
                  button.setEnabled(true);
                  button.setText("");
               }
            }

            g2.setFont(new Font("Arial", Font.PLAIN,
                  (int) (gridWidth / Constants.UNIVERSE_SIZE * kFONT_SIZE)));
            int height = g2.getFontMetrics().getHeight();
            String msg = null;
            if (square == Main.players.X) {
               g2.setColor(X_COLOR);
               msg = "X";
            }
            else if (square == Main.players.O) {
               g2.setColor(O_COLOR);
               msg = "O";
            }
            if (msg != null) {
               int width = g2.getFontMetrics().stringWidth(msg);
               int msgX = startX + (gridWidth / Constants.UNIVERSE_SIZE - width) / 2;
               int msgY = startY + (gridWidth / Constants.UNIVERSE_SIZE - height) / 2;
               g2.drawString(msg, msgX, msgY + gridWidth / Constants.UNIVERSE_SIZE);
            }
         }
      }

      if (universe.won())
      {
         g2.setColor((universe.getWinner() == Main.players.X) ? X_COLOR : O_COLOR);
         g2.drawRect(x, y, gridWidth, gridWidth);
      }
   }

   /**
    * Invoked when an action occurs.
    *
    * @param e the event to be processed
    */
   @Override
   public void actionPerformed(ActionEvent e)
   {
      JButton button = (JButton) e.getSource();
      if (button == newUniverseButton) {
         game.createNewUniverse();
      } else {
         button.setEnabled(false);
         int ind = buttons.indexOf(button);
         int x = ind / Constants.UNIVERSE_SIZE;
         int y = ind % Constants.UNIVERSE_SIZE;
         game.move(x, y);
      }

      if (game.gameOver()) {
         frame.remove(newUniverseButton);
         for (JButton b : buttons) {
            frame.remove(b);
         }
      }
   }

   public JFrame getFrame() {
      return frame;
   }

   public void setMessage(String message, Font font, Graphics2D g2, Color color) {
      g2.setColor(color);
      g2.setFont(font);
      g2.setStroke(DEFAULT_STROKE);
      int width = g2.getFontMetrics().stringWidth(message);
      int height = g2.getFontMetrics().getHeight();
      int x = (getPreferredSize().width - width) / 2;
      int y = getPreferredSize().height - (ADDITIONAL_MESSAGE_HEIGHT - height) / 2;
      g2.drawString(message, x, y);
   }
}