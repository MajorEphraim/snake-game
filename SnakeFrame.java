import javax.swing.JFrame;
import java.awt.event.WindowEvent;    
import java.awt.event.WindowListener; 

public class SnakeFrame extends JFrame
{
    // instance variables - replace the example below with your own
    private int x;

    /**
     * Constructor for objects of class SnakeFrame
     */
    public SnakeFrame()
    {
      this.add(new SnakePanel());
      this.setTitle("Snake Game by Ephraim Ncube");
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setResizable(false);
      this.pack();
      this.setVisible(true);
      this.setLocationRelativeTo(null);
    }
    
    static void quitGame()
    {
        System.exit(0);
    }
    
}
