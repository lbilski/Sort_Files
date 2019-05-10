package pl.lukaszbilski.sort.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import pl.lukaszbilski.sort.models.DataTransfer;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

  @FXML
  Button sortButton ,setPathFromButton ,setPathToButton;

  @FXML
  ProgressBar progressBar;

  @FXML
  Label labelShowName;

  @FXML
  TextField pathFromTextField, pathToTextField;

  private final String DEFAULT_DIRECTORY = "user.dir";

  public void initialize(URL location, ResourceBundle resources) {

    //Listener set values when progress bar will be complete
    progressBar.progressProperty().addListener(new ChangeListener<Number>() {
      public void changed(ObservableValue<? extends Number>
                            observable, Number oldValue, Number newValue) {
        if (newValue.intValue() == 1) {
          labelShowName.textProperty().unbind();
          labelShowName.setText("KONIEC");
          progressBar.setVisible(false);
          sortButton.setVisible(true);
        }
      }
    });

    //This listener prevents sorting if paths are not selected
    sortButton.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        if(!isPathIsCorrect()) {
          startSort();
        }else {
          confirmationDialog("Błąd wprowadzonych danych", "Ustaw poprawne ścieżki katalogów");
        }
      }
    });
  }

  /**
   * This method start after "Sortuj" button will be clicked.
   */
  private void startSort() {
    sortButton.setVisible(false);
    progressBar.setVisible(true);

    DataTransfer task = new DataTransfer(pathFromTextField.getText(), pathToTextField.getText());

    progressBar.progressProperty().bind(task.progressProperty());
    labelShowName.textProperty().bind(task.messageProperty());
    new Thread(task).start();
  }

  /**
   * This method set text in pathFromTextField.
   */
  public void setPathFrom() {
    pathFromTextField.setText(getDirectoryChooser());
  }

  /**
   * This method set text in pathToTextField.
   */
  public void setPathTo() {
    pathToTextField.setText(getDirectoryChooser());
  }

  /**
   * Return File directory chosen by the user, if user did'nt chose directory return empty string.
   */
  private String getDirectoryChooser() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    File selectedDirectory = new File(System.getProperty(DEFAULT_DIRECTORY));

    directoryChooser.setInitialDirectory(selectedDirectory);
    selectedDirectory = directoryChooser.showDialog(null);

    if(selectedDirectory != null) {
        return selectedDirectory.getAbsolutePath();
    }else{
        return "";
    }
  }

  /**
   * The method checks if a path has been selected.
   * @return true if user didn't chose the directory "To" or "From".
  */
  private boolean isPathIsCorrect() {
    return pathFromTextField.getLength() == 0 || pathToTextField.getLength() == 0;
  }

  /**
   * This method create a information dialog.
   * @param title this is a window title.
   * @param message is a message to user.
  */
  private void confirmationDialog(String title, String message) {
     Alert alert = new Alert(Alert.AlertType.INFORMATION);
     alert.setTitle(title);
     alert.setHeaderText(null);
     alert.setContentText(message);
     alert.showAndWait();
  }
}