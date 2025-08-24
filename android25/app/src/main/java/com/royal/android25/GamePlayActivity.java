package com.royal.android25;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashSet;

public class GamePlayActivity extends AppCompatActivity {

    ImageButton imgBtn [] = new ImageButton[12];
    TextView tvBetAmt, tvWinningAmt;
    Button btnCashOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_play);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imgBtn[0] = findViewById(R. id.btnGamePlay1);
        imgBtn[1] = findViewById(R.id.btnGamePlay2);
        imgBtn[2] = findViewById(R.id.btnGamePlay3);
        imgBtn[3] = findViewById(R.id.btnGamePlay4);
        imgBtn[4] = findViewById(R.id.btnGamePlay5);
        imgBtn[5] = findViewById(R.id.btnGamePlay6);
        imgBtn[6] = findViewById(R.id.btnGamePlay7);
        imgBtn[7] = findViewById(R.id.btnGamePlay8);
        imgBtn[8] = findViewById(R.id.btnGamePlay9);
        imgBtn[9] = findViewById(R.id.btnGamePlay10);
        imgBtn[10] = findViewById(R.id.btnGamePlay11);
        imgBtn[11] = findViewById(R.id.btnGamePlay12);

        tvBetAmt = findViewById(R.id.tvGameActivityBetAmount);
        tvWinningAmt = findViewById(R.id.tvGameActivityWinningAmount);
        btnCashOut = findViewById(R.id.btnGamePlayCashout);

        Intent intent = getIntent();
        int amount = intent.getIntExtra("amount",0);
        int winningAmount = 0 ;

        tvBetAmt.setText(String.valueOf(amount));
        tvWinningAmt.setText(String.valueOf(winningAmount));

        HashSet<Integer> btns = new HashSet<>();

        while (btns.size() != 4) {
            int index = (int) (Math.random() * 12); // 0 to 11
            btns.add(imgBtn[index].getId());
        }

        for (int i = 0; i < imgBtn.length; i++) {
//            imgBtn[i].setBackgroundColor(getResources().getColor(R.color.diamond_green));
            imgBtn[i].setBackgroundResource(R.drawable.question);
            imgBtn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageButton im = findViewById(view.getId());
                    if (btns.contains(view.getId())) {
                        im.setBackgroundResource(R.drawable.blast);

                        SharedPreferences sp = getSharedPreferences("diamondgame",MODE_PRIVATE);
                        String _id = sp.getString("userId","-1");

                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                    } else {
                        im.setBackgroundResource(R.drawable.diamond);
                        int x = Integer.parseInt(tvWinningAmt.getText().toString());
                        x = x + amount;
                        tvWinningAmt.setText(String.valueOf(x));
                        im.setEnabled(false);
                    }
                }
            });
        }

    }
}