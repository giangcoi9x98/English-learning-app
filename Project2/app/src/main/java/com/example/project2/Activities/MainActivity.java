package com.example.project2.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.example.project2.Adapters.ToeicExpandaleListAdapter;
import com.example.project2.Models.CategoryModel;
import com.example.project2.Models.TopicModel;
import com.example.project2.R;
import com.example.project2.databases.DataBaseUtils;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private ExpandableListView lvTopics;
    private ToeicExpandaleListAdapter toeicExpandaleListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DataBaseUtils.getInstance(this).getListTopic();
        lvTopics = findViewById(R.id.lv_topic);
        List<TopicModel>topicModels=DataBaseUtils.getInstance(this).getListTopic();
        List<CategoryModel>categoryModels=DataBaseUtils.getInstance(this).getListCategory(topicModels);
        HashMap<String,List<TopicModel>>hashMap=DataBaseUtils.getInstance(this).getHashMapTopic(topicModels,categoryModels);
        toeicExpandaleListAdapter=new ToeicExpandaleListAdapter(categoryModels,hashMap);
//        toeicExpandaleListAdapter = new ToeicExpandaleListAdapter(DataBaseUtils.getInstance(this)
//                .getListCategory(DataBaseUtils.getInstance(this).getListTopic()),
//                DataBaseUtils.getInstance(this).getHashMapTopic(DataBaseUtils.getInstance(this).getListTopic(),
//                        DataBaseUtils.getInstance(this).getListCategory(DataBaseUtils.getInstance(this).getListTopic())));

        lvTopics.setAdapter(toeicExpandaleListAdapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
        toeicExpandaleListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(this,SettingActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}
