package Graphics;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/*
 * From http://www.javaworld.com/community/node/8373 "Pixel Graphics and JavaFX" 26/04/2012
 * For doing pixel orientated JavaFX graphics.
 */
public class Canvas extends Group {

    private Line[][] pixels;

    public Canvas(int x, int y, int width, int height) {
        pixels = new Line[height][];
        for (int row = 0; row < width; row++) {
            pixels[row] = new Line[width];
            for (int col = 0; col < height; col++) {
                pixels[row][col] = new Line(x + col, y + row, x + col, y + row);
            }
            getChildren().addAll(pixels[row]);
        }
    }

    public int getHeight() {
        return pixels.length;
    }

    public int getWidth() {
        return pixels[0].length;
    }

    private boolean different(Color left, Color right) {
        if (left.getRed() != right.getRed() || left.getGreen() != right.getGreen() || left.getBlue() != right.getBlue()) {
            return true;
        }
        return false;
    }
    public void setPixel(int x, int y, Color c) {
//        if (different((Color) pixels[y][x].getStroke(), c)) {
//            System.out.format("setPixel(%3d,%3d) - stroke changed from %s to %s\n", x,y,pixels[y][x].getStroke(),c);
//        }
        pixels[y][x].setStroke(c);
    }

    public void setRect(int x1, int y1, int x2, int y2, int[] colors) {
        int index = 0;
        for (int y = y1; y <= y2; y++) {
            for (int x = x1; x <= x2; x++, index++) {
                pixels[y][x].setStroke(Color.rgb((colors[index] >> 16) & 255,
                        (colors[index] >> 8) & 255,
                        colors[index] & 255));
            }
        }
    }
}
