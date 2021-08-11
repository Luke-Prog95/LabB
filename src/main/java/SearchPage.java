import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.Scanner;

public class SearchPage extends JFrame {
    private JPanel panel3;
    private JButton cercaButton;
    private JTextArea textArea1;
    private JList list1;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JFrame frame;
    private Scanner scan;
    private ImageIcon icon;
    private JButton back;

    public SearchPage() {
        icon = new ImageIcon(this.getClass().getResource("/back.png"));
        frame = new JFrame("Search");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500, 450));
        frame.setResizable(false);

        frame.add(panel3);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        back.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.setVisible(false);
                new LoginPage();
            }
        });

        cercaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              /*  serverCV s = new serverCV();
                if (nomeText.getText().trim().isEmpty() && comuneText.getText().trim().isEmpty() && tipoText.getText().trim().isEmpty())
                    JOptionPane.showMessageDialog(cercaButton,"Nessun criterio inserito");
                else if (!nomeText.getText().trim().isEmpty() && comuneText.getText().trim().isEmpty() && tipoText.getText().trim().isEmpty())
                    s.cercaCentroVaccinale(nomeText.getText().trim());
                else if (nomeText.getText().trim().isEmpty() && !comuneText.getText().trim().isEmpty() && !tipoText.getText().trim().isEmpty())
                    s.cercaCentroVaccinale(comuneText.getText().trim(),tipoText.getText().trim());
                else JOptionPane.showMessageDialog(cercaButton,"Criteri non adatti");*/
            }
        });
    }


}
