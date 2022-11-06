package interfaces;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

public class ActionListenerCorewar implements ActionListener{
    public ArrayList<String> warriors;
    public JTextField field;
    public int max;

    public ActionListenerCorewar(ArrayList<String> warriors, JTextField field, int max) {
        this.warriors = warriors;
        this.field = field;
        this.max = max;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // if(warriors == null)
        // {
        //     field.setText("Erreur : pas de nombre de programmes donné !!");
        // }
        if(warriors.size() < max) {
            File file = new File(field.getText());
            if(file.isFile()) {
                warriors.add(field.getText()); 
                field.setText(warriors.size() + " programme(s) déjà ajouté(s). (supprimez ce texte avant d'écrire)");
            }
            else {
                field.setText("Ce fichier n'existe pas.");
            }
        }
        else {
            field.setText("Nombre limite de programmes atteind");
        }
        
    }
    
}
