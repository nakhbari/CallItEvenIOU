package com.nakhbari.calliteven;

public class ListOfLatestEntryItems {
    private String m_list;
    private static int MAX_PREV_LIST_ITEM_SIZE = 20;

    public ListOfLatestEntryItems() {
        m_list = "";
    }

    public void recalculateNewList(String[] strArray) {
        m_list = "";

        for (int i = 0; i < strArray.length; i++) {

            if (strArray[i] == null) {
                break;
            }

            m_list += strArray[i].substring(0, 1).toUpperCase()
                    + strArray[i].substring(1) + " \u00B7 ";

        }

        if (!m_list.isEmpty()) {
            m_list = m_list.substring(0, m_list.length() - 3);
        }
    }

    public String getList() {
        return m_list;
    }

    public boolean isEmpty() {
        return m_list.isEmpty();
    }

    public int getMaxLatestItemSize() {
        return MAX_PREV_LIST_ITEM_SIZE;
    }
}
