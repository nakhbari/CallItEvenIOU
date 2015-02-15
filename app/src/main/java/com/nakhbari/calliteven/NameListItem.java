package com.nakhbari.calliteven;

import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;

import java.util.ArrayList;

public class NameListItem {
    private String m_Name;
    @Since(4)
    private double m_Balance;
    @Until(4)
    private double m_BalanceEuro;
    @Until(4)
    private double m_BalanceYen;
    @Until(4)
    private double m_BalancePound;
    @Until(4)
    private double m_BalanceFranc;
    private ArrayList<EntryListItem> m_EntryArray;
    private ListOfLatestEntryItems m_listOfLatestItems;

    public NameListItem() {
        m_Name = "default";
        m_Balance = 0;
        m_EntryArray = new ArrayList<EntryListItem>();
        m_listOfLatestItems = new ListOfLatestEntryItems();
    }

    public String getName() {
        return m_Name;
    }

    public void setName(String name) {
        this.m_Name = name;
    }

    public double getBalance() {
        return m_Balance;
    }

    public void setBalance(double balance) {
        this.m_Balance = balance;
    }

    public ArrayList<EntryListItem> getEntryArray() {
        return m_EntryArray;
    }

    public void setEntryArray(ArrayList<EntryListItem> array) {
        this.m_EntryArray = array;
    }

    public ListOfLatestEntryItems getListOfLatestEntries() {
        return m_listOfLatestItems;
    }

    public void setListOfLatestEntries(ListOfLatestEntryItems list) {
        m_listOfLatestItems = list;
    }
}
