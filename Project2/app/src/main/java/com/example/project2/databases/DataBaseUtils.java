package com.example.project2.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.project2.Models.CategoryModel;
import com.example.project2.Models.TopicModel;
import com.example.project2.Models.WordModels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DataBaseUtils {
    private final String TAG = "DatabaseUtils";
    private final String TABLE_TOPIC = "tbl_topic";
    private final String TABLE_WORD = "tbl_word";

    public SQLiteDatabase sqLiteDatabase;
    private MyDatabase myDatabase;

    //Singletion
    private static DataBaseUtils dataBaseUtils;


    public DataBaseUtils(Context context) {
        myDatabase = new MyDatabase(context);
    }

    public static DataBaseUtils getInstance(Context context) {
        if (dataBaseUtils == null) {
            dataBaseUtils = new DataBaseUtils(context);
        }
        return dataBaseUtils;
    }


    public List<TopicModel> getListTopic() {
        sqLiteDatabase = myDatabase.getReadableDatabase();
        List<TopicModel> topicModels = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_TOPIC, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String category = cursor.getString(4);
            String color = cursor.getString(5);
            String lastTime = cursor.getString(6);

            TopicModel topicModel = new TopicModel(id, name, category, color, lastTime);
            topicModels.add(topicModel);
            cursor.moveToNext();
        }
        Log.d(TAG, "getListTopic: " + topicModels);
        return topicModels;
    }

    public List<CategoryModel> getListCategory(List<TopicModel> topicModels) {
        List<CategoryModel> categoryModelList = new ArrayList<>();
        for (int i = 0; i < topicModels.size(); i = i + 5) {
            CategoryModel categoryModel = new CategoryModel(topicModels.get(i).getName(), topicModels.get(i).getColor(),topicModels.get(i).getCategory());
            categoryModelList.add(categoryModel);


        }
        Log.d(TAG, "getListCategory: " + categoryModelList);
        return categoryModelList;
    }

    public HashMap<String, List<TopicModel>> getHashMapTopic(List<TopicModel> topicModels, List<CategoryModel> categoryModels) {
        HashMap<String, List<TopicModel>> hashMap = new HashMap<>();
        for (int i = 0; i < categoryModels.size(); i++) {
            int potision = i * 5;
            hashMap.put(
                    categoryModels.get(i).getName(),
                    topicModels.subList(potision, potision + 5)
            );
            Log.d(TAG, "getHashMapTopic:------------ " + categoryModels.get(i).getName());
            Log.d(TAG, "getHashMapTopic: " + hashMap.get(categoryModels.get(i).getName()));
        }
        return hashMap;
    }

    public WordModels getRandomWord(int topicId, int preWordId) {
        sqLiteDatabase = myDatabase.getReadableDatabase();
        int level = 0;
        Cursor cursor;
        do {
            double random = Math.random() * 100;
            if (random <= 5) level = 4;
            else if (random <= 15) level = 3;
            else if (random <= 30) level = 2;
            else if (random <= 55) level = 1;
            else level = 0;

            cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_WORD +
                    " WHERE topic_id =  " + topicId +
                    " AND LEVEL = " + level +
                    " AND id <> " + preWordId +
                    " ORDER BY RANDOM() LIMIT 1", null);

        } while (cursor.getCount() == 0);

        cursor.moveToFirst();
        int id = cursor.getInt(0);
        String origin = cursor.getString(1);
        String explanation = cursor.getString(2);
        String type = cursor.getString(3);
        String pronunciation = cursor.getString(4);
        String imageUrl = cursor.getString(5);
        String example = cursor.getString(6);
        String example_trans = cursor.getString(7);

        return new WordModels(id, origin, explanation, type, pronunciation, imageUrl,
                example, example_trans, topicId, level);
    }

    public void updateWordLevel(WordModels wordModels, boolean isKnown) {

        sqLiteDatabase = myDatabase.getWritableDatabase();
        int level = wordModels.getLevel();
        if (isKnown && level < 4) {
            level++;
        } else if (!isKnown && level > 0) {
            level--;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("level", level);
        sqLiteDatabase.update(TABLE_WORD, contentValues, " id = " + wordModels.getId(), null);
    }

    public int getNumberOfNewWordBytopicId(int topicId) {
        sqLiteDatabase = myDatabase.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_WORD + " WHERE level =0 and topic_id = " + topicId
                , null);
        return cursor.getCount();
    }

    public int getNumberOfMasterdBytopicId(int topicId) {
        sqLiteDatabase = myDatabase.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_WORD + " WHERE level =0 and topic_id = " + topicId
                , null);
        return cursor.getCount();
    }

    public void updateLastTime(TopicModel topicModel, String lastTime) {
        sqLiteDatabase = myDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("last_time ", lastTime);
        sqLiteDatabase.update(TABLE_TOPIC, contentValues, "id= " + topicModel.getId(), null);

    }

    public String getLastTimeByTopicId(int topicId) {
        sqLiteDatabase = myDatabase.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT last_time FROM " + TABLE_TOPIC
                        + " WHERE id = " + topicId, null
        );
        cursor.moveToFirst();
        return cursor.getString(0);
    }
}
