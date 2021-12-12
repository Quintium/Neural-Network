import javax.swing.*;
import java.awt.*;

class Frame extends JFrame {
    public Frame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setTitle("Neural network sine test");

        NNTest graphics = new NNTest();
        graphics.setPreferredSize(new Dimension(NNTest.width, NNTest.height));

        graphics.setFocusable(true);

        add(graphics);
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        new Frame();
    }
}