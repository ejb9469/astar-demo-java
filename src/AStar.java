import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class AStar {

    private static final double STR_DIST = 10;
    private static final double DIA_DIST = 14.14; // Root 2 x10

    static final int dimX = 10;
    static final int dimY = 10;

    private static final int startX = 1;
    private static final int startY = 1;
    private static final int endX = 10;
    private static final int endY = 10;

    private static final int[] obstacles = new int[] {1, 2, 1, 3, 1, 4, 2, 4, 3, 4, 4, 4, 5, 4, 6, 4, 6, 3, 7, 3, 10, 6, 9, 6, 8, 6, 7, 6, 6, 6, 6, 7, 6, 8, 6, 9, 5, 9, 4, 9, 3, 9, 2, 9, 2, 8, 2, 7, 2, 6, 9, 10, 9, 9, 9, 8};

    private static List<Node> openList = new ArrayList<>();
    private static List<Node> closedList = new ArrayList<>();
    private static Node[][] grid = new Node[dimX][dimY];

    private static double calculateH(int x, int y) {
        int xDistance = Math.abs(endX - x);
        int yDistance = Math.abs(endY - y);
        return (xDistance + yDistance)*STR_DIST;
    }

    private static void initializeGrid() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = new Node(i + 1, j + 1);
            }
        }
    }

    private static Node initializeEndNode() {
        Node endNode = new EndNode(endX, endY);
        grid[endX-1][endY-1] = endNode;
        return endNode;
    }

    private static Node initializeStartNode() {
        double h = calculateH(startX, startY);
        Node startNode = new StartNode(startX, startY, h);
        grid[startX-1][startY-1] = startNode;
        return startNode;
    }

    private static void toggleObstacle(int x, int y) {
        Node n = grid[x-1][y-1];
        n.obstacle = !(n.obstacle);
    }

    private static void initializeObstacles() {
        for (int i = 1; i < obstacles.length; i += 2) {
            toggleObstacle(obstacles[i-1], obstacles[i]);
        }
    }

    private static void openSurroundingNodes(Node parentNode) {
        int x = parentNode.x;
        int y = parentNode.y;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) { // don't select the current node
                    //System.out.println("Center node.");
                    j++;
                }
                int trueX = x - 1;
                int trueY = y - 1;
                boolean inGridX = (trueX + i >= 0 && trueX + i <= dimX - 1); // Makes sure theoretical node is in bounds
                boolean inGridY = (trueY + j >= 0 && trueY + j <= dimY - 1);
                if (inGridX && inGridY) {
                    Node n = grid[trueX+i][trueY+j];
                    boolean isNotObstacle = !(n.obstacle); // Makes sure theoretical node isn't an obstacle
                    boolean hasNoParent = (n.parent == null); // Makes sure node isn't in the closed or open lists
                    boolean isNotStart = (n.nodeType() != 1); // Makes sure node isn't the start node
                    if (isNotObstacle && hasNoParent && isNotStart) {
                        n.parent = parentNode;
                        if (i == 0 || j == 0) { // If both i and j have a non-zero value, it must be a diagonal jump
                            n.g = parentNode.g + STR_DIST;
                        } else {
                            n.g = parentNode.g + DIA_DIST;
                        }
                        n.h = calculateH(x + i, y + j);
                        n.f = n.g + n.h;
                        openList.add(n);
                    }
                }
            }
        }
    }

    private static Node findCheapestNode() {
        Node cheapestNode = openList.get(0);
        for (Node n : openList) {
            if (n.f <= cheapestNode.f) { // it's <= instead of < so that it takes the node closest to the end of the list in the case of a tie
                cheapestNode = n;
            }
        }
        return cheapestNode;
    }

    private static void improveAdjacentG(Node selected) {
        int x = selected.x;
        int y = selected.y;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) { // don't select the current node
                    j++;
                }
                int trueX = x - 1;
                int trueY = y - 1;
                boolean inGridX = (trueX + i >= 0 && trueX + i <= dimX - 1); // Makes sure theoretical node is in bounds
                boolean inGridY = (trueY + j >= 0 && trueY + j <= dimY - 1);
                if (inGridX && inGridY) {
                    Node n = grid[trueX+i][trueY+j];
                    boolean isNotObstacle = !(n.obstacle); // Makes sure theoretical node isn't an obstacle
                    boolean hasParent = (n.parent != null); // Makes sure node IS in either the closed or open list
                    boolean isNotStart = (n.nodeType() != 1); // Makes sure node isn't the start node
                    if (isNotObstacle && hasParent && isNotStart) {
                        boolean isStraight = (i == 0 || j == 0);
                        double jumpValue;
                        if (isStraight)
                            jumpValue = STR_DIST;
                        else
                            jumpValue = DIA_DIST;
                        if (selected.g + jumpValue < n.g) {
                            n.g = selected.g + jumpValue;
                            n.f = n.g + n.h;
                            n.parent = selected;
                        }
                    }
                }
            }
        }
    }

    private static void openNode(Node n) {
        openList.add(n);
    }

    private static void closeNode(Node n) {
        openList.remove(n);
        closedList.add(n);
    }

    private static void printGrid() {
        String output = "";
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                output += grid[i][j];
                if (j != grid[i].length - 1)
                    output += " ||| ";
            }
            if (i != grid.length - 1)
                output += "\n";
        }
        System.out.println(output);
    }

    private static void printNodeList(List<Node> nodeList) {
        String output = "";
        for (Node node : nodeList) {
            output += node;
            if (!(node.equals(nodeList.get(nodeList.size()-1)))) {
                output += " ||| ";
            }
        }
        System.out.println(output);
    }

    public static void main(String[] args) {

        Node selected; // Currently selected node

        initializeGrid(); // Initialize grid
        Node endNode = initializeEndNode(); // Initialize ending point
        Node startNode = initializeStartNode(); // Initialize starting point
        initializeObstacles(); // Initialize obstacles

        selected = startNode; // Select starting node
        closeNode(selected); // Add starting point to the closed list
        openSurroundingNodes(selected); // Open all possible surrounding nodes

        List<Node> optimalPath = new ArrayList<>();
        List<Node> logicalPath = new ArrayList<>();
        while (true) { // main loop
            /*printGrid();
            System.out.println();
            System.out.println();*/
            if (closedList.contains(endNode)) { // Repeat until the destination node is added to the closed list (path found) or the open list is empty (no possible path).
                System.out.println("Exit Case 1: Path Found.");
                optimalPath.add(endNode);
                Node n = endNode.parent;
                while (!(n.equals(startNode))) { // If the destination node is added to the closed list, trace back the parents to the source. This is the optimal path.
                    optimalPath.add(n);
                    n = n.parent;
                }
                optimalPath.add(startNode);
                Collections.reverse(optimalPath);
                printNodeList(optimalPath);
                break;
            } else if (openList.size() == 0) {
                System.out.println("Exit Case 2: No Path Available.");
                break;
            }
            // Check node with greatest f-score. Make this the current node and add it to the closed list.

            logicalPath.add(selected);
            selected = findCheapestNode();
            logicalPath.add(selected);
            closeNode(selected);
            // Check for all adjacent open nodes if the preexisting g-score can be improved by moving directly to them. If so, rewrite the scores of the cell(s) in question and change the parent to the current node.
            improveAdjacentG(selected);
            // Regardless add all blank adjacent nodes to the open list and assign them their scores - their parent is the current node.
            openSurroundingNodes(selected);
        }

        JFrame frame = new JFrame();
        frame.setSize(GraphicsManager.WIDTH, GraphicsManager.HEIGHT);
        //frame.setResizable(false);
        List<Node> gridList = new ArrayList<>();
        for (int i = 0; i < grid.length; i++) {
            Collections.addAll(gridList,grid[i]);
        }
        frame.add(new GraphicsManager(gridList, logicalPath));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}