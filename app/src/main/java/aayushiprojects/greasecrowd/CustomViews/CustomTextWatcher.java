package aayushiprojects.greasecrowd.CustomViews;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import aayushiprojects.greasecrowd.helper.HelperMethods;

/**
 * Created by technource on 29/3/18.
 */

public class CustomTextWatcher implements TextWatcher {
    Context context;
    EditText editText;
    View view;

    public CustomTextWatcher(Context context, EditText editText, View view) {
        this.context = context;
        this.editText = editText;
        this.view = view;
    }



    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.length()>0){
            HelperMethods.Valid(context,view);
        }else {
            // HelperMethods.ValidateFields(appContext,ll_fname);
        }
    }
}
