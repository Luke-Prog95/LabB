import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.*;

public class LoggedPage extends JFrame {
    private JFrame frame3;
    private JPanel panel4;
    private JCheckBox malDiTestaCheckBox;
    private JCheckBox febbreCheckBox;
    private JCheckBox doloriMuscolariCheckBox;
    private JCheckBox linfoadenopatiaCheckBox;
    private JCheckBox tachicardiaCheckBox;
    private JCheckBox crisiIpertensivaCheckBox;
    private JButton confermaButton;
    private JTextField textField1;
    private JTextField textField2;
    private JSlider slider1;
    private JSlider slider2;
    private JSlider slider3;
    private JSlider slider4;
    private JSlider slider5;
    private JSlider slider6;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;

    public LoggedPage() {
        frame3 = new JFrame("Login");
        frame3.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame3.setPreferredSize(new Dimension(550, 450));
        frame3.setResizable(false);
        frame3.add(panel4);
        frame3.pack();
        frame3.setLocationRelativeTo(null);
        frame3.setVisible(true);
        textField1.setEditable(false);
        textField2.setEditable(false);
        textField3.setEditable(false);
        textField4.setEditable(false);
        textField5.setEditable(false);
        textField6.setEditable(false);
        slider1.setEnabled(false);
        slider2.setEnabled(false);
        slider3.setEnabled(false);
        slider4.setEnabled(false);
        slider5.setEnabled(false);
        slider6.setEnabled(false);

        malDiTestaCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    slider1.setEnabled(true);
                    textField1.setEditable(true);
                } else {
                    slider1.setEnabled(false);
                    textField1.setEditable(false);
                };
            }
        });

        febbreCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    slider2.setEnabled(true);
                    textField2.setEditable(true);
                } else {
                    slider2.setEnabled(false);
                    textField2.setEditable(false);
                };
            }
        });

        doloriMuscolariCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    slider3.setEnabled(true);
                    textField3.setEditable(true);
                } else {
                    slider3.setEnabled(false);
                    textField3.setEditable(false);
                };
            }
        });

        linfoadenopatiaCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    slider4.setEnabled(true);
                    textField4.setEditable(true);
                } else {
                    slider4.setEnabled(false);
                    textField4.setEditable(false);
                };
            }
        });

        tachicardiaCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    slider5.setEnabled(true);
                    textField5.setEditable(true);
                } else {
                    slider5.setEnabled(false);
                    textField5.setEditable(false);
                };
            }
        });

        crisiIpertensivaCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    slider6.setEnabled(true);
                    textField6.setEditable(true);
                } else {
                    slider6.setEnabled(false);
                    textField6.setEditable(false);
                };
            }
        });
    }
}
