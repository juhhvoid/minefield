package minefield.models;

import minefield.exceptions.ExplosionException;

import java.util.ArrayList;
import java.util.List;

public class Field {
    private final int fieldLine;
    private final int fieldColumn;
    private boolean markedField = false;
    private boolean openField = false;
    private boolean minedField = false;

    private List<Field> neighboringFields = new ArrayList<>();

    Field(int fieldLine, int fieldColumn) {
        this.fieldLine = fieldLine;
        this.fieldColumn = fieldColumn;
    }

    boolean addNeighbor(Field neighbor) {
        boolean differentLine = fieldLine != neighbor.fieldLine;
        boolean differentColumn = fieldColumn != neighbor.fieldColumn;
        boolean diagonal = differentLine && differentColumn;

        int deltaLine = Math.abs(fieldLine - neighbor.fieldLine);
        int deltaColumn = Math.abs(fieldColumn - neighbor.fieldColumn);
        int deltaGeneral = deltaLine + deltaColumn;

        if(deltaGeneral == 1 && !diagonal) {
            neighboringFields.add(neighbor);
            return true;
        } else if(deltaGeneral == 2 && diagonal) {
            neighboringFields.add(neighbor);
            return true;
        } else {
            return false;
        }
    }

    void toggleFieldMarkup() {
        if(!openField) {
            markedField = !markedField;
        }
    }

    boolean openField() {
        if(!openField && !markedField) {
           openField = true;

           if(minedField) {
               throw new ExplosionException();
           }

           if(safeNeighborhood()) {
               neighboringFields.forEach(neighbor -> neighbor.openField());
           }

           return true;
        } else {
            return false;
        }
    }

    boolean safeNeighborhood() {
        return neighboringFields.stream().noneMatch(neighbor -> neighbor.minedField);
    }

    void undermine() {
        minedField = true;
    }

    public boolean isMarked() {
        return markedField;
    }

    public boolean isOpen() {
        return openField;
    }

    public boolean isClosed() {
        return !openField;
    }

}
