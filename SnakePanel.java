import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.Color;
import java.util.Random;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Font;
import java.awt.FontMetrics;

//image imports 
import java.awt.Image;
import javax.swing.ImageIcon;

import java.awt.Toolkit;
import java.awt.Dimension;

public class SnakePanel extends JPanel implements ActionListener {
    
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    static int GAME_WIDTH = screenSize.width;
    static int GAME_HEIGHT = screenSize.height;
    
    static int UNIT_SIZE = 40;
    static int SNAKE_SPEED = 150;
    
    static int GRID_WIDTH = UNIT_SIZE*25;
    static int GRID_HEIGHT = UNIT_SIZE*16;
    
    int gridX = (GAME_WIDTH - GRID_WIDTH) / 2;
    int gridY = (GAME_HEIGHT - GRID_HEIGHT) / 2;
    
    private Image background;
    
    Color backgroundColor = new Color(0, 0, 204);  
    Color snakeColor = new Color(0, 0, 0);  
    Color foodColor = new Color(0, 255, 0);  
    Color scoreColor = new Color(255, 255, 102);
    Color gameOverColor = new Color(213, 50, 80);
    
    int foodX;
    int foodY;
    int snakeX = GAME_WIDTH/2;
    int snakeY = GAME_HEIGHT/2;
    int changeX = UNIT_SIZE;
    int changeY = 0;
    
    int snakeSize = 1;

    ArrayList<Integer> xValues = new ArrayList<Integer>(GAME_WIDTH);
    ArrayList<Integer> yValues = new ArrayList<Integer>(GAME_HEIGHT);
    
    boolean isQuit = false;
    boolean isOver = false;

    Random random;
    Timer timer;
    public SnakePanel()
    {
        random = new Random();
        this.setPreferredSize(new Dimension(GAME_WIDTH,GAME_HEIGHT));
        this.setBackground(backgroundColor);
        this.setFocusable(true);
        this.addKeyListener(new SnakeKeyAdapter());
        background = new ImageIcon(getClass().getResource("/assets/background.png"))
        .getImage();
        initializeGame();
    }

    void initializeGame()
    {
    
        isOver = false;
        changeY =0;
        changeX = 0;
        snakeX = GAME_WIDTH/2;
        snakeY = GAME_HEIGHT/2;
        xValues.clear();
        yValues.clear();
        xValues.add(snakeX);
        yValues.add(snakeY);
        snakeSize = 1;
        displayFood();
        
        if(timer != null)
        {
            timer.stop();
        }
        
        timer = new Timer(SNAKE_SPEED, this);
        timer.start();
    }
    
    void displayFood()
    {
        boolean valid;
    
        do
        {
            valid = true;
    
            foodX = random.nextInt(GAME_WIDTH / UNIT_SIZE) * UNIT_SIZE;
            foodY = random.nextInt(GAME_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    
            for(int i = 0; i < xValues.size(); i++)
            {
                if(foodX == xValues.get(i) &&
                   foodY == yValues.get(i))
                {
                    valid = false;
                    break;
                }
            }
    
        } while(!valid);
    }
    
    void moveSnake()
    {
        snakeX += changeX;
        snakeY += changeY;
        
        xValues.add(snakeX);
        yValues.add(snakeY);
        
        while(xValues.size() > snakeSize)
        {
            xValues.remove(0);
            yValues.remove(0);
        }
    }
    
    void checkMovements(){
        if(snakeX == foodX && snakeY == foodY)
        {
            snakeSize++;
            displayFood();
        }
        
        //collisions with sides
        if (snakeX < gridX || snakeX >= (GRID_WIDTH + gridX))
            isOver = true;
            
        //top or bottom collisions
        if (snakeY < gridY || snakeY >= (GRID_HEIGHT + gridY))
            isOver = true;
        
        //snake colliding with itself
       for(int i = 0; i < xValues.size() - 1; i++)
        {
            if(snakeX == xValues.get(i) &&
               snakeY == yValues.get(i))
            {
                isOver = true;
                break;
            }
        }
        
        //stop the speed when the game is over
        if(isOver)
            timer.stop();
        
    
    }

     void drawFood(Graphics g)
    {
       
        g.setColor(foodColor);
        g.fillRect(foodX, foodY, UNIT_SIZE, UNIT_SIZE);
        
    }
    
     void drawScore(Graphics g)
    {
        g.setColor(scoreColor);
        g.setFont(new Font("Comic Sans MS", Font.PLAIN, 35));
        g.drawString("Your score is "+ (snakeSize-1), 5,35);
    }
    
    void drawGameOver(Graphics g)
    {
        String text = "You Lost! Press C-Play Again or Q-Quit";
        g.setColor(gameOverColor);
        g.setFont(new Font("Bahnschrift", Font.PLAIN, 25));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString(text,(GAME_WIDTH - metrics.stringWidth(text))/2 ,GAME_HEIGHT/3);
    }
    
    void drawSnake(Graphics g)
    {
        for (int i = 0; i < xValues.size(); i++)
        {
            int x = xValues.get(i);
            int y = yValues.get(i);
            g.setColor(snakeColor);
            g.fillRect(x, y, UNIT_SIZE, UNIT_SIZE);
        }
    }
    
    void drawControls(Graphics g)
    {
        int boxWidth = 420;
        int boxHeight = 60;
    
        // Center the box with the grid
        int boxX = gridX + (GRID_WIDTH - boxWidth) / 2;
        int boxY = gridY + GRID_HEIGHT + 20;
    
        // Background
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 25, 25);
    
        // Border
        g.setColor(Color.WHITE);
        g.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 25, 25);
    
        // Text
        g.setColor(Color.WHITE);
        g.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 18));
    
        String controls = "↑ ↓ ← → Move    C Restart    Q Quit";
    
        FontMetrics fm = g.getFontMetrics();
        int textX = boxX + (boxWidth - fm.stringWidth(controls)) / 2;
        int textY = boxY + ((boxHeight - fm.getHeight()) / 2) + fm.getAscent();
    
        g.drawString(controls, textX, textY);
    }
     
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        drawGrid(g);
        drawFood(g);
        drawSnake(g);
        drawScore(g);
        drawControls(g);
        
        if(isOver)
        {
            drawGameOver(g);
        }
    }
    
    void drawGrid(Graphics g)
    {
        g.setColor(new Color(255, 255, 255, 70)); // translucent white
    
        // Vertical lines
        for(int x = 0; x <= GRID_WIDTH; x += UNIT_SIZE)
        {
            g.drawLine(
                gridX + x,
                gridY,
                gridX + x,
                gridY + GRID_HEIGHT
            );
        }
    
        // Horizontal lines
        for(int y = 0; y <= GRID_HEIGHT; y += UNIT_SIZE)
        {
            g.drawLine(
                gridX,
                gridY + y,
                gridX + GRID_WIDTH,
                gridY + y
            );
        }
    
        // Optional border
        g.setColor(Color.WHITE);
        g.drawRect(gridX, gridY, GRID_WIDTH, GRID_HEIGHT);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(!isOver){
            moveSnake();
            checkMovements();
        }

        repaint();
    }
    
    public class SnakeKeyAdapter extends KeyAdapter{
    
       public void keyPressed(KeyEvent e)
       {
            int keyCode = e.getKeyCode();
            
            if(keyCode == KeyEvent.VK_Q)
                SnakeFrame.quitGame();
            
            if(keyCode == KeyEvent.VK_C)
                initializeGame();
            
            if(keyCode == KeyEvent.VK_UP && changeY != UNIT_SIZE)
            {
                changeY = -UNIT_SIZE;
                changeX = 0;
            }
            else if(keyCode == KeyEvent.VK_DOWN && changeY != -UNIT_SIZE)
            {
                changeY = UNIT_SIZE;
                changeX = 0;
            }
            else if(keyCode == KeyEvent.VK_LEFT && changeX != UNIT_SIZE)
            {
                changeX = -UNIT_SIZE;
                changeY = 0;
            }
            else if(keyCode == KeyEvent.VK_RIGHT && changeX != -UNIT_SIZE)
            {
                changeX = UNIT_SIZE;
                changeY = 0;
            }
       }
    }
}
