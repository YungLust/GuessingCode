package org.example.passwordguessing;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.concurrent.Task;

public class SimulationGUI  extends Application {

    private static final int DEFAULT_ATTEMPTS = 126; // Кількість спроб для ймовірності 0.6
    private static final int DEFAULT_SIMULATIONS = 10000;

    private Label statusLabel;
    private Label resultLabel;
    private ProgressBar progressBar;
    private PieChart resultChart;
    private Button startButton;
    private TextField attemptsField;
    private TextField simulationsField;
    private TextArea logArea;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Моделювання відкриття сейфу");

        // Creating UI objects
        Label attemptsLabel = new Label("Кількість спроб:");
        attemptsField = new TextField(String.valueOf(DEFAULT_ATTEMPTS));
        attemptsField.setPrefWidth(100);

        Label simulationsLabel = new Label("Кількість симуляцій:");
        simulationsField = new TextField(String.valueOf(DEFAULT_SIMULATIONS));
        simulationsField.setPrefWidth(100);

        startButton = new Button("Почати моделювання");
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(Double.MAX_VALUE);

        statusLabel = new Label("Готовий до моделювання");
        resultLabel = new Label("");

        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefHeight(150);

        // building diagram
        resultChart = new PieChart();
        resultChart.setTitle("Результати моделювання");
        resultChart.setLabelsVisible(true);

        // setting action for button
        //startButton.setOnAction();

        // layout
        GridPane inputGrid = new GridPane();
        inputGrid.setAlignment(Pos.CENTER);
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.addRow(0, attemptsLabel, attemptsField);
        inputGrid.addRow(1, simulationsLabel, simulationsField);

        //full width button
        startButton.setPadding(new Insets(8));
        inputGrid.addRow(2,startButton);
        GridPane.setColumnSpan(startButton,2);
        GridPane.setHgrow(startButton, Priority.ALWAYS);
        startButton.setMaxWidth(Double.MAX_VALUE);


        VBox controlBox = new VBox(10);
        controlBox.setPadding(new Insets(10));
        controlBox.getChildren().addAll(inputGrid, progressBar, statusLabel, resultLabel);

        BorderPane resultPane = new BorderPane();
        resultPane.setCenter(resultChart);

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(resultPane, logArea);
        splitPane.setDividerPositions(0.7);

        BorderPane root = new BorderPane();
        root.setTop(controlBox);
        root.setCenter(splitPane);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

