package workscheduler.model;

import javafx.beans.property.*;

import java.time.LocalDateTime;

public class Appointment {

    private IntegerProperty id;
    private IntegerProperty customerId;
    private StringProperty type;
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

    public IntegerProperty customerIdProperty() {
        return customerId;
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
        return "Appointment Id: " + getId() +
                ", Customer Id: " + getCustomerId() +
                ", Type: " + getType() +
                ", Start: " + getStart() +
                ", End : " + getEnd();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Appointment that = (Appointment) o;

        if (getId() != that.getId()) return false;
        if ((getCustomerId() != that.getCustomerId())) return false;
        if (getType() != null ? !getType().equals(that.getType()) : that.getType() != null) return false;
        if (getStart() != null ? !getStart().equals(that.getStart()) : that.getStart() != null) return false;
        return getEnd() != null ? getEnd().equals(that.getEnd()) : that.getEnd() == null;
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getCustomerId();
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (getStart() != null ? getStart().hashCode() : 0);
        result = 31 * result + (getEnd() != null ? getEnd().hashCode() : 0);
        return result;
    }
}