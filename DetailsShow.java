package com.example.store;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;



public class DetailsShow extends AppCompatActivity {
    private final static int MY_REQUEST_CODE = 6758;
    private final String TAG = "DetailsShow.java";
    private String tag = "";
    TextView mTextView;
    Button mButton, updateButton, homeButton;

    void setTag(String s){
        tag = s;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==MY_REQUEST_CODE){
                Log.d(TAG, "onActivityResult: return from updateDetailsPage");
                if(data!=null){
                    setTag(data.getStringExtra("tag"));
                    mTextView.setText(data.getStringExtra("details"));
                }
            }
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_details_show);
        mTextView = findViewById(R.id.displayInfo);
        mButton = findViewById(R.id.delete);
        updateButton = findViewById(R.id.DetailsShow_UpdateButton);
        homeButton = findViewById(R.id.DetailsShow_Home);
        Bundle extras = getIntent().getExtras();
        mTextView.setText(extras.getString("details"));
        setTag(extras.getString("type"));
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                finish();
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =  new Intent(getApplicationContext(), updateDetailsPage.class);
                i.putExtra("tag",tag);
                startActivityForResult(i,MY_REQUEST_CODE);
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(DetailsShow.this);
                builder1.setMessage("Are you sure?? Deleted Details cannot be recovered!!");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent();
                                intent.putExtra("type",tag);
                                setResult(RESULT_OK,intent);
                                finish();
                            }
                        });

                builder1.setNegativeButton(
                        "Retain",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Toast.makeText(getApplicationContext(),"Hmm Lucky!! Details not deleted",Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });
    }
}