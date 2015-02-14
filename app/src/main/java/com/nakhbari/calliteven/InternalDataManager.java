package com.nakhbari.calliteven;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import android.content.Context;

public class InternalDataManager {

    private static String FILE_NAME = "InternalData.JSON";
    private static int EOF = -1;

    public void SaveData(ArrayList<NameListItem> array, Context context) {
        FileOutputStream fos = null;

        try {

            // Open the file
            fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);

            String s = ConvertArrayToGsonString(array);

            // Write the data to the file
            fos.write(s.getBytes());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if (fos != null) {

                    //Close File
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String ConvertArrayToGsonString(ArrayList<NameListItem> array) {
        Gson g = new Gson();
        String gs = g.toJson(array);
        return gs;
    }

    public ArrayList<NameListItem> LoadData(Context context) {
        ArrayList<NameListItem> data = new ArrayList<NameListItem>();

        String collected = null;
        FileInputStream fis = null;

        try {

            fis = context.openFileInput(FILE_NAME);
            byte[] dataArray = new byte[fis.available()];

            // Read file until End of File
            while (fis.read(dataArray) != EOF) {
                collected = new String(dataArray);
            }

            data.clear();
            // Convert the collected string into the desired Structure
            data.addAll(ConvertStringToArrayList(collected));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {

                    // Close File
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return data;

    }

    private ArrayList<NameListItem> ConvertStringToArrayList(String collected) {
        Gson g = new Gson();
        ArrayList<NameListItem> data = g.fromJson(collected,
                new TypeToken<ArrayList<NameListItem>>() {
                }.getType());

        if (data.isEmpty()) {
            data = new ArrayList<NameListItem>();
        }

        return data;
    }

}
