import java.awt.*;
import javax.swing.*;

class Line {
    Node n1, n2;

    Line(Node n1, Node n2) {
        this.n1 = n1;
        this.n2 = n2;
    }

    Line(Node n1) {
        this.n1 = n1;
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
        g.setColor(GraphicsManager.LINE_COLOR);
        ((Graphics2D)g).setStroke(new BasicStroke(r));
        g.drawLine((width/dimX)*(n1.x-1)+r*2, (height/dimY)*(n1.y-1)+r*2, (width/dimX)*(n2.x-1)+r*2, (height/dimY)*(n2.y-1)+r*2);
        ((Graphics2D)g).setStroke(new BasicStroke(1));
    }

}