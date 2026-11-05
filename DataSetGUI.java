import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

interface Measurable {
    double getMeasure();
    String getName();
}

class BankAccount implements Measurable {

    private int accNumber;
    private double balance;
    private static int LastAssignedNumber = 1000;

    public BankAccount() {
        LastAssignedNumber++;
        accNumber = LastAssignedNumber;
        balance = 0;
    }

    public BankAccount(double balance) {
        LastAssignedNumber++;
        accNumber = LastAssignedNumber;
        this.balance = balance;
    }

    public int getAcccountNumber() {
        return accNumber;
    }

    public double getMeasure() {
        return balance;
    }

    public String getName() {
        return "Account #" + accNumber;
    }
}

class Coin implements Measurable {

    private double value;
    private String name;

    public Coin(double value, String name) {
        this.value = value;
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public double getMeasure() {
        return value;
    }

    public String getName() {
        return name;
    }
}

class DataSet {

    private double sum;
    private Measurable max;
    private int count;

    public DataSet() {
        sum = 0;
        count = 0;
        max = null;
    }

    public void add(Measurable x) {
        sum += x.getMeasure();

        if (count == 0 || x.getMeasure() > max.getMeasure()) {
            max = x;
        }

        count++;
    }

    public Measurable getMax() {
        return max;
    }

    public double getAverage() {
        if (count == 0)
            return 0;

        return sum / count;
    }
}

class Student implements Measurable {

    private String name;
    private double height;

    public Student(String name, double height) {
        this.name = name;
        this.height = height;
    }

    public double getHeight() {
        return height;
    }

    public double getMeasure() {
        return height;
    }

    public String getName() {
        return name;
    }
}



public class DataSetGUI extends JFrame {

    private static final Color HEADER_BG   = new Color(63, 81, 181);
    private static final Color INPUT_BG    = new Color(232, 234, 246);
    private static final Color RESULT_BG   = new Color(255, 248, 225);
    private static final Color ADD_COLOR   = new Color(76, 175, 80);
    private static final Color CALC_COLOR  = new Color(33, 150, 243);
    private static final Color CLEAR_COLOR = new Color(244, 67, 54);

    private static final String BANK = "Bank Account";
    private static final String COIN = "Coin";
    private static final String STUDENT = "Student";

    private final DecimalFormat df = new DecimalFormat("0.00");

    
    private DataSet bankData = new DataSet();
    private DataSet coinData = new DataSet();
    private DataSet studentData = new DataSet();
    private final List<String> bankItems = new ArrayList<>();
    private final List<String> coinItems = new ArrayList<>();
    private final List<String> studentItems = new ArrayList<>();

   
    private JComboBox<String> typeCombo;
    private JLabel nameLabel, valueLabel;
    private JTextField nameField, valueField;
    private JCheckBox twoDecimalsBox;
    private JRadioButton showAllRadio, showCurrentRadio;
    private JTextArea itemsArea, resultArea;
    private JLabel statusLabel;

    public DataSetGUI() {
        setTitle("DataSet Analyzer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(780, 560);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));
        getContentPane().setBackground(Color.WHITE);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildInputPanel(), BorderLayout.WEST);
        add(buildCenterPanel(), BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);

        updateFieldsForType();
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 12));
        header.setBackground(HEADER_BG);
        JLabel title = new JLabel("DataSet Analyzer - Average & Maximum");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        header.add(title);
        return header;
    }

    private JPanel buildInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(INPUT_BG);
        panel.setBorder(new CompoundBorder(
                new EmptyBorder(8, 8, 8, 4),
                new TitledBorder(new LineBorder(HEADER_BG, 2, true), "Add Item")));
        panel.setPreferredSize(new Dimension(270, 0));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 10, 6, 10);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridx = 0;
        g.weightx = 1;

        g.gridy = 0;
        panel.add(new JLabel("Category:"), g);
        typeCombo = new JComboBox<>(new String[]{BANK, COIN, STUDENT});
        typeCombo.addActionListener(e -> updateFieldsForType());
        g.gridy = 1;
        panel.add(typeCombo, g);

        nameLabel = new JLabel("Name:");
        g.gridy = 2;
        panel.add(nameLabel, g);
        nameField = new JTextField();
        g.gridy = 3;
        panel.add(nameField, g);

        valueLabel = new JLabel("Value:");
        g.gridy = 4;
        panel.add(valueLabel, g);
        valueField = new JTextField();
        g.gridy = 5;
        panel.add(valueField, g);

        twoDecimalsBox = new JCheckBox("Format results to 2 decimals", true);
        twoDecimalsBox.setOpaque(false);
        g.gridy = 6;
        panel.add(twoDecimalsBox, g);

        showCurrentRadio = new JRadioButton("Results: selected category", true);
        showAllRadio = new JRadioButton("Results: all categories");
        showCurrentRadio.setOpaque(false);
        showAllRadio.setOpaque(false);
        ButtonGroup group = new ButtonGroup();
        group.add(showCurrentRadio);
        group.add(showAllRadio);
        g.gridy = 7;
        panel.add(showCurrentRadio, g);
        g.gridy = 8;
        panel.add(showAllRadio, g);

        JPanel buttons = new JPanel(new GridLayout(1, 3, 6, 0));
        buttons.setOpaque(false);
        JButton addBtn = makeButton("Add", ADD_COLOR);
        JButton calcBtn = makeButton("Calculate", CALC_COLOR);
        JButton clearBtn = makeButton("Clear", CLEAR_COLOR);
        addBtn.addActionListener(e -> addItem());
        calcBtn.addActionListener(e -> calculate());
        clearBtn.addActionListener(e -> clearAll());
        buttons.add(addBtn);
        buttons.add(calcBtn);
        buttons.add(clearBtn);
        g.gridy = 9;
        g.insets = new Insets(14, 10, 6, 10);
        panel.add(buttons, g);

        g.gridy = 10;
        g.weighty = 1;
        panel.add(Box.createVerticalGlue(), g);

        return panel;
    }

    private JPanel buildCenterPanel() {
        JPanel center = new JPanel(new GridLayout(2, 1, 8, 8));
        center.setBorder(new EmptyBorder(8, 4, 8, 8));
        center.setBackground(Color.WHITE);

        itemsArea = new JTextArea();
        itemsArea.setEditable(false);
        itemsArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        itemsArea.setBackground(new Color(227, 242, 253));
        JScrollPane itemsScroll = new JScrollPane(itemsArea);
        itemsScroll.setBorder(new TitledBorder(new LineBorder(CALC_COLOR, 2, true), "Items Added"));

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.BOLD, 13));
        resultArea.setBackground(RESULT_BG);
        JScrollPane resultScroll = new JScrollPane(resultArea);
        resultScroll.setBorder(new TitledBorder(new LineBorder(new Color(255, 160, 0), 2, true), "Results"));

        center.add(itemsScroll);
        center.add(resultScroll);
        return center;
    }

    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bar.setBackground(new Color(224, 224, 224));
        statusLabel = new JLabel("Ready.");
        bar.add(statusLabel);
        return bar;
    }

    private JButton makeButton(String text, Color color) {
        JButton b = new JButton(text);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setOpaque(true);
        b.setBorderPainted(false);
        return b;
    }

    private void updateFieldsForType() {
        String type = (String) typeCombo.getSelectedItem();
        nameField.setText("");
        valueField.setText("");
        if (BANK.equals(type)) {
            nameLabel.setText("Name: (auto-assigned)");
            nameField.setEnabled(false);
            valueLabel.setText("Balance:");
        } else if (COIN.equals(type)) {
            nameLabel.setText("Coin name:");
            nameField.setEnabled(true);
            valueLabel.setText("Coin value:");
        } else {
            nameLabel.setText("Student name:");
            nameField.setEnabled(true);
            valueLabel.setText("Height (cm):");
        }
    }

    private void addItem() {
        String type = (String) typeCombo.getSelectedItem();
        double value;
        try {
            value = Double.parseDouble(valueField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for the value.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String name = nameField.getText().trim();

        if (BANK.equals(type)) {
            BankAccount acc = new BankAccount(value);
            bankData.add(acc);
            bankItems.add(acc.getName() + "  |  Balance: " + df.format(acc.getMeasure()));
        } else if (COIN.equals(type)) {
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a coin name.",
                        "Missing Name", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Coin c = new Coin(value, name);
            coinData.add(c);
            coinItems.add(c.getName() + "  |  Value: " + df.format(c.getMeasure()));
        } else {
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a student name.",
                        "Missing Name", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Student s = new Student(name, value);
            studentData.add(s);
            studentItems.add(s.getName() + "  |  Height: " + df.format(s.getMeasure()));
        }

        nameField.setText("");
        valueField.setText("");
        refreshItems();
        statusLabel.setText("Added a " + type + " item.");
    }

    private void refreshItems() {
        StringBuilder sb = new StringBuilder();
        appendSection(sb, "BANK ACCOUNTS", bankItems);
        appendSection(sb, "COINS", coinItems);
        appendSection(sb, "STUDENTS", studentItems);
        itemsArea.setText(sb.toString());
    }

    private void appendSection(StringBuilder sb, String title, List<String> items) {
        sb.append("== ").append(title).append(" ==\n");
        if (items.isEmpty()) {
            sb.append("  (none)\n");
        } else {
            for (String s : items) sb.append("  ").append(s).append("\n");
        }
        sb.append("\n");
    }

    private void calculate() {
        StringBuilder sb = new StringBuilder();
        if (showAllRadio.isSelected()) {
            describe(sb, BANK, bankData, "Balance");
            describe(sb, COIN, coinData, "Value");
            describe(sb, STUDENT, studentData, "Height");
        } else {
            String type = (String) typeCombo.getSelectedItem();
            if (BANK.equals(type)) describe(sb, BANK, bankData, "Balance");
            else if (COIN.equals(type)) describe(sb, COIN, coinData, "Value");
            else describe(sb, STUDENT, studentData, "Height");
        }
        resultArea.setText(sb.toString());
        statusLabel.setText("Calculation complete.");
    }

    private void describe(StringBuilder sb, String title, DataSet ds, String measureName) {
        sb.append("== ").append(title.toUpperCase()).append(" ==\n");
        Measurable max = ds.getMax();
        if (max == null) {
            sb.append("  No data yet.\n\n");
            return;
        }
        sb.append("  Average ").append(measureName).append(": ").append(fmt(ds.getAverage())).append("\n");
        sb.append("  Highest ").append(measureName).append(": ").append(fmt(max.getMeasure())).append("\n");
        sb.append("  Highest Name: ").append(max.getName()).append("\n\n");
    }

    private String fmt(double v) {
        return twoDecimalsBox.isSelected() ? df.format(v) : String.valueOf(v);
    }

    private void clearAll() {
        int choice = JOptionPane.showConfirmDialog(this, "Clear all data?", "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) return;
        bankData = new DataSet();
        coinData = new DataSet();
        studentData = new DataSet();
        bankItems.clear();
        coinItems.clear();
        studentItems.clear();
        itemsArea.setText("");
        resultArea.setText("");
        nameField.setText("");
        valueField.setText("");
        statusLabel.setText("All data cleared.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DataSetGUI().setVisible(true));
    }
}
