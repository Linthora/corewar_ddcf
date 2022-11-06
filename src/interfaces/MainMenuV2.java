package interfaces;

import javax.swing.*;

import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

public class MainMenuV2 extends JFrame {
    public ArrayList<String> warriors = new ArrayList<>();
    public boolean changeNum = true, play = false;
    public int max = 4;

    public MainMenuV2() {
        super("CoreWar Menu");
        JFrame frame = new JFrame("Corewar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(false);
        frame.setSize(952,885);
        WindowListener l = new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        };
        this.addWindowListener(l);
        JTextField field = new JTextField(40);
        field.setText("progs/custom.red\n\nhello");
        JButton button1 = new JButton("Ajouter");
        JButton button3 = new JButton("Jouer");
        JButton button4 = new JButton("Quitter");
        // JLabel corewar = new JLabel("CoreWar", JLabel.CENTER);
        JLabel label = new JLabel("Donnez le chemin vers votre programme en RedCode (au moins 2)", JLabel.CENTER);
        // JLabel label2 = new JLabel("CoreWar");
        JPanel panel1 = new JPanel();
        // panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
        // panel1.add(label2);
        // p1.add(p2);
        JPanel panel2 = new JPanel();
        ActionMan action = new ActionMan(warriors, field, max);
        button1.addActionListener(action);
       
        button3.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenuV2.this.dispose();
                synchronized(MainMenuV2.this) {
                    MainMenuV2.this.notify();
                }
                
            }
            
        });
        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenuV2.this.dispose();
                frame.dispose();
                System.exit(0);
            }
        });
        panel1.add(label);
        panel2.add(button1);
        panel1.add(panel2);
        panel1.add(button3);
        panel1.add(button4);
        this.add(panel1);
        this.setSize(500,100);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        try {
            synchronized(this) {
                wait();
            }
            try {
                CorewarWindow.main(MainMenuV2.this.warriors.toArray(new String[MainMenuV2.this.warriors.size()]));
            } catch (IOException | InterruptedException e1) {
                e1.printStackTrace();
            }
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException
    {
        new MainMenuV2();
    }
}
