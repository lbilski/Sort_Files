package pl.lukaszbilski.sort.controllers;

import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class MainViewController {

  @FXML
  Button sortButton;

  @FXML
  ProgressBar progressBar;

  @FXML
  Label labelShowName;

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

    progressBar.progressProperty().addListener(new ChangeListener<Number>() {
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        if(newValue.intValue() == 1){
          labelShowName.textProperty().unbind();
          labelShowName.setText("KONIEC");
          progressBar.setVisible(false);
          sortButton.setVisible(true);
        }
      }
    });
  }
}
