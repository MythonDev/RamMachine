package eu.michalbugno.ram;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class UiController {
    private static final String TITLE = "RAM Machine Alpha 0.0.0";
    private static final Dimension TEXT_AREA_DIMENSION = new Dimension(15, 20);
    private JFrame mainFrame = new JFrame(TITLE);
    private JTextArea textArea = new JTextArea(TEXT_AREA_DIMENSION.height, TEXT_AREA_DIMENSION.width);
    private JButton startButton, nextButton, showMemory;
    private JTextField input, output;

    private static final UiController INSTANCE = new UiController();


    public static UiController getInstance(){
        return INSTANCE;
    }

    public List<String> getCode(){
        String text = textArea.getText();
        String[] lines = text.split("\n");
        return Arrays.asList(lines);
    }

    public String getInput(){
        return input.toString();
    }

    public void showOutput(String result){
        output.setText(result);
    }

    public void setStartAction(Runnable task){
        startButton.addActionListener(event -> {
            task.run();
        });
    }

    public void setNextStepAction(Runnable task){
        nextButton.addActionListener(event -> {
            task.run();
        });
    }

    public void showMessage(String message){
        JOptionPane.showMessageDialog(mainFrame, message);
    }

    private UiController(){
        EventQueue.invokeLater(()->{
            Dimension size = getScreenSize();
            mainFrame.setSize(size.width/2, size.height/2);
            mainFrame.setContentPane(getMainPanel());
            mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            mainFrame.setVisible(true);
        });

    }

    private JPanel getMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.add(getTextAreaScrollPane(), BorderLayout.CENTER);
        mainPanel.add(getButtonsPanel(), BorderLayout.EAST);
        mainPanel.add(getTextFieldsPanel(), BorderLayout.SOUTH);
        return mainPanel;
    }

    private JComponent getTextAreaScrollPane(){
        JScrollPane scrollPane = new JScrollPane(textArea);
        return scrollPane;
    }

    private JPanel getButtonsPanel(){
        JPanel buttons = new JPanel();
        startButton = new JButton("START");
        nextButton = new JButton("NEXT");
        showMemory = new JButton("SHOW MEMORY STATE");
        buttons.add(startButton);
        buttons.add(nextButton);
        buttons.add(showMemory);
        return buttons;
    }

    private JPanel getTextFieldsPanel(){
        JPanel textFieldsPanel = new JPanel();
        input = new JTextField(TEXT_AREA_DIMENSION.width);
        output = new JTextField(TEXT_AREA_DIMENSION.width);
        output.setEditable(false);
        textFieldsPanel.add(input);
        textFieldsPanel.add(output);
        return textFieldsPanel;
    }

    private static Dimension getScreenSize(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        return toolkit.getScreenSize();
    }

    //----------------------------
    public static void main(String[] args) {
        var instance = UiController.getInstance();
        instance.showMessage("Hello");
    }
}
