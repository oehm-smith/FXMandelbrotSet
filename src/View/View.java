/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Graphics.Canvas;
import Model.Model;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.ReflectionBuilder;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;

/**
 *
 * @author Brooke Smith brooke@tintuna.org
 */
public class View {

    final Group group;
    public final Text titleText;
    public final Label labelx;
    public final TextField textx;
    public final Label labely;
    public final TextField texty;
    public final Label labelfactor;
    public final TextField textfactor;
    public final Label labelIters;
    public final TextField textIters;
    public final Button updateButton;
    public final Canvas canvas;
    private int[] palette;
    private byte[][] plasma;
    private int[] colors;
    public Scene scene;

    public View(Model model) {
        group = new Group();

        titleText = new Text();
        titleText.setText("Mandelbrot Set");
        titleText.setFill(Color.YELLOW);
        titleText.setFont(javafx.scene.text.Font.font("Arial BOLD", 24.0));
        titleText.setEffect(new DropShadow());
//        titleText.xProperty().bind(primaryStage.widthProperty()
//                .subtract(titleText.layoutBoundsProperty().getValue().getWidth())
//                .divide(2));
        titleText.xProperty().bind(model.WIDTHProperty()
                .subtract(titleText.layoutBoundsProperty().getValue().getWidth())
                .divide(2));
        titleText.setY(30.0);
        titleText.setTextOrigin(VPos.TOP);
        //group.getChildren().add(text);

        //progress = new ProgressBar();

        labelx = new Label("x");
        textx = new TextField();
        textx.setMaxWidth(80.0);
        textx.setText(Double.toString(model.getX()));

        labely = new Label("y");
        texty = new TextField();
        texty.setMaxWidth(80.0);
        texty.setText(Double.toString(model.getY()));

        labelfactor = new Label("Factor");
        textfactor = new TextField();
        textfactor.setMaxWidth(80.0);
        textfactor.setText(Double.toString(model.getFactor()));

        labelIters = new Label("Iterations");
        textIters = new TextField();
        textIters.setMaxWidth(80.0);
        textIters.setText(Integer.toString(model.getIters()));


        updateButton = new Button("Update");

        VBox vbox = new VBox(8);
        vbox.getChildren().addAll(labelx, textx, labely, texty, labelfactor, textfactor, labelIters, textIters, updateButton);
        group.getChildren().addAll(titleText, vbox);


        canvas = new Canvas(model.START_X, model.START_Y, model.getWIDTH(), model.getHEIGHT());

        group.getChildren().add(canvas);

        group.setEffect(ReflectionBuilder.create().input(new DropShadow()).build());  // 10.0, Color.GRAY

        LinearGradient lg = new LinearGradient(0.0, 0.0, 0.0, 1.0, true,
                CycleMethod.REFLECT,
                new Stop(0, Color.GREEN),
                new Stop(0.5, Color.GREENYELLOW), new Stop(1.0, Color.YELLOW));
        scene = new Scene(group, lg);

        setup(canvas);
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
}
