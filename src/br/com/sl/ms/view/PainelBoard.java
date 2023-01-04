package br.com.sl.ms.view;

import br.com.sl.ms.model.Board;

import javax.swing.*;
import java.awt.*;

public class PainelBoard extends JPanel {
    public  PainelBoard(Board board) {
        setLayout(new GridLayout(board.getRows(), board.getColumns()));

        board.forEach(f -> add(new FieldButton(f)));
        board.registrarObserver(e -> {
            SwingUtilities.invokeLater(() -> {
                if(e) {
                    JOptionPane.showMessageDialog(this, "win");
                } else {
                    JOptionPane.showMessageDialog(this, "lost");
                }

                board.restart();
            });
        });
    }
}
