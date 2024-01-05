package br.com.ssl.ms.view;

import javax.swing.*;

import br.com.ssl.ms.model.Board;

import java.awt.*;

public class PainelBoard extends JPanel {
    public  PainelBoard(Board board) {
        setLayout(new GridLayout(board.getRows(), board.getColumns()));

        board.forEach(f -> add(new FieldButton(f)));

        
        board.registerObserver(e -> {
            SwingUtilities.invokeLater(() -> {
                if(e) {
                    JOptionPane.showMessageDialog(this, "Yeeyyy :)");
                } else {
                    JOptionPane.showMessageDialog(this, "You lost :(");
                }

                board.restart();
            });
        });
    }
}
