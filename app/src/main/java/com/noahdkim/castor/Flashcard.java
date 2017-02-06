package com.noahdkim.castor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableRow;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class Flashcard extends AppCompatActivity {
    int index = 0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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


        for (int i = 0; i < (diction.length/2); i++) {
            final Button flashArea = new Button(this);
            flashArea.setText(diction[i*2]);
            index = i;

            TableRow nextRow = new TableRow(this);
            nextRow.setId(i);
            RelativeLayout content_flashcard = (RelativeLayout) findViewById(R.id.content_flashcard);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

            flashArea.setId(i+1);
            if (i == 0) {

                content_flashcard.addView(flashArea);
            } else {

                params.addRule(RelativeLayout.BELOW, i);
                content_flashcard.addView(flashArea,params);
            }
            flashArea.setOnClickListener(new View.OnClickListener() {

                int touches = 0;
                @Override
                public void onClick(View view) {
                    if(touches == 0){
                        index = flashArea.getId()*2-1;
                        touches++;
                    }
                    else{
                        index = (flashArea.getId()*2)-2;
                        touches--;
                    }
                    flashArea.setText(diction[index]);




                }
            });


        }



        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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

    private String readFromSDFile(String title) {

        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(getFilesDir() + "/MyFiles");
        File file = new File(directory, title + ".txt");

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');

            }
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here

        }
        return text.toString();
    }

    private int flashcardTap(String[] diction, int currentInd) {
        if (currentInd % 2 == 0) {
            return currentInd + 1;
        } else {
//            Random rand = new Random();
//
//            int  n = rand.nextInt(diction.length/2);
//            return n*2;
            return currentInd - 1;
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Flashcard Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
