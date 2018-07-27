package com.example.logarithm.notemaker;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.aware.PublishConfig;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ObjectStreamException;
import java.security.spec.ECField;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {

    String m_Text = "";
    ListView listView;
    TextView textView;
    ArrayAdapter<String> arrayAdapter;
    SharedPreferences sharedPreferences;
    ArrayList<String> gotNotes;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.note:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Title");

// Set up the input
                final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT );
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        if(m_Text.isEmpty()){
                            Toast.makeText(MainActivity.this, "Name Cannot be Blank !", Toast.LENGTH_LONG).show();
                        }
                        else {
                            ArrayList<String> adder =new ArrayList<>();
                            try {
                                adder = (ArrayList) ObjectSerializer.deserialize(sharedPreferences.getString("Notes", ObjectSerializer.serialize(new ArrayList<String>())));
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            adder.add(m_Text);
                            gotNotes.add(m_Text);
                            try {
                                sharedPreferences.edit().putString("Notes", ObjectSerializer.serialize(adder)).apply();
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            textView.setText("");
                            arrayAdapter.notifyDataSetChanged();

                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();



                return true;
            case R.id.About:
                //Code for About
                return true;
            default:
                return false;

        }


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences=getSharedPreferences("com.example.logarithm.notemaker", MODE_PRIVATE);
        gotNotes=new ArrayList<>();
        try {
            gotNotes = (ArrayList)ObjectSerializer.deserialize(sharedPreferences.getString("Notes",ObjectSerializer.serialize(new ArrayList<String>())));
        }catch (Exception e){

            e.printStackTrace();
        }

        Log.i("Got  Notes",gotNotes.toString());
        listView=findViewById(R.id.listview);
        textView=findViewById(R.id.nonotes);
        arrayAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,gotNotes);
        listView.setAdapter(arrayAdapter);
        if(gotNotes.isEmpty()){

            textView.setText("No Notes ");
        }
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i("Item  LONG CLICK",gotNotes.get(position));
                sharedPreferences.edit().remove(gotNotes.get(position)).apply();
                try {
                    gotNotes.remove(position);

                    sharedPreferences.edit().putString("Notes", ObjectSerializer.serialize(gotNotes)).apply();
                    arrayAdapter.notifyDataSetChanged();
                    if(gotNotes.isEmpty()){
                        textView.setText("No Notes");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(),EditFile.class);
                intent.putExtra("NoteTitle",gotNotes.get(position));
                startActivity(intent);

            }
        });


}}
