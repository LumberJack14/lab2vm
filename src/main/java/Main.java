import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Scanner;

public class Main {
    private JFrame frame;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private PlotPanel plotPanel;
    private SystemPlotPanel systemPlotPanel;

    private double x = 0.0;
    private double y = 0.0;

    private double a = 0.0;
    private double b = 0.5;
    private double e = 0.01;
    private String fileName = "test.txt";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }

    public Main() {
        frame = new JFrame("LAB 2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

        JPanel initialPanel = createInitialPanel();
        cardPanel.add(initialPanel, "initial");

        frame.add(cardPanel);

        cardLayout.show(cardPanel, "initial");

        frame.setVisible(true);
    }



    private JPanel createInitialPanel() {
        JPanel initialPanel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout());

        JButton nonlinearEquationsButton = new JButton("нелинейные уравнения");
        JButton systemsButton = new JButton("системы нелинейных уравнений");

        nonlinearEquationsButton.addActionListener(e -> showEquationSelectionPanel());
        systemsButton.addActionListener(e -> showSystemSelectionPanel());

        topPanel.add(nonlinearEquationsButton);
        topPanel.add(systemsButton);

        initialPanel.add(topPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel(new FlowLayout());

        JButton exitButton = new JButton("EXIT");
        exitButton.addActionListener(e -> System.exit(0));

        bottomPanel.add(exitButton);

        initialPanel.add(bottomPanel, BorderLayout.SOUTH);

        return initialPanel;
    }



    private void showEquationSelectionPanel() {
        JPanel equationPanel = createEquationPanel();
        cardPanel.add(equationPanel, "equationSelection");
        cardLayout.show(cardPanel, "equationSelection");
    }



    private void showSystemSelectionPanel() {
        JPanel systemPanel = createSystemPanel();
        cardPanel.add(systemPanel, "systemSelection");
        cardLayout.show(cardPanel, "systemSelection");
    }



    private JPanel createResultPanel(Result result) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel resultPanel = new JPanel(new GridLayout(0, 1, 0, 1));

        JLabel root = new JLabel("X: " + new DecimalFormat("##.##").format(result.x));
        JLabel f = new JLabel("Y: " + new DecimalFormat("##.##").format(result.f));
        JLabel iter = new JLabel("Произведено итераций: " + new DecimalFormat("##.##").format(result.n));

        Font labelFont = root.getFont();
        root.setFont(new Font(labelFont.getName(), Font.PLAIN, 18));
        f.setFont(new Font(labelFont.getName(), Font.PLAIN, 18));
        iter.setFont(new Font(labelFont.getName(), Font.PLAIN, 18));

        root.setHorizontalAlignment(SwingConstants.CENTER);
        f.setHorizontalAlignment(SwingConstants.CENTER);
        iter.setHorizontalAlignment(SwingConstants.CENTER);

        resultPanel.add(root);
        resultPanel.add(f);
        resultPanel.add(iter);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Вернуться");

        backButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "initial");
            cardPanel.remove(mainPanel);
        });

        buttonPanel.add(backButton);

        mainPanel.add(resultPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        return mainPanel;
    }



    private void updateSelectedSystemPanel(JPanel selectedSystemPanel, String[] equations) {
        selectedSystemPanel.removeAll();
        for (String equation : equations) {
            JLabel equationLabel = new JLabel(equation);
            equationLabel.setFont(new Font("Serif", Font.PLAIN, 18));
            selectedSystemPanel.add(equationLabel);
        }
        selectedSystemPanel.revalidate();
        selectedSystemPanel.repaint();
    }



    private JPanel createSystemPanel() {
        JPanel systemPanel = new JPanel(new BorderLayout());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        JPanel xyePanel = new JPanel();
        xyePanel.setLayout(new BoxLayout(xyePanel, BoxLayout.Y_AXIS));
        JPanel filePanel = new JPanel();
        filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.Y_AXIS));

        JLabel errorLabel = new JLabel("Проверьте корректность данных");
        errorLabel.setForeground(new Color(161, 13, 42));

        JPanel xPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel xLabel = new JLabel("x:");
        JTextField xField = new JTextField(10);
        xField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            private void update() {
                try {
                    x = Double.parseDouble(xField.getText().replace(",", "."));
                    errorLabel.setText("Введенные данные корректны");
                    errorLabel.setForeground(new Color(38, 150, 68));
                } catch (NumberFormatException ex) {
                    errorLabel.setText("Проверьте корректность данных");
                    errorLabel.setForeground(new Color(161, 13, 42));
                }
            }
        });
        xPanel.add(xLabel);
        xPanel.add(xField);

        JPanel yPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel yLabel = new JLabel("y:");
        JTextField yField = new JTextField(10);
        yField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            private void update() {
                try {
                    y = Double.parseDouble(yField.getText().replace(",", "."));
                    errorLabel.setText("Введенные данные корректны");
                    errorLabel.setForeground(new Color(38, 150, 68));
                } catch (NumberFormatException ex) {
                    errorLabel.setText("Проверьте корректность данных");
                    errorLabel.setForeground(new Color(161, 13, 42));
                }
            }
        });
        yPanel.add(yLabel);
        yPanel.add(yField);

        JPanel ePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel eLabel = new JLabel("e:");
        JTextField eField = new JTextField(10);
        eField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateE();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateE();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateE();
            }

            private void updateE() {
                try {
                    e = Double.parseDouble(eField.getText().replace(",", "."));
                    errorLabel.setText("Введенные данные корректны");
                    errorLabel.setForeground(new Color(38, 150, 68));
                } catch (NumberFormatException ex) {
                    errorLabel.setText("Проверьте корректность данных");
                    errorLabel.setForeground(new Color(161, 13, 42));
                }
            }
        });
        ePanel.add(eLabel);
        ePanel.add(eField);

        xyePanel.add(xPanel);
        xyePanel.add(yPanel);
        xyePanel.add(ePanel);

        JPanel fileNamePanel = new JPanel();
        JLabel fileNameLabel = new JLabel("Название файла:");
        JTextField fileNameField = new JTextField(10);
        fileNameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            private void update() {
                try {
                    fileName = fileNameField.getText();
                } catch (NumberFormatException ex) {
                    errorLabel.setText("Проверьте корректность названия");
                    errorLabel.setForeground(new Color(161, 13, 42));
                }
            }
        });
        fileNamePanel.add(fileNameLabel);
        fileNamePanel.add(fileNameField);

        JButton fileButton = new JButton("Из файла ↑");
        fileButton.addActionListener(event -> {
            try {
                FileInputStream fileInputStream = new FileInputStream(fileName);
                Scanner scanner = new Scanner(fileInputStream);

                double x = scanner.nextDouble();
                double y = scanner.nextDouble();
                double e = scanner.nextDouble();

                xField.setText(Double.toString(x));
                yField.setText(Double.toString(y));
                eField.setText(Double.toString(e));

                scanner.close();
                fileInputStream.close();
            } catch (FileNotFoundException ex) {
                errorLabel.setText("Файл не найден");
                errorLabel.setForeground(new Color(161, 13, 42));
            } catch (Exception ex) {
                errorLabel.setText("Ошибка при чтении файла");
                errorLabel.setForeground(new Color(161, 13, 42));
            }
        });

        filePanel.add(fileNamePanel);
        filePanel.add(fileButton);
        filePanel.add(errorLabel);



        JPanel systemSelectionPanel = new JPanel();
        systemSelectionPanel.setLayout(new BoxLayout(systemSelectionPanel, BoxLayout.X_AXIS));
        systemSelectionPanel.setBorder(BorderFactory.createTitledBorder("Выберите систему уравнений"));

        String[] system1 = {
                "x² + y² = 4",
                "y = 3x²"
        };

        String[] system2 = {
                "x² + 2y = 3",
                "y² + 3x = 5"
        };

        JRadioButton system1Button = new JRadioButton("Система 1");
        JRadioButton system2Button = new JRadioButton("Система 2");

        ButtonGroup group = new ButtonGroup();
        group.add(system1Button);
        group.add(system2Button);

        JPanel selectedSystemPanel = new JPanel();
        selectedSystemPanel.setLayout(new BoxLayout(selectedSystemPanel, BoxLayout.Y_AXIS));
        selectedSystemPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        SystemPlotPanel systemPlotPanel = new SystemPlotPanel();
        this.systemPlotPanel = systemPlotPanel;


        system1Button.addActionListener(e -> {
            updateSelectedSystemPanel(selectedSystemPanel, system1);
            systemPlotPanel.setSystem(1);
        });
        system2Button.addActionListener(e -> {
            updateSelectedSystemPanel(selectedSystemPanel, system2);
            systemPlotPanel.setSystem(2);
        });

        systemSelectionPanel.add(system1Button);
        systemSelectionPanel.add(system2Button);
        systemSelectionPanel.add(selectedSystemPanel);
        systemSelectionPanel.add(Box.createVerticalStrut(10));
        systemSelectionPanel.add(systemPlotPanel);

        system2Button.setSelected(true);
        systemPlotPanel.setSystem(2); //idk
        updateSelectedSystemPanel(selectedSystemPanel, system2);


        JPanel methodPanel = new JPanel();
        methodPanel.setLayout(new BoxLayout(methodPanel, BoxLayout.Y_AXIS));
        methodPanel.setBorder(BorderFactory.createTitledBorder("Выберите метод"));

        JRadioButton method1 = new JRadioButton("Метод Ньютона");

        ButtonGroup methodGroup = new ButtonGroup();
        methodGroup.add(method1);

        methodPanel.add(method1);

        method1.setSelected(true);

        bottomPanel.add(methodPanel);
        bottomPanel.add(xyePanel);
        bottomPanel.add(filePanel);

        PlotPanel plotPanel = new PlotPanel();
        this.plotPanel = plotPanel;
        plotPanel.setFunction(1);

        JButton backButton = new JButton("Вернуться");
        backButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "initial");
            cardPanel.remove(systemPanel);
        });

        JButton calculateButton = new JButton("Посчитать");
        calculateButton.addActionListener(event -> {
            if (e <= 0) {
                errorLabel.setText("Проверьте корректность данных (e)");
                errorLabel.setForeground(new Color(161, 13, 42));
                return;
            }
            Algorithm alg = new Algorithm();
            Result result = alg.newtonSystem(x,y,e,systemPlotPanel.getSelectedSystem());
            JPanel resultPanel = createResultPanel(result);
            cardPanel.add(resultPanel, "result");
            cardLayout.show(cardPanel, "result");
            cardPanel.remove(systemPanel);
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(systemSelectionPanel, BorderLayout.CENTER);
        topPanel.add(calculateButton, BorderLayout.EAST);

        systemPanel.add(topPanel, BorderLayout.NORTH);
        systemPanel.add(bottomPanel, BorderLayout.SOUTH);
        systemPanel.add(systemPlotPanel, BorderLayout.CENTER);

        return systemPanel;
    }



    private JPanel createEquationPanel() {
        JPanel functionPanel = new JPanel(new BorderLayout());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        JPanel abePanel = new JPanel();
        abePanel.setLayout(new BoxLayout(abePanel, BoxLayout.Y_AXIS));
        JPanel filePanel = new JPanel();
        filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.Y_AXIS));

        JLabel errorLabel = new JLabel("Проверьте корректность данных");
        errorLabel.setForeground(new Color(161, 13, 42));

        JPanel aPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel aLabel = new JLabel("a:");
        JTextField aField = new JTextField(10);
        aField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateA();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateA();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateA();
            }

            private void updateA() {
                try {
                    a = Double.parseDouble(aField.getText().replace(",", "."));
                    errorLabel.setText("Введенные данные корректны");
                    errorLabel.setForeground(new Color(38, 150, 68));
                } catch (NumberFormatException ex) {
                    errorLabel.setText("Проверьте корректность данных");
                    errorLabel.setForeground(new Color(161, 13, 42));
                }
            }
        });
        aPanel.add(aLabel);
        aPanel.add(aField);

        JPanel bPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel bLabel = new JLabel("b:");
        JTextField bField = new JTextField(10);
        bField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateB();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateB();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateB();
            }

            private void updateB() {
                try {
                    b = Double.parseDouble(bField.getText().replace(",", "."));
                    errorLabel.setText("Введенные данные корректны");
                    errorLabel.setForeground(new Color(38, 150, 68));
                } catch (NumberFormatException ex) {
                    errorLabel.setText("Проверьте корректность данных");
                    errorLabel.setForeground(new Color(161, 13, 42));
                }
            }
        });
        bPanel.add(bLabel);
        bPanel.add(bField);

        JPanel ePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel eLabel = new JLabel("e:");
        JTextField eField = new JTextField(10);
        eField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateE();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateE();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateE();
            }

            private void updateE() {
                try {
                    e = Double.parseDouble(eField.getText().replace(",", "."));
                    errorLabel.setText("Введенные данные корректны");
                    errorLabel.setForeground(new Color(38, 150, 68));
                } catch (NumberFormatException ex) {
                    errorLabel.setText("Проверьте корректность данных");
                    errorLabel.setForeground(new Color(161, 13, 42));
                }
            }
        });
        ePanel.add(eLabel);
        ePanel.add(eField);

        abePanel.add(aPanel);
        abePanel.add(bPanel);
        abePanel.add(ePanel);

        JPanel fileNamePanel = new JPanel();
        JLabel fileNameLabel = new JLabel("Название файла:");
        JTextField fileNameField = new JTextField(10);
        fileNameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            private void update() {
                try {
                    fileName = fileNameField.getText();
                } catch (NumberFormatException ex) {
                    errorLabel.setText("Проверьте корректность названия");
                    errorLabel.setForeground(new Color(161, 13, 42));
                }
            }
        });
        fileNamePanel.add(fileNameLabel);
        fileNamePanel.add(fileNameField);

        JButton fileButton = new JButton("Из файла ↑");
        fileButton.addActionListener(event -> {
            try {
                FileInputStream fileInputStream = new FileInputStream(fileName);
                Scanner scanner = new Scanner(fileInputStream);

                double a = scanner.nextDouble();
                double b = scanner.nextDouble();
                double e = scanner.nextDouble();

                aField.setText(Double.toString(a));
                bField.setText(Double.toString(b));
                eField.setText(Double.toString(e));

                scanner.close();
                fileInputStream.close();
            } catch (FileNotFoundException ex) {
                errorLabel.setText("Файл не найден");
                errorLabel.setForeground(new Color(161, 13, 42));
            } catch (Exception ex) {
                errorLabel.setText("Ошибка при чтении файла");
                errorLabel.setForeground(new Color(161, 13, 42));
            }
        });

        filePanel.add(fileNamePanel);
        filePanel.add(fileButton);
        filePanel.add(errorLabel);

        JPanel equationSelectionPanel = new JPanel();
        equationSelectionPanel.setBorder(BorderFactory.createTitledBorder("Выберите уравнение"));

        JRadioButton func1 = new JRadioButton("2.3x³ + 5.75x² - 7.41x - 10.6");
        JRadioButton func2 = new JRadioButton("sin(x) + 0.25x - 1");
        JRadioButton func3 = new JRadioButton("1.8x³ - x² -5x + 1.539");
        Font funcFont = func1.getFont();
        func1.setFont(new Font(funcFont.getName(), Font.PLAIN, 13));
        func2.setFont(new Font(funcFont.getName(), Font.PLAIN, 13));
        func3.setFont(new Font(funcFont.getName(), Font.PLAIN, 13));


        ButtonGroup functionGroup = new ButtonGroup();
        functionGroup.add(func1);
        functionGroup.add(func2);
        functionGroup.add(func3);

        equationSelectionPanel.add(func1);
        equationSelectionPanel.add(func2);
        equationSelectionPanel.add(func3);

        func1.setSelected(true);

        JPanel methodPanel = new JPanel();
        methodPanel.setLayout(new BoxLayout(methodPanel, BoxLayout.Y_AXIS));
        methodPanel.setBorder(BorderFactory.createTitledBorder("Выберите метод"));

        JRadioButton method1 = new JRadioButton("Метод половинного деления ");
        JRadioButton method2 = new JRadioButton("Метод Ньютона");
        JRadioButton method3 = new JRadioButton("Метод простой итерации");

        ButtonGroup methodGroup = new ButtonGroup();
        methodGroup.add(method1);
        methodGroup.add(method2);
        methodGroup.add(method3);

        methodPanel.add(method1);
        methodPanel.add(method2);
        methodPanel.add(method3);

        method1.setSelected(true);

        bottomPanel.add(methodPanel);
        bottomPanel.add(abePanel);
        bottomPanel.add(filePanel);

        PlotPanel plotPanel = new PlotPanel();
        this.plotPanel = plotPanel;
        plotPanel.setFunction(1);

        func1.addActionListener(e -> plotPanel.setFunction(1));
        func2.addActionListener(e -> plotPanel.setFunction(2));
        func3.addActionListener(e -> plotPanel.setFunction(3));

        method1.addActionListener(e -> plotPanel.setMethod(1));
        method2.addActionListener(e -> plotPanel.setMethod(2));
        method3.addActionListener(e -> plotPanel.setMethod(3));

        JButton backButton = new JButton("Вернуться");
        backButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "initial");
            cardPanel.remove(functionPanel);
        });

        JButton calculateButton = new JButton("Посчитать");
        calculateButton.addActionListener(event -> {
            if (e <= 0) {
                errorLabel.setText("Проверьте корректность данных (e)");
                errorLabel.setForeground(new Color(161, 13, 42));
                return;
            }
            Algorithm alg = new Algorithm();
            try {
                alg.verifySingleRoot(plotPanel.getSelectedFunction(), a, b);
            } catch (SameSignsException e) {
                errorLabel.setText("Найдено 0 или больше 1 корня в промежутке");
                errorLabel.setForeground(new Color(161, 13, 42));
                return;
            } catch (NotMonotonicException e) {
                errorLabel.setText("Пожалуйста, сузьте интервал до монотонного");
                errorLabel.setForeground(new Color(161, 13, 42));
                return;
            }
            Result result = alg.calculate(a, b, e, plotPanel.getSelectedMethod(), plotPanel.getSelectedFunction());
            JPanel resultPanel = createResultPanel(result);
            cardPanel.add(resultPanel, "result");
            cardLayout.show(cardPanel, "result");
            cardPanel.remove(functionPanel);
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(equationSelectionPanel, BorderLayout.CENTER);
        topPanel.add(calculateButton, BorderLayout.EAST);

        functionPanel.add(topPanel, BorderLayout.NORTH);
        functionPanel.add(bottomPanel, BorderLayout.SOUTH);
        functionPanel.add(plotPanel, BorderLayout.CENTER);

        return functionPanel;
    }
}