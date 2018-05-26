package com.gghouse.wardah.wardahba.helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * Created by michael on 6/22/2017.
 */

public class NumberTextWatcher implements TextWatcher {

    private DecimalFormat df;
    private DecimalFormat dfnd;
//    private boolean hasFractionalPart;

    private EditText et;

    public NumberTextWatcher(EditText et) {
        df = new DecimalFormat("#,###");
        df.setDecimalSeparatorAlwaysShown(false);
        dfnd = new DecimalFormat("#,###");
        this.et = et;
//        hasFractionalPart = false;
    }

    @SuppressWarnings("unused")
    private static final String TAG = "NumberTextWatcher";

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        et.removeTextChangedListener(this);

        try {
            int inilen, endlen;
            inilen = et.getText().length();

            String v = s.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "");
            Number n = df.parse(v);
            int cp = et.getSelectionStart();
            et.setText(dfnd.format(n));
            endlen = et.getText().length();
            int sel = (cp + (endlen - inilen));
            if (sel > 0 && sel <= et.getText().length()) {
                et.setSelection(sel);
            } else {
                et.setSelection(et.getText().length());
            }
        } catch (NumberFormatException nfe) {
            // do nothing?
        } catch (ParseException e) {
            // do nothing?
        }

        et.addTextChangedListener(this);
    }
}