package com.nakhbari.calliteven;

import java.util.Calendar;

public class EntryListItem {

    private String m_Title;
    private Double m_Price;
    private Calendar m_CurrentDate;
    private Calendar m_DueDate;
    private boolean m_IsItemMonetary;
    private int m_CurrencyArrayPos;
    private boolean m_DidYouLend;

    public EntryListItem() {
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

    public Double getPrice() {
        return m_Price;
    }

    public Calendar getCurrentDate() {
        return m_CurrentDate;
    }

    public Calendar getDueDate() {
        return m_DueDate;
    }

    public void setTitle(String name) {
        this.m_Title = name;
    }

    public void setPrice(Double price) {
        this.m_Price = price;
    }

    public void setCurrentDate(Calendar cal) {
        this.m_CurrentDate = cal;
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

    public void setCurrencyArrayPos(int curPos) {
        m_CurrencyArrayPos = curPos;
    }

    public int getCurrencyArrayPos() {
        return m_CurrencyArrayPos;
    }

    public boolean didYouLend() {
        return m_DidYouLend;
    }

    public void youLent(boolean bool) {
        m_DidYouLend = bool;

    }
}
