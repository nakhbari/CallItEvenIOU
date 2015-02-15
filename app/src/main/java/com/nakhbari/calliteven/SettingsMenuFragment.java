package com.nakhbari.calliteven;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class SettingsMenuFragment extends Fragment implements OnClickListener {
    private SettingsCommunicator activityCommunicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        initializeDetails(view);
        super.onViewCreated(view, savedInstanceState);
    }

    private void initializeDetails(View view) {
        Button bCurrency = ((Button) view.findViewById(R.id.bSettingsCurrency));
        Button bColorScheme = ((Button) view.findViewById(R.id.bSettingsColorScheme));

        bColorScheme.setOnClickListener(this);
        bCurrency.setOnClickListener(this);


        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Light.ttf");

        bCurrency.setTypeface(tf);
        bColorScheme.setTypeface(tf);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bSettingsCurrency:
                activityCommunicator.OpenCurrencyDialog();
                Toast.makeText(getActivity(), "Currency Clicked", Toast.LENGTH_SHORT).show();
                break;

            case R.id.bSettingsColorScheme:
                Toast.makeText(getActivity(), "ColorScheme Clicked", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void onAttach(Activity activity) {
        // Attach the interface to the activity
        super.onAttach(activity);
        try {
            activityCommunicator = (SettingsCommunicator) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement SettingsCommunicator");
        }
    }

    public interface SettingsCommunicator {
        public void OpenCurrencyDialog();
    }

}
