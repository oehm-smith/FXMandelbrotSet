/*
 * 
 */
package mandelbrotset;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.ReflectionBuilder;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import Graphics.Canvas;
import Model.Model;
import View.View;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.VBox;
import org.apache.commons.math3.complex.Complex;

/**
 *
 * @author Brooke Smith brooke@tintuna.org
 */
public class MandelbrotSet extends Application {

    Model model;
    View view;
    private static int START_X = 100;
    private static int START_Y = 100;
    private static int WIDTH = 400;
    private static int HEIGHT = 400;
    private int[] palette;
    private byte[][] plasma;
    private int[] colors;
    int times = 410;
    private static final double MS_INFINITY = 4.0;
    private static final int MAX_ITERS = 20;
    private boolean sceneChanged = true;
    private double x = -1.5;
    private double y = -1.0;
    private double factor = 2.0;
    private int iters = MAX_ITERS;
    private boolean mouseDragState = false;
    private Point2D mouseDragFirstPoint = null;
    //ProgressBar progress = null;
//    Group group = null;
    double mset_width = 1.0;    // How wide the mandelbrot set is
    double mset_height = 1.0;   // How High the mandelbrot set is

    public MandelbrotSet() {
    }

    @Override
    public void start(final Stage primaryStage) {
        model = new Model();
        view = new View(model);

        primaryStage.setTitle("Animated Plasma");
        primaryStage.setWidth(600);
        primaryStage.setHeight(800);
        hookupEvents();
        primaryStage.setScene(view.scene);
        primaryStage.setResizable(true);
        primaryStage.show();


        final PauseTransition pt = new PauseTransition(Duration.millis(3));
        pt.setCycleCount(1);
        pt.setOnFinished(new EventHandler() {
            @Override
            public void handle(Event t) {
                update(view.canvas);
                pt.play();
            }
        });
        pt.play();
    }

    private void hookupEvents() {
        view.updateButton.setOnAction(new EventHandler() {
            @Override
            public void handle(Event t) {
                double newX = Double.parseDouble(view.textx.getText());
                double newY = Double.parseDouble(view.texty.getText());
                double newFactor = Double.parseDouble(view.textfactor.getText());
                int newIters = Integer.parseInt(view.textIters.getText());
                if (newX != model.getX() || newY != model.getY() || newFactor != model.getFactor() || newIters != model.getIters()) {
                    model.setX(newX);
                    model.setY(newY);
                    model.setFactor(newFactor);
                    model.setIters(newIters);
                    sceneChanged = true;
                }
            }
        });

        view.canvas.setOnDragDetected(new EventHandler() {
            // Activate the Drag Event
            @Override
            public void handle(Event t) {
                System.out.println("DragDetected ");
                view.canvas.startFullDrag();
                t.consume();
            }
        });

        view.canvas.setOnMouseDragEntered(new EventHandler<MouseDragEvent>() {
            // Where the drag starts
            @Override
            public void handle(MouseDragEvent t) {
                System.out.println("OnMouseDragEntered - event: " + t.getEventType() + ", x:" + t.getX() + ", y:" + t.getY());
                mouseDragFirstPoint = new Point2D(t.getX(), t.getY());
            }
        });
        view.canvas.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {
            // Where the drag ends
            @Override
            public void handle(MouseDragEvent t) {
                System.out.println("OnMouseDragReleased - event: " + t.getEventType() + ", x:" + t.getX() + ", y:" + t.getY());
                // Calculate the new x and y points:
                // Previous Mandelbrot Set's width and height / canvas' width and height * (left/upper-most) mouse drag x/y + last x/y
                model.setX(((model.getMSet_width() / WIDTH) * mouseDragFirstPoint.getX()) + model.getX());
                model.setX(((model.getMSet_height() / HEIGHT) * mouseDragFirstPoint.getY()) + model.getY());
                // Calculate the new width of the Mandelbrot Set to draw.  Its the width / height selected over the
                // canvas' width and height proportion of the previous Mandelbrot Set's width and height
                model.setMSet_height(model.getMSet_height() * (Math.abs(t.getY() - mouseDragFirstPoint.getY()) / (double) HEIGHT));
                model.setMSet_width(model.getMSet_width() * (Math.abs(t.getX() - mouseDragFirstPoint.getX()) / (double) WIDTH));
                sceneChanged = true;
            }
        });
    }

    void update(final Canvas canvas) {
        if (!sceneChanged) {
            return;
        }
//x        Task task = new Task<Void>() {
//x            @Override
//x            public Void call() {
        double width = canvas.getWidth();
        double height = canvas.getHeight();
//        int normaliseIters = 0;
//        int divider = 10;
//        int blogee = MAX_ITERS;
        double x = model.getX();//-0.2;
        double xstepfactor = model.getFactor();//0.2;
        double stepx = mset_width / width * xstepfactor;
        double y = model.getY();//-0.2;
        double stepy = mset_height / height * xstepfactor;
        int iters = model.getIters();
        Color msColor = null;
//        int maxProgress = (int) (width * height);
//        int progressStep = 0;
        sceneChanged = false;

        System.out.format(
                "width:%10.4f, height:%10.4f, MSetWidth:%10.4f, MSetHeight:%10.4f, x:%10.4f, y:%10.4f, stepx:%10.4f, factor:%10.4f, iters:%5d\n", width, height, mset_width, mset_height, x, y, stepx, xstepfactor, iters);

        for (int row = 0; row < height; row++) {
//            System.out.println("Row:" + row);
            for (int col = 0; col < width; col++) {
                Complex c = new Complex(x, y);
                //x double c = x;
                //x double ci = y;
                Complex z = new Complex(0, 0);
                //x double z = 0;
                //x double zi = 0;

                boolean goneInf = true;
                int depth = 0;
                label_for:
                for (; depth < iters; depth++) {
                    Complex z_new = z.multiply(z).add(c);
                    //x double z_new = (z * z) - (zi * zi) + x;
                    //x double z_newi = 2 * z * zi + y;
                    if ((z_new.getReal() * z_new.getReal()) + (z_new.getImaginary() + z_new.getImaginary()) > MS_INFINITY) {
                        //x if (((z_new * z_new) + (z_newi * z_newi)) > 4) {
                        goneInf = false;
                        break label_for;
                    }
                    z = z_new;
                }
//                progressStep++;
                //updateProgress(progressStep, maxProgress);
                // System.out.print(depth+" ");
                if (!goneInf) {
                    msColor = getColor(depth);
                } else {
                    msColor = getColor(0);
                }
//                if (times > 0) {
//                    //times--;
//                    System.out.format("col %4d:, row:%4d, x%10.4f:, y:%10.4f, depth:%4d, msColor:%s, c:%s\n", col, row, x, y, depth, msColor, c);
//                }
                canvas.setPixel(col, row, msColor);//new Color(red, green, blue, 0.0));
                x += stepx;
            }
            y += stepy;
            x = model.getX();
            times = 410;
        }
        sceneChanged = false;
//x                return null;
//x            }
//x        };
        //ProgressBar progress = new ProgressBar();
        //group.getChildren().add(progress);
        //progress.progressProperty().bind(task.progressProperty());
        //x new Thread(task).start();
    }

    private Color getColor(int depth) {
        Color mscolor;
        if (depth > MAX_ITERS || depth == 0) {
            mscolor = Color.BLACK;
        } else {
            double red = (double) depth / model.getIters() * 255;
            double green = (double) depth / model.getIters() * 200;
            double blue = (double) depth / model.getIters() * 100;
            //mscolor = new Color(red, green, blue, 1.0);
            mscolor = Color.rgb((int) red, (int) green, (int) blue, 0.4);
        }
        return mscolor;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
