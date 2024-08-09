package globattack;
import processing.core.PApplet;
import java.util.ArrayList;

public class GlobAttack extends PApplet {
    float playerX = 40;
    float playerY = 40;

    boolean left, right, up, down;

    float enemySpeed = 1f;

    ArrayList<Enemy> enemies = new ArrayList<>();

    float spawnRate = 300;

    float bulletSpeed = 5;

    ArrayList<Bullet> bullets = new ArrayList<Bullet>();



    int timer;
    boolean debounce;

    public static void main(String[] args) {
        PApplet.main("globattack.GlobAttack");
    }

    public void settings() {
        size(512, 704);
    }

    public void setup() {
        enemies.add(new Enemy(random(0, width), random(0, width)));
    }

    public void draw() {
        background(250);
        noStroke();
        drawPlayer();
        increaseDifficulty();

        for (int b = 0; b < bullets.size(); b++) {
            Bullet bull = bullets.get(b);
            bull.move();
            bull.drawBullet();
            if (bull.x < 0 || bull.x > width || bull.y < 0 || bull.y > height) {
                bullets.remove(b);
            }
        }

        for (int i = 0; i < enemies.size(); i++) {
            Enemy en = enemies.get(i);
            en.move(playerX, playerY);
            en.drawEnemy();

            for (int j = 0; j < bullets.size(); j++) {
                Bullet b = bullets.get(j);
                if (abs(b.x - en.x) < 15 && abs(b.y - en.y) < 15) {
                    enemies.remove(i);
                    bullets.remove(j);
                    break;
                }
            }

            if (abs(playerX - en.x) < 15 && abs(playerY - en.y) < 15) {
                System.out.println("Game Over!");
            }
        }
    }

    public void keyPressed() {
        if (key == 'w') {
            up = true;
        }
        if (key == 'a') {
            left = true;
        }
        if (key == 's') {
            down = true;
        }
        if (key == 'd') {
            right = true;
        }
    }

    public void keyReleased() {
        if (key == 'w') {
            up = false;
        }
        if (key == 'a') {
            left = false;
        }
        if (key == 's') {
            down = false;
        }
        if (key == 'd') {
            right = false;
        }
    }

    public void drawPlayer() {
        fill(0, 230, 172);
        rectMode(CENTER);
        playerX = constrain(playerX, 0, width);
        playerY = constrain(playerY, 0, height);
        rect(playerX, playerY, 30, 30);

        if (up) {
            playerY -= 5;
        }
        if (left) {
            playerX -= 5;
        }
        if (right) {
            playerX += 5;
        }
        if (down) {
            playerY += 5;
        }
    }

    public void increaseDifficulty() {
        if (frameCount % spawnRate == 0) {
            generateEnemy();
            if (enemySpeed < 3) {
                enemySpeed += 0.1f;
            }
            if (spawnRate > 50) {
                spawnRate -= 10;
            }
        }
    }

    public void generateEnemy() {
        int side = (int) random(0, 2);
        int side2 = (int) random(0, 2);
        if (side % 2 == 0) {
            enemies.add(new Enemy(random(0, width), height * (side2 % 2)));
        }
        else {
            enemies.add(new Enemy(width * (side2 % 2), random(0, height)));
        }
    }

    public void mousePressed() {
        if(debounce == false) {
            runDebounce();
            float dx = mouseX - playerX;
            float dy = mouseY - playerY;
            float angle = atan2(dy, dx);
            float vx = bulletSpeed * cos(angle);
            float vy = bulletSpeed * sin(angle);
            bullets.add(new Bullet(playerX, playerY, vx, vy));
        }
    }
    public void runDebounce(){
        debounce = true;
        timer = millis() + 5;
        System.out.println(millis() - timer);
        if (millis() - timer == 0);
    }

    class Enemy {
        float x, y, vx, vy;

        Enemy(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public void drawEnemy() {
            rectMode(CENTER);
            fill(255, 102, 102);
            rect(x, y, 25, 25);
        }

        public void move(float px, float py) {
            float angle = atan2(py - y, px - x);
            vx = cos(angle);
            vy = sin(angle);
            x += vx * enemySpeed;
            y += vy * enemySpeed;
        }
    }

    class Bullet {
        float x, y, vx, vy;

        Bullet(float x, float y, float vx, float vy) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
        }

        public void drawBullet() {
            fill(0, 255, 0);
            ellipse(x, y, 10, 10);
        }

        public void move() {
            x += vx;
            y += vy;
        }
    }
}