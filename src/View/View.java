/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

//import Graphics.Canvas;
import Model.Model;
import javafx.beans.binding.StringBinding;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressBarBuilder;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.ReflectionBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

/**
 *
 * @author Brooke Smith brooke@tintuna.org
 */
public class View {

    private Model model;
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
    public Canvas canvas;
    public Canvas transparentcanvas;    // Sits over the top to receive mouse clicks.
    public final ProgressBar progress;
    public final Label message;
    private int[] palette;
    private byte[][] plasma;
    private int[] colors;
    public Scene scene;
    public final Label labelMSetWidth;
    public final TextField textMSetWidth;
    public final Label labelMSetHeight;
    public final TextField textMSetHeight;

    public View(final Model model) {
        this.model = model;
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

        progress = ProgressBarBuilder.create().minWidth(250).build();
        progress.progressProperty().bind(model.worker.progressProperty());

        labelx = new Label("x");
        textx = new TextField();
        textx.setMaxWidth(80.0);
        StringConverter scx = new DoubleStringConverter();
        textx.textProperty().bindBidirectional(model.xProperty(), scx);

        labely = new Label("y");
        texty = new TextField();
        texty.setMaxWidth(80.0);
        StringConverter scy = new DoubleStringConverter();
        texty.textProperty().bindBidirectional(model.yProperty(), scy);

        labelfactor = new Label("Factor");
        textfactor = new TextField();
        textfactor.setMaxWidth(80.0);
        StringConverter scfactor = new DoubleStringConverter();
        textfactor.textProperty().bindBidirectional(model.factorProperty(), scfactor);

        labelIters = new Label("Iterations");
        textIters = new TextField();

        textIters.setMaxWidth(80.0);
        textIters.setText(Integer.toString(model.getIters()));
        StringConverter sciters = new IntegerStringConverter();
        textIters.textProperty().bindBidirectional(model.itersProperty(), sciters);

        message = new Label("x");

        message.textProperty()
                .bind(model.worker.messageProperty());
        updateButton = new Button("Update");

        labelMSetWidth = new Label("MSet Width");
        textMSetWidth = new TextField();
        textMSetWidth.setMaxWidth(80.0);
        StringConverter scmsw = new DoubleStringConverter();
        textMSetWidth.textProperty().bindBidirectional(model.mset_widthProperty(), scmsw);

        labelMSetHeight = new Label("MSet Height");
        textMSetHeight = new TextField();
        textMSetHeight.setMaxWidth(80.0);
        StringConverter mssch = new DoubleStringConverter();
        textMSetHeight.textProperty().bindBidirectional(model.mset_heightProperty(), mssch);
        VBox vbox = new VBox(8);

        vbox.getChildren()
                .addAll(progress, labelx, textx, labely, texty, labelMSetHeight, textMSetHeight, labelMSetWidth, textMSetWidth, labelfactor, textfactor, labelIters, textIters, message, updateButton);
        group.getChildren()
                .addAll(titleText, vbox);

        canvas = new Canvas(model.getWIDTH(), model.getHEIGHT());   //x  model.START_X, model.START_Y, 
        canvas.setId("the_canvas");

        canvas.setTranslateX(model.getStageOffsetX());
        canvas.setTranslateY(model.getStageOffsetY());

        transparentcanvas = new Canvas(model.getWIDTH(), model.getHEIGHT());   //x  model.START_X, model.START_Y, 

        transparentcanvas.setTranslateX(model.getStageOffsetX());
        transparentcanvas.setTranslateY(model.getStageOffsetY());
        transparentcanvas.setOpacity(50);

        group.getChildren().addAll(canvas, transparentcanvas);

        group.setEffect(ReflectionBuilder.create().input(new DropShadow()).build());  // 10.0, Color.GRAY

        LinearGradient lg = new LinearGradient(0.0, 0.0, 0.0, 1.0, true,
                CycleMethod.REFLECT,
                new Stop(0, Color.GREEN),
                new Stop(0.5, Color.GREENYELLOW), new Stop(1.0, Color.YELLOW));
        scene = new Scene(group, lg);

        setup(canvas);
    }

    public void replaceCanvas(Canvas newCanvas) {
        Node n = group.lookup("#the_canvas");
        System.out.println("replaceCanvas - new canvas:" + newCanvas);
        if (newCanvas == null) {
            throw new RuntimeException("The New Canvas passed to replaceCanvas is null");
        }
        System.out.println("  Old canvas node n:" + n);
        int index = group.getChildren().lastIndexOf(n);
        System.out.println("  Index in group:" + index);
        if (index > -1) {
            group.getChildren().set(index, newCanvas);
            newCanvas.setId("the_canvas");
            newCanvas.setTranslateX(model.getStageOffsetX());
            newCanvas.setTranslateY(model.getStageOffsetY());
            canvas = newCanvas;
        } else {
            throw new RuntimeException("Index for canvas node doesn't exist:" + n);
        }
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

        plasma = new byte[(int) canvas.getHeight()][];
        for (int i = 0; i < plasma.length; i++) {
            plasma[i] = new byte[(int) canvas.getWidth()];
        }

        colors = new int[(int) canvas.getWidth() * (int) canvas.getHeight()];

        for (int i = 0; i < model.getWIDTH(); i++) {
            for (int j = 0; j < model.getHEIGHT(); j++) {
                canvas.getGraphicsContext2D().getPixelWriter().setColor(i, j, Color.AQUA);
            }
        }
    }
}
