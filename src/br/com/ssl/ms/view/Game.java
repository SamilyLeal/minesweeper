package br.com.ssl.ms.view;

import javax.swing.*;

import br.com.ssl.ms.model.Board;

public class Game extends JFrame {
    public Game() {
        Board board = new Board(16, 30, 50);
        add(new PainelBoard(board));

        setTitle("Minesweeper");
        setSize(690, 438);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }
    public static void main(String[] args) {
        new Game();
    }
}
