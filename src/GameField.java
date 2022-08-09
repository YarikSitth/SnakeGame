import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {
    private final int SIZE = 640;
    private final int DOT_SIZE = 32;
    private final int ALL_DOTS = (SIZE / DOT_SIZE) * (SIZE / DOT_SIZE);
    private final int DEFAULT_PIZZA_LIFE = 25;
    final int R = 66;
    final int G = 194;
    final int B = 110;
    final Paint p = new GradientPaint(0.0f, 0.0f, new Color(R, G, B, 100),
            getWidth(), getHeight(), new Color(R, G, B, 200), true);
    private Image dot;
    private Image apple;
    private Image snake_head_up;
    private Image snake_head_down;
    private Image snake_head_left;
    private Image snake_head_right;
    private Image header;
    private Image pizza;
    private int appleX;
    private int appleY;
    private int pizzaX;
    private int pizzaY;
    private int[] snakesX = new int[ALL_DOTS];
    private int[] snakesY = new int[ALL_DOTS];
    private int dots;
    private int score = 0;
    private int tickSpeed = 150;
    private int current_pizza_life;
    private Timer timer;
    private boolean left = false;
    private boolean right = false;
    private boolean up = true;
    private boolean down = false;
    private boolean inGame = true;
    private boolean is_painted_frame = false;
    private boolean pizza_exist = false;


    public GameField() {
        for (int i = 0; i < ALL_DOTS; i++) {
            snakesY[i] = -100;
            snakesX[i] = -100;
        }
        setBackground(Color.black);
        loadImages();
        initGame();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
    }

    public void initGame() {
        dots = 3;
        for (int i = 0; i < dots; i++) {
            snakesX[i] = 96 - i * DOT_SIZE;
            snakesY[i] = 320;
        }
        timer = new Timer(tickSpeed, this);
        timer.start();

        createApple();
    }

    public void loadImages() {
        ImageIcon iia = new ImageIcon("apple.png");
        apple = iia.getImage();
        ImageIcon iid = new ImageIcon("dot.png");
        dot = iid.getImage();
        ImageIcon iihs_up = new ImageIcon("snake_head_up.png");
        snake_head_up = iihs_up.getImage();
        ImageIcon iihs_down = new ImageIcon("snake_head_down.png");
        snake_head_down = iihs_down.getImage();
        ImageIcon iihs_left = new ImageIcon("snake_head_left.png");
        snake_head_left = iihs_left.getImage();
        ImageIcon iihs_right = new ImageIcon("snake_head_right.png");
        snake_head_right = iihs_right.getImage();
        ImageIcon iih = new ImageIcon("header.png");
        header = iih.getImage();
        ImageIcon iic = new ImageIcon("pizza.png");
        pizza = iic.getImage();


    }

    public void createPizza() {
        current_pizza_life = DEFAULT_PIZZA_LIFE;
        boolean is_crossing = true;
        while (is_crossing) {
            is_crossing = false;
            pizzaX = new Random().nextInt(18) * DOT_SIZE;
            pizzaY = (new Random().nextInt(17) + 1) * DOT_SIZE;
            pizza_exist = true;
            if (appleX == pizzaX && appleY == pizzaY) {
                is_crossing = true;
            }
            if (appleX == pizzaX + DOT_SIZE && appleY == pizzaY) {
                is_crossing = true;
            }
            if (appleX == pizzaX && appleY == pizzaY + DOT_SIZE) {
                is_crossing = true;
            }
            if (appleX == pizzaX + DOT_SIZE && appleY == pizzaY + DOT_SIZE) {
                is_crossing = true;
            }
            for (int i = 0; i < dots; i++) {
                if (pizzaX == snakesX[i] && pizzaY == snakesY[i])
                    is_crossing = true;
                if (pizzaX + DOT_SIZE == snakesX[i] && pizzaY == snakesY[i])
                    is_crossing = true;
                if (pizzaX == snakesX[i] && pizzaY + DOT_SIZE == snakesY[i])
                    is_crossing = true;
                if (pizzaX + DOT_SIZE == snakesX[i] && pizzaY + DOT_SIZE == snakesY[i])
                    is_crossing = true;
            }
        }

    }

    public void createApple() {
        boolean is_crossing = true;
        while (is_crossing) {
            appleX = new Random().nextInt(19) * DOT_SIZE;
            appleY = (new Random().nextInt(18) + 1) * DOT_SIZE;
            is_crossing = false;
            for (int i = 0; i < dots; i++) {
                if (appleX == snakesX[i] && appleY == snakesY[i])
                    is_crossing = true;
            }
            if (pizza_exist) {
                if (appleX == pizzaX && appleY == pizzaY) {
                    is_crossing = true;
                }
                if (appleX == pizzaX + DOT_SIZE && appleY == pizzaY) {
                    is_crossing = true;
                }
                if (appleX == pizzaX && appleY == pizzaY + DOT_SIZE) {
                    is_crossing = true;
                }
                if (appleX == pizzaX + DOT_SIZE && appleY == pizzaY + DOT_SIZE) {
                    is_crossing = true;
                }
            }
        }


    }

    public void move() {
        for (int i = dots; i > 0; i--) {
            snakesX[i] = snakesX[i - 1];
            snakesY[i] = snakesY[i - 1];
        }
        if (left) {
            snakesX[0] -= DOT_SIZE;
        }
        if (right) {
            snakesX[0] += DOT_SIZE;
        }
        if (up) {
            snakesY[0] -= DOT_SIZE;
        }
        if (down) {
            snakesY[0] += DOT_SIZE;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println("current_pizza_life = " + current_pizza_life);
        if (pizza_exist && current_pizza_life > 0) {
            current_pizza_life--;
        } else if (pizza_exist) {
            pizza_exist = false;
        }
        is_painted_frame = true;
        if (inGame) {


            g.drawImage(header, 0, 0, this);
            g.drawImage(apple, appleX, appleY, this);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setPaint(p);
            g2d.setColor(new Color(255,0,0));
            if (pizza_exist) {
                g.drawImage(pizza, pizzaX, pizzaY, this);
                g2d.fillRect(200, 10, current_pizza_life * 10, 15);
            }
            for (int i = 0; i < dots; i++) {
                if (i == 0) {
                    if (left)
                        g.drawImage(snake_head_left, snakesX[0], snakesY[0], this);
                    if (right)
                        g.drawImage(snake_head_right, snakesX[0], snakesY[0], this);
                    if (up)
                        g.drawImage(snake_head_up, snakesX[0], snakesY[0], this);
                    if (down)
                        g.drawImage(snake_head_down, snakesX[0], snakesY[0], this);
                } else {
                    g.drawImage(dot, snakesX[i], snakesY[i], this);
                }
            }
            String score_str = "Score: " + score;
            Font f = new Font("TimesRoman", Font.BOLD, 20);
            g.setColor(Color.yellow);
            g.setFont(f);
            g.drawString(score_str, 5, 25);
        } else {
            String score_str = "Your score is " + score;
            String over_str = "Game over";
            Font f = new Font("TimesRoman", Font.ITALIC, 70);
            Font f2 = new Font("TimesRoman", Font.ITALIC, 30);
            g.setColor(Color.red);
            g.setFont(f);
            g.drawString(over_str, 125, SIZE / 2);
            g.setFont(f2);
            g.drawString(score_str, 193, SIZE / 2 + 70);

        }

    }

    public void checkPizza() {
        if (pizza_exist) {
            if (snakesX[0] == pizzaX && snakesY[0] == pizzaY) {
                dots += 2;
                pizza_exist = false;
                score += 2;
                current_pizza_life = DEFAULT_PIZZA_LIFE;
            }
            if (snakesX[0] == pizzaX + DOT_SIZE && snakesY[0] == pizzaY) {
                dots += 2;
                pizza_exist = false;
                score += 2;
                current_pizza_life = DEFAULT_PIZZA_LIFE;
            }
            if (snakesX[0] == pizzaX && snakesY[0] == pizzaY + DOT_SIZE) {
                dots += 2;
                pizza_exist = false;
                score += 2;
                current_pizza_life = DEFAULT_PIZZA_LIFE;
            }
            if (snakesX[0] == pizzaX + DOT_SIZE && snakesY[0] == pizzaY + DOT_SIZE) {
                dots += 2;
                pizza_exist = false;
                score += 2;
                current_pizza_life = DEFAULT_PIZZA_LIFE;
            }
        }
    }

    public void checkApple() {
        if (snakesX[0] == appleX && snakesY[0] == appleY) {
            dots++;
            createApple();
            score++;
            if (tickSpeed > 70) {
                tickSpeed -= 2;
                timer.setDelay(tickSpeed);
            }
            if (!pizza_exist) {
                int pizza_chance = new Random().nextInt(10);
                if (pizza_chance < 4) {
                    createPizza();
                }
            }
        }

    }

    public void checkCollisions() {
        for (int i = dots; i > 0; i--) {
            if (i > 4 && snakesX[0] == snakesX[i] && snakesY[0] == snakesY[i]) {
                inGame = false;
            }
        }

        if (snakesX[0] > SIZE - 2 * DOT_SIZE) {
            snakesX[0] = 0;
        }
        if (snakesX[0] < 0) {
            snakesX[0] = SIZE - 2 * DOT_SIZE;
        }
        if (snakesY[0] > SIZE - DOT_SIZE) {
            snakesY[0] = DOT_SIZE;
        }
        if (snakesY[0] < DOT_SIZE) {
            snakesY[0] = SIZE - DOT_SIZE;
        }

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            move();
            checkApple();
            checkCollisions();
            checkPizza();
        }
        repaint();
    }

    class FieldKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            if (is_painted_frame) {
                is_painted_frame = false;
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_LEFT && !right) {
                    left = true;
                    up = false;
                    down = false;

                }
                if (key == KeyEvent.VK_RIGHT && !left) {
                    right = true;
                    up = false;
                    down = false;
                }

                if (key == KeyEvent.VK_UP && !down) {
                    right = false;
                    up = true;
                    left = false;
                }
                if (key == KeyEvent.VK_DOWN && !up) {
                    right = false;
                    down = true;
                    left = false;
                }
            }
        }

    }
}
