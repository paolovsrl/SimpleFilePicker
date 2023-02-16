package com.omsi.fileprovidertest;



import static com.omsi.simplefilepicker.PickerActivity.PICKER_TYPE;
import static com.omsi.simplefilepicker.PickerActivity.TYPE_FILE;
import static com.omsi.simplefilepicker.PickerActivity.TYPE_FOLDER;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.TextView;

import com.omsi.simplefilepicker.PickerActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

public class MainActivity extends AppCompatActivity {


    TextView selectedPathTx;
    int selection = 0;
    int NONE = 0, FOLDER = 1, FILE =2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        selectedPathTx = findViewById(R.id.main_activity_choosen_path_tx);

        findViewById(R.id.main_activity_choose_folder_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PickerActivity.class);
                i.putExtra(PICKER_TYPE, TYPE_FOLDER);
                startActivityForResult(i, 1);
            }
        });

        findViewById(R.id.main_activity_choose_file_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PickerActivity.class);

                i.putExtra(PICKER_TYPE, TYPE_FILE);
                startActivityForResult(i, 2);
            }
        });


        findViewById(R.id.main_activity_write_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selection==1){
                    File file = new File(selectedPathTx.getText().toString()+"/new_file.txt");
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        fileOutputStream.write(0b1);
                        fileOutputStream.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==2 || requestCode ==1){
            if(resultCode== Activity.RESULT_OK){
                selection=requestCode;
                selectedPathTx.setText(data.getData().getPath());
            } else{
                selection=0;
            }
        }
    }
}