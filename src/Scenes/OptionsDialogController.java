/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Scenes;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Brooke Smith brooke@tintuna.org
 */
public class OptionsDialogController implements Initializable {
    @FXML
    private TextField x;
    @FXML
    private TextField y;
    @FXML
    private TextField factor;
    @FXML
    private TextField iterations;
    @FXML
    private Button update;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void updateAction(ActionEvent event) {
        System.out.println("updateAction:"+event);
    }
}
