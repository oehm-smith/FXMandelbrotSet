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
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import org.apache.commons.math3.complex.Complex;

/**
 *
 * @author Brooke Smith brooke@tintuna.org
 */
public class MandelbrotSet extends Application {

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

    public double getMSet_width() {
        return mset_width;
    }

    public void setMSet_width(double set_width) {
        this.mset_width = set_width;
    }

    public double getMSet_height() {
        return mset_height;
    }

    public void setMSet_height(double set_height) {
        this.mset_height = set_height;
    }

    public MandelbrotSet() {
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getFactor() {
        return factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }

    public int getIters() {
        return iters;
    }

    public void setIters(int iters) {
        this.iters = iters;
    }

    @Override
    public void start(final Stage primaryStage) {
        primaryStage.setTitle("Animated Plasma");
        primaryStage.setWidth(600);
        primaryStage.setHeight(800);

        final Group group = new Group();

        Text text = new Text();
        text.setText("Mandelbrot Set");
        text.setFill(Color.YELLOW);
        text.setFont(javafx.scene.text.Font.font("Arial BOLD", 24.0));
        text.setEffect(new DropShadow());
        text.xProperty().bind(primaryStage.widthProperty()
                .subtract(text.layoutBoundsProperty().getValue().getWidth())
                .divide(2));
        text.setY(30.0);
        text.setTextOrigin(VPos.TOP);
        //group.getChildren().add(text);

        //progress = new ProgressBar();

        Label labelx = new Label("x");
        final TextField textx = new TextField();
        textx.setMaxWidth(80.0);
        textx.setText(Double.toString(getX()));

        Label labely = new Label("y");
        final TextField texty = new TextField();
        texty.setMaxWidth(80.0);
        texty.setText(Double.toString(getY()));

        Label labelfactor = new Label("Factor");
        final TextField textfactor = new TextField();
        textfactor.setMaxWidth(80.0);
        textfactor.setText(Double.toString(getFactor()));

        Label labelIters = new Label("Iterations");
        final TextField textIters = new TextField();
        textIters.setMaxWidth(80.0);
        textIters.setText(Integer.toString(getIters()));


        Button updateButton = new Button("Update");
        updateButton.setOnAction(new EventHandler() {
            @Override
            public void handle(Event t) {
                double newX = Double.parseDouble(textx.getText());
                double newY = Double.parseDouble(texty.getText());
                double newFactor = Double.parseDouble(textfactor.getText());
                int newIters = Integer.parseInt(textIters.getText());
                if (newX != getX() || newY != getY() || newFactor != getFactor() || newIters != getIters()) {
                    setX(newX);
                    setY(newY);
                    setFactor(newFactor);
                    setIters(newIters);
                    sceneChanged = true;
                }
            }
        });
        VBox vbox = new VBox(8);
        vbox.getChildren().addAll(labelx, textx, labely, texty, labelfactor, textfactor, labelIters, textIters, updateButton);
        group.getChildren().addAll(text, vbox);


        final Canvas canvas = new Canvas(START_X, START_Y, WIDTH, HEIGHT);

        canvas.setOnDragDetected(new EventHandler() {
            // Activate the Drag Event
            @Override
            public void handle(Event t) {
                System.out.println("DragDetected ");
                canvas.startFullDrag();
                t.consume();
            }
        });

        canvas.setOnMouseDragEntered(new EventHandler<MouseDragEvent>() {
            // Where the drag starts
            @Override
            public void handle(MouseDragEvent t) {
                System.out.println("OnMouseDragEntered - event: " + t.getEventType() + ", x:" + t.getX() + ", y:" + t.getY());
                mouseDragFirstPoint = new Point2D(t.getX(), t.getY());
            }
        });
        canvas.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {
            // Where the drag ends
            @Override
            public void handle(MouseDragEvent t) {
                System.out.println("OnMouseDragReleased - event: " + t.getEventType() + ", x:" + t.getX() + ", y:" + t.getY());
                // Calculate the new x and y points:
                // Previous Mandelbrot Set's width and height / canvas' width and height * (left/upper-most) mouse drag x/y + last x/y
                setX(((getMSet_width() / WIDTH) * mouseDragFirstPoint.getX()) + getX());
                setX(((getMSet_height() / HEIGHT) * mouseDragFirstPoint.getY()) + getY());
                // Calculate the new width of the Mandelbrot Set to draw.  Its the width / height selected over the
                // canvas' width and height proportion of the previous Mandelbrot Set's width and height
                setMSet_height(getMSet_height() * (Math.abs(t.getY() - mouseDragFirstPoint.getY()) / (double) HEIGHT));
                setMSet_width(getMSet_width() * (Math.abs(t.getX() - mouseDragFirstPoint.getX()) / (double) WIDTH));
                sceneChanged = true;
            }
        });

        group.getChildren().add(canvas);

        group.setEffect(ReflectionBuilder.create().input(new DropShadow()).build());  // 10.0, Color.GRAY

        LinearGradient lg = new LinearGradient(0.0, 0.0, 0.0, 1.0, true,
                CycleMethod.REFLECT,
                new Stop(0, Color.GREEN),
                new Stop(0.5, Color.GREENYELLOW), new Stop(1.0, Color.YELLOW));
        Scene scene = new Scene(group, lg);
        primaryStage.setScene(scene);

        primaryStage.setResizable(true);
        primaryStage.show();

        setup(canvas);

        final PauseTransition pt = new PauseTransition(Duration.millis(3));
        pt.setCycleCount(1);
        pt.setOnFinished(new EventHandler() {
            @Override
            public void handle(Event t) {
                update(canvas);
                pt.play();
            }
        });
        pt.play();
    }

    void setup(Canvas canvas) {
        palette = new int[512];
        for (int i = 0; i < palette.length; i++) {
            Color color = Color.hsb(i / 255.0 * 360, 1.0, 1.0);
            int red = (int) (color.getRed() * 255);
            int grn = (int) (color.getGreen() * 255);
            int blu = (int) (color.getBlue() * 255);
            palette[i] = (red << 16) | (grn << 8) | blu;
        }

        plasma = new byte[canvas.getHeight()][];
        for (int i = 0; i < plasma.length; i++) {
            plasma[i] = new byte[canvas.getWidth()];
        }

        colors = new int[canvas.getWidth() * canvas.getHeight()];
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
        double x = getX();//-0.2;
        double xstepfactor = getFactor();//0.2;
        double stepx = mset_width / width * xstepfactor;
        double y = getY();//-0.2;
        double stepy = mset_height / height * xstepfactor;
        int iters = getIters();
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
            x = getX();
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
            double red = (double) depth / getIters() * 255;
            double green = (double) depth / getIters() * 200;
            double blue = (double) depth / getIters() * 100;
            //mscolor = new Color(red, green, blue, 1.0);
            mscolor = Color.rgb((int) red, (int) green, (int) blue, 0.4);
        }
        return mscolor;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
