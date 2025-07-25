import javax.swing.*;
import java.awt.*;


public class Main{
    private static JPanel setupPanel = new JPanel(new GridLayout(6, 2)); //Sets up a panel to

    //Java swing fields
    private static JTextField rField = new JTextField("0.5"); //r value (scaling ratio)
    private static JTextField vertexField = new JTextField("3"); //Number of vertices used in the chaos game
    private static JTextField radiusField = new JTextField("400"); //Distance of one vertex to the centroid.
    private static JTextField edgeField = new JTextField("0"); //Number of edges in between every 2 vertices
    private static JTextField ruleField = new JTextField();
    /*
        ruleField takes string representing the skip condition (even
        indices represent the proximity of the last chosen anchor,
        and the odd indices represent the occurrence count that is allowed
    */

    private static Frame f; //Java swing frame

    public static void launch() {
        if(f == null){
            setupPanel.add(new JLabel("r:"));
            setupPanel.add(rField);
            setupPanel.add(new JLabel("Vertex points:"));
            setupPanel.add(vertexField);
            setupPanel.add(new JLabel("Radius:"));
            setupPanel.add(radiusField);
            setupPanel.add(new JLabel("Edge points:"));
            setupPanel.add(edgeField);
            setupPanel.add(new JLabel("Rules: "));
            setupPanel.add(ruleField);


            int result = JOptionPane.showConfirmDialog(
                    null, setupPanel, "Enter Parameters", JOptionPane.OK_CANCEL_OPTION
            );
            if (result == JOptionPane.OK_OPTION) {
                double r = Double.parseDouble(rField.getText());
                int vertex = Integer.parseInt(vertexField.getText());
                double radius = Integer.parseInt(radiusField.getText());
                int edge = Integer.parseInt(edgeField.getText());
                String rule = ruleField.getText();

                f = new Frame(r, vertex, radius, edge, rule);
            }
        }
        else if(f.isActive()){
            f.dispose();
            f = null;
            setupPanel = new JPanel(new GridLayout(6, 2));
            launch();
        }
    }

    void main(){
        Main.launch();
    }
}

