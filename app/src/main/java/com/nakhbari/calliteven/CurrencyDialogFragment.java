package com.nakhbari.calliteven;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CurrencyDialogFragment extends DialogFragment implements
        OnClickListener, AdapterView.OnItemSelectedListener {

    String[] currencies;
    private CurrencyDialogCommunicator activityCommunicator;
    private ViewHolder holder = new ViewHolder();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        currencies = getResources().getStringArray(R.array.currency_array);
        setStyle(DialogFragment.STYLE_NO_TITLE, DialogFragment.STYLE_NORMAL);

    }

    @Override
    public void onStart() {
        super.onStart();

        // safety check
        if (getDialog() == null) {
            return;
        }

        // set the animations to use on showing and hiding the dialog
        getDialog().getWindow().setWindowAnimations(
                R.anim.abc_fade_in);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_currency_dialog, null);

        initializeDialog(view);

        return view;
    }

    private void initializeDialog(View view) {
        holder.bCancel = (Button) view.findViewById(R.id.bCurrencyCancel);
        holder.bSave = (Button) view.findViewById(R.id.bCurrencySave);
        holder.spCurrency = (Spinner) view.findViewById(R.id.spCurrency);
        holder.tvTitle = (TextView) view
                .findViewById(R.id.tvCurrencyDiagCurrency);
        holder.etOtherCurrency = (EditText) view
                .findViewById(R.id.etOtherCurrency);

        Typeface roboto_med = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Medium.ttf");
        Typeface roboto_reg = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Regular.ttf");

        holder.tvTitle.setTypeface(roboto_med);
        holder.bCancel.setTypeface(roboto_med);
        holder.bSave.setTypeface(roboto_med);
        holder.bSave.setTextColor(getActivity().getResources().getColor(
                R.color.main_theme_color));
        holder.etOtherCurrency.setTypeface(roboto_reg);
        holder.spCurrency.setOnItemSelectedListener(this);
        holder.bCancel.setOnClickListener(this);
        holder.bSave.setOnClickListener(this);

        // Find current currency
        int pos = -1;
        String currentCurrency = activityCommunicator.GetCurrency();

        for (int i = 0; i < currencies.length; i++) {
            if (currencies[i].equals(currentCurrency)) pos = i;
        }

        if (pos >= 0) {
            holder.spCurrency.setSelection(pos, true);
        } else {
            holder.spCurrency.setSelection(currencies.length - 1);
            holder.etOtherCurrency.setText(currentCurrency);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (currencies[position].equals("Other")) {
            holder.etOtherCurrency.setEnabled(true);
            holder.etOtherCurrency.setVisibility(View.VISIBLE);
        } else {
            holder.etOtherCurrency.setEnabled(false);
            holder.etOtherCurrency.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bCurrencyCancel:
                dismiss();
                break;
            case R.id.bCurrencySave:
                if (holder.etOtherCurrency.isEnabled()) {
                    if (holder.etOtherCurrency.getText().toString().equals(""))
                        Toast.makeText(getActivity(), "Please enter a Currency", Toast.LENGTH_SHORT).show();
                    else {
                        activityCommunicator.SetCurrency(holder.etOtherCurrency.getText().toString());
                        dismiss();
                    }
                } else {
                    activityCommunicator.SetCurrency(holder.spCurrency.getSelectedItem().toString());
                    dismiss();
                }
                break;

        }

    }

    /**
     * ----------------------- Activity Interface ---------------------------
     */

    @Override
    public void onAttach(Activity activity) {
        // Attach the interface to the activity
        super.onAttach(activity);
        try {
            activityCommunicator = (CurrencyDialogCommunicator) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement CurrencyDialogCommunicator");
        }
    }

    public interface CurrencyDialogCommunicator {
        public void SetCurrency(String currency);

        public String GetCurrency();
    }

    private class ViewHolder {
        Button bCancel;
        Button bSave;
        Spinner spCurrency;
        TextView tvTitle;
        EditText etOtherCurrency;
    }
}
