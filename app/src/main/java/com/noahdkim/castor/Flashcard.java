package com.noahdkim.castor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Flashcard extends AppCompatActivity {
    int index=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_flashcard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent startFlashcard = getIntent();
        String title = startFlashcard.getStringExtra("title");


        String noteBody = readFromSDFile(title);
        final String diction[] = noteBody.split(":|\n");


        final Button flashArea = new Button(this);

        flashArea.setText(diction[index]);
        flashArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                index = flashcardTap(diction, index);
                flashArea.setText(diction[index]);


            }
        });
        RelativeLayout content_flashcard = (RelativeLayout) findViewById(R.id.content_flashcard);
        content_flashcard.addView(flashArea);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }




    public static int countLines(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
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
    private int flashcardTap(String[] diction, int currentInd){
        if(currentInd%2==0){
            return currentInd+1;
        }
        else{
            Random rand = new Random();

            int  n = rand.nextInt(diction.length/2);
            return n*2;
        }
    }
}
