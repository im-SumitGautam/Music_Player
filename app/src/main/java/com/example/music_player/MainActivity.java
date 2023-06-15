package com.example.music_player;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listViewSongs);
        askPermission();
    }

    private void askPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        displaySongs();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                }).check();
    }

    public ArrayList<File> findSongs(File file){

        ArrayList<File> arrayList=new ArrayList<>();
        try {
            File[] files = file.listFiles();
            for (File singleFiles : files)
                if (singleFiles.isDirectory() && !singleFiles.isHidden()) {
                    arrayList.addAll(findSongs(singleFiles));
                } else {
                    if (singleFiles.getName().endsWith(".mp3") || singleFiles.getName().endsWith(".wav")) {
                        arrayList.add(singleFiles);
                    }
                }
        }catch (Throwable e){
            e.printStackTrace();
        }
        return arrayList;
    }

    void displaySongs(){
        try {
        final ArrayList<File> mySongs = findSongs(Environment.getExternalStorageDirectory());
            items = new String[mySongs.size()];
            for (int i = 0; i < mySongs.size(); i++) {
                items[i] = mySongs.get(i).getName().toString();
//                        .replace(".mp3",
//                        null).replace("wav", null);
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
        ArrayAdapter<String> myAdapter= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(myAdapter);
    }
}