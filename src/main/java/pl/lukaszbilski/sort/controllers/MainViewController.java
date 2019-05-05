package pl.lukaszbilski.sort.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

  @FXML
  Button sortButton ,setPathFromButton ,setPathToButton;

  @FXML
  ProgressBar progressBar;

  @FXML
  Label labelShowName;

  @FXML TextField pathFromTextField, pathToTextField;

  public void initialize(URL location, ResourceBundle resources) {
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
  }

  /**
   * This method start after "Sortuj" button will be clicked.
   */
  public void startSort() {
    sortButton.setVisible(false);
    progressBar.setVisible(true);

    final Task task = new Task<Void>() {
      @Override public Void call() {
        final long max = 50;
        for (int i = 1; i <= max; i++) {
          if (isCancelled()) {
            break;
          }
          try {
            Thread.sleep(25);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          updateProgress(i, max);
          updateMessage(String.valueOf(i));
        }
        return null;
      }
    };
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
   * Return File directory chosen by the user.
   */
  private String getDirectoryChooser() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    File selectedDirectory = new File(System.getProperty("user.dir"));

    directoryChooser.setInitialDirectory(selectedDirectory);
    selectedDirectory = directoryChooser.showDialog(null);

    if(selectedDirectory != null) {
        return selectedDirectory.getAbsolutePath();
    }else{
        return "";
    }
  }
}
