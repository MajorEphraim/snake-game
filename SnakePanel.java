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

public class SnakePanel extends JPanel implements ActionListener {
    
    static int GAME_WIDTH = 600;
    static int GAME_HEIGHT = 400;
    static int SNAKE_SPEED = 80;
    
    Color backgroundColor = new Color(0, 0, 204);  
    Color snakeColor = new Color(0, 0, 0);  
    Color foodColor = new Color(0, 255, 0);  
    Color scoreColor = new Color(255, 255, 102);
    Color gameOverColor = new Color(213, 50, 80);
    
    static int UNIT_SIZE = 10;
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
        if (snakeX < 0 || snakeX >= GAME_WIDTH)
            isOver = true;
            
        //top or bottom collisions
        if (snakeY < 0 || snakeY >= GAME_HEIGHT)
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
    
    //x: [1, 2, 3, 3, 3]
    //y: [1, 1, 1, 2, 3]
    
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
     
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        drawFood(g);
        drawSnake(g);
        drawScore(g);
        
        if(isOver)
        {
            drawGameOver(g);
        }
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
