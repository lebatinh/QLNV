package com.example.qlnv;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.qlnv.QLTK.QLTKActivity;

public class CodeByMe extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.codebyme);

        Button btnQuayLai = findViewById(R.id.btnQuayLai);
        Button btnGui = findViewById(R.id.btnGui);

        //ấn nút quay lại để về tab đăng nhập
        btnQuayLai.setOnClickListener(v -> startActivity(new Intent(CodeByMe.this, QLTKActivity.class)));
        //ấn nút gửi để đến messenger để gửi tin nhắn cho nhà phát triển
        btnGui.setOnClickListener(v -> {
            Toast.makeText(this, "Chuẩn bị chuyển sang trang messenger", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.me/leba.tinh.36vip7star.sv7.real"));
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

}
