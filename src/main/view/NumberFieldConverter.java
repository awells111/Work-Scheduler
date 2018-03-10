package main.view;

import javafx.scene.control.TextFormatter;
import javafx.util.converter.NumberStringConverter;

import java.text.ParsePosition;
import java.util.function.UnaryOperator;

public class NumberFieldConverter extends NumberStringConverter {

    private TextFormatter<Number> formatter;

    public NumberFieldConverter() {
        this(0);
    }

    public NumberFieldConverter(int defaultValue) {
        formatter = new TextFormatter<>(
                this,
                defaultValue,
                this.getFilter());
    }

    private UnaryOperator<TextFormatter.Change> getFilter() {
        return change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty()) {
                return change;
            }

            ParsePosition parsePosition = new ParsePosition(0);
            Object object = getNumberFormat().parse(newText, parsePosition);
            if (object == null || parsePosition.getIndex() < newText.length()) {
                return null;
            } else {
                return change;
            }
        };
    }

    public TextFormatter<Number> getFormatter() {
        return formatter;
    }
}

