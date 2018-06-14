package com.example.teachergradebook.UI.PractieGroupList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Switch;
import android.widget.Toast;

import com.example.teachergradebook.R;

import com.example.teachergradebook.UI.Authentication.AuthActivity;
import com.example.teachergradebook.UI.Base.BaseActivity;
import com.example.teachergradebook.UI.Base.BaseContract;
import com.example.teachergradebook.UI.Table.MainActivity;
import com.example.teachergradebook.data.model.Predmet;
import com.example.teachergradebook.data.model.Res;
import com.example.teachergradebook.data.model.ResList;
import com.example.teachergradebook.data.model.Student;
import com.example.teachergradebook.data.model.StudentGroup;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PGActivity extends BaseActivity implements PGContract.View {

    @BindView(R.id.expListView)    ExpandableListView expandableListView;
    @BindView(R.id.switch1)    Switch onlineSwitch;


    Long userId;
    String token,password,login;
    Boolean onlineRequired=false;

    @Inject
    PGPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pg_activity);
        Intent intent = getIntent();
        userId = intent.getExtras().getLong("userId",0);
        token = intent.getExtras().getString("token","");
        login = intent.getExtras().getString("login","");
        password = intent.getExtras().getString("password","");
        onlineRequired = intent.getExtras().getBoolean("onlineRequired",false);



        onlineSwitch.setChecked(onlineRequired);
        onlineSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    presenter.login(login,password);
                }else {

                }
            }
        });

        ButterKnife.bind(this);
        initPresenter();
        presenter.loadTable(onlineRequired,token,userId);


    }





    @Override
    protected BaseContract.Presenter initPresenter() {
        DaggerPGComonent.builder()
                .pGPresenterModule(new PGPresenterModule(this))
                .tableRepositoryComponent(getTableRepositoryComponent())
                .build()
                .inject(this);
        return presenter;
    }




    @Override
    protected void onDestroy() {
        presenter = null;
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    public void onClickListener(View view) {
        switch (view.getId()){
            default:
                break;
        }
    }




    @Override
    public void showTable(List<Predmet> predmets) {
        for(Predmet predmet : predmets){
            if(predmet.getUserId()!=userId) predmets.remove(predmet);
        }
        List<StudentGroup> groups = new ArrayList<>();
        for(int i=0;i<predmets.size()*3;i++){
            StudentGroup studentGroup = new StudentGroup();
            studentGroup.setName("IO-"+i);
            studentGroup.setId(i);
            groups.add(studentGroup);

        }

        Map<String, String> map;
        // коллекция для групп
        ArrayList<Map<String, String>> groupDataList = new ArrayList<>();
        // заполняем коллекцию групп из массива с названиями групп

        for (Predmet predmet : predmets) {
            // заполняем список атрибутов для каждой группы
            map = new HashMap<>();
            map.put("groupName", predmet.getName()); // время года
            groupDataList.add(map);
        }

        // список атрибутов групп для чтения
        String groupFrom[] = new String[] { "groupName" };
        // список ID view-элементов, в которые будет помещены атрибуты групп
        int groupTo[] = new int[] { android.R.id.text1 };

        // создаем общую коллекцию для коллекций элементов
        ArrayList<ArrayList<Map<String, String>>> сhildDataList = new ArrayList<>();

        // в итоге получится сhildDataList = ArrayList<сhildDataItemList>

        // создаем коллекцию элементов для  групп
        ArrayList<Map<String, String>> сhildDataItemList;
        // заполняем список атрибутов для каждого элемента
        for (int i =0;i<predmets.size();i++) {
            // создаем коллекцию элементов для N группы
            сhildDataItemList = new ArrayList<>();
            for (int j=0;j<3;j++) {
                groups.get(i*j).setPredmetId(predmets.get(i).getId());
                //studentGroup.setPredmetId(resList.getData().get(i).getId());
                map = new HashMap<>();
                map.put("monthName", groups.get(i*j).getName()); // название месяца
                сhildDataItemList.add(map);
            }
            // добавляем в коллекцию коллекций
            сhildDataList.add(сhildDataItemList);
        }
        // создаем коллекцию элементов для второй группы


        // список атрибутов элементов для чтения
        String childFrom[] = new String[] { "monthName" };
        // список ID view-элементов, в которые будет помещены атрибуты
        // элементов
        int childTo[] = new int[] { android.R.id.text1 };





        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                this, groupDataList,
                android.R.layout.simple_expandable_list_item_1, groupFrom,
                groupTo, сhildDataList, android.R.layout.simple_list_item_1,
                childFrom, childTo);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(PGActivity.this,groupPosition + " " + childPosition,Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(PGActivity.this, MainActivity.class);
                        if(childPosition==0){
                            intent.putExtra("groupId","1");
                        }
                        if(childPosition==1){
                            intent.putExtra("groupId","8");
                        }
                        if(childPosition==2){
                            intent.putExtra("groupId","15");
                        }
                            intent.putExtra("courceId","1");
                            intent.putExtra("token",token);
                startActivity(intent);
               // presenter.logout(token);
                return false;


            }
        });
        expandableListView.setAdapter(adapter);

    }

    @Override
    public void showErrorMessage(String error) {
        Log.i("1",error);
    }

    @Override
    public void clearQuestions() {

    }

    @Override
    public void showNoDataMessage() {

    }

    @Override
    public void stopLoadingIndicator() {

    }

    @Override
    public void showError(String message) {
        Log.i("1",message);
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
}
