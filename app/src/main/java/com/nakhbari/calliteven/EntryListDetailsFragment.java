package com.nakhbari.calliteven;

import java.text.DateFormat;
import java.util.Calendar;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;

public class EntryListDetailsFragment extends Fragment implements
        OnClickListener, OnDateSetListener {

    static class ViewHolder {
        EditText etTitle;
        EditText etPrice;
        Button bCurrentDateButton;
        Button bDueDateButton;
        Button bYes;
        Button bCancel;
        RadioButton rbIsMoneyItem;
        RadioButton rbIsObjectItem;
        Spinner spWhoPaid;
        TextView tvCurrency;
    }

    EntryListDetailsCommunicator activityCommunicator;
    private int m_namePosition = 0;
    private int m_entryPosition = 0;
    private EntryListItem m_entryItem;
    private ViewHolder holder = new ViewHolder();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_entry_details, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        initializeDetails(view);
        super.onViewCreated(view, savedInstanceState);
    }

    /** ----------------------- Action Bar ------------------------- */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        // Notify the fragment that there is an option menu to inflate
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // This function will catch when the actionbar button have been clicked
        switch (item.getItemId()) {

            case android.R.id.home:
                activityCommunicator.NavigateBack();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {

        ActionBar actionBar = getActivity().getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(false);

        super.onPause();
    }

    @Override
    public void onResume() {

        ActionBar actionBar = getActivity().getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

        super.onPause();
    }

    /** ----------------------- Functions -------------------------------------- */

    private void initializeDetails(View view) {

        holder.etTitle = (EditText) view.findViewById(R.id.entryDetailsTitle);
        holder.etPrice = (EditText) view.findViewById(R.id.entryDetailsPrice);
        holder.etPrice
                .setFilters(new InputFilter[] { new InputFilterPriceNumber() });
        holder.spWhoPaid = (Spinner) view
                .findViewById(R.id.entryDetailsWhoPaid);
        holder.tvCurrency = (TextView) view
                .findViewById(R.id.entryDetailsCurrency);
        holder.rbIsMoneyItem = (RadioButton) view
                .findViewById(R.id.entryDetailsMonetary);
        holder.rbIsObjectItem = (RadioButton) view
                .findViewById(R.id.entryDetailsObject);

        // Find the yes and cancel buttons in the dialog
        holder.bYes = (Button) view.findViewById(R.id.entryOK);
        holder.bCancel = (Button) view.findViewById(R.id.entryCancel);
        holder.bCurrentDateButton = (Button) view
                .findViewById(R.id.entryDetailsCurrentDate);
        holder.bDueDateButton = (Button) view
                .findViewById(R.id.entryDetailsDueDate);

        // Set the edit texts within the Dialog, if the entry item is filled out
        if (m_entryItem != null && holder.etTitle != null
                && holder.etPrice != null && holder.bCurrentDateButton != null
                && holder.rbIsMoneyItem != null
                && holder.bDueDateButton != null && holder.spWhoPaid != null) {

            if (m_entryItem.getTitle() != "default") {

                String currency = activityCommunicator.GetCurrency();

                holder.etTitle.setText(m_entryItem.getTitle());
                holder.etTitle.setSelection(m_entryItem.getTitle().length());
                holder.bCurrentDateButton
                        .setText(GetStringFromCalendar(m_entryItem
                                .getCurrentDate()));
                holder.tvCurrency.setText(currency);
                holder.etPrice.setText(FormatDoubleToString(m_entryItem
                        .getPrice()));

                if (!m_entryItem.isItemMonetary()) {
                    // Item is am Object
                    holder.etPrice.setEnabled(false);
                    holder.etPrice.setTextColor(Color.LTGRAY);
                    holder.rbIsMoneyItem.setChecked(false);
                    holder.rbIsObjectItem.setChecked(true);
                }
            } else {

                final Calendar calendar = Calendar.getInstance();
                holder.bCurrentDateButton
                        .setText(GetStringFromCalendar(calendar));

            }

            if (m_entryItem.getDueDate() == null) {

                holder.bDueDateButton.setText("No Date");
            } else {

                holder.bDueDateButton.setText(GetStringFromCalendar(m_entryItem
                        .getDueDate()));
            }

        }
        // Listen for clicks
        if (holder.bYes != null) {

            holder.bYes.setOnClickListener(this);
        }
        if (holder.bCancel != null) {

            holder.bCancel.setOnClickListener(this);
        }
        if (holder.bCurrentDateButton != null) {

            holder.bCurrentDateButton.setOnClickListener(this);

        }
        if (holder.bDueDateButton != null) {

            holder.bDueDateButton.setOnClickListener(this);
        }
        if (holder.rbIsMoneyItem != null) {
            holder.rbIsMoneyItem
                    .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {
                            if (isChecked) {

                                // Item is a Monetary Value
                                holder.etPrice.setEnabled(true);
                                holder.etPrice.setTextColor(Color.BLACK);
                            } else {

                                // Item is am Object
                                holder.etPrice.setEnabled(false);
                                holder.etPrice.setTextColor(Color.LTGRAY);
                            }

                        }

                    });
        }

    }

    // Gets called when an item is clicked
    @Override
    public void onClick(View v) {

        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), false);
        datePickerDialog.setYearRange(1985, 2028);

        switch (v.getId()) {
            case R.id.entryOK:

                if (holder.etTitle.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "Enter Title", Toast.LENGTH_SHORT)
                            .show();
                } else if (holder.rbIsMoneyItem.isChecked()
                        && holder.etPrice.getText().toString().isEmpty()) {

                    Toast.makeText(getActivity(), "Enter Amount",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // fill in the entrylistitem with the dialog data
                    m_entryItem
                            .setTitle(holder.etTitle.getText().toString().trim());

                    // If there is a monetary value, then set it in the entry item
                    m_entryItem.setItemMonetary(holder.rbIsMoneyItem.isChecked());

                    if (holder.rbIsMoneyItem.isChecked()) {
                        if (holder.spWhoPaid.getSelectedItemPosition() == 0) {

                            m_entryItem.setPrice(Double.parseDouble(holder.etPrice
                                    .getText().toString()));

                        } else {
                            // Then the other person paid and we need to made the
                            // price negative
                            m_entryItem.setPrice((-1)
                                    * Double.parseDouble(holder.etPrice.getText()
                                    .toString()));

                        }
                    }else{

                        m_entryItem.setPrice(0.0);
                    }

                    // Send data to the fragment
                    activityCommunicator.SendEntryItemData(m_entryItem,
                            m_namePosition, m_entryPosition);

                    getActivity().getSupportFragmentManager().popBackStack();
                }

                break;
            case R.id.entryCancel:

                getActivity().getSupportFragmentManager().popBackStack();
                break;

            case R.id.entryDetailsDueDate:
                datePickerDialog.show(getFragmentManager(), "Due DatePicker");
                break;
            case R.id.entryDetailsCurrentDate:
                datePickerDialog.show(getFragmentManager(), "Current DatePicker");
                break;
        }

    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year,
                          int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        if (datePickerDialog.getTag() == "Current DatePicker") {

            m_entryItem.setCurrentDate(cal);
            holder.bCurrentDateButton.setText(GetStringFromCalendar(cal));
        } else {

            m_entryItem.setDueDate(cal);
            holder.bDueDateButton.setText(GetStringFromCalendar(cal));
        }
    }

    private String GetStringFromCalendar(Calendar cal) {
        String result = "";

        DateFormat dateFormat = DateFormat.getDateInstance();

        result = dateFormat.format(cal.getTime());
        return result;
    }

    public static String FormatDoubleToString(double d) {
        if (d == (int) d)
            return String.format("%d", (int) d);
        else
            return String.format("%.2f", d);
    }

    /** ----------------------- Activity Callbacks --------------------------- */

    public void SetEntryListItem(EntryListItem item, int namePosition,
                                 int entryPosition) {
        m_entryItem = item;
        this.m_namePosition = namePosition;
        this.m_entryPosition = entryPosition;
    }

    /** ----------------------- Activity Interface --------------------------- */

    @Override
    public void onAttach(Activity activity) {
        // Attach the interface to the activity
        super.onAttach(activity);
        try {
            activityCommunicator = (EntryListDetailsCommunicator) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement EntryDialogCommunicator");
        }
    }

    public interface EntryListDetailsCommunicator {
        public void SendEntryItemData(EntryListItem item, int namePosition,
                                      int entryPosition);
        public String GetCurrency();
        public void NavigateBack();
    }

}
