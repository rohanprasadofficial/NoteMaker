package com.example.logarithm.notemaker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditFile extends AppCompatActivity {

    EditText editText;
    Intent intent,intentbackt;
    String s;
     SharedPreferences sharedPreferences;
    public void Save(View view){

        String writtenText= String.valueOf(editText.getText());
        if(writtenText.isEmpty()){
            Toast.makeText(this, "Empty Text Cannot be Saved", Toast.LENGTH_SHORT).show();
        }
        else{
        //Log.i("Message Written",writtenText);

        sharedPreferences.edit().putString(s,writtenText).apply();
        Toast.makeText(this, "Text Saved", Toast.LENGTH_SHORT).show();
        //Log.i("The title got",s);
        startActivity(intentbackt);


    }}

    public void Cancel(View view){

        Toast.makeText(this, "Task  Cancelled", Toast.LENGTH_SHORT).show();
        startActivity(intentbackt);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_file);
        intentbackt=new Intent(getApplicationContext(),MainActivity.class);

        editText=findViewById(R.id.EditPad);
        intent=getIntent();
        s= intent.getStringExtra("NoteTitle");
        sharedPreferences=getSharedPreferences("com.example.logarithm.notemaker",MODE_PRIVATE);
        String savedText= sharedPreferences.getString(s,"");
        editText.setText(savedText);

    }
}
