package com.noahdkim.castor;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


import java.io.File;

public class MainActivity extends AppCompatActivity {

    static int numNote = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestPermissions();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton addNote = (FloatingActionButton) findViewById(R.id.addNote);
        final Context context = this;
        final Intent intent = new Intent(MainActivity.this, Note.class);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent);


            }
        });
    }
    protected void onResume(){
        super.onResume();

        LinearLayout noteList = (LinearLayout) findViewById(R.id.Menu);
        noteList.removeAllViews();
        final String[] noteTitles = getNotes();
        if(noteTitles==null){
            numNote=0;
        }
        else {
            numNote = noteTitles.length;
        }
        final Context context = this;
        final Intent intent = new Intent(MainActivity.this, Note.class);
        for(int i = 0; i<numNote; i++){
            final Button toAdd = new Button(this);
            Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
            toAdd.setId(numNote);
            toAdd.setText(noteTitles[i]);

            final int noteIndex = i;
            toAdd.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    File sdCard = Environment.getExternalStorageDirectory();
                    File directory = new File (getFilesDir() + "/MyFiles");
                    File file = new File(directory, noteTitles[noteIndex] + ".txt");
                    file.delete();
                    ViewGroup layout = (ViewGroup) toAdd.getParent();
                    if(null!=layout) //for safety only  as you are doing onClick
                        layout.removeView(toAdd);
                    return true;
                }
            });
            toAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent.putExtra("title",noteTitles[noteIndex]);
                    startActivity(intent);
                }
            });


            noteList.addView(toAdd,lp); // noteList is the container of the buttons
        }
    }
    protected void onPause(){

        super.onPause();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void requestPermissions(){
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
    }

    public String[] getNotes(){
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File (getFilesDir() + "/MyFiles");
        if(directory.listFiles()!=null) {
            File[] listOfNotes = directory.listFiles();
            int noteCount = listOfNotes.length;
            String[] returnList = new String[noteCount];
            for (int i = 0; i < listOfNotes.length; i++) {
                if (listOfNotes[i].isFile()) {
                    String fname = listOfNotes[i].getName();
                    int pos = fname.lastIndexOf(".");
                    if (pos > 0) {
                        fname = fname.substring(0, pos);
                    }
                    returnList[i] = fname;

                } else if (listOfNotes[i].isDirectory()) {
                    returnList[i] = listOfNotes[i].getName();
                }
            }
            return returnList;
        }
        else{
            return null;
        }


    }



}
