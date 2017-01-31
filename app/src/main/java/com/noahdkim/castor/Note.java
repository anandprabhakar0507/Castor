package com.noahdkim.castor;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;


public class Note extends AppCompatActivity {

    private EditText editBody;
    private EditText editTitle;
    public static String initialTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Intent intent = getIntent();
        initialTitle = intent.getStringExtra("title");
        String title = initialTitle;
        editTitle = (EditText) findViewById(R.id.title);
        editTitle.setText(title);
        if(isExternalStorageReadable()){
            String noteBody = readFromSDFile(title);
            System.out.println("note body:" + noteBody);
            editBody = (EditText) findViewById(R.id.body);
            editBody.setText(noteBody);
        }
    }

    protected void onStop() {
        editBody = (EditText) findViewById(R.id.body);
        editTitle = (EditText) findViewById(R.id.title);
        String writeBody = editBody.getText().toString();
        String finalTitle = editTitle.getText().toString();
        Log.d("SAVE", writeBody);
        if(isExternalStorageWritable()) {
            Log.d("SAVE", "writable");
            writeToSDFile(finalTitle, writeBody);
        }
        else{
            Log.d("SAVE", "not writable");
        }


        super.onStop();
    }



    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    private void writeToSDFile(String title, String toWrite){
        try {
            //This will get the SD Card directory and create a folder named MyFiles in it.
            File sdCard = Environment.getExternalStorageDirectory();
            File directory = new File (sdCard.getAbsolutePath() + "/MyFiles");

            if(directory.exists()) {
                Log.d("FILE", "found directory");
            }
            else{
                directory.mkdirs();
                Log.d("FILE", "directory not found");
            }
//Now create the file in the above directory and write the contents into it
            File file = new File(directory, initialTitle + ".txt");
            if(!title.equals(initialTitle)){
                file.delete();
                Log.d("FILE", "rename");
                file = new File(directory, title + ".txt");

            }

            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(toWrite);
            osw.flush();
            osw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("FILE", "not found");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private String readFromSDFile(String title){

        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File (sdCard.getAbsolutePath() + "/MyFiles");
        File file = new File(directory,title+".txt");
        System.out.println(file.toString());
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
                System.out.println("text toString"+text.toString());
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }
        return text.toString();
    }


}



