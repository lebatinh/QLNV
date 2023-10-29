package com.example.qlnv;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.example.qlnv.QLTK.QLTKActivity;
import com.google.android.material.textfield.TextInputEditText;


public class ForgetPassword extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpassword);

        Button btnQuayLai = findViewById(R.id.btnQuayLai);
        Button btnGuiMk = findViewById(R.id.btnGuiMk);
        TextInputEditText edtTk_Forget = findViewById(R.id.edtTk_Forget);

        btnQuayLai.setOnClickListener(v -> startActivity(new Intent(ForgetPassword.this, QLTKActivity.class)));
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }


}