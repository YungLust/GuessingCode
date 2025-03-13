package org.example.passwordguessing;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SimulationGUI extends Application {

    private static final int DEFAULT_ATTEMPTS = 192; // Кількість спроб для ймовірності 0.6
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
        startButton.setOnAction(e -> runSimulation());

        // layout
        GridPane inputGrid = new GridPane();
        inputGrid.setAlignment(Pos.CENTER);
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.addRow(0, attemptsLabel, attemptsField);
        inputGrid.addRow(1, simulationsLabel, simulationsField);

        //full width button
        startButton.setPadding(new Insets(8));
        inputGrid.addRow(2, startButton);
        GridPane.setColumnSpan(startButton, 2);
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

    private void runSimulation() {
        try {
            int attempts = Integer.parseInt(attemptsField.getText().trim());
            int simulations = Integer.parseInt(simulationsField.getText().trim());

            if (attempts <= 0 || simulations <= 0) {
                UIUtils.showAlert("Помилка", "Кількість спроб та симуляцій повинна бути більше нуля");
                return;
            }

            if (attempts > SimulationLogic.TOTAL_COMBINATIONS) {
                UIUtils.showAlert("Увага", "Кількість спроб перевищує загальну кількість комбінацій ("
                        + SimulationLogic.TOTAL_COMBINATIONS + ")");
            }

            startButton.setDisable(true);
            logArea.clear();
            progressBar.setProgress(0);
            statusLabel.setText("Моделювання запущено...");

            Task<SimulationResult> task = new Task<>() {
                @Override
                protected SimulationResult call() throws Exception {
                    SimulationResult result = SimulationLogic.runMultipleSimulations(attempts, simulations,
                            (sim, log) -> {
                                updateProgress(sim + 1, simulations);

                                // add logging but only for 100 simulations
                                if (sim < 100) {
                                    final int simIndex = sim;
                                    Platform.runLater(() -> {
                                        logArea.appendText("Симуляція #" + (simIndex + 1) + ": " +
                                                (log.isSuccess() ? "УСПІХ" : "НЕВДАЧА") +
                                                " (код: " + log.getActualCode() +
                                                (log.isSuccess() ? ", спроба: " + log.getAttemptNumber() : "") +
                                                ")\n");
                                    });
                                }
                            });
                    return result;
                }
            };

            // Оновлення прогресбару
            progressBar.progressProperty().bind(task.progressProperty());

            // Оновлення UI після завершення
            task.setOnSucceeded(event -> {
                SimulationResult result = task.getValue();
                updateResults(result, attempts);
                startButton.setDisable(false);
                progressBar.progressProperty().unbind();
                progressBar.setProgress(1);
                statusLabel.setText("Моделювання завершено");

                if (simulations > 100) {
                    logArea.appendText("\n... і ще " + (simulations - 100) + " симуляцій (показано лише перші 100)\n");
                }
            });

            // Обробка помилок
            task.setOnFailed(event -> {
                startButton.setDisable(false);
                progressBar.progressProperty().unbind();
                statusLabel.setText("Помилка моделювання");
                UIUtils.showAlert("Помилка", "Сталася помилка під час моделювання: " + task.getException().getMessage());
            });

            // Запуск завдання в окремому потоці
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();

        } catch (NumberFormatException e) {
            UIUtils.showAlert("Помилка", "Введіть коректні числові значення");
        }
    }

    private void updateResults(SimulationResult result, int attempts) {
        resultLabel.setText(String.format(
                "Результат: Успішно %d з %d симуляцій (%.2f%%)\n" +
                        "Теоретична ймовірність для спроб %s: %s\n" +
                        "Експериментальна ймовірність: %s",
                result.getSuccessCount(),
                result.getTotalCount(),
                result.getProbability() * 100,
                attempts,
                UIUtils.formatDecimal(SimulationLogic.calculateTheoretical(attempts)),
                UIUtils.formatDecimal(result.getProbability())
        ));

        // Оновлення діаграми
        resultChart.getData().clear();
        PieChart.Data successData = new PieChart.Data("Успішно (" + result.getSuccessCount() + ")", result.getSuccessCount());
        PieChart.Data failureData = new PieChart.Data("Невдача (" + result.getFailureCount() + ")", result.getFailureCount());
        resultChart.getData().addAll(successData, failureData);
    }


    public static void main(String[] args) {
        launch(args);
    }
}

