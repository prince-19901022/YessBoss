package com.example.princeporosh.yessboss.utility;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.princeporosh.yessboss.model.TheTask;

/**
 * Created by Prince Porosh on 9/22/2018.
 */

public class EditTextValidator implements TextWatcher {

    //If it is required to imply different logic for validation

    public interface ValidationLogicSpecification{
        boolean isValidAccordingToSpecifiedLogic(String text);
    }

    //The edit text to validate
    private EditText editText;

    //Which will display the error message
    private TextView errorView;


    private boolean isValid;

    //Default border color of edit text which is under consideration.
    private ColorStateList defaultStateList = null;

    private String errorMessage = "This field must be nonempty";

    //Error color indicates intensity of error. My plan is to use different color for different error.
    //So, this field provides option to change color depending on the type of error.

    private String errorColor = "#FF0011";

    //To inject custom logic for validation.
    private ValidationLogicSpecification customValidation;

    public EditTextValidator(EditText editText, TextView errorView){
        this.editText = editText;
        this.errorView = errorView;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        isValid();
    }

    private void setEditTextBorderColor(String colorCode){
        ColorStateList csl = colorCode == null ? defaultStateList : ColorStateList.valueOf(Color.parseColor(colorCode));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            editText.setBackgroundTintList(csl);
        }
    }

    public boolean isValid() {

        String text = editText.getText().toString();

        if(customValidation != null){ //Means i want to use custom validation.
            isValid = customValidation.isValidAccordingToSpecifiedLogic(text);
        }else{

            if (text.isEmpty()) {
                isValid = false;
                setEditTextBorderColor(errorColor);
                errorView.setVisibility(View.VISIBLE);
                errorView.setText(errorMessage);
            } else {
                isValid = true;
                setEditTextBorderColor(null);
                errorView.setVisibility(View.GONE);
            }
        }
        return isValid;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setErrorColor(String errorColor) {
        this.errorColor = errorColor;
    }

    public void setCustomValidation(ValidationLogicSpecification customValidation){
        this.customValidation = customValidation;
    }
}
