package com.nakhbari.calliteven;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class NameListAdapter extends ArrayAdapter<NameListItem> {

    // List of names
    private ArrayList<NameListItem> objects;
    private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();
    private String currency = "";


    /*
     * here we must override the constructor for ArrayAdapter the only variable
     * we care about now is ArrayList<Item> objects, because it is the list of
     * objects we want to display.
     */
    public NameListAdapter(Context context, int textViewResourceId,
                           ArrayList<NameListItem> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    public static String FormatDoubleToString(double d) {
        if (d == (int) d)
            return String.format("%d", (int) d);
        else
            return String.format("%.2f", d);
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

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

    public void clearSelection() {
        mSelection = new HashMap<Integer, Boolean>();
        notifyDataSetChanged();
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        // Create a new viewholder to store data
        ViewHolder holder = new ViewHolder();

        // assign the view we are converting to a local variable
        View view = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_name_list, parent, false);
            holder.name = (TextView) view.findViewById(R.id.nameText);
            holder.latestEntries = (TextView) view
                    .findViewById(R.id.tvLatestItems);
            holder.divLine2
                    = (TextView) view.findViewById(R.id.divLine2);
            holder.owesWho = (TextView) view
                    .findViewById(R.id.tvOwing);
            holder.balance = (TextView) view
                    .findViewById(R.id.tvBalance);
            holder.circleImage = (ImageView) view
                    .findViewById(R.id.nameItemCircle);

            // set fonts
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                    "fonts/Roboto-Light.ttf");
            Typeface tfItalic = Typeface.createFromAsset(getContext()
                    .getAssets(), "fonts/Roboto-LightItalic.ttf");

            holder.name.setTypeface(tf);
            holder.latestEntries.setTypeface(tfItalic);

            holder.balance.setTypeface(tf);
            holder.owesWho.setTypeface(tf);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // Sliding in animation if its not the same position and the item isn't
        // clicked
        if (position != holder.lastPosition && mSelection.get(position) == null) {
            holder.lastPosition = position;
        }

		/*
         * Recall that the variable position is sent in as an argument to this
		 * method. The variable simply refers to the position of the current
		 * object in the list. (The ArrayAdapter iterates through the list we
		 * sent it)
		 * 
		 * Therefore, i refers to the current Item object.
		 */

        if (mSelection.get(position) != null) {
            view.setBackgroundResource(R.color.list_item_long_press); // this is
            // a selected position so make it dark blue
        } else {
            view.setBackgroundResource(android.R.color.transparent);
        }

        NameListItem i = objects.get(position);

        if (i != null) {

            // Check to see if each individual name is null
            if (holder.name != null) {
                holder.name.setText(i.getName());
            }

            double balance = i.getBalance();

            // Manage what happens with the balance
            if (holder.balance != null) {

                if (balance < 0) {

                    holder.balance.setVisibility(View.VISIBLE);
                    holder.owesWho.setText("Is Owed");
                    holder.balance.setText(
                            currency
                                    + FormatDoubleToString(Math
                                    .abs(balance)));

                } else if (balance > 0) {

                    holder.balance.setVisibility(View.VISIBLE);
                    holder.owesWho.setText("Owes You");
                    holder.balance.setText(
                            currency
                                    + FormatDoubleToString(Math
                                    .abs(balance)));

                } else {
                    // No Money Owed
                    holder.balance.setVisibility(View.GONE);

                    if (i.getListOfLatestEntries().isEmpty()) {
                        // Nothing is owed

                        holder.owesWho.setText(
                                R.string.no_balance);
                    } else {
                        holder.owesWho.setText("Object Lent");
                    }
                }
            }


            // Manage Latest Items
            if (i.getListOfLatestEntries().isEmpty()) {

                holder.latestEntries.setVisibility(View.GONE);
                holder.divLine2.setVisibility(View.GONE);
                holder.owesWho.setTextColor(
                        getContext().getResources().getColor(R.color.green));
                holder.circleImage.setImageResource(R.drawable.ic_check);
                holder.circleImage.setBackgroundResource(
                        R.drawable.green_circle);
            } else {

                holder.latestEntries.setText(i.getListOfLatestEntries()
                        .getList());
                holder.latestEntries.setVisibility(View.VISIBLE);
                holder.divLine2.setVisibility(View.VISIBLE);

                if (balance == 0) {

                    holder.owesWho.setTextColor(
                            getContext().getResources().getColor(
                                    R.color.actionbar_background));
                    holder.circleImage.setImageResource(
                            R.drawable.ic_arrow_forward);
                    holder.circleImage.setBackgroundResource(
                            R.drawable.blue_circle);
                } 
            }

        }


        // Return the view to the activity
        return view;

    }

    static class ViewHolder {
        TextView name;
        TextView owesWho;
        TextView balance;
        TextView latestEntries;
        TextView divLine2;
        ImageView circleImage;
        int lastPosition;
    }
}
