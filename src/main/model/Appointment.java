package main.model;

import javafx.beans.property.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Appointment {

    private IntegerProperty id; //appointmentId
    private IntegerProperty customerId; //customerId
    private StringProperty type; //type
    private ObjectProperty<LocalDateTime> start;
    private ObjectProperty<LocalDateTime> end;

    /*Used for new Appointments*/
    public Appointment(int customerId, String type, LocalDateTime start, LocalDateTime end) {
        this(Integer.MIN_VALUE, customerId, type, start, end);
    }

    /*Used for existing Appointments*/
    public Appointment(int id, int customerId, String type, LocalDateTime start, LocalDateTime end) {
        this.id = new SimpleIntegerProperty(id);
        this.customerId = new SimpleIntegerProperty(customerId);
        this.type = new SimpleStringProperty(type);
        this.start = new SimpleObjectProperty<>(start);
        this.end = new SimpleObjectProperty<>(end);
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

    public LocalDateTime getStart() {
        return start.get();
    }

    public ObjectProperty<LocalDateTime> startProperty() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end.get();
    }

    public ObjectProperty<LocalDateTime> endProperty() {
        return end;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + getId() +
                ", customerId=" + getCustomerId() +
                ", type=" + getType() +
                ", start=" + getStart() +
                ", end=" + getEnd() +
                '}';
    }

    public String toStringUserFriendly() {
        return "Appointment Id: " + getId() +
                ", Customer Id: " + getCustomerId() +
                ", Type: " + getType() +
                ", Start: " + getStart() +
                ", End : " + getEnd();
    }

    public String startEpochString() {
        return epochSecondString(getStart());
    }

    public String endEpochString() {
        return epochSecondString(getEnd());
    }

    private String epochSecondString(LocalDateTime localDateTime) {
        return Long.toString(getEpochSecond(localDateTime));
    }

    private long getEpochSecond(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }
}