package com.example.teachergradebook.UI.Table;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.filter.Filter;
import com.evrencoskun.tableview.pagination.Pagination;
import com.example.teachergradebook.R;
import com.example.teachergradebook.UI.Base.BaseContract;
import com.example.teachergradebook.UI.Table.TableView.TableViewAdapter;
import com.example.teachergradebook.UI.Table.TableView.TableViewListener;
import com.example.teachergradebook.UI.Base.BaseActivity;
import com.example.teachergradebook.data.model.Grade;
import com.example.teachergradebook.data.model.Practice;
import com.example.teachergradebook.data.model.Student;

import java.security.acl.Group;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements TableContract.View {

//    @BindView(R.id.refresh) SwipeRefreshLayout refreshLayout;

    private List<com.example.teachergradebook.data.model.Student> mRowHeaderList;
    private List<Practice> mColumnHeaderList;



    private long STUDENTGROUPID = 1;
    private long PREDMETID= 1;

    private AbstractTableAdapter mTableViewAdapter;
    private TableView mTableView;
    private Filter mTableFilter; // This is used for filtering the table.
    private Pagination mPagination; // This is used for paginating the table.
    String token;
    Boolean onlineRequired;
    @Inject TablePresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        ButterKnife.bind(this);
        initPresenter();

        Intent intent = getIntent();
        token = intent.getExtras().getString("token"," ");
        presenter.token = token;
        presenter.groupId = String.valueOf(intent.getExtras().getLong("groupId"));
        presenter.predmetId = intent.getExtras().getString("predmetId","1");
        onlineRequired = intent.getExtras().getBoolean("onlineRequired",false);
        presenter.onlineRequired = onlineRequired;

                SetupTable();

        presenter.loadTable(onlineRequired);

    }



    private void SetupTable() {
        RelativeLayout fragment_container = findViewById(R.id.fragment_container);
        mTableView = createTableView();
        fragment_container.addView(mTableView);
    }

    @Override
    protected BaseContract.Presenter initPresenter() {
        DaggerTableComonent.builder()
                .tablePresenterModule(new TablePresenterModule(this))
                .tableRepositoryComponent(getTableRepositoryComponent())
                .build()
                .inject(this);
        return presenter;
    }

    private TableView createTableView() {
        Log.i("1","createTale");
        TableView tableView = new TableView(getApplicationContext());

        // Set adapter
        mTableViewAdapter = new TableViewAdapter(getApplicationContext(),presenter);
        tableView.setAdapter(mTableViewAdapter);

//        refreshLayout.setOnRefreshListener(() -> presenter.loadTable(false));

        //mTableViewAdapter.setRowHeaderItems();

        // Disable shadow
        //tableView.getSelectionHandler().setShadowEnabled(false);

        // Set layout params
        FrameLayout.LayoutParams tlp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams
                .MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        tableView.setLayoutParams(tlp);

        // Set TableView listener
        tableView.setTableViewListener(new TableViewListener(tableView));
        return tableView;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Добавить студента");
        menu.add("Добавить практику");
        menu.add("обновить таблицу");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getTitle().toString()){
            case "Добавить студента":
                presenter.addStudent("Petya",STUDENTGROUPID++);
                break;
            case "Добавить практику":
                presenter.addPractice(STUDENTGROUPID,PREDMETID++);
                break;
            case "обновить таблицу":
                //TODO this
//                presenter.loadTable(false);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        //presenter.logout(token);
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    @Override
    public void showGroup(List<Group> groups) {

    }

    @Override
    public void showStudents(List<Student> student) {
//        mTableViewAdapter.setRowHeaderItems(students);
//        mTableViewAdapter.setCellItems(students);
    }

    @Override
    public void showPractice(List<Practice> practice) {

    }

    @Override
    public void showTable(List<Student> students, List<Practice> practices, List<List<Grade>> grades) {

        mTableViewAdapter.setAllItems(practices,students,grades);
        Toast.makeText(this,"refresh",Toast.LENGTH_SHORT).show();


    }

    @Override
    public void clearQuestions() {

    }

    @Override
    public void showNoDataMessage() {
        Log.i("1","nodata");

    }

    @Override
    public void showErrorMessage(String error) {
//        Toast.makeText(this,error,Toast.LENGTH_LONG).show();
        Log.i("1",error);

    }



    @Override
    public void stopLoadingIndicator() {
//        if (refreshLayout.isRefreshing()) {
//            refreshLayout.setRefreshing(false);
//        }

    }

    @Override
    public void showEmptySearchResult() {
    }
}
