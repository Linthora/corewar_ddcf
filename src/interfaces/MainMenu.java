package interfaces;

import javax.swing.*;

import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

public class MainMenu extends JFrame {
    public ArrayList<String> warriors = new ArrayList<>();
    public boolean changeNum = true, play = false;
    public int max = 4;

    public MainMenu() {
        super("CoreWar Menu");
        JFrame frame = new JFrame("Corewar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(false);
        frame.setSize(952,885);
        // JLabel instruct = new JLabel("Entrez d'abord le nombre de programmes à faire combattre.");
        // JLabel label1 = new JLabel("Nombre de programmes : ");
        // JTextField nbWarrior = new JTextField(40);
        // JButton button = new JButton("Valider");
        // button.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent event) {
        //         if(changeNum) {
        //             if(nbWarrior.getText().length() > 0) {
        //                 max = Integer.valueOf(nbWarrior.getText());
        //                 changeNum = false;
        //             }
        //         }
        //         else {
        //             nbWarrior.setText("Nombre de programmes deja indiqué.");
        //         }
        //     }
        // });
        WindowListener l = new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        };
        this.addWindowListener(l);
        JTextField field = new JTextField(40);
        field.setText("progs/custom.red");
        JButton button1 = new JButton("Valider");
        JButton button2 = new JButton("Vider le champs");
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
        ActionListenerCorewar action = new ActionListenerCorewar(warriors, field, max);
        button1.addActionListener(action);
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                field.setText(null);
            }
        });
        button3.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenu.this.dispose();
                synchronized(MainMenu.this) {
                    MainMenu.this.notify();
                }
                // play = true;
                // MainMenu.this.dispose();
                // frame.setVisible(true);
                // try {
                //     CorewarWindow corewarWindow = new CorewarWindow(new Interpreter(warriors.toArray(new String[warriors.size()])));
                //     frame.getContentPane().add(corewarWindow);
                //     while(!corewarWindow.interpreter.isOver()) {
                //         corewarWindow.interpreter.playNextTour();
                //         corewarWindow.repaint();
                //         Thread.sleep(10);
                //     }
                // } catch (Exception e1) {
                //     e1.printStackTrace();
                // }
                
            }
            
        });
        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenu.this.dispose();
                frame.dispose();
            }
        });
        // panel1.add(corewar);
        // panel1.add(instruct);
        // panel1.add(label1);
        // panel1.add(nbWarrior);
        // panel1.add(button);
        panel1.add(label);
        panel1.add(field);
        panel2.add(button1);
        panel2.add(button2);
        panel1.add(panel2);
        panel1.add(button3);
        panel1.add(button4);
        this.add(panel1);
        this.setSize(450,250);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        try {
            synchronized(this) {
                wait();
            }
            try {
                CorewarWindow.main(MainMenu.this.warriors.toArray(new String[MainMenu.this.warriors.size()]));
            } catch (IOException | InterruptedException e1) {
                e1.printStackTrace();
            }
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException
    {
        new MainMenu();
    }
}
