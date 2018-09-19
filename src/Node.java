import java.awt.*;
import javax.swing.*;

class Node {
    final int x, y;
    double g;
    double h;
    double f;
    Node parent;
    boolean obstacle;

    private static final int NODE_TYPE = 0;

    Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.parent = null;
        this.obstacle = false;
    }

    Node(int x, int y, double g, double h, double f, Node parent, boolean obstacle) {
        this.x = x;
        this.y = y;
        this.g = g;
        this.h = h;
        this.f = f;
        this.parent = parent;
        this.obstacle = obstacle;
    }

    int nodeType() {
        return NODE_TYPE;
    }

    @Override
    public String toString() {
        return ("(" + x + "," + y + "): " + Math.round(g)+"|"+Math.round(h)+"|"+Math.round(f));
    }

    void draw(Graphics g) {
        int dimX = AStar.dimX;
        int dimY = AStar.dimY;
        int width = GraphicsManager.WIDTH;
        int height = GraphicsManager.HEIGHT;
        int r;
        if (height < width) {
            r = height/(dimX*dimY);
        } else {
            r = width/(dimX*dimY);
        }
        if (!obstacle) {
            g.setColor(GraphicsManager.NODE_COLOR);
            g.fillOval((width / dimX) * (x - 1) + r, (height / dimY) * (y - 1) + r, r * 2, r * 2);
        } else {
            g.setColor(GraphicsManager.OBSTACLE_COLOR);
            g.fillRect((width / dimX) * (x - 1) + r, (height / dimY) * (y - 1) + r, r * 2, r * 2);
        }
    }
}