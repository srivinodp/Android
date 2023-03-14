package com.example.store;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {
    final static String tgle = "ks947rliu84g484bi8uf8489";
    private final static int MY_REQUEST_CODE = 178;
    TextInputLayout tag, details[];
    Button submit,toggleButton;
    DBHelper dbHelper;
    LinearLayout lLayout;
    RelativeLayout rLayout;
    ListView mListView;
    ArrayList<String> mArrayList;
    TreeSet<String> mTags = new TreeSet<>();
    String toggle = "1";
    private void addButtonToLL(String typeStr){
        Button b = new Button(getApplicationContext());
        b.setText(typeStr);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =  new Intent(getApplicationContext(), DetailsShow.class);
                i.putExtra("details", dbHelper.getDetails(typeStr));
                i.putExtra("type", typeStr);
                startActivityForResult(i,MY_REQUEST_CODE);
                Log.d("nlkgk", "came back ");
            }
        });
        lLayout.addView(b);
    }
    private void loadListView(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mArrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mListView.setAdapter(arrayAdapter);
    }
    private void addListView(){
        mListView = (ListView)findViewById(R.id.ListView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int I, long l) {
                String s = ((TextView) view).getText().toString();
                Intent i =  new Intent(getApplicationContext(), DetailsShow.class);
                i.putExtra("details", dbHelper.getDetails(s));
                i.putExtra("type", s);
                startActivityForResult(i,MY_REQUEST_CODE);
            }
        });
//        rLayout.addView(mListView);
        showListView(dbHelper.getTags());
    }
    private void addLLayout(){
        lLayout = (LinearLayout) findViewById(R.id.ll);
//        rLayout.addView(lLayout);
        showlLayout(dbHelper.getTags());
    }
    private void showListView(TreeSet<String> tags){
        mTags = tags;
        Log.d("", "showListView: "+String.join(",",tags));
        mArrayList = new ArrayList<>(tags);
        if(mArrayList.isEmpty()){
            Toast.makeText(getApplicationContext(),"No History!!",Toast.LENGTH_SHORT).show();
        }else{
            loadListView();
        }
    }

    private void showlLayout(TreeSet<String> tags){
        mTags = tags;
        lLayout.removeAllViews();
        for(String s:tags){
            addButtonToLL(s);

        }
        if(tags.isEmpty()){
            Toast.makeText(getApplicationContext(),"No History!!",Toast.LENGTH_SHORT).show();
        }
    }
    private boolean checkTag(String tag){
        if(mTags==null) mTags = dbHelper.getTags();
        if(mTags.contains(tag)) return true;
        return false;
    }
    private void addTag(String tag){
        mTags.add(tag);
        if(toggle=="0") addButtonToLL(tag);
        else {
            mArrayList.add(tag);
            loadListView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(toggle=="0") showlLayout(dbHelper.getTags());
        else showListView(dbHelper.getTags());
        Log.d("", "onResume: Just now");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == MY_REQUEST_CODE) {
                if (data != null)
                {
                    String type = data.getStringExtra("type");
                    dbHelper.delDetails(type);
                    Toast.makeText(getApplicationContext(),"Details deleted successfully",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    void clearViews(){
        tag.getEditText().setText("");
        for(int i = 1;i<6;i++) details[i].getEditText().setText("");
    }
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tag = findViewById(R.id.tag);
        details = new TextInputLayout[6];
        details[1] = findViewById(R.id.field1);
        details[2] = findViewById(R.id.field2);
        details[3] = findViewById(R.id.field3);
        details[4] = findViewById(R.id.field4);
        details[5] = findViewById(R.id.field5);

        submit = findViewById(R.id.Submit);
        toggleButton = findViewById(R.id.Toggle);
        dbHelper = new DBHelper(this,"AndroidDB",null,1);
        lLayout = (LinearLayout) findViewById(R.id.ll);
        rLayout = (RelativeLayout)findViewById(R.id.RelLayout);
        addListView();
        addLLayout();
        toggle = dbHelper.getDetails(tgle);
        if(toggle=="0"){
            mListView.setVisibility(View.INVISIBLE);
        }else{
            lLayout.setVisibility(View.INVISIBLE);
        }
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle = (toggle=="1"?"0":"1");
                Log.d("", "Toggle click: "+toggle);
                dbHelper.updateDetails(tgle,new String[]{toggle});
                if(toggle=="0"){
                    mListView.setVisibility(View.INVISIBLE);
                    showlLayout(dbHelper.getTags());
                    lLayout.setVisibility(View.VISIBLE);
                }else{
                    lLayout.setVisibility(View.INVISIBLE);
                    showListView(dbHelper.getTags());
                    mListView.setVisibility(View.VISIBLE);
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String typeStr = String.valueOf(tag.getEditText().getText()).toLowerCase(Locale.ROOT);
                String[] detailsStr = new String[6];
                int count = 0;
                for(int i = 1;i<6;i++) {
                    detailsStr[i] = String.valueOf(details[i].getEditText().getText());
                    if(detailsStr[i].length()!=0) count++;
                }
                if(typeStr.length()==0){
                    Toast.makeText(getApplicationContext(),"Invalid Tag",Toast.LENGTH_SHORT).show();
                    return;
                }else if(count<1){
                    Toast.makeText(getApplicationContext(),"Atleast one Detail should be filled",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(checkTag(typeStr)==false){
                    dbHelper.addDetails(typeStr, detailsStr);
                    addTag(typeStr);
                    Toast.makeText(getApplicationContext(),"Details added",Toast.LENGTH_SHORT).show();
                    clearViews();
                }
                else{
                    Log.d("Inside", "entered to check update ");
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setMessage(typeStr +" registered already!! Do you want to update with new details? If yes, old details will be lost. [TAG should be unique]");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Update",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    dbHelper.updateDetails(typeStr,detailsStr);
                                    Toast.makeText(getApplicationContext(),"Details Updated",Toast.LENGTH_SHORT).show();
                                    clearViews();
                                }
                            });

                    builder1.setNegativeButton(
                            "Change Tag",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    Toast.makeText(getApplicationContext(),"Try changing tag name!!",Toast.LENGTH_SHORT).show();
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }
        });

    }
}