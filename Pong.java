import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
class Pair{
    public double x;
    public double y;
    public Pair(double initX, double initY){
        x = initX;
        y = initY;
    }
    public Pair add(Pair toAdd){
        return new Pair(x + toAdd.x, y + toAdd.y);
    }

    public Pair times(double val){
        return new Pair(x * val, y * val);
    }
    public void flipY(){
        y = -y;
    }
}
class Paddle {
    Pair position;
    Pair velocity;
    Color color;
    int score =0;
    final int width = 20;
    final int height = 110;

    public Paddle(Boolean left) {
        velocity = new Pair(0, 0);
        if (left) {
            position = new Pair(34,329);
            color= Color.red;
        } else {
            position = new Pair(980, 329);
            color = Color.blue;
        }
    }

    public void setVelocity(Pair v) {
        velocity = v;
    }


    public void update(World w, double time) {
        position = position.add(velocity.times(time));
        bounce(w);
    }

    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillRect((int) (position.x), (int) (position.y), (int) (width), (int) (height));
    }

    private void bounce(World w) {
        if (position.y < 0) {
            velocity = new Pair(0, 0);
            position.y = 0;
        } else if (position.y +height > 768) {
            velocity = new Pair(0, 0);
            position.y = w.height-height;
        }
    }
    public void point(){
        this.score += 1;
    }
    public int getScore(){
        return score;
    }
}

    class Sphere {
        Pair position;
        Pair velocity;
        final double radius =20;
        final static double factor =1.4;
        final Color color = new Color(246, 210, 163);
        static double velFactor=0;
        public Sphere() {
            position = new Pair(512-radius, 384-radius);
            velocity = new Pair(0.0 ,0.0);
        }
        public void update(World w, double time, Paddle[] paddles) {
            position = position.add(velocity.times(time));
            bounce(w, paddles);
        }
        public void draw(Graphics g) {
            Color c = g.getColor();
            g.setColor(color);
            g.fillOval((int) (position.x), (int) (position.y), (int) (2 * radius), (int) (2 * radius));
        }
        private void bounce(World w, Paddle[] paddles) {
            //height of paddle 110, width 20
            //boolean bounced is that so the ball is sped up as players bounce it.
            Boolean bounced = false;
            double XpaddleL = paddles[0].position.x;
            double YpaddleL = paddles[0].position.y;
            double XpaddleR = paddles[1].position.x;
            double YpaddleR = paddles[1].position.y;
            double Yball = position.y;
            double Xball = position.x;
            //LEFT PADDLE
            //top of left paddle
            if (Yball < YpaddleL && Yball+2*radius> YpaddleL && Xball<XpaddleL +15&& Xball > XpaddleL) {
                velocity = new Pair(500*Math.pow(factor,velFactor)*Math.cos(2.3),500*Math.pow(factor,velFactor)*Math.sin(-2.3));
                position = new Pair(Xball, YpaddleL -2*radius);
            }
            //bottom of left paddle
            if (Yball+ 2*radius>YpaddleL+110 &&Yball<YpaddleL+110 && Xball < XpaddleL +15 && Xball+2*radius>XpaddleL) {
                velocity = new Pair(500*Math.pow(factor,velFactor)*Math.cos(2.3),500*Math.pow(factor,velFactor)*Math.sin(2.3));
                position = new Pair(Xball, YpaddleL+110);
            }
            //when ball hits left paddle including upper and lower right corners
            if (Xball > XpaddleL && Xball+2*radius>=XpaddleL+20&& Xball <=XpaddleL +20&&Yball+2*radius>= YpaddleL&&Yball<= YpaddleL +110){
                //theta is the angle that the ball will be bounced at, with a max of 0.9 rad
                //with less angle as the midpoint of the ball is further from the center of the paddle
                double theta= ((0.9)/(55))*Math.abs(YpaddleL +55 - (Yball +radius));
                //because y is pointing downwards, theta is negative when it hits the upper half
                if(Yball + 2*radius< YpaddleL +55&&Yball + 2*radius> YpaddleL){theta = -1*theta;}
                velocity = new Pair(500*Math.pow(factor,velFactor)*Math.cos(theta),500*Math.pow(factor,velFactor)*Math.sin(theta));
                position = new Pair(XpaddleL+20, Yball);
                bounced = true;
            }

            //RIGHT PADDLE
            //top of right paddle
            if (Yball < YpaddleR && Yball+2*radius> YpaddleR && Xball> XpaddleR+5 &&Xball < XpaddleR +20) {
                velocity = new Pair(500*Math.pow(factor,velFactor)*Math.cos(Math.PI-2.3),500*Math.pow(factor,velFactor)*Math.sin(-2.3));
                position = new Pair(Xball, YpaddleR -2*radius);
            }
            //bottom of right paddle
            if (Yball<YpaddleR+110 &&Yball+ 2*radius>YpaddleR+110 && Xball> XpaddleR+5 && Xball< XpaddleR+20) {
                velocity = new Pair(500*Math.pow(factor,velFactor)*Math.cos(Math.PI-2.3),500*Math.pow(factor,velFactor)*Math.sin(2.3));
                position = new Pair(Xball, YpaddleR+110);
            }
            //when ball hits right paddle including upper and lower left corners
            if (Xball + 2*radius>=XpaddleR && Xball<XpaddleR && Yball +2*radius>=YpaddleR &&Yball<= YpaddleR +110){
                //theta is the angle that the ball will be bounced at, with a max of 0.9 rad
                //with less angle as the midpoint of the ball is further from the center of the paddle
                double theta= Math.PI-((0.9)/(55)*Math.abs(YpaddleR +55 - (Yball+radius)));
                if(Yball + 2*radius> YpaddleR &&Yball+ 2*radius< YpaddleR +55){
                    theta = -1*theta;
                }
                velocity = new Pair(500*Math.pow(factor,velFactor)*Math.cos(theta),500*Math.pow(factor,velFactor)*Math.sin(theta));
                position = new Pair(XpaddleR-2*radius, Yball);
                bounced = true;
            }
            //top and bottom walls
            if (Yball < 0){
                velocity.flipY();
                Yball= 0;
            }
            else if(Yball+ 2*radius> w.height){
                velocity.flipY();
                Yball = w.height - 2* radius;
            }
            if (Xball < 0){
                paddles[0].point();
                position = new Pair(527, 364);
                velocity = new Pair(500*Math.pow(factor,velFactor), 0);
            }
            else if (Xball > w.width){
                paddles[1].point();
                position = new Pair(527, 364);
                velocity = new Pair(-500*Math.pow(factor,velFactor),0);
            }
            if(bounced){
                velocity =velocity.times(factor);
                velFactor=+1;
            }
        }
    }

    class World {
        int height;
        int width;
        Sphere ball;
        Paddle[] paddles;
        Boolean scored= false;
        Boolean started= false;
        public World(int initWidth, int initHeight) {
            width = initWidth;
            height = initHeight;
            ball = new Sphere();
            paddles = new Paddle[2];
            paddles[0] = new Paddle(true);
            paddles[1] = new Paddle(false);
        }
        public void drawWorld(Graphics g) {
            g.setColor(Color.blue);
            g.fillRect(524,0,100,100);
            g.setColor(Color.red);
            g.fillRect(400,0,100,100);
            g.setColor(Color.white);
            g.drawString("Player 1: ",425,40);
            g.drawString("Player 2: ",549,40);
            g.drawString(""+paddles[1].getScore(),445,70);
            g.drawString(""+paddles[0].getScore(),569,70);
            g.setColor(Color.white);
            g.drawLine(512,0,512,768);
            ball.draw(g);
            paddles[0].draw(g);
            paddles[1].draw(g);
            if(!started){
                g.setColor(new Color(161, 246, 202));
                g.fillRect(440,425,150,40);
                g.setColor(Color.black);
                g.drawString("Press space to start!",450,450);
            }
        }
        public void updateWorld(double time) {
            ball.update(this, time, paddles);
            paddles[0].update(this, time);
            paddles[1].update(this, time);
        }
    }

    public class Pong extends JPanel implements KeyListener {
        public static final int WIDTH = 1024;
        public static final int HEIGHT = 768;
        public static final int FPS = 60;
        World world;

        class Runner implements Runnable {
            public void run() {
                while (true) {
                    world.updateWorld(1.0 / (double) FPS);
                    repaint();
                    try {
                        Thread.sleep(1000 / FPS);
                    } catch (InterruptedException e) {
                    }
                }

            }
        }

        public void keyPressed(KeyEvent e) {
        }
        public void keyReleased(KeyEvent e) {
            char c = e.getKeyChar();
            Pair newVel1 = null;
            Pair newVel2 = null;
            switch (c) {
                case 'v':
                    newVel1 = new Pair(0, 300);
                    break;
                case 'r':
                    newVel1 = new Pair(0, -300);
                    break;
                case 'n':
                    newVel2 = new Pair(0, 300);
                    break;
                case 'u':
                    newVel2 = new Pair(0, -300);
                    break;
                case 'f':
                    newVel1 = new Pair(0, 0);
                    break;
                case 'j':
                    newVel2 = new Pair(0, 0);
                    break;
                case' ':
                    if(!world.started){
                        world.ball.velocity= new Pair(500,0);
                        world.started =true;
                    }
            }
            if (newVel1 != null) {
                world.paddles[0].setVelocity(newVel1);
            }
            if(newVel2!= null){
                world.paddles[1].setVelocity(newVel2);
            }

        }
        public void keyTyped(KeyEvent e) {}
        public void addNotify() {
            super.addNotify();
            requestFocus();
        }
        public Pong() {
            world = new World(WIDTH, HEIGHT);
            addKeyListener(this);
            this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
            Thread mainThread = new Thread(new Runner());
            mainThread.start();
        }

        public static void main(String[] args) {
            JFrame frame = new JFrame("Pong");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Pong mainInstance = new Pong();
            frame.setContentPane(mainInstance);
            frame.pack();
            frame.setVisible(true);
        }


        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            world.drawWorld(g);
        }

    }
