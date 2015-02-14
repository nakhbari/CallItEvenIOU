package com.nakhbari.calliteven;

import java.util.ArrayList;

public class NameListItem {
    private String m_Name;
    private double m_BalanceDollar;
    private double m_BalanceEuro;
    private double m_BalanceYen;
    private double m_BalancePound;
    private double m_BalanceFranc;
    private ArrayList<EntryListItem> m_EntryArray;
    private ListOfLatestEntryItems m_listOfLatestItems;

    public NameListItem() {
        m_Name = "default";
        m_BalanceDollar = 0;
        m_BalanceEuro = 0;
        m_BalanceYen = 0;
        m_BalancePound = 0;
        m_BalanceFranc = 0;
        m_EntryArray = new ArrayList<EntryListItem>();
        m_listOfLatestItems = new ListOfLatestEntryItems();
    }

    public String getName() {
        return m_Name;
    }

    public void setName(String name) {
        this.m_Name = name;
    }

    public double getBalanceDollar() {
        return m_BalanceDollar;
    }

    public void setBalanceDollar(double balance) {
        this.m_BalanceDollar = balance;
    }

    public double getBalanceEuro() {
        return m_BalanceEuro;
    }

    public void setBalanceEuro(double balance) {
        this.m_BalanceEuro = balance;
    }

    public double getBalanceYen() {
        return m_BalanceYen;
    }

    public void setBalanceYen(double balance) {
        this.m_BalanceYen = balance;
    }

    public double getBalancePound() {
        return m_BalancePound;
    }

    public void setBalancePound(double balance) {
        this.m_BalancePound = balance;
    }

    public double getBalanceFranc() {
        return m_BalanceFranc;
    }

    public void setBalanceFranc(double balance) {
        this.m_BalanceFranc = balance;
    }

    public ArrayList<EntryListItem> getEntryArray() {
        return m_EntryArray;
    }

    public void setEntryArray(ArrayList<EntryListItem> array) {
        this.m_EntryArray = array;
    }

    public void setListOfLatestEntries(ListOfLatestEntryItems list) {
        m_listOfLatestItems = list;
    }

    public ListOfLatestEntryItems getListOfLatestEntries() {
        return m_listOfLatestItems;
    }
}
