import java.awt.*;

class StartNode extends Node {

    private static final int NODE_TYPE = 1;

    StartNode(int x, int y, double h) {
        super(x, y, 0, h, h, null, false);
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
        g.setColor(GraphicsManager.STARTNODE_COLOR);
        g.fillOval((width/dimX)*(x-1)+r, (height/dimY)*(y-1)+r, r*2, r*2);
    }
}