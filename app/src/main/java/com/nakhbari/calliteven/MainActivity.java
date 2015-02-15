package com.nakhbari.calliteven;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MainActivity extends ActionBarActivity implements NameListFragment.NameListCommunicator,
        NameDialogFragment.NameDialogCommunicator,
        EntryListFragment.EntryListCommunicator,
        EntryListDetailsFragment.EntryListDetailsCommunicator, SettingsMenuFragment.SettingsCommunicator, CurrencyDialogFragment.CurrencyDialogCommunicator {

    private ArrayList<NameListItem> m_nameEntry = new ArrayList<>();

    private FragmentManager fm = getSupportFragmentManager();
    private EntryListFragment entryListFragment = new EntryListFragment();
    private NameListFragment nameListFragment = new NameListFragment();
    private String currency = "$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            fm.beginTransaction()
                    .add(R.id.mainContainer, nameListFragment)
                    .commit();
        }

        // Turn off the up button initially
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        // Checks for if this version is new
        CheckIfNewUpdate();
    }

    @Override
    protected void onNewIntent(Intent intent) {

        if (ContactsContract.Intents.SEARCH_SUGGESTION_CLICKED.equals(intent
                .getAction())) {
            String displayName = getContactName(intent);
            NameDialogFragment dialogFrag = (NameDialogFragment) fm
                    .findFragmentByTag("NameDialog");
            dialogFrag.SetSearchQuery(displayName);
        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // String query = intent.getStringExtra(SearchManager.QUERY);
            //
            // TextView tv = (TextView) findViewById(R.id.dialogName);
            // tv.setText(query);
        }
    }

    @Override
    protected void onStart() {
        // Load the saved data on application start
        super.onStart();

        //Get Currency
        SharedPreferences settings = getSharedPreferences(getString(R.string.sharedPref), 0);
        currency = settings.getString(getString(R.string.keyCurrency), getString(R.string.defaultCurrency));

        LoadDataStructure();
    }

    @Override
    protected void onStop() {
        // Save the data when the app Stops (before closing)
        super.onStop();


        //Save Currency
        SharedPreferences settings = getSharedPreferences(getString(R.string.sharedPref), 0);
        settings.edit().putString(getString(R.string.keyCurrency), currency).apply();
        settings.edit().putInt(getString(R.string.keyVersion), getVersionCode());
        SaveDataStructure();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == R.id.action_settings) {
            SettingsMenuFragment menuFrag = new SettingsMenuFragment();

            FragmentTransaction ft = fm.beginTransaction();

            ft.setCustomAnimations(R.anim.abc_fade_in,
                    R.anim.abc_fade_out, R.anim.abc_fade_in,
                    R.anim.abc_fade_out);
            ft.replace(R.id.mainContainer, menuFrag);
            ft.addToBackStack(null).commit();
            Toast.makeText(this, "Settings Clicked", Toast.LENGTH_SHORT).show();

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * ----------------------- NameListFragment Functions -----------------
     */
    private void UpdateNameListFragment() {
        // Update the Name Fragment with the newly change name list
        nameListFragment.SetNameListFragment(m_nameEntry);

    }

    /**
     * ----------------------- NameListFragment Callbacks -----------------
     */
    @Override
    public void AddNewNameEntryClicked() {
        // Open Dialog so a new person can be added to the list
        NameDialogFragment newDialogFragment = new NameDialogFragment();

        // Do not populate the dialog, since it is a NEW entry
        newDialogFragment.SetNameListItem(new NameListItem(), -1);
        newDialogFragment.show(fm, "NameDialog");
    }

    @Override
    public void NameListItemClicked(int position) {
        // a Name was clicked, so move to the detail fragment\
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.animator.enter_from_right,
                R.animator.exit_to_left, R.animator.enter_from_left,
                R.animator.exit_to_right);
        ft.replace(R.id.mainContainer, entryListFragment);
        ft.addToBackStack(null).commit();

        // Set the data in the detail fragment
        UpdateEntryListFragment(position, false);

        // Enable the up button on the action bar
        // so the detail fragment can navigate back
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(m_nameEntry.get(position).getName());

    }

    @Override
    public String GetCurrency() {
        return currency;
    }

    @Override
    public void RemoveCheckedNameListItems(ListView listView) {
        RemoveListViewItems(listView, -1);

    }

    @Override
    public void EditNameEntry(int position) {
        // Open Dialog so a new person can be added to the list
        NameDialogFragment newDialogFragment = new NameDialogFragment();

        // populate the dialog
        newDialogFragment.SetNameListItem(m_nameEntry.get(position), position);
        newDialogFragment.show(fm, "NameDialog");

    }

    /**
     * ----------------------- NameDialogFragment Callbacks -----------------
     */
    @Override
    public void SendNameData(NameListItem item, int position) {
        // When the dialog returns, we must add the name to
        // the structure and inform the name fragment
        if (position >= 0) {
            m_nameEntry.set(position, item);
        } else {
            m_nameEntry.add(item);

        }

        // Sort the List Alphabetically
        Collections.sort(m_nameEntry, new Comparator<NameListItem>() {
            public int compare(NameListItem item1, NameListItem item2) {
                return item1.getName().compareTo(item2.getName());
            }
        });

        UpdateNameListFragment();

    }

    @Override
    public void InitSearchView(SearchView searchView) {
        // initialize SearchView
        initSearchView(searchView);

    }

    /**
     * ----------------------- EntryListFragment Functions -----------------
     */
    private void UpdateEntryListFragment(int namePosition,
                                         boolean shouldCalculateBalance) {
        // When the dialog returns, we must add the entry to
        // the structure and inform the entry fragment
        entryListFragment.SetData(namePosition, m_nameEntry.get(namePosition)
                .getEntryArray());

        if (shouldCalculateBalance) {

            CalculateBalance(namePosition);
        }

    }

    /**
     * ----------------------- EntryListFragment Callbacks -----------------
     */
    @Override
    public void AddNewListEntryClicked(int namePosition) {
        // The Add New entry actionbar item was clicked so we need
        // to open the dialog which the user can put in the details
        // of the new entry
        FragmentTransaction ft = fm.beginTransaction();
        EntryListDetailsFragment newEntryDetailsFragment = new EntryListDetailsFragment();
        newEntryDetailsFragment.SetEntryListItem(new EntryListItem(),
                namePosition, -1);

        ft.setCustomAnimations(R.animator.enter_from_right,
                R.animator.exit_to_left, R.animator.enter_from_left,
                R.animator.exit_to_right);
        ft.replace(R.id.mainContainer, newEntryDetailsFragment);
        ft.addToBackStack(null).commit();

    }

    @Override
    public void NavigateBackToHome() {
        // Pop back to the last fragment
        fm.popBackStack();
    }

    @Override
    public void RemoveCheckedEntryListItems(ListView listView,
                                            int nameListPosition) {
        RemoveListViewItems(listView, nameListPosition);

    }

    @Override
    public void EditEntryItem(int namePosition, int entryPosition) {
        FragmentTransaction ft = fm.beginTransaction();
        // Open Dialog so a new entry item can be added to the list
        EntryListDetailsFragment newEntryDetailsFragment = new EntryListDetailsFragment();
        newEntryDetailsFragment.SetEntryListItem(m_nameEntry.get(namePosition)
                        .getEntryArray().get(entryPosition), namePosition,
                entryPosition);

        ft.setCustomAnimations(R.animator.enter_from_right,
                R.animator.exit_to_left, R.animator.enter_from_left,
                R.animator.exit_to_right);
        ft.replace(R.id.mainContainer, newEntryDetailsFragment);
        ft.addToBackStack(null).commit();

    }

    /**
     * ----------------------- EntryDetailFragment Callbacks -----------------
     */

    @Override
    public void SendEntryItemData(EntryListItem item, int namePosition,
                                  int entryPosition) {
        // Update the Entry Fragment with new data
        if (entryPosition >= 0) {
            m_nameEntry.get(namePosition).getEntryArray()
                    .set(entryPosition, item);
        } else {
            m_nameEntry.get(namePosition).getEntryArray().add(item);
        }

        // Sort the List by Dates
        Collections.sort(m_nameEntry.get(namePosition).getEntryArray(),
                new Comparator<EntryListItem>() {
                    public int compare(EntryListItem item1, EntryListItem item2) {
                        return (item1.getCurrentDate().getTime()
                                .compareTo(item2.getCurrentDate().getTime()));
                    }
                });

        RecalculateLatestItems(namePosition);
        UpdateEntryListFragment(namePosition, true);

    }

    private void RecalculateLatestItems(int namePosition) {
        ArrayList<EntryListItem> entries = m_nameEntry.get(namePosition)
                .getEntryArray();
        int listSize = m_nameEntry.get(namePosition).getListOfLatestEntries()
                .getMaxLatestItemSize();
        String[] strArray = new String[listSize];

        for (int i = 0; i < entries.size() && i < listSize; i++) {
            strArray[i] = entries.get(i).getTitle();
        }

        m_nameEntry.get(namePosition).getListOfLatestEntries()
                .recalculateNewList(strArray);

    }

    @Override
    public void NavigateBack() {
        // Pop back to the last fragment
        fm.popBackStack();
    }

    /**
     * ----------------------------- Functions ------------------------------
     */
    private void CalculateBalance(int namePosition) {
        // Calculate how much the person is owed, from the sum of entries
        double balanceSum = 0.0;

        for (int i = 0; i < m_nameEntry.get(namePosition).getEntryArray()
                .size(); i++) {

            balanceSum += m_nameEntry.get(namePosition)
                    .getEntryArray().get(i).getPrice();
        }

        m_nameEntry.get(namePosition).setBalance(balanceSum);
    }

    private void SaveDataStructure() {
        InternalDataManager dataManager = new InternalDataManager(getVersionCode());
        dataManager.SaveData(m_nameEntry, this);
    }

    private void LoadDataStructure() {
        InternalDataManager dataManager = new InternalDataManager(getVersionCode());
        m_nameEntry.clear();
        m_nameEntry.addAll(dataManager.LoadData(this));
        UpdateNameListFragment();

    }

    private void RemoveListViewItems(ListView listView,
                                     final int nameListPosition) {
        // Get array of list items that are checked
        SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
        int numCheckedItems = listView.getCheckedItemCount();

        // iterate through each checked item
        for (int i = numCheckedItems - 1; i >= 0; --i) {
            if (!checkedItems.valueAt(i)) {
                continue;
            }

            final int position = checkedItems.keyAt(i);

            if (nameListPosition == -1) {

                m_nameEntry.remove(position);
                UpdateNameListFragment();
            } else {
                m_nameEntry.get(nameListPosition).getEntryArray()
                        .remove(position);
                RecalculateLatestItems(nameListPosition);
                UpdateEntryListFragment(nameListPosition, true);
            }
        }
    }

    private void initSearchView(SearchView searchView) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo = searchManager
                .getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(searchableInfo);
    }

    private String getContactName(Intent intent) {
        Cursor phoneCursor = getContentResolver().query(intent.getData(), null,
                null, null, null);
        phoneCursor.moveToFirst();
        int colNameIndex = phoneCursor
                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        String contactName = phoneCursor.getString(colNameIndex);
        phoneCursor.close();
        return contactName;
    }

    private int getVersionCode() {
        int versionCode = -1;
        PackageInfo pInfo = new PackageInfo();
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (pInfo != null) {
                versionCode = pInfo.versionCode;
            }
        }

        return versionCode;
    }


    @Override
    public void OpenCurrencyDialog() {
        CurrencyDialogFragment frag = new CurrencyDialogFragment();
        frag.show(fm, "Currency Dialog");
    }

    @Override
    public void SetCurrency(String currency) {
        this.currency = currency;
        Toast.makeText(this, "Currency: " + currency, Toast.LENGTH_SHORT).show();
    }

    public void CheckIfNewUpdate() {
        SharedPreferences settings = getSharedPreferences(getString(R.string.sharedPref), 0);
        if (getVersionCode() != settings.getInt(getString(R.string.keyVersion), 0)) {
            // This is a new version update

            //Recalculate balances
            for (int i = 0; i < m_nameEntry.size(); i++) {
                CalculateBalance(i);
            }
        }
    }
}
