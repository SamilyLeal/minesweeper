package br.com.ssl.ms.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Board implements FieldObserver {

    private int rows, columns, mines;

    private List<Field> fields = new ArrayList<>();
    private List<Consumer<Boolean>> observers = new ArrayList<>();

    public Board(int rows, int columns, int mines){
        this.rows = rows;
        this.columns = columns;
        this.mines = mines;

        generateFields();
        connectNeighborhood();
        raffleMines();
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public void registerObserver(Consumer<Boolean> observer){
        observers.add(observer);
    }

    public void notifyObservers(boolean result){
        observers.stream().forEach(o -> o.accept(result));
    }

    private void generateFields(){
        for(int row = 0; row < rows; row++){
            for(int column = 0; column < columns; column++){
                Field field = new Field(row, column);
                field.addObserver(this);

                fields.add(field);
            }
        }
    }

    private void connectNeighborhood(){
        for(Field f1: fields){
            for(Field f2: fields){
                f1.addNeighbor(f2);
            }
        }
    }

    private void raffleMines(){
        long minesSeted = 0;
        Predicate<Field> undermined = f -> f.isUndermined();

        do {
            int random = (int) (Math.random() * fields.size());
            fields.get(random).undermine();
            minesSeted = fields.stream().filter(undermined).count();
        } while (minesSeted < mines);
    }

    public void open(int row, int column){
        try{
            fields.parallelStream()
            .filter(f -> f.getX() == row && f.getY() == column)
            .findFirst()
            .ifPresent(f -> f.openUp());
        } catch (Exception e){
            fields.forEach(f -> f.setOpen(true));
            throw e;
        }
    }

    public void redflag(int row, int column){
        fields.parallelStream()
        .filter(f -> f.getX() == row && f.getY() == column)
        .findFirst()
        .ifPresent(f -> f.toggleCheckUp());
    }

    private void showMines(){
        fields.stream()
        .filter(f -> f.isUndermined()).filter(f -> !f.isCheck())
        .forEach(f -> f.setOpen(true));
    }

    public boolean goalAchieved(){
        return fields.stream().allMatch(f -> f.goalAchieved());
    }

    public void forEach(Consumer<Field> fun){
        fields.forEach(fun);
    }

    public void restart() {
        fields.stream().forEach(f -> f.restart());
        
        raffleMines();
    }

    @Override
    public void registerEvent(Field field, FieldEvent event) {
        if(event == FieldEvent.EXPLODE){
            showMines();
            notifyObservers(false);
        } 

        if(goalAchieved()){
            notifyObservers(true);
        }
    }
}
