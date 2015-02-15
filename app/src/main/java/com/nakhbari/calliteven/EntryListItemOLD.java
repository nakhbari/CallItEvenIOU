package com.nakhbari.calliteven;

import java.util.Calendar;

public class EntryListItemOLD {

    private String m_Title;
    private Double m_Price;
    private Calendar m_CurrentDate;
    private Calendar m_DueDate;
    private boolean m_IsItemMonetary;
    private int m_CurrencyArrayPos;
    private boolean m_DidYouLend;

    public EntryListItemOLD() {
        m_Title = "default";
        m_Price = 0.0;
        m_CurrentDate = Calendar.getInstance();
        m_DueDate = null;
        m_CurrencyArrayPos = 0;
        m_IsItemMonetary = true;
        m_DidYouLend = true;
    }

    public String getTitle() {
        return m_Title;
    }

    public void setTitle(String name) {
        this.m_Title = name;
    }

    public Double getPrice() {
        return m_Price;
    }

    public void setPrice(Double price) {
        this.m_Price = price;
    }

    public Calendar getCurrentDate() {
        return m_CurrentDate;
    }

    public void setCurrentDate(Calendar cal) {
        this.m_CurrentDate = cal;
    }

    public Calendar getDueDate() {
        return m_DueDate;
    }

    public void setDueDate(Calendar cal) {
        this.m_DueDate = cal;
    }

    public boolean isItemMonetary() {
        return m_IsItemMonetary;
    }

    public void setItemMonetary(boolean isMonetary) {
        m_IsItemMonetary = isMonetary;
    }

    public int getCurrencyArrayPos() {
        return m_CurrencyArrayPos;
    }

    public void setCurrencyArrayPos(int curPos) {
        m_CurrencyArrayPos = curPos;
    }

    public boolean didYouLend() {
        return m_DidYouLend;
    }

    public void youLent(boolean bool) {
        m_DidYouLend = bool;

    }
}
