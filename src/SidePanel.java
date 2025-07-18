import Custom.CollapseBtn;

import javax.swing.*;
import java.awt.*;

public class SidePanel extends JPanel {
    private final JButton collapse = new CollapseBtn(10, 10);

    public SidePanel(int WIDTH, int HEIGHT){
        this.setBounds(0, 0, WIDTH, HEIGHT);
        this.setBackground(new Color(190, 190, 190, 50));
        this.setLayout(null);
        this.setFocusable(true);

        this.add(collapse);
    }

}
