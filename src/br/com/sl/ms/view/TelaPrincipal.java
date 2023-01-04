package br.com.sl.ms.view;

import br.com.sl.ms.model.Board;

import javax.swing.*;

public class TelaPrincipal extends JFrame {
    public TelaPrincipal() {
        Board board = new Board(16, 30, 50);
        add(new PainelBoard(board));

        setTitle("minesweeper");
        setSize(690, 438);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }
    public static void main(String[] args) {
        new TelaPrincipal();
    }
}
