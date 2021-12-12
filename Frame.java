import javax.swing.*;
import java.awt.*;

class Frame extends JFrame {
    public Frame() {
        // set properties
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Neural network sine test");

        // create graphics object
        NNTest graphics = new NNTest();
        graphics.setPreferredSize(new Dimension(NNTest.width, NNTest.height));
        graphics.setFocusable(true);

        // create window
        add(graphics);
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        new Frame();
    }
}