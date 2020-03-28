package tools;

import javafx.beans.property.SimpleStringProperty;

public class Row {
    private final SimpleStringProperty north;
    private final SimpleStringProperty east;
    private final SimpleStringProperty south;
    private final SimpleStringProperty west;

    public Row(String[] row) {
        north = new SimpleStringProperty(row[0]);
        east = new SimpleStringProperty(row[1]);
        south = new SimpleStringProperty(row[2]);
        west = new SimpleStringProperty(row[3]);
    }

    public String getNorth() {
        return north.get();
    }

    public String getEast() {
        return east.get();
    }

    public String getSouth() {
        return south.get();
    }

    public String getWest() {
        return west.get();
    }
}
