import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class algo_visualizer extends JPanel {
    private int[] array;
    private int barWidth;
    private int HEIGHT;
    private int WIDTH;
    private Color barColor = Color.BLUE;
    private String complexityInfo = "";
    private int highlightedIndex = -1;

    public AlgoVisualizer(int size) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        WIDTH = screenSize.width - 100;
        HEIGHT = screenSize.height - 200;
        array = new int[size];
        barWidth = Math.max(WIDTH / size, 8);
        generateArray();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    public void generateArray() {
        Random rand = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = rand.nextInt(HEIGHT - 100) + 50;
        }
        repaint();
    }

    public void bubbleSort() {
        barColor = Color.BLUE;
        complexityInfo = "Bubble Sort: O(n^2)";
        new Thread(() -> {
            for (int i = 0; i < array.length - 1; i++) {
                for (int j = 0; j < array.length - i - 1; j++) {
                    highlightedIndex = j;
                    if (array[j] > array[j + 1]) {
                        int temp = array[j];
                        array[j] = array[j + 1];
                        array[j + 1] = temp;
                    }
                    repaint();
                    sleep();
                }
            }
            highlightedIndex = -1;
            repaint();
        }).start();
    }

    public void insertionSort() {
        barColor = Color.MAGENTA;
        complexityInfo = "Insertion Sort: O(n^2)";
        new Thread(() -> {
            for (int i = 1; i < array.length; i++) {
                int key = array[i];
                int j = i - 1;
                highlightedIndex = i;
                while (j >= 0 && array[j] > key) {
                    array[j + 1] = array[j];
                    j = j - 1;
                    repaint();
                    sleep();
                }
                array[j + 1] = key;
                repaint();
                sleep();
            }
            highlightedIndex = -1;
            repaint();
        }).start();
    }

    public void selectionSort() {
        barColor = Color.ORANGE;
        complexityInfo = "Selection Sort: O(n^2)";
        new Thread(() -> {
            for (int i = 0; i < array.length - 1; i++) {
                int min_idx = i;
                for (int j = i + 1; j < array.length; j++) {
                    highlightedIndex = j;
                    if (array[j] < array[min_idx]) {
                        min_idx = j;
                    }
                    repaint();
                    sleep();
                }
                int temp = array[min_idx];
                array[min_idx] = array[i];
                array[i] = temp;
                repaint();
                sleep();
            }
            highlightedIndex = -1;
            repaint();
        }).start();
    }

    public void quickSort(int low, int high) {
        barColor = Color.GREEN;
        complexityInfo = "Quick Sort: O(n log n)";
        new Thread(() -> {
            quickSortInternal(low, high);
            highlightedIndex = -1;
            repaint();
        }).start();
    }

    private void quickSortInternal(int low, int high) {
        if (low < high) {
            int pi = partition(low, high);
            quickSortInternal(low, pi - 1);
            quickSortInternal(pi + 1, high);
        }
    }

    private int partition(int low, int high) {
        int pivot = array[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            highlightedIndex = j;
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

    public void mergeSort() {
        barColor = Color.CYAN;
        complexityInfo = "Merge Sort: O(n log n)";
        new Thread(() -> {
            mergeSortInternal(0, array.length - 1);
            highlightedIndex = -1;
            repaint();
        }).start();
    }

    private void mergeSortInternal(int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSortInternal(left, mid);
            mergeSortInternal(mid + 1, right);
            merge(left, mid, right);
        }
    }

    private void merge(int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] L = new int[n1];
        int[] R = new int[n2];

        System.arraycopy(array, left, L, 0, n1);
        System.arraycopy(array, mid + 1, R, 0, n2);

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            highlightedIndex = k;
            if (L[i] <= R[j]) {
                array[k] = L[i];
                i++;
            } else {
                array[k] = R[j];
                j++;
            }
            k++;
            repaint();
            sleep();
        }

        while (i < n1) {
            highlightedIndex = k;
            array[k] = L[i];
            i++;
            k++;
            repaint();
            sleep();
        }

        while (j < n2) {
            highlightedIndex = k;
            array[k] = R[j];
            j++;
            k++;
            repaint();
            sleep();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        for (int i = 0; i < array.length; i++) {
            if (i == highlightedIndex) {
                g2.setColor(Color.RED);
                g2.fillRect(i * barWidth, HEIGHT - array[i], barWidth - 2, array[i]);
                g2.setColor(Color.BLACK);
                g2.drawRect(i * barWidth, HEIGHT - array[i], barWidth - 2, array[i]);
                g2.drawString(String.valueOf(array[i]), i * barWidth + 2, HEIGHT - array[i] - 6);
            } else {
                g2.setColor(barColor);
                g2.fillRect(i * barWidth, HEIGHT - array[i], barWidth - 2, array[i]);
                g2.setColor(Color.BLACK);
                g2.drawRect(i * barWidth, HEIGHT - array[i], barWidth - 2, array[i]);
                if (barWidth >= 18) {
                    g2.drawString(String.valueOf(array[i]), i * barWidth + 2, HEIGHT - array[i] - 6);
                }
            }
        }
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.setColor(Color.BLACK);
        g2.drawString(complexityInfo, 10, 30);
    }

    private void sleep() {
        try {
            Thread.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("AlgoVisualizer – Sorting Algorithms");
        algo_visualizer panel = new algo_visualizer(100);

        JPanel buttons = new JPanel();
        JButton generate = new JButton("Generate Array");
        JButton bubble = new JButton("Bubble Sort");
        JButton quick = new JButton("Quick Sort");
        JButton insertion = new JButton("Insertion Sort");
        JButton selection = new JButton("Selection Sort");
        JButton merge = new JButton("Merge Sort");

        generate.addActionListener(e -> panel.generateArray());
        bubble.addActionListener(e -> panel.bubbleSort());
        quick.addActionListener(e -> panel.quickSort(0, panel.array.length - 1));
        insertion.addActionListener(e -> panel.insertionSort());
        selection.addActionListener(e -> panel.selectionSort());
        merge.addActionListener(e -> panel.mergeSort());

        buttons.add(generate);
        buttons.add(bubble);
        buttons.add(quick);
        buttons.add(insertion);
        buttons.add(selection);
        buttons.add(merge);

        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.add(buttons, BorderLayout.SOUTH);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
