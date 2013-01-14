/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point2D;
import javafx.scene.shape.Line;

/**
 *
 * @author Brooke Smith brooke@tintuna.org
 */
public class Model {

    public static final int START_X = 100;
    public static final int START_Y = 100;
    public static final IntegerProperty WIDTH = new SimpleIntegerProperty(400);
    public static final IntegerProperty HEIGHT = new SimpleIntegerProperty(400);
    int times = 410;
    private static final double MS_INFINITY = 4.0;
    private static final int MAX_ITERS = 20;
    private boolean sceneChanged = true;
    // Fields care about
    private DoubleProperty x = new SimpleDoubleProperty(-1.5);
    private DoubleProperty y = new SimpleDoubleProperty(-1.0);
    private DoubleProperty factor = new SimpleDoubleProperty(2.0);
    private IntegerProperty iters = new SimpleIntegerProperty(MAX_ITERS);
//    private double x = -1.5;
//    private double y = -1.0;
//    private double factor = 2.0;
//    private int iters = MAX_ITERS;
    private boolean mouseDragState = false;
    private Point2D mouseDragFirstPoint = null;
    //ProgressBar progress = null;
//    Group group = null;
    private DoubleProperty mset_width = new SimpleDoubleProperty(1.0);    // How wide the mandelbrot set is
    private DoubleProperty mset_height = new SimpleDoubleProperty(1.0);   // How High the mandelbrot set is
//    double mset_width = 1.0;    // How wide the mandelbrot set is
//    double mset_height = 1.0;   // How High the mandelbrot set is
    private Line[][] pixels;    // load canvas with new values  

    public final double getMSet_width() {
        return mset_width.get();
    }

    public final void setMSet_width(double set_width) {
        this.mset_width.set(set_width);
    }

    public DoubleProperty mset_widthProperty() {
        return mset_width;
    }

    public final double getMSet_height() {
        return mset_height.get();
    }

    public final void setMSet_height(double set_height) {
        this.mset_height.set(set_height);
    }

    public DoubleProperty mset_heightProperty() {
        return mset_height;
    }

    public final double getX() {
        return x.get();
    }

    public final void setX(double x) {
        this.x.set(x);
    }

    public DoubleProperty xProperty() {
        return x;
    }

    public final double getY() {
        return y.get();
    }

    public final void setY(double y) {
        this.y.set(y);
    }

    public DoubleProperty yProperty() {
        return y;
    }

    public final double getFactor() {
        return factor.get();
    }

    public final void setFactor(double factor) {
        this.factor.set(factor);
    }

    public DoubleProperty factorProperty() {
        return factor;
    }

    public final int getIters() {
        return iters.get();
    }

    public final void setIters(int iters) {
        this.iters.set(iters);
    }

    public IntegerProperty itersProperty() {
        return iters;
    }

    public final int getWIDTH() {
        return WIDTH.get();
    }

    public final void setWIDTH(int WIDTH) {
        this.WIDTH.set(WIDTH);
    }

    public IntegerProperty WIDTHProperty() {
        return WIDTH;
    }

    public final int getHEIGHT() {
        return HEIGHT.get();
    }

    public final void setHEIGHT(int HEIGHT) {
        this.HEIGHT.set(HEIGHT);
    }

    public IntegerProperty HEIGHTProperty() {
        return HEIGHT;
    }
}
