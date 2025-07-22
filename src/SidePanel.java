import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SidePanel extends JPanel {
    private boolean expanded = true;
    private final JButton toggleButton = new JButton("<<");

    public SidePanel() {
        setPreferredSize(new Dimension(200, 0));
        setOpaque(true);
        setBackground(new Color(100, 100, 100, 100)); // semi-transparent black
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        toggleButton.setFocusable(false);
        toggleButton.addActionListener(e -> toggle());
    }

    public void toggle() {
        expanded = !expanded;
        setVisible(expanded);
        toggleButton.setText(expanded ? "<<" : ">>");
        toggleButton.setBackground(new Color(0, 0, 0, 0));
    }

    public JButton getToggleButton() {
        return toggleButton;
    }

    public void setText(){

    }
}