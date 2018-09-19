import java.awt.*;

class EndNode extends Node {

    private static final int NODE_TYPE = 2;

    EndNode(int x, int y) {
        super(x, y);
    }

    @Override
    int nodeType() {
        return NODE_TYPE;
    }

    @Override
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
        g.setColor(GraphicsManager.ENDNODE_COLOR);
        g.fillOval((width/dimX)*(x-1)+r, (height/dimY)*(y-1)+r, r*2, r*2);
    }
}