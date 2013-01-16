/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Engine.EngineTask;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.shape.Line;

/**
 *
 * @author Brooke Smith brooke@tintuna.org
 */
public class Model {
    // TODO - fix up all these publics

    private IntegerProperty stageWIDTH = new SimpleIntegerProperty(600);
    private IntegerProperty stageHEIGHT = new SimpleIntegerProperty(800);
    private IntegerProperty stageOffsetX = new SimpleIntegerProperty(100);
    private IntegerProperty stageOffsetY = new SimpleIntegerProperty(100);
    public static final IntegerProperty WIDTH = new SimpleIntegerProperty(400);
    public static final IntegerProperty HEIGHT = new SimpleIntegerProperty(400);
    public int times = 410;
    public static final double MS_INFINITY = 4.0;
    public static final int MAX_ITERS = 20;
    public boolean sceneChanged = true;
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
    public Point2D mouseDragFirstPoint = null;
    //ProgressBar progress = null;
//    Group group = null;
    private DoubleProperty mset_width = new SimpleDoubleProperty(1.0);    // How wide the mandelbrot set is
    private DoubleProperty mset_height = new SimpleDoubleProperty(1.0);   // How High the mandelbrot set is
//    double mset_width = 1.0;    // How wide the mandelbrot set is
//    double mset_height = 1.0;   // How High the mandelbrot set is
//    private Line[][] pixels;    // load canvas with new values  
    public Worker<Canvas> worker;
//    private EngineTask engine;

    public Model() {//EngineTask engine) {
//        this.engine = engine;
//        engine.setModel(this);

        worker = new Service<Canvas>() {
            @Override
            protected Task createTask() {
                return new EngineTask(Model.this);
//                return new Task<Canvas>() {
//                    @Override
//                    protected Canvas call() throws Exception {
//                        updateTitle(" Example Service");
//                        updateMessage("Starting...");
//                        Canvas newCanvas = new Canvas(10, 10);//model.START_X, model.START_Y, model.getWIDTH(), model.getHEIGHT());
//                        final int total = 100;
//
//                        updateProgress(0, total);
//                        for (int i = 1; i <= total; i++) {
//                            try {
//                                Thread.sleep(20);
//                            } catch (InterruptedException e) {
//                                return null;
//                            }
//                            updateTitle("Example Service(" + i + ")");
//                            updateMessage("P " + i + " of " + total + " items.");
//                            updateProgress(i, total);
//                        }
//                        return newCanvas;
//                    }
//                };//rem this 
            }
        };
    }

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

    public final int getStageWIDTH() {
        return stageWIDTH.get();
    }

    public final void setStageWIDTH(int WIDTH) {
        this.stageWIDTH.set(WIDTH);
    }

    public IntegerProperty stageWIDTHProperty() {
        return stageWIDTH;
    }

    public final int getStageHEIGHT() {
        return stageHEIGHT.get();
    }

    public final void setStageHEIGHT(int HEIGHT) {
        this.stageHEIGHT.set(HEIGHT);
    }

    public IntegerProperty stageHEIGHTProperty() {
        return stageHEIGHT;
    }

    public final int getStageOffsetX() {
        return stageOffsetX.get();
    }

    public final void setStageOffsetX(int offset) {
        this.stageOffsetX.set(offset);
    }

    public IntegerProperty stageOffsetXProperty() {
        return stageOffsetX;
    }

    public final int getStageOffsetY() {
        return stageOffsetY.get();
    }

    public final void setStageOffsetY(int offset) {
        this.stageOffsetY.set(offset);
    }

    public IntegerProperty stageOffsetYProperty() {
        return stageOffsetY;
    }
 }
