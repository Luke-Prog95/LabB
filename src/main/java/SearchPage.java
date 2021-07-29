import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.Scanner;

public class SearchPage extends JFrame {
    private JTextField nomeText;
    private JTextField comuneText;
    private JTextField tipoText;
    private JList list1;
    private JPanel panel3;
    private JLabel back;
    private JButton cercaButton;
    private JFrame frame;
    private Scanner scan;
    private ImageIcon icon;

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

       /* back.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                back.setBackground(back.getForeground());
                back.setForeground(Color.BLUE);
            }
            public void mouseExited(MouseEvent evt) {
                Color c = back.getBackground();
                back.setBackground(back.getForeground());
                back.setForeground(c);
            }
        });*/

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
