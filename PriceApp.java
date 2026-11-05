import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;

interface Price {
    void getPrice(Customer c);
}

class Customer {
    private String nameCust;
    private double salary;

    public Customer(String nameCust, double salary) {
        this.nameCust = nameCust;
        this.salary = salary;
    }

    public String getName() {
        return nameCust;
    }

    public double getSalary() {
        return salary;
    }
}

class Product implements Price {
    private String type;
    private Customer buy;

    public Product(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String toString() {
        return "The product is ";
    }

    public void getPrice(Customer c) {
        this.buy = c;
        if (c.getSalary() > 4000) {
            System.out.println(toString());
            System.out.println("The customer's name is " + c.getName() + " with salary RM" + c.getSalary() + " can buy this product.");
            System.out.println("Your salary is higher than the price's product.\n");
        } else {
            System.out.println(toString());
            System.out.println("The customer's name is " + c.getName() + " with salary RM" + c.getSalary() + " cannot buy this product.");
            System.out.println("The product price is more than your salary.\n");
        }
    }
}

class SmartPhone extends Product {
    private String nameSP;
    private double priceSP;

    public SmartPhone(String type, String nameSP, double priceSP) {
        super(type);
        this.nameSP = nameSP;
        this.priceSP = priceSP;
    }

    public String toString() {
        return super.toString() + nameSP + ".";
    }
}

class Laptop extends Product {
    private String nameL;
    private double priceL;

    public Laptop(String type, String nameL, double priceL) {
        super(type);
        this.nameL = nameL;
        this.priceL = priceL;
    }

    public String toString() {
        return super.toString() + nameL + ".";
    }
}

class Car implements Price {
    private String brand;
    private double priceCar;
    private Customer buy;

    public Car(String brand, double priceCar) {
        this.brand = brand;
        this.priceCar = priceCar;
    }

    public String getBrand() {
        return brand;
    }

    public String toString() {
        return "The car brand is " + brand + " with price RM" + priceCar + ".";
    }

    public void getPrice(Customer c) {
        this.buy = c;
        if (c.getSalary() > 8000) {
            System.out.println(toString());
            System.out.println("The customer's name is " + c.getName() + " with salary RM" + c.getSalary() + " can buy this car. Your salary is higher than the price's car.\n");
        } else {
            System.out.println(toString());
            System.out.println("The customer's name is " + c.getName() + " with salary RM" + c.getSalary() + " cannot buy this car. The car price is more than your salary.\n");
        }
    }
}



public class PriceApp extends JFrame {

    
    private static final Color HEADER_BG = new Color(0, 77, 64);     
    private static final Color GOLD      = new Color(255, 193, 7);
    private static final Color PANEL_BG  = new Color(241, 248, 233); 
    private static final Color TEAL      = new Color(0, 150, 136);
    private static final Color GREEN     = new Color(46, 125, 50);
    private static final Color RED       = new Color(198, 40, 40);
    private static final Color AMBER     = new Color(255, 143, 0);
    private static final Color BROWN     = new Color(109, 76, 65);
    private static final Color SCREEN_BG = new Color(255, 253, 231);

    
    private static final String[] CATEGORIES = {"SmartPhone", "Laptop", "Car"};
    private static final String[] DEFAULT_ITEM = {"Iphone 14 ProMax", "Lenovo Yoga", "BMW"};
    private static final double[] DEFAULT_PRICE = {5500.00, 6000.00, 200000.00};

   
    private static final String[] CUSTOMER_PRESETS = {"-- Custom --", "Sally", "Aaron", "Simon"};
    private static final double[] PRESET_SALARY = {0, 3000.00, 8000.00, 10000.00};

    private final DecimalFormat money = new DecimalFormat("#,##0.00");

    // Components
    private JComboBox<String> customerPreset, categoryCombo;
    private JTextField custNameField, itemNameField;
    private JSpinner salarySpinner, priceSpinner;
    private JLabel itemLabel;
    private JLabel resultBanner;
    private JCheckBox clearBeforeBox;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextArea outputArea;
    private JLabel statusLabel;

    public PriceApp() {
        setTitle("Price Checker - Can the Customer Buy?");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(960, 640);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));
        getContentPane().setBackground(PANEL_BG);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildFormPanel(), BorderLayout.WEST);
        add(buildResultsPanel(), BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);

        onCategoryChanged();
    }

   
    private JPanel buildHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 12));
        header.setBackground(HEADER_BG);
        JLabel title = new JLabel("Price Checker - Can the Customer Buy?");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(GOLD);
        header.add(title);
        return header;
    }

    private JPanel buildFormPanel() {
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(PANEL_BG);
        left.setBorder(new EmptyBorder(8, 8, 8, 4));
        left.setPreferredSize(new Dimension(330, 0));

        left.add(buildCustomerSection());
        left.add(Box.createVerticalStrut(8));
        left.add(buildProductSection());
        left.add(Box.createVerticalStrut(8));
        left.add(buildActionSection());
        left.add(Box.createVerticalGlue());
        return left;
    }

    private JPanel buildCustomerSection() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(PANEL_BG);
        p.setBorder(new TitledBorder(new LineBorder(TEAL, 2, true), "1. Customer"));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 230));

        GridBagConstraints g = newConstraints();

        g.gridy = 0;
        p.add(new JLabel("Quick select:"), g);
        customerPreset = new JComboBox<>(CUSTOMER_PRESETS);
        customerPreset.addActionListener(e -> onCustomerPreset());
        g.gridy = 1;
        p.add(customerPreset, g);

        g.gridy = 2;
        p.add(new JLabel("Customer name:"), g);
        custNameField = new JTextField();
        g.gridy = 3;
        p.add(custNameField, g);

        g.gridy = 4;
        p.add(new JLabel("Monthly salary (RM):"), g);
        salarySpinner = new JSpinner(new SpinnerNumberModel(3000.0, 0.0, 1000000.0, 500.0));
        salarySpinner.setEditor(new JSpinner.NumberEditor(salarySpinner, "#,##0.00"));
        g.gridy = 5;
        p.add(salarySpinner, g);

        return p;
    }

    private JPanel buildProductSection() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(PANEL_BG);
        p.setBorder(new TitledBorder(new LineBorder(AMBER, 2, true), "2. Item to Buy"));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 230));

        GridBagConstraints g = newConstraints();

        g.gridy = 0;
        p.add(new JLabel("Category:"), g);
        categoryCombo = new JComboBox<>(CATEGORIES);
        categoryCombo.addActionListener(e -> onCategoryChanged());
        g.gridy = 1;
        p.add(categoryCombo, g);

        itemLabel = new JLabel("Product name:");
        g.gridy = 2;
        p.add(itemLabel, g);
        itemNameField = new JTextField();
        g.gridy = 3;
        p.add(itemNameField, g);

        g.gridy = 4;
        p.add(new JLabel("Price (RM):"), g);
        priceSpinner = new JSpinner(new SpinnerNumberModel(5500.0, 0.0, 10000000.0, 100.0));
        priceSpinner.setEditor(new JSpinner.NumberEditor(priceSpinner, "#,##0.00"));
        g.gridy = 5;
        p.add(priceSpinner, g);

        return p;
    }

    private JPanel buildActionSection() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(PANEL_BG);
        p.setBorder(new TitledBorder(new LineBorder(HEADER_BG, 2, true), "3. Actions"));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 210));

        GridBagConstraints g = newConstraints();

        JButton checkBtn = makeButton("Check Purchase", GREEN);
        checkBtn.addActionListener(e -> onCheck());
        g.gridy = 0;
        p.add(checkBtn, g);

        JButton demoBtn = makeButton("Run Original Demo", TEAL);
        demoBtn.addActionListener(e -> onDemo());
        g.gridy = 1;
        p.add(demoBtn, g);

        JButton clearBtn = makeButton("Clear All", BROWN);
        clearBtn.addActionListener(e -> onClear());
        g.gridy = 2;
        p.add(clearBtn, g);

        clearBeforeBox = new JCheckBox("Clear output before each check");
        clearBeforeBox.setOpaque(false);
        g.gridy = 3;
        p.add(clearBeforeBox, g);

        return p;
    }

    private GridBagConstraints newConstraints() {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 10, 4, 10);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridx = 0;
        g.weightx = 1;
        return g;
    }

    
    private JPanel buildResultsPanel() {
        JPanel right = new JPanel(new BorderLayout(8, 8));
        right.setBackground(PANEL_BG);
        right.setBorder(new EmptyBorder(8, 4, 8, 8));

        
        resultBanner = new JLabel("No check yet - fill in the form and press \"Check Purchase\"", SwingConstants.CENTER);
        resultBanner.setOpaque(true);
        resultBanner.setBackground(new Color(207, 216, 220));
        resultBanner.setForeground(Color.DARK_GRAY);
        resultBanner.setFont(new Font("SansSerif", Font.BOLD, 16));
        resultBanner.setBorder(new EmptyBorder(14, 10, 14, 10));

       
        tableModel = new DefaultTableModel(
                new String[]{"Customer", "Salary (RM)", "Category", "Item", "Price (RM)", "Result"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(24);
        table.setFillsViewportHeight(true);
        table.setSelectionBackground(new Color(178, 223, 219));
        table.setSelectionForeground(Color.BLACK);
        table.getTableHeader().setBackground(HEADER_BG);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getColumnModel().getColumn(5).setCellRenderer(new ResultRenderer());
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(new TitledBorder(new LineBorder(TEAL, 2, true), "Check History"));

        
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        outputArea.setBackground(SCREEN_BG);
        outputArea.setMargin(new Insets(6, 8, 6, 8));
        JScrollPane outScroll = new JScrollPane(outputArea);
        outScroll.setBorder(new TitledBorder(new LineBorder(AMBER, 2, true), "Detailed Output"));

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScroll, outScroll);
        split.setResizeWeight(0.5);
        split.setBorder(null);
        split.setOpaque(false);

        right.add(resultBanner, BorderLayout.NORTH);
        right.add(split, BorderLayout.CENTER);
        return right;
    }

    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bar.setBackground(new Color(200, 230, 201));
        statusLabel = new JLabel("Ready.");
        bar.add(statusLabel);
        return bar;
    }

    private JButton makeButton(String text, Color color) {
        JButton b = new JButton(text);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setOpaque(true);
        b.setBorderPainted(false);
        return b;
    }

    private static class ResultRenderer extends DefaultTableCellRenderer {
        ResultRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object value, boolean sel,
                                                       boolean focus, int row, int col) {
            Component c = super.getTableCellRendererComponent(t, value, sel, focus, row, col);
            if (!sel) {
                boolean can = "CAN BUY".equals(value);
                c.setForeground(can ? new Color(27, 94, 32) : new Color(183, 28, 28));
                c.setFont(c.getFont().deriveFont(Font.BOLD));
            }
            return c;
        }
    }


    private void onCustomerPreset() {
        int i = customerPreset.getSelectedIndex();
        if (i <= 0) return;
        custNameField.setText(CUSTOMER_PRESETS[i]);
        salarySpinner.setValue(PRESET_SALARY[i]);
    }

    private void onCategoryChanged() {
        int i = categoryCombo.getSelectedIndex();
        itemLabel.setText("Car".equals(CATEGORIES[i]) ? "Car brand:" : "Product name:");
        itemNameField.setText(DEFAULT_ITEM[i]);
        priceSpinner.setValue(DEFAULT_PRICE[i]);
    }

    private void onCheck() {
        String custName = custNameField.getText().trim();
        String itemName = itemNameField.getText().trim();
        if (custName.isEmpty()) {
            warn("Please enter the customer's name.", "Missing Name");
            return;
        }
        if (itemName.isEmpty()) {
            warn("Please enter the item name.", "Missing Item");
            return;
        }

        double salary = ((Number) salarySpinner.getValue()).doubleValue();
        double price = ((Number) priceSpinner.getValue()).doubleValue();
        String category = (String) categoryCombo.getSelectedItem();

        Customer customer = new Customer(custName, salary);
        Price item;
        if ("SmartPhone".equals(category)) {
            item = new SmartPhone("SmartPhone", itemName, price);
        } else if ("Laptop".equals(category)) {
            item = new Laptop("Laptop", itemName, price);
        } else {
            item = new Car(itemName, price);
        }

        String output = capture(() -> item.getPrice(customer));
        boolean canBuy = !output.contains("cannot buy");

        if (clearBeforeBox.isSelected()) outputArea.setText("");
        outputArea.append(output);
        outputArea.setCaretPosition(outputArea.getDocument().getLength());

        tableModel.addRow(new Object[]{custName, money.format(salary), category, itemName,
                money.format(price), canBuy ? "CAN BUY" : "CANNOT BUY"});

        if (canBuy) {
            setBanner(custName + " CAN buy the " + itemName + ".", new Color(200, 230, 201), new Color(27, 94, 32));
        } else {
            setBanner(custName + " CANNOT buy the " + itemName + ".", new Color(255, 205, 210), new Color(183, 28, 28));
        }
        statusLabel.setText("Checked " + custName + " for " + itemName + ".");
    }


    private void onDemo() {
        String result = capture(() -> {
            SmartPhone sp = new SmartPhone("SmartPhone", "Iphone 14 ProMax", 5500.00);
            Laptop lp = new Laptop("Laptop", "Lenovo Yoga", 6000.00);
            Car c = new Car("BMW", 200000);

            Customer c1 = new Customer("Sally", 3000.00);
            Customer c2 = new Customer("Aaron", 8000.00);
            Customer c3 = new Customer("Simon", 10000.00);

            sp.getPrice(c1);
            lp.getPrice(c2);
            c.getPrice(c3);
        });
        outputArea.append(result);
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
        statusLabel.setText("Original demo executed.");
    }

    private void onClear() {
        outputArea.setText("");
        tableModel.setRowCount(0);
        custNameField.setText("");
        customerPreset.setSelectedIndex(0);
        salarySpinner.setValue(3000.0);
        categoryCombo.setSelectedIndex(0);
        onCategoryChanged();
        setBanner("No check yet - fill in the form and press \"Check Purchase\"",
                new Color(207, 216, 220), Color.DARK_GRAY);
        statusLabel.setText("Cleared.");
    }

    private void setBanner(String text, Color bg, Color fg) {
        resultBanner.setText(text);
        resultBanner.setBackground(bg);
        resultBanner.setForeground(fg);
    }

    private void warn(String msg, String title) {
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
    }

   
    private String capture(Runnable r) {
        PrintStream old = System.out;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bos, true));
        try {
            r.run();
        } finally {
            System.setOut(old);
        }
        return bos.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PriceApp().setVisible(true));
    }
}
