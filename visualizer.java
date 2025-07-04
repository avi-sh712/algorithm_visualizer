import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class AlgoVisualizer extends JPanel {
    private int[] array;
    private int barWidth;
    private final int HEIGHT = 400;
    private final int WIDTH = 800;
    private String currentAlgo = "Bubble Sort";

    public AlgoVisualizer(int size) {
        array = new int[size];
        barWidth = WIDTH / size;
        generateArray();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    public void generateArray() {
        Random rand = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = rand.nextInt(HEIGHT);
        }
        repaint();
    }

    public void bubbleSort() {
        new Thread(() -> {
            for (int i = 0; i < array.length - 1; i++) {
                for (int j = 0; j < array.length - i - 1; j++) {
                    if (array[j] > array[j + 1]) {
                        int temp = array[j];
                        array[j] = array[j + 1];
                        array[j + 1] = temp;
                    }
                    repaint();
                    sleep();
                }
            }
        }).start();
    }

    public void quickSort(int low, int high) {
        new Thread(() -> quickSortInternal(low, high)).start();
    }

    private void quickSortInternal(int low, int high) {
        if (low < high) {
            int pi = partition(low, high);
            quickSortInternal(low, pi - 1);
            quickSortInternal(pi + 1, high);
        }
        repaint();
    }

    private int partition(int low, int high) {
        int pivot = array[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (array[j] < pivot) {
                i++;
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                repaint();
                sleep();
            }
        }
        int temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;
        return i + 1;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < array.length; i++) {
            g.setColor(Color.BLUE);
            g.fillRect(i * barWidth, HEIGHT - array[i], barWidth, array[i]);
        }
    }

    private void sleep() {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("AlgoVisualizer – Sorting Algorithms");
        AlgoVisualizer panel = new AlgoVisualizer(100);

        JPanel buttons = new JPanel();
        JButton generate = new JButton("Generate Array");
        JButton bubble = new JButton("Bubble Sort");
        JButton quick = new JButton("Quick Sort");

        generate.addActionListener(e -> panel.generateArray());
        bubble.addActionListener(e -> panel.bubbleSort());
        quick.addActionListener(e -> panel.quickSort(0, panel.array.length - 1));

        buttons.add(generate);
        buttons.add(bubble);
        buttons.add(quick);

        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.add(buttons, BorderLayout.SOUTH);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
