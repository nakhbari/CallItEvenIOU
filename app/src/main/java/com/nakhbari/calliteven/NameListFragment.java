package com.nakhbari.calliteven;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

public class NameListFragment extends ListFragment {

    private ArrayList<NameListItem> m_nameEntry = new ArrayList<NameListItem>();
    NameListCommunicator activityCommunicator;
    private NameListAdapter m_Adapter;
    ImageButton bAddNew;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.fragment_name_list, container, false);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
//        getActivity().getActionBar().setTitle(R.string.app_name);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);

        bAddNew = (ImageButton) view.findViewById(R.id.nameListFloatingButton);
        bAddNew.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                activityCommunicator.AddNewNameEntryClicked();

            }

        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        // Setting the custom adapter which will handle the inflation of the
        // individual rows.
        m_Adapter = new NameListAdapter(getActivity(), R.layout.row_name_list,
                m_nameEntry);
        setListAdapter(m_Adapter);

        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        getListView().setMultiChoiceModeListener(new MultiChoiceModeListener() {
            private int nr = 0;

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                if (nr == 1) {
                    menu.findItem(R.id.name_item_edit).setVisible(true);
                } else {

                    menu.findItem(R.id.name_item_edit).setVisible(false);
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // TODO Auto-generated method stub
                m_Adapter.clearSelection();

                // Show Add Button
                bAddNew.animate().translationY(0).start();
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub

                nr = 0;
                getActivity().getMenuInflater().inflate(R.menu.name_list_contextual_menu,
                        menu);

                // Hide Add Button
                bAddNew.animate().translationY(700).start();
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // TODO Auto-generated method stub
                switch (item.getItemId()) {

                    case R.id.name_item_delete:
                        nr = 0;
                        activityCommunicator
                                .RemoveCheckedNameListItems(getListView());
                        m_Adapter.clearSelection();
                        mode.finish();
                        break;

                    case R.id.name_item_edit:
                        int pos = m_Adapter.getCurrentCheckedPosition();
                        if (pos >= 0) {

                            activityCommunicator.EditNameEntry(pos);
                        }

                        m_Adapter.clearSelection();
                        mode.finish();
                        break;
                }

                return false;
            }

            @Override
            public void onItemCheckedStateChanged(ActionMode mode,
                                                  int position, long id, boolean checked) {
                // TODO Auto-generated method stub
                if (checked) {
                    nr++;
                    m_Adapter.setNewSelection(position, checked);

                } else {
                    nr--;
                    m_Adapter.removeSelection(position);
                }
                mode.setTitle(nr + " selected");

                mode.invalidate();

            }
        });

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        activityCommunicator.NameListItemClicked(position);
    }

    /** ----------------------- Activity Functions --------------------------- */

    public void SetNameListFragment(ArrayList<NameListItem> array) {
        m_nameEntry.clear();
        m_nameEntry.addAll(array);
        if (m_Adapter != null) {

            m_Adapter.notifyDataSetChanged();
        }
    }

    /** ----------------------- Activity Interface --------------------------- */

    @Override
    public void onAttach(Activity activity) {
        // Attach the interface to the activity
        super.onAttach(activity);
        try {
            activityCommunicator = (NameListCommunicator) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NameListCommunicator");
        }
    }

    public interface NameListCommunicator {
        public void AddNewNameEntryClicked();

        public void NameListItemClicked(int position);

        public void RemoveCheckedNameListItems(ListView list);

        public void EditNameEntry(int position);
    }

    /** --------------- Contextual Action Menu Interface ------------------ */

}
