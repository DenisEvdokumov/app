package com.example.teachergradebook.UI.PractieGroupList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Switch;
import android.widget.Toast;

import com.example.teachergradebook.R;

import com.example.teachergradebook.UI.Base.BaseActivity;
import com.example.teachergradebook.UI.Base.BaseContract;
import com.example.teachergradebook.UI.Table.MainActivity;
import com.example.teachergradebook.data.model.Predmet;
import com.example.teachergradebook.data.model.StudentGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PGActivity extends BaseActivity implements PGContract.View {

    @BindView(R.id.expListView)    ExpandableListView expandableListView;
   @BindView(R.id.switch1)
     Switch onlineSwitch;

     Map<String,Predmet> predmetMap = new HashMap<String, Predmet>();
     Map<String,StudentGroup> stringStudentGroupMap = new HashMap<String, StudentGroup>();
    ArrayList<ArrayList<Map<String, String>>> сhildDataList = new ArrayList<>();
    ArrayList<Map<String, String>> groupDataList = new ArrayList<>();

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


        ButterKnife.bind(this);
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
    public void showTable(List<Predmet> predmets,List<StudentGroup> studentGroups) {

        Map<String, String> map;
        // коллекция для групп

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


        // в итоге получится сhildDataList = ArrayList<сhildDataItemList>

        // создаем коллекцию элементов для  групп
        ArrayList<Map<String, String>> сhildDataItemList;
        // заполняем список атрибутов для каждого элемента
        for (Predmet predmet : predmets) {
            // создаем коллекцию элементов для N группы
            predmetMap.put(predmet.getName(),predmet);
            сhildDataItemList = new ArrayList<>();
            for (StudentGroup studentGroup :studentGroups) {
//                groups.get(i*j).setPredmetId(predmets.get(i).getId());
                //studentGroup.setPredmetId(resList.getData().get(i).getId())
                if(studentGroup.getPredmetId()==Long.valueOf(predmet.getId())) {
                    stringStudentGroupMap.put(studentGroup.getName(),studentGroup);
                    map = new HashMap<>();
                    map.put("monthName", studentGroup.getName()); // название месяца
                    сhildDataItemList.add(map);
                }
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
                //Toast.makeText(PGActivity.this,groupPosition + " " + childPosition,Toast.LENGTH_SHORT).show();
                Log.i("1",сhildDataList.get(groupPosition).get(childPosition).get("monthName")+ " " + groupDataList.get(groupPosition).get("groupName"));
                Intent intent = new Intent(PGActivity.this, MainActivity.class);

                            intent.putExtra("groupId",
                                    stringStudentGroupMap.get(сhildDataList.get(groupPosition).get(childPosition).get("monthName")).getId());

                            intent.putExtra("predmetId",
                                    predmetMap.get(groupDataList.get(groupPosition).get("groupName")).getId());
                            intent.putExtra("token",token);
                            intent.putExtra("onlineRequired",onlineRequired);
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
