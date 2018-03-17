package main.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Appointment {

    private IntegerProperty id; //appointmentId
    private IntegerProperty customerId; //customerId
    private StringProperty type; //type
    private StringProperty start; //start
    private StringProperty end; //end

    public Appointment(int id, int customerId, String type, String start, String end) {
        this.id = new SimpleIntegerProperty(id);
        this.customerId = new SimpleIntegerProperty(customerId);
        this.type = new SimpleStringProperty(type);
        this.start = new SimpleStringProperty(start);
        this.end = new SimpleStringProperty(end);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getCustomerId() {
        return customerId.get();
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public String getStart() {
        return start.get();
    }

    public StringProperty startProperty() {
        return start;
    }

    public String getEnd() {
        return end.get();
    }

    public StringProperty endProperty() {
        return end;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", type=" + type +
                ", start=" + start +
                ", end=" + end +
                '}';
    }

    public String toStringUserFriendly() {
        return "Appointment Id: " + getId() +
                ", Customer Id: " + getCustomerId() +
                ", Type: " + getType() +
                ", Start: " + getStart() +
                ", End : " + getEnd();
    }
}
