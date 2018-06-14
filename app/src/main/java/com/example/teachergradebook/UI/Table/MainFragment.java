package com.example.teachergradebook.UI.Table;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.filter.Filter;
import com.evrencoskun.tableview.pagination.Pagination;

import java.util.List;

import com.example.teachergradebook.R;
import com.example.teachergradebook.UI.Table.TableView.TableViewAdapter;
import com.example.teachergradebook.data.model.Practice;

/**
 * Created by Денис on 19.02.2018.
 */

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    public static final int COLUMN_SIZE = 100;
    public static final int ROW_SIZE = 100;

    private List<com.example.teachergradebook.data.model.Student> mRowHeaderList;
    private List<Practice> mColumnHeaderList;
    //private List<List<Student>> mCellList;

    private AbstractTableAdapter mTableViewAdapter;
    private TableView mTableView;
    private Filter mTableFilter; // This is used for filtering the table.
    private Pagination mPagination; // This is used for paginating the table.

    private MainActivity mainActivity;


    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        RelativeLayout fragment_container = view.findViewById(R.id.fragment_container);

        // Create Table view
        mTableView = createTableView();

        fragment_container.addView(mTableView);

        return view;
    }

    private TableView createTableView() {
        TableView tableView = new TableView(getContext());

        // Set adapter
//        mTableViewAdapter = new TableViewAdapter(getContext(),presenter);
//        tableView.setAdapter(mTableViewAdapter);
//        //mTableViewAdapter.setRowHeaderItems();

        // Disable shadow
        //tableView.getSelectionHandler().setShadowEnabled(false);

        // Set layout params
        FrameLayout.LayoutParams tlp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams
                .MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        tableView.setLayoutParams(tlp);

        // Set TableView listener
        //tableView.setTableViewListener(new TableViewListener(tableView));
        return tableView;
    }


}
