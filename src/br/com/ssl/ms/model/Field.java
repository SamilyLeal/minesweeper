package br.com.ssl.ms.model;

import java.util.ArrayList;
import java.util.List;

public class Field {

    private int x, y;

    private boolean open, undermined, check;

    private List<Field> neighbors = new ArrayList<>();
    private List<FieldObserver> observers = new ArrayList<>();

    public Field(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void addObserver(FieldObserver observer){
        observers.add(observer);
    }

    public boolean addNeighbor(Field field){

        boolean xd = x != field.x;
        boolean yd = y != field.y;
        boolean diagonal = xd && yd;

        int deltaX = Math.abs(x - field.x);
        int deltaY = Math.abs(y - field.y);
        int delta = deltaX + deltaY;

        if(delta == 1 && !diagonal){
            neighbors.add(field);
            return true;
        }

        if(delta == 2 && diagonal){
            neighbors.add(field);
            return true;
        }

        return false;
    }

    public void notifyObserver(FieldEvent event){
        observers.stream().forEach(o -> o.registerEvent(this, event));
    }

    public void toggleCheckUp() {
        if(!open){
            check = !check;

            if(check){
                notifyObserver(FieldEvent.CHECK);
            } else {
                notifyObserver(FieldEvent.UNCHECK);
            }
        }
    }

    public boolean safeNeighborhood(){
        return neighbors.stream().noneMatch(v -> v.undermined);
    }

    void undermine(){
        undermined = true;
    }

    public boolean isUndermined(){
        return undermined;
    }

    public boolean isCheck(){
        return check;
    }

    public boolean isOpen(){
        return open;
    }

    public void setOpen(boolean open){
        this.open = open;

        if(open){
            notifyObserver(FieldEvent.OPEN);
        }
    }

    public boolean openUp(){
        if(!open && !check){
            open = true;

            if(undermined){
                notifyObserver(FieldEvent.EXPLODE);
                return true;
            }

            setOpen(true);
            if(safeNeighborhood()){
                neighbors.forEach(n -> n.openUp());
            }

            return true;
        }

        return false;
    }

    public int neighborhoodMines(){
        return (int) neighbors.stream().filter(v -> v.undermined).count();
    }

    public boolean goalAchieved(){
        boolean unraveled = !undermined && open;
        boolean safe = undermined && check;
        return unraveled || safe;
    }

    void restart(){
        open = false;
        undermined = false;
        check = false;

        notifyObserver(FieldEvent.RESTART);
    }
}
