package com.example.teachergradebook.UI.Table.TableView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

import com.example.teachergradebook.R;

import com.example.teachergradebook.UI.Table.TablePresenter;
import com.example.teachergradebook.data.model.Grade;
import com.example.teachergradebook.data.model.Practice;
import com.example.teachergradebook.data.model.Student;
import com.example.teachergradebook.UI.Table.holder.CellViewHolder;
import com.example.teachergradebook.UI.Table.holder.ColumnHeaderViewHolder;
//import com.example.teachergradebook.UI.Table.holder.MoodCellViewHolder;
import com.example.teachergradebook.UI.Table.holder.RowHeaderViewHolder;

/**
 * Created by Денис on 19.02.2018.
 */


public class TableViewAdapter extends AbstractTableAdapter<Practice, Student, Grade> {

    // Student View Types by Column Position
    private static final int MOOD_CELL_TYPE = 1;
    private static final int GENDER_CELL_TYPE = 2;
    // add new one if it necessary..

    private static final String LOG_TAG = TableViewAdapter.class.getSimpleName();
    private TablePresenter presenter;

    public TableViewAdapter(Context p_jContext, TablePresenter presenter) {
        super(p_jContext);
        this.presenter = presenter;

    }


    @Override
    public RecyclerView.ViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        View layout;
        EditText editText;
        layout = LayoutInflater.from(mContext).inflate(R.layout.table_view_cell_layout,
                        parent, false);
        editText = layout.findViewById(R.id.cell_data);
       // editText.addTextChangedListener(new MyCustomEditTextListener(editText));

        return new CellViewHolder(layout);


    }


    @Override
    public void onBindCellViewHolder(AbstractViewHolder holder, Object cellItemModel, int
            columnPosition, int rowPosition) {
        Grade grade = (Grade) cellItemModel;

//             Get the holder to update student item text

        CellViewHolder viewHolder = (CellViewHolder) holder;


//        viewHolder.cell_textview

        viewHolder.setData(grade.getName());
        viewHolder.cell_textview.addTextChangedListener(new MyCustomEditTextListener(columnPosition,rowPosition));

    }


    @Override
    public RecyclerView.ViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {

        // Get Column Header xml Layout
        View layout = LayoutInflater.from(mContext).inflate(R.layout
                .table_view_column_header_layout, parent, false);

        // Create a Practice ViewHolder
        return new ColumnHeaderViewHolder(layout, getTableView());
    }


    @Override
    public void onBindColumnHeaderViewHolder(AbstractViewHolder holder, Object
            columnHeaderItemModel, int columnPosition) {
        Practice practice = (Practice) columnHeaderItemModel;

        // Get the holder to update cell item text
        ColumnHeaderViewHolder columnHeaderViewHolder = (ColumnHeaderViewHolder) holder;
        columnHeaderViewHolder.setColumnHeader(practice);
    }


    @Override
    public RecyclerView.ViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {

        // Get Row Header xml Layout
        View layout = LayoutInflater.from(mContext).inflate(R.layout
                .table_view_row_header_layout, parent, false);

        // Create a Row Header ViewHolder
        return new RowHeaderViewHolder(layout);
    }


    @Override
    public void onBindRowHeaderViewHolder(AbstractViewHolder holder, Object rowHeaderItemModel,
                                          int rowPosition) {
        Student student = (Student) rowHeaderItemModel;

        // Get the holder to update row header item text
        RowHeaderViewHolder rowHeaderViewHolder = (RowHeaderViewHolder) holder;
        rowHeaderViewHolder.row_header_textview.setText(String.valueOf(student.getName()));
    }


    @Override
    public View onCreateCornerView() {
        // Get Corner xml layout
        View corner = LayoutInflater.from(mContext).inflate(R.layout.table_view_corner_layout, null);
//        corner.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SortState sortState = TableViewAdapter.this.getTableView().getRowHeaderSortingStatus();
//                if(sortState != SortState.ASCENDING) {
//                    Log.d("TableViewAdapter", "Order Ascending");
//                    TableViewAdapter.this.getTableView().sortRowHeader(SortState.ASCENDING);
//                } else {
//                    Log.d("TableViewAdapter", "Order Descending");
//                    TableViewAdapter.this.getTableView().sortRowHeader(SortState.DESCENDING);
//                }
//            }
//        });
        return corner;
    }

    @Override
    public int getColumnHeaderItemViewType(int position) {
        // The unique ID for this type of column header item
        // If you have different items for Student View by X (Column) position,
        // then you should fill this method to be able create different
        // type of CellViewHolder on "onCreateCellViewHolder"
        return 0;
    }

    @Override
    public int getRowHeaderItemViewType(int position) {
        // The unique ID for this type of row header item
        // If you have different items for Row Header View by Y (Row) position,
        // then you should fill this method to be able create different
        // type of RowHeaderViewHolder on "onCreateRowHeaderViewHolder"
        return 0;
    }

    @Override
    public int getCellItemViewType(int column) {
        // The unique ID for this type of cell item
        // If you have different items for Student View by X (Column) position,
        // then you should fill this method to be able create different
        // type of CellViewHolder on "onCreateCellViewHolder"

        return 0;

    }

    public class MyCustomEditTextListener implements TextWatcher {
        private int columnPosition;
        private int rawPosition;
        EditText editText;
        String s;

        public MyCustomEditTextListener(EditText editText) {
            this.editText= editText;
        }

        public MyCustomEditTextListener(int columnPosition, int rowPosition) {
            this.columnPosition = columnPosition;
            this.rawPosition = rowPosition;
        }



        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            //TODO  mDataset[position] = charSequence.toString();

            s = charSequence.toString();


        }

        @Override
        public void afterTextChanged(Editable editable) {
            Grade grade = getCellItem(columnPosition,rawPosition);
            grade.setName(s);

            presenter.updateGrade(grade);
            // no op
        }
    }
}
