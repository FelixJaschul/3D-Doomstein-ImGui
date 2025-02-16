import org.ice1000.jimgui.JImGui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Camera implements KeyListener, MouseMotionListener {

    public final double MOVE_SPEED = 0.06;
    public final double ROTATION_SPEED = 0.045;
    public double xPosition, yPosition, xDirection, yDirection, xPlane, yPlane;
    public boolean left, right, forward, back, sprint;
    private final int centerX;
    private final int centerY;
    private final JImGui imGui;
    private Robot robot;

    public Camera(double xPosition, double yPosition, double xDirection, double yDirection, double xPlane, double yPlane, int screenWidth, int screenHeight, JImGui imGui) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.xDirection = xDirection;
        this.yDirection = yDirection;
        this.xPlane = xPlane;
        this.yPlane = yPlane;

        this.imGui = imGui;

        centerX = screenWidth / 2;
        centerY = screenHeight / 2;

        try {
            robot = new Robot();
        } catch (AWTException e) { e.printStackTrace();}

        Toolkit.getDefaultToolkit().getBestCursorSize(1, 1); // Hide cursor
    }

    public void keyPressed(KeyEvent key) {
        // Movement
        if (key.getKeyCode() == KeyEvent.VK_W) forward = true;
        if (key.getKeyCode() == KeyEvent.VK_S) back = true;
        if (key.getKeyCode() == KeyEvent.VK_A) left = true;
        if (key.getKeyCode() == KeyEvent.VK_D) right = true;
    }

    public void keyReleased(KeyEvent key) {
        // Movement
        if (key.getKeyCode() == KeyEvent.VK_W) forward = false;
        if (key.getKeyCode() == KeyEvent.VK_S) back = false;
        if (key.getKeyCode() == KeyEvent.VK_A) left = false;
        if (key.getKeyCode() == KeyEvent.VK_D) right = false;
    }

    public void update(int[][] map) {
        if (forward) {
            if (map[(int) (xPosition + xDirection * MOVE_SPEED)][(int) yPosition] == 0) xPosition += xDirection * MOVE_SPEED;
            if (map[(int) xPosition][(int) (yPosition + yDirection * MOVE_SPEED)] == 0) yPosition += yDirection * MOVE_SPEED;
        }

        if (back) {
            if (map[(int) (xPosition - xDirection * MOVE_SPEED)][(int) yPosition] == 0) xPosition -= xDirection * MOVE_SPEED;
            if (map[(int) xPosition][(int) (yPosition - yDirection * MOVE_SPEED)] == 0) yPosition -= yDirection * MOVE_SPEED;
        }

        if (right) {
            if (map[(int) (xPosition + yDirection * MOVE_SPEED)][(int) yPosition] == 0) xPosition += yDirection * MOVE_SPEED;
            if (map[(int) xPosition][(int) (yPosition - xDirection * MOVE_SPEED)] == 0) yPosition -= xDirection * MOVE_SPEED;
        }

        if (left) {
            if (map[(int) (xPosition - yDirection * MOVE_SPEED)][(int) yPosition] == 0) xPosition -= yDirection * MOVE_SPEED;
            if (map[(int) xPosition][(int) (yPosition + xDirection * MOVE_SPEED)] == 0) yPosition += xDirection * MOVE_SPEED;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (robot == null || imGui == null) return;

        int DirectionX = e.getX() - centerX;

        if (DirectionX != 0) { // Only update if there's actual movement
            double rotationAmount = -DirectionX * ROTATION_SPEED * 0.1;
            rotate(rotationAmount);
        }
    }

    private void rotate(double angle) {
        double oldDirectionX = xDirection;
        xDirection = xDirection * Math.cos(angle) - yDirection * Math.sin(angle);
        yDirection = oldDirectionX * Math.sin(angle) + yDirection * Math.cos(angle);

        double oldPlaneX = xPlane;
        xPlane = xPlane * Math.cos(angle) - yPlane * Math.sin(angle);
        yPlane = oldPlaneX * Math.sin(angle) + yPlane * Math.cos(angle);
    }

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
