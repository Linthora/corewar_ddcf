package interfaces;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;

import java.nio.file.Path;
import java.nio.file.Paths;

import corewar.Interpreter;

public class CorewarWindow extends JPanel {
    final int SQUARE_SIZE = 25;
    final int WRAP_SIZE = 50;
    Interpreter interpreter;
    Map<Integer, Color> colors;

    public CorewarWindow(Interpreter interpreter) {
        this.interpreter = interpreter;
        Random random = new Random();
        colors = new HashMap<>(interpreter.heads.size());
        for (Integer team: interpreter.heads.keySet()) {
            colors.put(team, new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (int i=0; i<interpreter.memory.tab.length; ++i) {
            if (interpreter.memory.tab[i].team == -1) {
                g.setColor(Color.WHITE);
            } else {
                g.setColor(colors.get(interpreter.memory.tab[i].team));
            }
            g.fillRect((i%WRAP_SIZE)*SQUARE_SIZE, (i/WRAP_SIZE)*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
        }
        g.setColor(Color.RED);
        for (Integer i: interpreter.heads.keySet()) {
            try {
                Integer p = interpreter.heads.get(i).getFirst();
                g.fillRect((p%WRAP_SIZE)*SQUARE_SIZE, (p/WRAP_SIZE)*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            } catch (NoSuchElementException e) {}
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(SQUARE_SIZE*WRAP_SIZE, (SQUARE_SIZE+1)*interpreter.memory.tab.length/WRAP_SIZE+100);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        CorewarWindow corewar = new CorewarWindow(new Interpreter(args));
        JFrame frame = new JFrame("CoreWar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(corewar);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        // JPanel names = new JPanel(); //test d'ajout des noms en dessous du jeu mais ca marche pas :'(
        // JLabel[] label = new JLabel[corewar.interpreter.programNames.size()];
        // Box box2 = Box.createVerticalBox();
        // for(int i = 0; i < corewar.interpreter.programNames.size(); i++)
        // {
        //     label[i] = new JLabel(corewar.interpreter.programNames.get(i));
        //     box2.add(label[i]);
        //     // label[i].setForeground(corewar.colors.get(i));
        // }
        // names.add(box2);
        // frame.add(names);
        while(!corewar.interpreter.isOver()) {
            corewar.interpreter.playNextTour();
            corewar.repaint();
            Thread.sleep(25);
        }
        JFrame popup = new JFrame("Game Over");
        popup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        popup.setLocationRelativeTo(frame);
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        Set<Integer> winners = corewar.interpreter.getWinners();
        if (winners.size() == 0) {
            panel.add(new JLabel("No winners"));
        } else {
            panel.add(new JLabel("Winners:"));
            Box box = Box.createVerticalBox();
            for (Integer t: winners) {
                Path path = Paths.get(corewar.interpreter.programNames.get(t));
                box.add(new JLabel(path.getFileName().toString()));
            }
            panel.add(box);
        }
        popup.add(panel);
        popup.pack();
        popup.setVisible(true);
    }
}
