package com.example.luyentapthread_bai01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.luyentapthread_bai01.databinding.ActivityMainBinding;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Random random = new Random();

    //anh xa item
    ActivityMainBinding mainBinding;

    //chinh sua giao dien
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    );

    //luu so luong cac button se tao
    int n = 0;

    //main thread
    Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int percent = msg.arg2;

            String thongBao = msg.obj.toString();
            if(thongBao.equals("END"))
            {
                Toast.makeText(MainActivity.this, "Da chay xong tien trinh", Toast.LENGTH_SHORT).show();
            }
            else
            {
                int value = (int) msg.obj;
                //gan TttPercent vao tu percent nhan duoc tu thread

                //tao ra button voi settext la value duoc tao ngau nhien trong thread
                Button button = new Button(MainActivity.this);
                button.setText(value + "");
                //doi mau button duoc tao khi click trong khi chuong trionh van dang chay
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button.setTextColor(Color.RED);
                    }
                });
                //chinh kich thuoc cho button
                button.setLayoutParams(layoutParams);

                //gan button len linenear
                mainBinding.llButton.addView(button);
            }
            mainBinding.txtPercent.setText(percent + "%");


        }
    };

    //thread
    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {

            int percent = 0;
            int value = 0;
            for (int i = 0; i < n; i++) {
                Message message = handler.obtainMessage();
                //percent la so % (nut) dang duoc tao
                percent = i * 100 / n;
                message.arg2 = percent;

                //value la gia tri ngau nhien de gan settext cho button duoc tao
                //max so random la 100
                value = random.nextInt(100);
                message.obj = value;

                handler.sendMessage(message);
                SystemClock.sleep(100);
            }

            Message message1= new Message();
            //chay 100% thi thong bao chay xong tien trinh
            message1.arg2=100;
            String thongBao = "END";
            message1.obj = thongBao;
            handler.sendMessage(message1);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();

        addEvents();
    }

    private void addEvents() {
        mainBinding.btnDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickButton();
            }
        });
    }

    private void onClickButton() {
        //xoa het cac button da tao sau khi chay lai
        mainBinding.llButton.removeAllViews();

        //luu so luong button se tao
        n = Integer.parseInt(mainBinding.edtNumber.getText().toString().trim());

        //tien trinh se chay trong ham nay
        thread.start();
    }

    private void addControls() {
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
    }


}