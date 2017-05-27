package com.example.anandparmeetsingh.books;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.anandparmeetsingh.booklisting.MainActivity;

public class WordActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_word);
        final EditText mEditText = (EditText) findViewById(R.id.search_go_btn);
        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String check = mEditText.getText().toString();
                if (check.isEmpty()) {
                    Toast.makeText(WordActivity.this, "Nothing to search", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(WordActivity.this, MainActivity.class);
                    intent.putExtra("location", mEditText.getText().toString());
                    startActivity(intent);
                }
            }

        });

    }


}




