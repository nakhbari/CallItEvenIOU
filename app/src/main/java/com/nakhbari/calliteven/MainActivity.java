package com.nakhbari.calliteven;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MainActivity extends ActionBarActivity implements NameListFragment.NameListCommunicator,
        NameDialogFragment.NameDialogCommunicator,
        EntryListFragment.EntryListCommunicator,
        EntryListDetailsFragment.EntryListDetailsCommunicator {

    private ArrayList<NameListItem> m_nameEntry = new ArrayList<NameListItem>();

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

//        getVersionCode();


        // Turn off the up button initially
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

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
        double balanceSum[] = {0.0, 0.0, 0.0, 0.0, 0.0};

        for (int i = 0; i < m_nameEntry.get(namePosition).getEntryArray()
                .size(); i++) {

            balanceSum[m_nameEntry.get(namePosition).getEntryArray().get(i)
                    .getCurrencyArrayPos()] += m_nameEntry.get(namePosition)
                    .getEntryArray().get(i).getPrice();
        }

        m_nameEntry.get(namePosition).setBalanceDollar(balanceSum[0]);
        m_nameEntry.get(namePosition).setBalanceEuro(balanceSum[1]);
        m_nameEntry.get(namePosition).setBalanceYen(balanceSum[2]);
        m_nameEntry.get(namePosition).setBalancePound(balanceSum[3]);
        m_nameEntry.get(namePosition).setBalanceFranc(balanceSum[4]);
    }

    private void SaveDataStructure() {
        InternalDataManager dataManager = new InternalDataManager();
        dataManager.SaveData(m_nameEntry, this);
    }

    private void LoadDataStructure() {
        InternalDataManager dataManager = new InternalDataManager();
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
            if (checkedItems.valueAt(i) == false) {
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
}
