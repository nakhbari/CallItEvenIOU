package com.nakhbari.calliteven;

import java.util.ArrayList;
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
import android.widget.ImageView;
import android.widget.TextView;

public class NameListAdapter extends ArrayAdapter<NameListItem> {

    // List of names
    private ArrayList<NameListItem> objects;
    private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();

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
     * here we must override the constructor for ArrayAdapter the only variable
     * we care about now is ArrayList<Item> objects, because it is the list of
     * objects we want to display.
     */
    public NameListAdapter(Context context, int textViewResourceId,
                           ArrayList<NameListItem> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    static class ViewHolder {
        TextView name;
        ArrayList<TextView> owesWho = new ArrayList<TextView>();
        ArrayList<TextView> balance = new ArrayList<TextView>();
        TextView latestEntries;
        ArrayList<TextView> divLine = new ArrayList<TextView>();
        ArrayList<ImageView> circleImage = new ArrayList<ImageView>();
        int lastPosition;
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
            holder.divLine
                    .add((TextView) view.findViewById(R.id.divLineDollar));
            holder.owesWho.add((TextView) view
                    .findViewById(R.id.tvOwingTextDollar));
            holder.balance.add((TextView) view
                    .findViewById(R.id.tvBalanceTextDollar));
            holder.circleImage.add((ImageView) view
                    .findViewById(R.id.nameItemCircleDollar));

            holder.divLine.add((TextView) view.findViewById(R.id.divLineEuro));
            holder.owesWho.add((TextView) view
                    .findViewById(R.id.tvOwingTextEuro));
            holder.balance.add((TextView) view
                    .findViewById(R.id.tvBalanceTextEuro));
            holder.circleImage.add((ImageView) view
                    .findViewById(R.id.nameItemCircleEuro));
            holder.divLine.add((TextView) view.findViewById(R.id.divLineYen));
            holder.owesWho.add((TextView) view
                    .findViewById(R.id.tvOwingTextYen));
            holder.balance.add((TextView) view
                    .findViewById(R.id.tvBalanceTextYen));
            holder.circleImage.add((ImageView) view
                    .findViewById(R.id.nameItemCircleYen));
            holder.divLine.add((TextView) view.findViewById(R.id.divLinePound));
            holder.owesWho.add((TextView) view
                    .findViewById(R.id.tvOwingTextPound));
            holder.balance.add((TextView) view
                    .findViewById(R.id.tvBalanceTextPound));
            holder.circleImage.add((ImageView) view
                    .findViewById(R.id.nameItemCirclePound));
            holder.divLine.add((TextView) view.findViewById(R.id.divLineFranc));
            holder.owesWho.add((TextView) view
                    .findViewById(R.id.tvOwingTextFranc));
            holder.balance.add((TextView) view
                    .findViewById(R.id.tvBalanceTextFranc));
            holder.circleImage.add((ImageView) view
                    .findViewById(R.id.nameItemCircleFranc));

            // set fonts
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                    "fonts/Roboto-Light.ttf");
            Typeface tfItalic = Typeface.createFromAsset(getContext()
                    .getAssets(), "fonts/Roboto-LightItalic.ttf");

            holder.name.setTypeface(tf);
            holder.latestEntries.setTypeface(tfItalic);

            for (int i = 0; i < holder.balance.size(); i++) {
                holder.balance.get(i).setTypeface(tf);
                holder.owesWho.get(i).setTypeface(tf);
            }

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
            // a
            // selected
            // position
            // so
            // make
            // it
            // dark
            // bluew
        } else {
            view.setBackgroundResource(android.R.color.transparent);
        }

        NameListItem i = objects.get(position);

        if (i != null) {

            // Check to see if each individual name is null
            if (holder.name != null) {
                holder.name.setText(i.getName());
            }

            double balance[] = {i.getBalanceDollar(), i.getBalanceEuro(),
                    i.getBalanceYen(), i.getBalancePound(), i.getBalanceFranc()};
            String currency[] = getContext().getResources().getStringArray(
                    R.array.currency_array);
            // Manage what happens with the balance
            if (holder.balance != null) {
                for (int j = 0; j < 5; j++) {

                    if (balance[j] < 0) {

                        holder.balance.get(j).setVisibility(View.VISIBLE);
                        holder.owesWho.get(j).setText("Is Owed");
                        holder.balance.get(j).setText(
                                currency[j]
                                        + FormatDoubleToString(Math
                                        .abs(balance[j])));

                    } else if (balance[j] > 0) {

                        holder.balance.get(j).setVisibility(View.VISIBLE);
                        holder.owesWho.get(j).setText("Owes You");
                        holder.balance.get(j).setText(
                                currency[j]
                                        + FormatDoubleToString(Math
                                        .abs(balance[j])));

                    } else {
                        // No Money Owed
                        holder.balance.get(j).setVisibility(View.GONE);

                        if (j == 0) {
                            if (i.getListOfLatestEntries().isEmpty()) {
                                // Nothing is owed

                                holder.owesWho.get(j).setText(
                                        R.string.no_balance);
                            } else {
                                holder.owesWho.get(j).setText("Object Lent");
                            }
                        } else {

                            holder.divLine.get(j).setVisibility(View.GONE);
                            holder.circleImage.get(j).setVisibility(View.GONE);
                            holder.owesWho.get(j).setVisibility(View.GONE);
                        }

                    }
                }
            }

            // Manage Latest Items
            if (i.getListOfLatestEntries().isEmpty()) {

                holder.latestEntries.setVisibility(View.GONE);
                holder.divLine.get(0).setVisibility(View.GONE);
                holder.owesWho.get(0).setTextColor(
                        getContext().getResources().getColor(R.color.green));
                holder.circleImage.get(0).setImageResource(R.drawable.ic_check);
                holder.circleImage.get(0).setBackgroundResource(
                        R.drawable.green_circle);
            } else {

                holder.latestEntries.setText(i.getListOfLatestEntries()
                        .getList());
                holder.latestEntries.setVisibility(View.VISIBLE);
                holder.divLine.get(0).setVisibility(View.VISIBLE);

                if (balance[1] + balance[2] + balance[3] + balance[4] == 0) {

                    holder.owesWho.get(0).setTextColor(
                            getContext().getResources().getColor(
                                    R.color.actionbar_background));
                    holder.circleImage.get(0).setImageResource(
                            R.drawable.ic_arrow_forward);
                    holder.circleImage.get(0).setBackgroundResource(
                            R.drawable.blue_circle);
                } else {

                    holder.divLine.get(0).setVisibility(View.GONE);
                    holder.circleImage.get(0).setVisibility(View.GONE);
                    holder.owesWho.get(0).setVisibility(View.GONE);
                }
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
