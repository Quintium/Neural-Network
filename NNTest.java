import java.awt.*;
import javax.swing.*;
import java.lang.Math;
import java.util.function.*;

public class NNTest extends JPanel {
   private static final long serialVersionUID = 1L;
   public static final int width = 1000;
   public static final int height = 700;
   public static final int FPS = 60;
   
   int gen = 0;
   final float start = 0f;
   final float stepSize = 0.01f;
   Model model;

   public NNTest() {
      this.setPreferredSize(new Dimension(width, height));

      model = new Model();
      model.addLayer(new Input(1));
      model.addLayer(new Dense(2, "relu"));
      model.addLayer(new Dense(1, "identity"));

      model.initialize();

      for (int i = 0; i < 0; i++) {
         float[] inputArr = {(float)Math.random() * width * stepSize + start};
         float[] expectedArr = {(float)Math.sin(inputArr[0])};
         model.train(0.1f, inputArr, expectedArr);
      }

      Thread gameThread = new Thread() {
         public void run() {
            while (true) {
               repaint();

               try {
                  Thread.sleep(1000 / FPS);
               } catch (InterruptedException ex) {
               }
            }
         }
      };

      gameThread.start();
   }

   @Override
   public void paintComponent(Graphics g) {
      super.paintComponent(g);

      Graphics2D g2 = (Graphics2D) g;
      RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2.setRenderingHints(rh);

      g.setColor(Color.WHITE);
      g.fillRect(0, 0, width, height);
      g2.setStroke(new BasicStroke(1));

      float[][] input = new float[width][];
      for (int i = 0; i < width; i++) {
         input[i] = new float[]{i * stepSize + start};
      }

      float[][] expected = applyFunction(input, n -> (float)Math.sin(n));
      float[][] predicted = model.predictBatch(input);

      g.setColor(Color.BLACK);
      drawGraph(g, expected);

      g.setColor(Color.RED);
      drawGraph(g, predicted);

      //System.out.println("Loss: " + model.calculateLoss(input, expected));

      for (int i = 0; i < 100; i++) {
         float[] inputArr = {(float)Math.random() * width * stepSize + start};
         float[] expectedArr = {(float)Math.sin(inputArr[0])};
         model.train(0.001f, inputArr, expectedArr);
         gen++;
      }
      
   }

   public float[][] applyFunction(float[][] input, Function<Float, Float> f) {
      float[][] result = new float[input.length][];
      for (int i = 0; i < input.length; i++) {
         result[i] = new float[]{f.apply(input[i][0])};
      }
      return result;
   }

   public void drawGraph(Graphics g, float[][] data) {
      int lastValue = (int) ((data[0][0] + 1) / 2 * height);
      for (int i = 1; i < width; i++) {
         int newValue = (int) ((data[i][0] + 1) / 2 * height);
         g.drawLine(i - 1, lastValue, i, newValue);
         lastValue = newValue;
      }
   }
}
