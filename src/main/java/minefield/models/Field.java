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

    boolean addNeighbor(Field neighbor) { //returns if a field is a neighbor and adds to a neighboringFields list
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

    void toggleFieldMarkup() { //toggle between open and close a field
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

    boolean safeNeighborhood() { //return if the neighbor is secure
        return neighboringFields.stream().noneMatch(neighbor -> neighbor.minedField);
    }

    void undermine() { //undermine a field
        minedField = true;
    }

    public boolean isMined() {
        return minedField;
    }

    public boolean isMarked() {
        return markedField;
    }

    void setOpenField(boolean openField) {
        this.openField = openField;
    }

    public boolean isOpen() {
        return openField;
    }

    public boolean isClosed() {
        return !openField;
    }

    public int getFieldLine() {
        return fieldLine;
    }

    public int getFieldColumn() {
        return fieldColumn;
    }

    boolean goalAchieved() { //return if the goal was achieved
       boolean unraveledField = !minedField && openField;
       boolean protectedField = minedField && markedField;
       return unraveledField || protectedField;
    }

    long neighborhoodMines() {
        return neighboringFields.stream().filter(neighbor -> neighbor.minedField).count(); //filter only neighbors with mines
    }

    void restartGame() { //restart the game
        openField = false;
        minedField = false;
        markedField = false;
    }

    public String toString() { //to string for set the board fields
        if(markedField) {
            return "X";
        } else if(openField && minedField) {
            return "*";
        } else if(openField && neighborhoodMines() > 0) {
            return Long.toString(neighborhoodMines());
        } else if(openField) {
            return " ";
        } else {
            return "?";
        }
    }

}
