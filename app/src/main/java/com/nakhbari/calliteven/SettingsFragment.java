package com.nakhbari.calliteven;

import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.machinarius.preferencefragment.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {
    private SettingsCommunicator activityCommunicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.fragment_preferences);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitializeView();
    }

    private void InitializeView() {

        Preference currency = findPreference(getString(R.string.pref_currency_key));
        currency.setSummary(activityCommunicator.GetCurrency());
        currency.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                activityCommunicator.OpenCurrencyDialog();
                return false;
            }
        });
    }

    public void updateCurrency(String newCurrency) {
        Preference currency = findPreference(getString(R.string.pref_currency_key));
        currency.setSummary(activityCommunicator.GetCurrency());
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

        public String GetCurrency();
    }
}
