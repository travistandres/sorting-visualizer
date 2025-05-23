package hellofx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sorters.BubbleSort;
import sorters.HeapSort;
import sorters.MergeSort;
import sorters.QuickSort;
import testing.SortEventListener;
import javax.sound.sampled.*;

public class Main extends Application {
    private static final int ARRAY_SIZE = 50;
    private static final int CANVAS_WIDTH = 800;
    private static final int CANVAS_HEIGHT = 400;

    private int[] array;
    private GraphicsContext gc;

    // Declare comparisonCountLabel as a class-level variable
    private Label comparisonCountLabel;

   @Override
public void start(Stage stage) {
    Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
    gc = canvas.getGraphicsContext2D();

    array = generateRandomArray(ARRAY_SIZE);
    drawArray(gc, array, -1, -1);

    // Dropdown for selecting sorting algorithm
    ComboBox<String> algorithmSelector = new ComboBox<>();
    algorithmSelector.getItems().addAll("Bubble Sort", "Quick Sort", "Merge Sort", "Heap Sort");
    algorithmSelector.setValue("Bubble Sort");

    // Buttons for controlling the visualization
    Button startButton = new Button("Start");
    Button resetButton = new Button("Reset");

    // Labels to display execution time for each algorithm
    Label bubbleSortTimeLabel = new Label("Bubble Sort Time: N/A");
    Label quickSortTimeLabel = new Label("Quick Sort Time: N/A");
    Label mergeSortTimeLabel = new Label("Merge Sort Time: N/A");
    Label heapSortTimeLabel = new Label("Heap Sort Time: N/A");

    comparisonCountLabel = new Label("Comparisons: 0");

    // Event handlers for buttons
    startButton.setOnAction(e -> {
        String selectedAlgorithm = algorithmSelector.getValue();
        System.out.println("Selected Algorithm: " + selectedAlgorithm); // Debugging
        if (selectedAlgorithm.equals("Bubble Sort")) {
            startSorting(new BubbleSort(), bubbleSortTimeLabel);
        } else if (selectedAlgorithm.equals("Quick Sort")) {
            startSorting(new QuickSort(), quickSortTimeLabel);
        } else if (selectedAlgorithm.equals("Merge Sort")) {
            startSorting(new MergeSort(), mergeSortTimeLabel);
        } else if (selectedAlgorithm.equals("Heap Sort")) {
            startSorting(new HeapSort(), heapSortTimeLabel);
        }
    });

    resetButton.setOnAction(e -> {
        array = generateRandomArray(ARRAY_SIZE);
        drawArray(gc, array, -1, -1);
        comparisonCountLabel.setText("Comparisons: 0");
    });

    // Layout for controls
    HBox controls = new HBox(10, algorithmSelector, startButton, resetButton);
    controls.setStyle("-fx-padding: 10; -fx-alignment: center;");

    // Layout for time labels
    VBox timeLabels = new VBox(10, bubbleSortTimeLabel, quickSortTimeLabel, mergeSortTimeLabel, heapSortTimeLabel, comparisonCountLabel);
    timeLabels.setStyle("-fx-padding: 10; -fx-alignment: center;");

    

    BorderPane root = new BorderPane();
    root.setCenter(new StackPane(canvas));
    root.setBottom(controls);
    root.setRight(timeLabels);

    Scene scene = new Scene(root, CANVAS_WIDTH + 200, CANVAS_HEIGHT + 50);
    stage.setScene(scene);
    stage.setTitle("Sorting Visualizer");
    stage.show();
}

private void startSorting(Object sorter, Label timeLabel) {
    final SortEventListener listener = new SortEventListener() { // Declare listener as final
        long startTime = 0;
        int comparisonCount = 0; // Track the number of comparisons
    
        @Override
        public void onEvent(eventHandling.SortEvent event) {
            if (event.getType() == eventHandling.EventType.TIMER_START) {
                startTime = System.nanoTime(); // Record start time
                comparisonCount = 0; // Reset comparison count
            }
    
            if (event.getType() == eventHandling.EventType.UPDATE_ARRAY) {
                int head = event.getHead();
                int tail = event.getTail();
                drawArray(gc, event.getArray(), head, tail);
    
                int[] arr = event.getArray();
                if (head >= 0 && head < arr.length) {
                    int value = arr[head];
                    double freq = mapValueToFrequency(value, 0, CANVAS_HEIGHT, 200, 1000);
                    new Thread(() -> playTone(freq, 30)).start();
                }
    
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    
            if (event.getType() == eventHandling.EventType.INCREMENT_COMPARISONS) {
                comparisonCount++; // Increment the comparison count
                javafx.application.Platform.runLater(() -> {
                    comparisonCountLabel.setText("Comparisons: " + comparisonCount);
                });
            }
    
            if (event.getType() == eventHandling.EventType.SORT_COMPLETE) {
                long endTime = System.nanoTime(); // Record end time
                long executionTime = endTime - startTime;
    
                new Thread(() -> playFinalMelody(event.getArray(), gc)).start();
    
                // Update the time label on the JavaFX Application Thread
                javafx.application.Platform.runLater(() -> {
                    String algorithmName = timeLabel.getText().split(":")[0]; // Extract algorithm name
                    timeLabel.setText(algorithmName + ": " + String.format("%.2f", executionTime / 1_000_000_000.0) + " seconds");
                });
            }
        }
    };
    if (sorter instanceof BubbleSort) {
        BubbleSort bubbleSort = (BubbleSort) sorter;
        bubbleSort.emitter.subscribe(listener);
        new Thread(() -> bubbleSort.sort(array)).start();
    } else if (sorter instanceof QuickSort) {
        QuickSort quickSort = (QuickSort) sorter;
        quickSort.emitter.subscribe(listener);
        new Thread(() -> quickSort.sort(array)).start();
    } else if (sorter instanceof MergeSort) {
        MergeSort mergeSort = (MergeSort) sorter;
        mergeSort.emitter.subscribe(listener);
        new Thread(() -> mergeSort.sort(array)).start();
    } else if (sorter instanceof HeapSort) {
        HeapSort heapSort = (HeapSort) sorter;
        heapSort.emitter.subscribe(listener);
        new Thread(() -> heapSort.sort(array)).start();
    }
}

    private int[] generateRandomArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = (int) (Math.random() * CANVAS_HEIGHT);
        }
        return array;
    }

    private void drawArray(GraphicsContext gc, int[] array, int head, int tail) {
        gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        double barWidth = (double) CANVAS_WIDTH / array.length;
    
        // Clamp tail to the array length
        if (tail > array.length) {
            tail = array.length;
        }
    
        for (int i = 0; i < array.length; i++) {
            double x = i * barWidth;
            double y = CANVAS_HEIGHT - array[i];
            double height = array[i];
    
            if (i >= head && i <= tail && tail < array.length) {
                gc.setFill(javafx.scene.paint.Color.RED);
            } else {
                gc.setFill(javafx.scene.paint.Color.BLUE);
            }
            
    
            gc.fillRect(x, y, barWidth, height);
        }
    }

    private void playTone(double frequency, int durationMs) {
        try {
            float sampleRate = 44100;
            byte[] buf = new byte[1];
            AudioFormat af = new AudioFormat(sampleRate, 8, 1, true, false);
            SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
            sdl.open(af);
            sdl.start();
    
            for (int i = 0; i < durationMs * (sampleRate / 1000); i++) {
                double angle = i / (sampleRate / frequency) * 2.0 * Math.PI;
                buf[0] = (byte)(Math.sin(angle) * 127.0);
                sdl.write(buf, 0, 1);
            }
    
            sdl.drain();
            sdl.stop();
            sdl.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double mapValueToFrequency(int value, int minVal, int maxVal, double minFreq, double maxFreq) {
        return minFreq + ((double)(value - minVal) / (maxVal - minVal)) * (maxFreq - minFreq);
    }

    private void playFinalMelody(int[] array, GraphicsContext gc) {
        double barWidth = (double) CANVAS_WIDTH / array.length;
        int numPointers = 5;
    
        for (int i = 0; i < array.length; i++) {
            final int[] indices = new int[numPointers];
    
            for (int j = 0; j < numPointers; j++) {
                indices[j] = i + j;
            }
    
            javafx.application.Platform.runLater(() -> {
                gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
    
                for (int k = 0; k < array.length; k++) {
                    double x = k * barWidth;
                    double y = CANVAS_HEIGHT - array[k];
                    double height = array[k];
    
                    boolean isPointer = false;
                    for (int idx : indices) {
                        if (k == idx) {
                            isPointer = true;
                            break;
                        }
                    }
    
                    if (isPointer) {
                        gc.setFill(javafx.scene.paint.Color.ORANGE);
                    } else {
                        gc.setFill(javafx.scene.paint.Color.BLUE);
                    }
    
                    gc.fillRect(x, y, barWidth, height);
                }
            });
    
            // Play tones
            for (int idx : indices) {
                if (idx < array.length) {
                    double freq = mapValueToFrequency(array[idx], 0, CANVAS_HEIGHT, 200, 1000);
                    new Thread(() -> playTone(freq, 8)).start();
                }
            }
    
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    
        javafx.application.Platform.runLater(() -> {
            drawArray(gc, array, -1, -1);
        });
    }
    
    
    

    public static void main(String[] args) {
        launch(args);
    }
}