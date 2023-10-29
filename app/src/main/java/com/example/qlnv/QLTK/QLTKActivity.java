package com.example.qlnv.QLTK;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlnv.CodeByMe;
import com.example.qlnv.Database;
import com.example.qlnv.ForgetPassword;
import com.example.qlnv.R;
import com.example.qlnv.WelcomeActivity;
import com.google.android.material.textfield.TextInputEditText;

public class QLTKActivity extends AppCompatActivity {

    Database database;
    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qltk);

        //tạo database QLTK
        database = new Database(QLTKActivity.this, "QLTK.sqlite", null, 1);

        //tạo bảng Quản lý tài khoản
        database.QueryData("CREATE TABLE IF NOT EXISTS QLTK(ID INTEGER PRIMARY KEY AUTOINCREMENT, TK VARCHAR(50), MK VARCHAR(50) )");

        //insert data
//        database.QueryData("INSERT INTO QLTK VALUES(null, 0123456789, 012345)");

        //select data
        Cursor dataTK = database.GetData("SELECT * FROM QLTK");
        while (dataTK.moveToNext()) {
            int id = dataTK.getInt(0);
            String tk = dataTK.getString(1);
            String mk = dataTK.getString(2);
        }
        GiaoDien();

        //khi ấn vào quên mật khẩu
        TextView txtForget = findViewById(R.id.txtForget);
        txtForget.setOnClickListener(v -> startActivity(new Intent(QLTKActivity.this, ForgetPassword.class)));
        //khi ấn vào dòng Code by me
        TextView txtCodeByMe = findViewById(R.id.txtCodeByMe);
        txtCodeByMe.setOnClickListener(v -> startActivity(new Intent(QLTKActivity.this, CodeByMe.class)));

        TextView txtChangeMk = findViewById(R.id.txtChangeMk);
        txtChangeMk.setOnClickListener(v -> startActivity(new Intent(QLTKActivity.this, com.example.qlnv.QLTK.Changepassword.class)));

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Nhấn quay lại một lần nữa để thoát ứng dụng", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
    private void GiaoDien() {
        TextView txtDangNhap = findViewById(R.id.txtDangNhap);
        LinearLayout linearDangNhap = findViewById(R.id.linearDangNhap);
        Button btnDangNhap = findViewById(R.id.btnDangNhap);
        TextInputEditText edtTk_Dn = findViewById(R.id.edtTk_Dn);
        TextInputEditText edtMk_Dn = findViewById(R.id.edtMk_Dn);

        TextView txtDangKy = findViewById(R.id.txtDangKy);
        LinearLayout linearDangKy = findViewById(R.id.linearDangKy);
        Button btnDangKy = findViewById(R.id.btnDangKy);
        TextInputEditText edtTk_Dk = findViewById(R.id.edtTk_Dk);
        TextInputEditText edtMk_Dk = findViewById(R.id.edtMk_Dk);
        TextInputEditText edtMk_Dk1 = findViewById(R.id.edtMk_Dk1);

        txtDangNhap.setOnClickListener(v -> {
            txtDangNhap.setBackground(getResources().getDrawable(R.drawable.swt_login, null));
            txtDangKy.setBackground(null);
            txtDangNhap.setTextColor(getResources().getColor(R.color.white, null));
            linearDangKy.setVisibility(View.GONE);
            linearDangNhap.setVisibility(View.VISIBLE);
        });

        txtDangKy.setOnClickListener(v -> {
            txtDangKy.setBackground(getResources().getDrawable(R.drawable.swt_login, null));
            txtDangNhap.setBackground(null);
            linearDangKy.setVisibility(View.VISIBLE);
            linearDangNhap.setVisibility(View.GONE);
        });

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Tk = edtTk_Dk.getText().toString().trim();
                String Mk = edtMk_Dk.getText().toString().trim();
                String Mk2 = edtMk_Dk1.getText().toString().trim();
                try {
                    Cursor cursor = database.GetData("SELECT * FROM QLTK where TK = '" + Tk + "'");
                    if (cursor.getCount() > 0) {
                        Toast.makeText(QLTKActivity.this, "Tài khoản đã được đăng ký. Vui lòng sử dụng tài khoản khác", Toast.LENGTH_LONG).show();
                    } else {
                        if (Mk.equals(Mk2) && !Tk.isEmpty()) {
                            database.QueryData("INSERT INTO QLTK(TK, MK) VALUES('" + Tk + "', '" + Mk + "')");
                            Toast.makeText(QLTKActivity.this, "Đăng ký tài khoản thành công. Vui lòng đăng nhập để sử dụng!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(QLTKActivity.this, "Mật khẩu nhập lại PHẢI trùng với mật khẩu trước đó", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Tk = edtTk_Dn.getText().toString().trim();
                String Mk = edtMk_Dn.getText().toString().trim();

                if (!Tk.isEmpty() && !Mk.isEmpty()) {
                    try {
                        Cursor cursor = database.GetData("SELECT * FROM QLTK where TK = '" + Tk + "' AND MK = '" + Mk + "'");
                        if (cursor.getCount() > 0) {
                            Toast.makeText(QLTKActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(QLTKActivity.this, WelcomeActivity.class);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }if(Tk.isEmpty()) {
                    edtTk_Dn.setText("Không được bỏ trống!");
                }if(Mk.isEmpty()){
                    edtMk_Dn.setText("Không được bỏ trống!");
                }
            }
        });

    }
}