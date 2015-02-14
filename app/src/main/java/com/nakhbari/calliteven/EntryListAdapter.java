package com.nakhbari.calliteven;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EntryListAdapter extends ArrayAdapter<EntryListItem> {

    // List of names
    private ArrayList<EntryListItem> m_rowEntries;
    private String[] arrayWhoPaid;
    private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();
    private String currency = "";

    public void setNewSelection(int position, boolean value) {
        mSelection.put(position, value);
        notifyDataSetChanged();
    }

    public boolean isPositionChecked(int position) {
        Boolean result = mSelection.get(position);
        return result == null ? false : result;
    }

    public int getCurrentCheckedPosition() {
        Set<Integer> i = mSelection.keySet();
        if (!i.isEmpty()) {
            return i.iterator().next();
        }

        return -1;
    }

    public void removeSelection(int position) {
        mSelection.remove(position);
        notifyDataSetChanged();
    }

    public void setCurrency(String cur){
        currency = cur;
    }

    public void clearSelection() {
        mSelection = new HashMap<Integer, Boolean>();
        notifyDataSetChanged();
    }

    /*
     * here we must override the constructor for ArrayAdapter the only variable
     * we care about now is ArrayList<Item> objects, because it is the list of
     * objects we want to display.
     */
    public EntryListAdapter(Context context, int textViewResourceId,
                            ArrayList<EntryListItem> rowEntries) {
        super(context, textViewResourceId, rowEntries);
        arrayWhoPaid = context.getResources().getStringArray(
                R.array.who_paid_array);
        this.m_rowEntries = rowEntries;
    }

    static class ViewHolder {
        TextView title;
        TextView price;
        TextView currentDate;
        TextView dueDate;
        TextView whoPaid;
        int lastPosition;
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();

        // assign the view we are converting to a local variable
        View view = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_entry_list, null);

            holder.title = (TextView) view.findViewById(R.id.entryTitle);
            holder.price = (TextView) view.findViewById(R.id.entryPrice);
            holder.currentDate = (TextView) view
                    .findViewById(R.id.entryListCurrentDate);
            holder.dueDate = (TextView) view
                    .findViewById(R.id.entryListDueDate);
            holder.whoPaid = (TextView) view.findViewById(R.id.tvWhoPaidEntry);

            //set fonts
            Typeface roboto_reg = Typeface.createFromAsset(getContext().getAssets(),
                    "fonts/Roboto-Regular.ttf");
            holder.title.setTypeface(roboto_reg);
            holder.price.setTypeface(roboto_reg);
            holder.dueDate.setTypeface(roboto_reg);
            holder.currentDate.setTypeface(roboto_reg);
            holder.whoPaid.setTypeface(roboto_reg);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // Sliding in animation
        if (position != holder.lastPosition) {
            holder.lastPosition = position;
        }

        if (mSelection.get(position) != null) {
            view.setBackgroundResource(R.color.list_item_long_press); // this is
            // a
            // selected
            // position
            // so
            // make
            // it
            // dark
            // bluew
        } else {
            view.setBackgroundResource(R.drawable.list_background_normal);
        }
		/*
		 * Recall that the variable position is sent in as an argument to this
		 * method. The variable simply refers to the position of the current
		 * object in the list. (The ArrayAdapter iterates through the list we
		 * sent it)
		 * 
		 * Therefore, i refers to the current Item object.
		 */
        EntryListItem entryItem = m_rowEntries.get(position);

        if (entryItem != null) {

            DateFormat dateFormat = DateFormat.getDateInstance();

            // Check to see if each individual name is null
            if (holder.title != null) {
                holder.title.setText(entryItem.getTitle());
            }

            if (holder.price != null) {
                if (entryItem.getPrice() >= 0) {
                    holder.whoPaid.setText(arrayWhoPaid[0]);
                } else {

                    holder.whoPaid.setText(arrayWhoPaid[1]);
                }

                holder.price.setText(currency
                        + FormatDoubleToString(Math.abs(entryItem.getPrice())));

            }

            if (holder.currentDate != null) {
                holder.currentDate.setText("On: "
                        + (dateFormat.format(entryItem.getCurrentDate()
                        .getTime()).toString()));

            }
            if (holder.dueDate != null) {
                if (entryItem.getDueDate() != null) {
                    holder.dueDate.setVisibility(View.VISIBLE);
                    holder.dueDate.setText("Due: "
                            + (dateFormat.format(entryItem.getDueDate()
                            .getTime()).toString()));
                } else {
                    holder.dueDate.setVisibility(View.GONE);
                }

            }
            // Check to see if the item has any monetary value or is it
            // just an item
            if (entryItem.isItemMonetary()) {
                holder.price.setVisibility(View.VISIBLE);
            } else {
                holder.price.setVisibility(View.GONE);
            }
        }

        // Return the view to the activity
        return view;

    }

    public static String FormatDoubleToString(double d) {
        if (d == (int) d)
            return String.format("%d", (int) d);
        else
            return String.format("%.2f", d);
    }
}
