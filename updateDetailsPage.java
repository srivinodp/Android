package com.example.store;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class updateDetailsPage extends AppCompatActivity {
    TextInputLayout tagView, details[];
    Button updateButton;
    DBHelper dbHelper;
    String tag = "";
    private final String TAG = "updateDetailsPage.java";
    List<String> oldDetailsList;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_details_page);
        tagView = findViewById(R.id.Etag);
        details = new TextInputLayout[6];
        details[1] = findViewById(R.id.Efield1);
        details[2] = findViewById(R.id.Efield2);
        details[3] = findViewById(R.id.Efield3);
        details[4] = findViewById(R.id.Efield4);
        details[5] = findViewById(R.id.Efield5);

        updateButton = findViewById(R.id.Update);
        Log.d(TAG, "onCreate: ");
        dbHelper = new DBHelper(getApplicationContext(),"AndroidDB",null,1);
        Bundle extras = getIntent().getExtras();
        tag = extras.getString("tag");
        Log.d(TAG, "tag  is"+tag);
        loadPastDetails(tag);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newTag = String.valueOf(tagView.getEditText().getText());
                String []newContents = new String[6];
                for(int i = 1;i<6;i++) newContents[i] = String.valueOf(details[i].getEditText().getText());
                AlertDialog.Builder builder1 = new AlertDialog.Builder(updateDetailsPage.this);
                builder1.setMessage("Are you sure to update?? Old Details cannot be recovered!!");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Update",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(newTag.equals(tag)==false){
                                    //tag changed
                                    dbHelper.delDetails(tag);
                                    dbHelper.addDetails(newTag,newContents);
                                }else{
                                    dbHelper.updateDetails(tag,newContents);
                                }

                                Intent intent = new Intent();
                                intent.putExtra("tag",newTag);
                                intent.putExtra("details",dbHelper.getDetails(newTag));
                                setResult(RESULT_OK,intent);
                                finish();
                            }
                        });

                builder1.setNegativeButton(
                        "Back",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Toast.makeText(getApplicationContext(),"Hmm Lucky!! Details not Updated",Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
    }
    private void loadPastDetails(String tag){
        oldDetailsList = dbHelper.getDetailsListWise(tag);
        tagView.getEditText().setText(tag);
        for(int i = 0;i<oldDetailsList.size();i++){
            details[i+1].getEditText().setText(oldDetailsList.get(i));
        }
    }
}