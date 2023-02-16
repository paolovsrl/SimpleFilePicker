package com.omsi.simplefilepicker;

import static com.omsi.fileprovidertest.FileUtilsKt.getFilesList;
import static com.omsi.fileprovidertest.FileUtilsKt.renderItem;
import static com.omsi.fileprovidertest.FileUtilsKt.renderParentLink;
import static com.omsi.fileprovidertest.PermissionUtils.checkStoragePermission;
import static com.omsi.fileprovidertest.PermissionUtils.requestStoragePermission;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.pab.simplefilepicker.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;

public class PickerActivity extends AppCompatActivity {

    private final String TAG ="PickerActivity";

    File currentDirectory;
    List<File> fileList = new ArrayList<>();
    ListView list;
    ArrayAdapter<String> adapter;
    ConstraintLayout requestPermissionsContainer, storageViewContainer;
    TextView currentPathTx;
    Button selectBtn;
    boolean returnIsFile = false;

    public static String PICKER_TYPE = "com.omsi.fileprovidertest.picker_type";
    public static int TYPE_FOLDER = 0;
    public static int TYPE_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);

        // Get the intent that started this activity
        Intent intent = getIntent();
        returnIsFile =  intent.getIntExtra(PICKER_TYPE, 0) == TYPE_FILE;

        requestPermissionsContainer = findViewById(R.id.picker_request_permission_container);
        storageViewContainer = findViewById(R.id.picker_storage_view_container);

        currentPathTx = findViewById(R.id.picker_current_path);
        selectBtn = findViewById(R.id.folder_picker_select_btn);

        findViewById(R.id.picker_request_permission_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestStoragePermission(PickerActivity.this);
            }
        });

        if(returnIsFile){
            currentPathTx.setVisibility(View.GONE);
            selectBtn.setVisibility(View.GONE);
        } else {
            selectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create intent to deliver some kind of result data
                    //Intent result = new Intent("com.example.RESULT_ACTION", Uri.parse("content://result_uri"));
                    setResultAndClose(currentDirectory);
                }
            });
        }


        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        list = findViewById(R.id.filesTreeView);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File selected = fileList.get(position);
                open(selected);
            }
        });

    }

    private void setResultAndClose(File selectedFile) {
        Intent result = new Intent("com.SimplePicker.RESULT_ACTION", Uri.parse(selectedFile.getPath()));
        setResult(Activity.RESULT_OK, result);
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();

        boolean hasPermission = checkStoragePermission(this);
        if(hasPermission){
            if(Build.VERSION.SDK_INT == Build.VERSION_CODES.Q){
                if(!Environment.isExternalStorageLegacy()){
                    requestPermissionsContainer.setVisibility(View.GONE);
                    storageViewContainer.setVisibility(View.VISIBLE);
                    return;
                }
            }

            requestPermissionsContainer.setVisibility(View.GONE);
            storageViewContainer.setVisibility(View.VISIBLE);
            open(Environment.getExternalStorageDirectory());
        } else{
            requestPermissionsContainer.setVisibility(View.VISIBLE);
            storageViewContainer.setVisibility(View.GONE);
        }

    }



    private void open(File selectedItem){
        if(selectedItem.isFile()){
            if(returnIsFile)
                setResultAndClose(selectedItem);
            return; //openFile(this, selectedItem)
        }

        currentDirectory = selectedItem;
        currentPathTx.setText(currentDirectory.getPath());
        fileList = getFilesList(currentDirectory);

        adapter.clear();

        if(!returnIsFile)
            removeFiles(fileList);

        List<String> strList = CollectionsKt.map(fileList, new Function1<File, String>() {
            @Override
            public String invoke(File file) {
                if(file.getPath().equals(selectedItem.getParentFile().getPath())){
                    return renderParentLink(PickerActivity.this);
                } else{
                    return renderItem(PickerActivity.this, file);
                }
            }
        });


        adapter.addAll(strList);
        adapter.notifyDataSetChanged();

    }


    private void removeFiles(List<File> fileList){
        List<File> files = new ArrayList<>();
        for(File file : fileList){
            if(file.isFile())
                files.add(file);
        }

        fileList.removeAll(files);
    }


}