package com.example.task2;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class game extends AppCompatActivity {
    int row;
    int column;
    int num = 0;
    char mode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        row = i.getIntExtra("row", 0) + 1;
        column = i.getIntExtra("column", 0);
        mode = i.getCharExtra("mode", 'x');

        if (mode == 's') {
            setActivity(row, column, 1);
        }
        else  if (mode == 'd') {
            setActivity(row, column, 2);
        }
        else if (mode == 'm') {
            setContentView(R.layout.game_layout);
            final TextView view = (TextView) findViewById(R.id.editText);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    num = Integer.parseInt(view.getText().toString());
                    if (num <= 0 || num == 1 || num == 2 || num > 4) {
                        Toast.makeText(game.this, " No.of Players must be either 3 or 4", Toast.LENGTH_SHORT).show();
                        view.setText("");
                    }
                    else {
                        view.setEnabled(false);
                        setActivity(row, column, num);
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    public void setActivity(int row, int column, int p) {
        layout_2 l = new layout_2(this, row, column, p);
        setContentView(l);
    }
}
