package pl.lukaszbilski.sort.controllers;

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
    progressBar.setProgress(-0.1);
  }
}
