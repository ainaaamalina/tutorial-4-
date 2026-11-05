import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

interface Edible {
    String howToEat();
}

class Animal implements Edible {
    private String name;

    public Animal(String name) {
        this.name = name;
    }

    public String howToEat() {
        return name + " needs to be cooked first before  eating.";
    }
}

abstract class Fruit implements Edible {
    protected String name;

    public Fruit(String name) {
        this.name = name;
    }
}

class Orange extends Fruit {
    public Orange(String name) {
        super(name);
    }

    public String howToEat() {
        return name + " can make orange butter cake.";
    }
}

class Apple extends Fruit {
    public Apple(String name) {
        super(name);
    }

    public String howToEat() {
        return name + " can make apple pie.";
    }
}

class Person {
    private String name;
    private Edible eat;

    public Person(String name) {
        this.name = name;
    }

    public String eating(Edible eat) {
        this.eat = eat;
        return name + " is eating " + eat.howToEat();
    }
}

public class EdibleApp extends JFrame {

    // Food-themed colour palette
    private static final Color HEADER_BG  = new Color(230, 81, 0);     
    private static final Color PANEL_BG   = new Color(255, 243, 224); 
    private static final Color MENU_BG    = new Color(255, 253, 231);  
    private static final Color LOG_BG     = new Color(255, 248, 225);
    private static final Color ADD_COLOR  = new Color(67, 160, 71);   
    private static final Color EAT_COLOR  = new Color(211, 47, 47);    
    private static final Color DEMO_COLOR = new Color(251, 140, 0);    
    private static final Color CLEAR_COLOR = new Color(121, 85, 72);   

    private static class MenuItem {
        final String label;
        final Edible edible;

        MenuItem(String label, Edible edible) {
            this.label = label;
            this.edible = edible;
        }

        public String toString() {
            return label;
        }
    }

    private final Person ali = new Person("Ali");
    private final Person sarah = new Person("Sarah");

    private JComboBox<String> typeCombo, personCombo;
    private JTextField nameField;
    private JCheckBox autoAssignBox;
    private JList<MenuItem> menuList;
    private DefaultListModel<MenuItem> menuModel;
    private JTextArea logArea;
    private JLabel statusLabel;

    public EdibleApp() {
        setTitle("Edible Kitchen");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 540);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));
        getContentPane().setBackground(PANEL_BG);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildLeftPanel(), BorderLayout.WEST);
        add(buildCenterPanel(), BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);

        addToMenu("Chicken", new Animal("Chicken"), "Animal");
        addToMenu("Red Apple", new Apple("Red Apple"), "Apple");
        addToMenu("Navel Orange", new Orange("Navel Orange"), "Orange");
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 12));
        header.setBackground(HEADER_BG);
        JLabel title = new JLabel("Edible Kitchen - Who Eats What?");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        header.add(title);
        return header;
    }

    private JPanel buildLeftPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(PANEL_BG);
        panel.setBorder(new CompoundBorder(
                new EmptyBorder(8, 8, 8, 4),
                new TitledBorder(new LineBorder(ADD_COLOR, 2, true), "Create Food")));
        panel.setPreferredSize(new Dimension(260, 0));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 10, 6, 10);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridx = 0;
        g.weightx = 1;

        g.gridy = 0;
        panel.add(new JLabel("Food type:"), g);
        typeCombo = new JComboBox<>(new String[]{"Animal", "Apple", "Orange"});
        g.gridy = 1;
        panel.add(typeCombo, g);

        g.gridy = 2;
        panel.add(new JLabel("Food name:"), g);
        nameField = new JTextField();
        g.gridy = 3;
        panel.add(nameField, g);

        JButton addBtn = makeButton("Add to Menu", ADD_COLOR);
        addBtn.addActionListener(e -> onAdd());
        g.gridy = 4;
        panel.add(addBtn, g);

        g.gridy = 5;
        g.insets = new Insets(16, 10, 6, 10);
        panel.add(new JSeparator(), g);

        g.insets = new Insets(6, 10, 6, 10);
        g.gridy = 6;
        panel.add(new JLabel("Who is eating?"), g);
        personCombo = new JComboBox<>(new String[]{"Ali", "Sarah"});
        g.gridy = 7;
        panel.add(personCombo, g);

        autoAssignBox = new JCheckBox("Auto-assign (Animal -> Ali, Fruit -> Sarah)", true);
        autoAssignBox.setOpaque(false);
        autoAssignBox.addActionListener(e -> personCombo.setEnabled(!autoAssignBox.isSelected()));
        personCombo.setEnabled(false);
        g.gridy = 8;
        panel.add(autoAssignBox, g);

        g.gridy = 9;
        g.weighty = 1;
        panel.add(Box.createVerticalGlue(), g);

        return panel;
    }

    private JPanel buildCenterPanel() {
        JPanel center = new JPanel(new BorderLayout(8, 8));
        center.setBackground(PANEL_BG);
        center.setBorder(new EmptyBorder(8, 4, 8, 8));

        menuModel = new DefaultListModel<>();
        menuList = new JList<>(menuModel);
        menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menuList.setBackground(MENU_BG);
        menuList.setFont(new Font("SansSerif", Font.PLAIN, 14));
        menuList.setSelectionBackground(DEMO_COLOR);
        menuList.setSelectionForeground(Color.WHITE);
        JScrollPane menuScroll = new JScrollPane(menuList);
        menuScroll.setBorder(new TitledBorder(new LineBorder(DEMO_COLOR, 2, true), "Menu (select a food)"));
        menuScroll.setPreferredSize(new Dimension(0, 140));

        JPanel buttons = new JPanel(new GridLayout(1, 3, 8, 0));
        buttons.setOpaque(false);
        JButton eatBtn = makeButton("Eat Selected", EAT_COLOR);
        JButton demoBtn = makeButton("Run Original Demo", DEMO_COLOR);
        JButton clearBtn = makeButton("Clear Log", CLEAR_COLOR);
        eatBtn.addActionListener(e -> onEat());
        demoBtn.addActionListener(e -> onDemo());
        clearBtn.addActionListener(e -> {
            logArea.setText("");
            statusLabel.setText("Log cleared.");
        });
        buttons.add(eatBtn);
        buttons.add(demoBtn);
        buttons.add(clearBtn);
        
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setFont(new Font("SansSerif", Font.BOLD, 14));
        logArea.setBackground(LOG_BG);
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(new TitledBorder(new LineBorder(EAT_COLOR, 2, true), "Dining Log"));

        JPanel top = new JPanel(new BorderLayout(0, 8));
        top.setOpaque(false);
        top.add(menuScroll, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);

        center.add(top, BorderLayout.NORTH);
        center.add(logScroll, BorderLayout.CENTER);
        return center;
    }

    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bar.setBackground(new Color(255, 224, 178));
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

    private void onAdd() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a food name.",
                    "Missing Name", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String type = (String) typeCombo.getSelectedItem();
        Edible edible;
        if ("Animal".equals(type)) edible = new Animal(name);
        else if ("Apple".equals(type)) edible = new Apple(name);
        else edible = new Orange(name);

        addToMenu(name, edible, type);
        nameField.setText("");
        statusLabel.setText("Added " + name + " (" + type + ") to the menu.");
    }

    private void addToMenu(String name, Edible edible, String type) {
        menuModel.addElement(new MenuItem(name + "  [" + type + "]", edible));
    }

    private Person choosePerson(Edible e) {
        if (autoAssignBox.isSelected()) {
            return (e instanceof Animal) ? ali : sarah;  
        }
        return "Ali".equals(personCombo.getSelectedItem()) ? ali : sarah;
    }

    private void onEat() {
        MenuItem item = menuList.getSelectedValue();
        if (item == null) {
            JOptionPane.showMessageDialog(this, "Please select a food from the menu first.",
                    "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        logArea.append(choosePerson(item.edible).eating(item.edible) + "\n");
        statusLabel.setText("Served " + item.label + ".");
    }

   
    private void onDemo() {
        Edible[] objects = {
            new Animal("Chicken"),
            new Apple("Red Apple"),
            new Orange("Navel Orange")
        };

        Person p1 = new Person("Ali");
        Person p2 = new Person("Sarah");

        for (int i = 0; i < objects.length; i++) {
            if (objects[i] instanceof Animal) {
                logArea.append(p1.eating(objects[i]) + "\n");
            } else {
                logArea.append(p2.eating(objects[i]) + "\n");
            }
        }
        statusLabel.setText("Original demo executed.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EdibleApp().setVisible(true));
    }
}
