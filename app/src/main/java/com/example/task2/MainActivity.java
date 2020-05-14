package com.example.task2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    final String s = "Single Player";
    final String d = "1 v 1";
    final String m = "Multi Player";
    char mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = (TextView) findViewById(R.id.textView1);
        final ImageView img1 = (ImageView) findViewById(R.id.imageView1);
        final ImageView img2 = (ImageView) findViewById(R.id.imageView2);
        final ImageView img3 = (ImageView) findViewById(R.id.imageView3);


        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(s);
                mode = 's';
                Toast.makeText(MainActivity.this, "Mode <Singleplayer> under Development", Toast.LENGTH_SHORT).show();
                textView.setText("");
                //showPopUp();
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img1.setEnabled(false);
                img3.setEnabled(false);
                textView.setText(d);
                mode = 'd';
                showPopUp();
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img1.setEnabled(false);
                img2.setEnabled(false);
                textView.setText(m);
                mode = 'm';
                showPopUp();
            }
        });

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    private void showPopUp() {
        ConstraintLayout lyt = (ConstraintLayout) findViewById(R.id.lyt);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.popup, null);
        PopupWindow window = new PopupWindow(view, 950, 700);
        window.showAtLocation(lyt, Gravity.CENTER, 0, 0);

        Button btn1 = (Button) view.findViewById(R.id.gridSize1);
        Button btn2 = (Button) view.findViewById(R.id.gridSize2);
        Button btn3 = (Button) view.findViewById(R.id.gridSize3);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVar(4, 3, mode);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVar(5, 4, mode);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVar(7, 6, mode);
            }
        });
    }

    private void sendVar(int row, int column, char mode) {
        Intent i = new Intent(MainActivity.this, game.class);
        i.putExtra("row", row);
        i.putExtra("column", column);
        i.putExtra("mode", mode);
        startActivity(i);
    }
}
