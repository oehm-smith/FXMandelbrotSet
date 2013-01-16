/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import Model.Model;
import javafx.concurrent.Task;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import org.apache.commons.math3.complex.Complex;

/**
 *
 * @author Brooke Smith brooke@tintuna.org
 */
public class EngineTask extends Task<Canvas> {

    private Model model;
//    private Canvas canvas;

    public EngineTask(Model model) {
        this.model = model;
    }

    public EngineTask(String x) {
    }
    @Override
    protected Canvas call() throws Exception {
        updateTitle("Calculating Mandelbrot Set ...");
        updateMessage("Calculating ...");
        System.out.println("00");
        updateProgress(0, 100);//x getMaxProgress());
        System.out.println("01");
        Canvas canvas = new Canvas(400,400);//x model.getWIDTH(), model.getHEIGHT());   //model.START_X, model.START_Y, 
        System.out.println("02");
        update(canvas);
        updateProgress(100, 100);//x getMaxProgress());
        // TODO - this is keeping newCanvas on heap - fix or else memory problems
        return canvas;
    }

    public void update(final Canvas canvas) {
        if (!model.sceneChanged) {
            return;
        }
        //this.newCanvas = canvas;

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
        double stepx = model.getMSet_width() / width * xstepfactor;
        double y = model.getY();//-0.2;
        double stepy = model.getMSet_height() / height * xstepfactor;
        int iters = model.getIters();
        Color msColor = null;
//        long maxProgress = (int) (width * height);
        long progressStep = 0;
        model.sceneChanged = false;

        System.out.format(
                "width:%10.4f, height:%10.4f, MSetWidth:%10.4f, MSetHeight:%10.4f, x:%10.4f, y:%10.4f, stepx:%10.4f, factor:%10.4f, iters:%5d\n",
                width, height, model.getMSet_width(), model.getMSet_height(), x, y, stepx, xstepfactor, iters);

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
                    if ((z_new.getReal() * z_new.getReal()) + (z_new.getImaginary() + z_new.getImaginary()) > model.MS_INFINITY) {
                        //x if (((z_new * z_new) + (z_newi * z_newi)) > 4) {
                        goneInf = false;
                        break label_for;
                    }
                    z = z_new;
                }
                progressStep++;
                updateProgress(progressStep, getMaxProgress(canvas));
                updateMessage(Long.toString(progressStep));
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
                canvas.getGraphicsContext2D().getPixelWriter().setColor(col, row, msColor);//new Color(red, green, blue, 0.0));
                x += stepx;
            }
            y += stepy;
            x = model.getX();
            model.times = 410;
        }
        model.sceneChanged = false;
//        System.out.println("FINISHED PROCESSING");
        return;// canvas.getPixelsData();

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
        if (depth > model.MAX_ITERS || depth == 0) {
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

    public int getMaxProgress(Canvas canvas) {
        return (int) (canvas.getWidth() * canvas.getHeight());
    }

    public void setModel(Model model) {
        this.model = model;
    }
}
