package br.com.sl.ms.model;

import java.util.ArrayList;
import java.util.List;

public class Field {

    private final int x;
    private final int y;

    private boolean open;
    private boolean undermined;
    private boolean check;

    private List<Field> neighbors = new ArrayList<>();
    private List<FieldObserver> observers = new ArrayList<>();

    Field(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setObservers(FieldObserver observer) {
        observers.add(observer);
    }

    public void notificarObservers(FieldEvent event) {
        observers.stream()
                .forEach(o -> o.eventoOcorreu(this, event));
    }

    boolean addNeighbor(Field neighbor) {
        boolean xd = x != neighbor.x;
        boolean yd = y != neighbor.y;
        boolean diagonal = xd && yd;

        int deltaX = Math.abs(x - neighbor.x);
        int deltaY = Math.abs(y - neighbor.y);
        int deltaGeral = deltaX + deltaY;

        if(deltaGeral == 1 && !diagonal) {
            neighbors.add(neighbor);
            return true;
        } else if(deltaGeral == 2 && diagonal) {
            neighbors.add(neighbor);
            return true;
        } else {
            return false;
        }
    }

    public void toggleCheckUp() {
        if(!open) {
            check = !check;

            if(check) {
                notificarObservers(FieldEvent.CHECK);
            } else {
                notificarObservers(FieldEvent.UNCHECK);
            }
        }
    }

    public boolean openUp() {
        if(!open && !check) {
            open = true;

            if(undermined) {
                notificarObservers(FieldEvent.EXPLODE);
                return true;
            }

            setOpen(true);
            if(safeNeighborhood()) {
                neighbors.forEach(v -> v.openUp());
            }

            return true;
        } else {
            return false;
        }
    }

    public boolean safeNeighborhood() {
        return neighbors.stream().noneMatch(v -> v.undermined);
    }

    void undermine() {
        undermined = true;
    }

    public boolean isUndermined() {
        return undermined;
    }

    boolean isCheck() {
        return check;
    }

    public void setOpen(boolean open) {
        this.open = open;

        if(open) {
            notificarObservers(FieldEvent.OPEN);
        }
    }

    public boolean isOpen() {
        return open;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean goalAchieved() {
        boolean unraveled = !undermined && open;
        boolean safe = undermined && check;
        return unraveled || safe;
    }

    public int neighborhoodMines() {
        return (int) neighbors.stream().filter(v -> v.undermined).count();
    }

    void restart() {
        open = false;
        undermined = false;
        check = false;
        notificarObservers(FieldEvent.RESTART);
    }
}