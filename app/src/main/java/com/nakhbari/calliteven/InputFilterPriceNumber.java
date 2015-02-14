package com.nakhbari.calliteven;

import android.text.InputFilter;
import android.text.Spanned;

public class InputFilterPriceNumber implements InputFilter {

    int beforeDecimal = 7, afterDecimal = 2;

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        String s = source.toString();
        String d = dest.toString();

        String r = d.substring(0, dstart) + s.substring(start, end)
                + d.substring(dend);

        if (r.equals(".")) {
            return "0.";
        } else if (r.equals("0")) {
            return ""; // don't allow beginning with a 0
        } else if (!r.contains(".")) {
            // no decimal point placed yet
            if (r.length() > beforeDecimal) {
                return "";
            }
        } else {
            r = r.substring(r.indexOf(".") + 1);
            if (r.length() > afterDecimal) {
                return "";
            }
        }

        return null;
    }
}