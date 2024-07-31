public class Move
{
   private int x;
   private int y;
   private boolean createNew;

   public Move(int x, int y, boolean createNew)
   {
      this.x = x;
      this.y = y;
      this.createNew = createNew;
   }

   public int getX()
   {
      return x;
   }

   public int getY()
   {
      return y;
   }

   public boolean getCreateNew()
   {
      return createNew;
   }

   public String toString()
   {
      return "Move to (" + x + ", " + y + ") - " + (createNew ? "True" : "False");
   }
}
