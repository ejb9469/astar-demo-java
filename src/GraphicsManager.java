import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;

class GraphicsManager extends JPanel {

    static final int WIDTH = 1920;
    static final int HEIGHT = 1080;

    static final Color NODE_COLOR = Color.black;
    static final Color STARTNODE_COLOR = Color.blue;
    static final Color ENDNODE_COLOR = Color.green;
    static final Color OBSTACLE_COLOR = Color.red;
    static final Color LINE_COLOR = Color.magenta;

    private static final int ANIMIATION_DELAY = 100;

    private List<Node> nodes;
    private List<Node> logicalPath;

    private int currentLine = 0;
    private boolean lineMode = false;

    GraphicsManager(List<Node> nodes, List<Node> logicalPath) {
        this.nodes = nodes;
        this.logicalPath = logicalPath;
        repaint();
        Timer timer = new Timer(ANIMIATION_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentLine >= logicalPath.size()-1) {
                    ((Timer)e.getSource()).stop();
                } else {
                    repaint();
                    currentLine++;
                }
            }
        });

        JButton start = new JButton("Start");
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.start();
            }
        });
        add(start);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        List<Line> lines = new ArrayList<>();
        Node node = logicalPath.get(currentLine);
        while (node.parent != null) {
            lines.add(new Line(node.parent, node));
            node = node.parent;
        }
        for (Line line : lines) {
            line.draw(g);
        }
        for (Node n : nodes) {
            n.draw(g);
        }
    }
}