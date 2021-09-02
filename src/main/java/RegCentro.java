import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.sql.SQLException;

public class RegCentro {
    private JFrame cenFrame;
    private JTextField nomeTextField;
    private JTextField comTextField;
    private JTextField qualTextField;
    private JTextField indTextField;
    private JTextField ncTextField;
    private JTextField provTextField;
    private JTextField capTextField;
    private JRadioButton hubRadioButton;
    private JRadioButton aziendaleRadioButton;
    private JRadioButton ospedalieroRadioButton;
    private ButtonGroup buttonGroup;
    private JButton registraButton;
    private JPanel panel;
    private serverCV s;
    private String tipo="";

    public RegCentro() throws RemoteException {
        cenFrame = new JFrame("RegCentro");
        cenFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        cenFrame.setPreferredSize(new Dimension(500,450));
        cenFrame.setResizable(false);
        cenFrame.add(panel);
        cenFrame.pack();
        cenFrame.setLocationRelativeTo(null);
        cenFrame.setVisible(true);
        s = new serverCV();
        buttonGroup = new ButtonGroup();
        buttonGroup.add(hubRadioButton);
        buttonGroup.add(aziendaleRadioButton);
        buttonGroup.add(ospedalieroRadioButton);

        registraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int cap = Integer.parseInt(capTextField.getText());
                if (hubRadioButton.isSelected())
                    tipo = hubRadioButton.getText();
                else if (aziendaleRadioButton.isSelected())
                    tipo = aziendaleRadioButton.getText();
                else
                    tipo = ospedalieroRadioButton.getText();
                try {
                    s.registraCentroVaccinale(nomeTextField.getText(),comTextField.getText(),qualTextField.getText(),ncTextField.getText(),provTextField.getText(),indTextField.getText(),tipo,cap);
                    JOptionPane.showMessageDialog(registraButton,"Centro registrato");
                } catch (RemoteException | SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) throws RemoteException {
        new RegCentro();
    }
}

