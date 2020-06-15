package brighttime.gui.util;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author annem
 */
public class ValidationManager {

    /**
     * Validates the input of a TextField.
     *
     * @param textField
     * @param message
     */
    public void inputValidation(JFXTextField textField, String message) {
        RequiredFieldValidator validator = new RequiredFieldValidator();
        textField.getValidators().add(validator);
        validator.setMessage(message);
        textField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                textField.validate();
            }
        });
    }

    /**
     * Validates the input of a PasswordField.
     *
     * @param passwordField
     * @param message
     */
    public void inputValidationPassword(JFXPasswordField passwordField, String message) {
        RequiredFieldValidator validator = new RequiredFieldValidator();
        passwordField.getValidators().add(validator);
        validator.setMessage(message);
        passwordField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                passwordField.validate();
            }
        });
    }

    /**
     * Validates the selection of a ComboBox.
     *
     * @param comboBox
     * @param message
     */
    public void comboBoxValidation(JFXComboBox comboBox, String message) {
        RequiredFieldValidator validator = new RequiredFieldValidator();
        comboBox.getValidators().add(validator);
        validator.setMessage(message);
        comboBox.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                comboBox.validate();
            }
        });
    }

    public void dateValidation(JFXDatePicker datePicker, String message) {
        RequiredFieldValidator validator = new RequiredFieldValidator();
        datePicker.getValidators().add(validator);
        validator.setMessage(message);
        datePicker.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                datePicker.validate();
            }
        });
    }
}
