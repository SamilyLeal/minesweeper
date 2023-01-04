package br.com.sl.ms.view;

import br.com.sl.ms.model.Field;
import br.com.sl.ms.model.FieldEvent;
import br.com.sl.ms.model.FieldObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class FieldButton extends JButton implements FieldObserver, MouseListener {

    private final Color PADRAO = new Color(184, 184, 184);
    private final Color MARK = new Color(8, 179, 247);
    private final Color EXPLODE = new Color(189, 66, 68);
    private final Color GREEN = new Color(0, 100, 0);

    private Field field;
    public FieldButton(Field field) {
        this.field = field;
        setBackground(PADRAO);
        setBorder(BorderFactory.createBevelBorder(0));

        addMouseListener(this);
        field.setObservers(this);
    }

    @Override
    public void eventoOcorreu(Field field, FieldEvent event) {
        switch (event) {
            case OPEN:
                setOpenStyle();
                break;
            case CHECK:
                setCheckStyle();
                break;
            case EXPLODE:
                setExplodeStyle();
                break;
            default:
                setStyle();
                break;
        }
    }

    private void setStyle() {
        setBackground(PADRAO);
        setBorder(BorderFactory.createBevelBorder(0));
        setText("");
    }

    private void setExplodeStyle() {
        setBackground(EXPLODE);
        setForeground(Color.WHITE);
        setText("X");
    }

    private void setCheckStyle() {
        setBackground(MARK);
        setText("M");
    }

    private void setOpenStyle() {
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        if(field.isUndermined()) {
            setBackground(EXPLODE);
            return;
        }

        setBackground(PADRAO);

        switch (field.neighborhoodMines()) {
            case 1:
                setForeground(GREEN);
                break;
            case 2:
                setForeground(Color.BLUE);
                break;
            case 3:
                setForeground(Color.YELLOW);
                break;
            case 4:
            case 5:
            case 6:
                setForeground(Color.RED);
                break;
            default:
                setForeground(Color.PINK);
        }

        String valueM = !field.safeNeighborhood() ? field.neighborhoodMines() + "" : "";
        setText(valueM);
    }

    //Mouse event

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == 1) {
            field.openUp();
        } else {
            field.toggleCheckUp();
        }
    }

    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

}
