package com.omsi.fileprovidertest;



import static com.pab.simplefilepicker.PickerActivity.PICKER_TYPE;
import static com.pab.simplefilepicker.PickerActivity.TYPE_FILE;
import static com.pab.simplefilepicker.PickerActivity.TYPE_FOLDER;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pab.simplefilepicker.PickerActivity;

public class MainActivity extends AppCompatActivity {


    TextView selectedPathTx;


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
                startActivityForResult(i, 2);
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



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==2){
            if(resultCode== Activity.RESULT_OK){
                selectedPathTx.setText(data.getData().getPath());
            }
        }
    }
}