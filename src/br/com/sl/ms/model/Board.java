package br.com.sl.ms.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Board implements FieldObserver {
    private final int rows;
    private final int columns;
    private final int mines;

    private final List<Field> fields = new ArrayList<>();
    private final List<Consumer<Boolean>> observers = new ArrayList<>();

    public Board(int rows, int columns, int mines) {
        this.rows = rows;
        this.columns = columns;
        this.mines = mines;

        generateFields();
        connectNeighbors();
        raffleMines();
    }

    public void forEach(Consumer<Field> function) {
        fields.forEach(function);
    }

    public void registrarObserver(Consumer<Boolean> observer) {
        observers.add(observer);
    }

    public void notificarObservers(boolean resultado) {
        observers.stream()
                .forEach(o -> o.accept(resultado));
    }

    public void open(int row, int column) {
        try {
            fields.parallelStream()
                    .filter(f -> f.getX() == row && f.getY() == column)
                    .findFirst()
                    .ifPresent(f -> f.openUp());
        } catch (Exception e) {
            fields.forEach(f -> f.setOpen(true));
            throw e;
            // FIXME arrumar a implementacao do catch
        }
    }

    public void mark(int row, int column) {
        fields.parallelStream()
                .filter(f -> f.getX() == row && f.getY() == column)
                .findFirst()
                .ifPresent(f -> f.toggleCheckUp());
    }

    private void showMines() {
        fields.stream()
                .filter(f -> f.isUndermined())
                .filter(f -> !f.isCheck())
                .forEach(c -> c.setOpen(true));
    }

    private void generateFields() {
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                Field field = new Field(row, column);
                field.setObservers(this);
                fields.add(field);
            }
        }
    }

    private void connectNeighbors() {
        for (Field f1: fields) {
            for (Field f2: fields) {
                f1.addNeighbor(f2);
            }
        }
    }

    private void raffleMines() {
        long minesSeted = 0;
        Predicate<Field> undermined = f -> f.isUndermined();

        do {
            int random = (int) (Math.random() * fields.size());
            fields.get(random).undermine();
            minesSeted = fields.stream().filter(undermined).count();
        } while (minesSeted < mines);
    }

    public boolean goalAchieved() {
        return fields.stream().allMatch(f -> f.goalAchieved());
    }

    public void restart() {
        fields.stream()
                .forEach(c -> c.restart());
        raffleMines();
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    @Override
    public void eventoOcorreu(Field field, FieldEvent event) {
        if(event == FieldEvent.EXPLODE) {
            showMines();
            notificarObservers(false);
        } else if(goalAchieved()){
            notificarObservers(true);
        }
    }
}
