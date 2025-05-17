package turing_machine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Class 1: Transition (Unchanged)
class Transition {
    char read;
    char write;
    char shift;
    int nextState;

    Transition(String s) {
        read = s.charAt(0);
        write = s.charAt(2);
        shift = s.charAt(4);
        int l = s.length();
        String substr = s.substring(6, l);
        nextState = Integer.parseInt(substr);
    }
}

// Class 2: State (Unchanged)
class State {
    ArrayList<Transition> trs;

    State(ArrayList<Transition> ts) { trs = ts; }
}

// Class 3: Machine (Slightly Modified for Enhanced GUI)
class Machine {
    Scanner fs;
    int stateCount;
    int currState;
    int finalState;
    char blankSym;
    StringBuffer Tape = new StringBuffer();
    ArrayList<State> states = new ArrayList<>();
    JTextArea outputArea;
    JPanel tapePanel; // Added for tape visualization

    Machine(JTextArea outputArea, JPanel tapePanel) {
        this.outputArea = outputArea;
        this.tapePanel = tapePanel;
    }

    void buildMachine(Scanner f) {
        this.fs = f;
        outputArea.append("\t" + readString() + "\n");
        String s = readString();
        outputArea.append("Input symbols: " + s + "\n");
        s += " " + readString();
        blankSym = readChar();
        outputArea.append("Blank symbol: " + blankSym + "\n");
        s += " " + blankSym;
        outputArea.append("Tape symbols: " + s + "\n");
        stateCount = readInt();
        outputArea.append("\nNumber of States: " + stateCount + "\n");
        currState = readInt();
        outputArea.append("Start State: " + currState + "\n");
        for (int i = 0; i < stateCount; i++)
            addState(i);
    }

    void addState(int ind) {
        int trCount = readInt();
        if (trCount == 0)
            finalState = ind;
        ArrayList<Transition> trs = new ArrayList<>();
        for (int i = 0; i < trCount; i++) {
            String s = readString();
            Transition tr = new Transition(s);
            trs.add(tr);
        }
        State st = new State(trs);
        states.add(st);
    }

    String readString() {
        String s = fs.next();
        while (s.startsWith("//") || s.isEmpty())
            s = fs.next();
        return s;
    }

    char readChar() {
        String s = fs.next();
        while (s.startsWith("//") || s.isEmpty())
            s = fs.next();
        return s.charAt(0);
    }

    int readInt() {
        String s = fs.next();
        while (s.startsWith("//") || s.isEmpty())
            s = fs.next();
        return Integer.parseInt(s);
    }

    void runTuring(int index) throws InterruptedException {
        while (currState != finalState) {
            index = makeTrans(index);
            if (index == -1) {
                outputArea.append("ERROR: Transition Not Found! Machine HALTED.\n");
                throw new InterruptedException("ERROR: Transition Not Found! Machine HALTED.");
            }
            printTape(index);
        }
        outputArea.append("Machine halted at state: q" + currState + "\n");
    }

    int makeTrans(int index) throws InterruptedException {
        if (Tape.charAt(index) == '$') {
            outputArea.append("ERROR: Head left the Tape boundary! Machine HALTED.\n");
            throw new InterruptedException("ERROR: Head left the Tape boundary! Machine HALTED.");
        }
        State st = states.get(currState);
        for (Transition tr : st.trs) {
            if (tr.read == Tape.charAt(index)) {
                Tape.replace(index, index + 1, String.valueOf(tr.write));
                currState = tr.nextState;
                switch (tr.shift) {
                    case 'R': return index + 1;
                    case 'L': return index - 1;
                    default: return -1;
                }
            }
        }
        return -1;
    }

    void printTape(int index) {
        int interval = 300; // Reduced for smoother animation
        StringBuilder spaces = new StringBuilder();
        for (int i = 0; i < index; i++)
            spaces.append(" ");
        outputArea.append("Tape: " + Tape + "\n");
        outputArea.append(spaces + "    ^q" + currState + "\n\n");
        updateTapeVisualization(index);
        try {
            Thread.sleep(interval);
            outputArea.update(outputArea.getGraphics());
            tapePanel.repaint();
        } catch (InterruptedException e) {
            outputArea.append(e.getMessage() + "\n");
        }
    }

    void updateTapeVisualization(int headIndex) {
        tapePanel.removeAll();
        tapePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        for (int i = 0; i < Tape.length(); i++) {
            JLabel cell = new JLabel(String.valueOf(Tape.charAt(i)), SwingConstants.CENTER);
            cell.setFont(new Font("Courier New", Font.BOLD, 14));
            cell.setOpaque(true);
            cell.setBackground(i == headIndex ? new Color(255, 193, 7) : new Color(255, 255, 255));
            cell.setForeground(i == headIndex ? Color.WHITE : Color.BLACK);
            cell.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
            cell.setPreferredSize(new Dimension(30, 30));
            tapePanel.add(cell);
        }
        tapePanel.revalidate();
        tapePanel.repaint();
    }
}

// Class 4: FileScanner (Unchanged)
class FileScanner {
    String inputstr;
    Scanner fileScan;

    FileScanner(String filePath) throws FileNotFoundException {
        fileScan = new Scanner(new File(filePath));
        fileScan.useDelimiter("\n");
    }

    String buildTape(String str, char blank) {
        String s = "$";
        for (int i = 0; i < 5; i++)
            s += blank;
        s = s.concat(str);
        for (int i = 0; i < 30; i++)
            s += blank;
        s += '$';
        return s;
    }

    void setTape(Machine m, String input) {
        inputstr = input;
        m.Tape = new StringBuffer(buildTape(inputstr, m.blankSym));
        m.printTape(6);
    }
}

// Class 5: Main (Enhanced GUI)
public class Main {
    private static Machine machine;
    private static FileScanner fileScanner;
    private static JTextArea outputArea;
    private static JPanel tapePanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        // Create main window
        JFrame frame = new JFrame("Turing Machine Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(18, 18, 18)); // Dark theme

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(33, 150, 243)); // Material blue
        headerPanel.setPreferredSize(new Dimension(800, 60));
        JLabel titleLabel = new JLabel("Turing Machine Simulator");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel);

        // Input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(new Color(30, 30, 30)); // Dark gray
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel fileLabel = new JLabel("Transition File Path:");
        fileLabel.setForeground(Color.WHITE);
        fileLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        JTextField fileField = new JTextField("unary.txt", 20);
        fileField.setFont(new Font("Arial", Font.PLAIN, 14));
        fileField.setBackground(new Color(50, 50, 50));
        fileField.setForeground(Color.WHITE);
        fileField.setCaretColor(Color.WHITE);

        JLabel inputLabel = new JLabel("Input String:");
        inputLabel.setForeground(Color.WHITE);
        inputLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        JTextField inputField = new JTextField("01", 20);
        inputField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputField.setBackground(new Color(50, 50, 50));
        inputField.setForeground(Color.WHITE);
        inputField.setCaretColor(Color.WHITE);

        JButton runButton = new JButton("Run Simulation");
        runButton.setBackground(new Color(76, 175, 80)); // Green
        runButton.setForeground(Color.WHITE);
        runButton.setFont(new Font("Arial", Font.BOLD, 14));
        runButton.setFocusPainted(false);
        runButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        runButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                runButton.setBackground(new Color(100, 200, 100)); // Hover effect
            }
            public void mouseExited(MouseEvent e) {
                runButton.setBackground(new Color(76, 175, 80));
            }
        });

        gbc.gridx = 0; gbc.gridy = 0; inputPanel.add(fileLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; inputPanel.add(fileField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; inputPanel.add(inputLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; inputPanel.add(inputField, gbc);
        gbc.gridx = 1; gbc.gridy = 2; inputPanel.add(runButton, gbc);

        // Tape visualization panel
        tapePanel = new JPanel();
        tapePanel.setBackground(new Color(30, 30, 30));
        tapePanel.setPreferredSize(new Dimension(800, 100));

        // Output area
        outputArea = new JTextArea(10, 60);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        outputArea.setBackground(new Color(50, 50, 50));
        outputArea.setForeground(Color.WHITE);
        outputArea.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(30, 30, 30));
        contentPanel.add(tapePanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Add components to frame
        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(contentPanel, BorderLayout.SOUTH);

        // Action listener for run button
        runButton.addActionListener(e -> {
            outputArea.setText("");
            tapePanel.removeAll();
            tapePanel.revalidate();
            tapePanel.repaint();
            String filePath = fileField.getText().trim();
            String input = inputField.getText().trim();

            try {
                fileScanner = new FileScanner(filePath);
                machine = new Machine(outputArea, tapePanel);
                machine.buildMachine(fileScanner.fileScan);
                fileScanner.setTape(machine, input);
                new Thread(() -> {
                    try {
                        machine.runTuring(6);
                    } catch (InterruptedException ex) {
                        outputArea.append(ex.getMessage() + "\n");
                    }
                }).start();
            } catch (FileNotFoundException ex) {
                outputArea.append("ERROR: File not found - " + filePath + "\n");
            }
        });

        frame.setVisible(true);
    }
}