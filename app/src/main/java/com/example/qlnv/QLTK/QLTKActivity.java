package com.example.qlnv.QLTK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlnv.CodeByMe;
import com.example.qlnv.Database;
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
        if (dataTK != null && dataTK.moveToFirst()) {
            do {
                int id = dataTK.getInt(0);
                String tk = dataTK.getString(1);
                String mk = dataTK.getString(2);
            } while (dataTK.moveToNext());
            dataTK.close();
        }

        GiaoDien();
        //khi ấn vào dòng Code by me
        TextView txtCodeByMe = findViewById(R.id.txtCodeByMe);
        txtCodeByMe.setOnClickListener(v -> startActivity(new Intent(QLTKActivity.this, CodeByMe.class)));

        //khi ấn vào dòng đổi mật khẩu
        TextView txtChangeMk = findViewById(R.id.txtChangeMk);
        txtChangeMk.setOnClickListener(v -> startActivity(new Intent(QLTKActivity.this, com.example.qlnv.QLTK.Changepassword.class)));

    }
    // Function to show AlertDialog
    private void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(QLTKActivity.this);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle OK button click if needed
            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

    private void GiaoDien() {
        TextView txtDangNhap = findViewById(R.id.txtDangNhap);
        LinearLayout linearDangNhap = findViewById(R.id.linearDangNhap);
        Button btnDangNhap = findViewById(R.id.btnDangNhap);
        TextInputEditText edtTk_Dn = findViewById(R.id.edtTk_Dn);
        TextInputEditText edtMk_Dn = findViewById(R.id.edtMk_Dn);
        TextInputEditText edtMk_admin = findViewById(R.id.edtMk_admin);

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

                if (Tk.isEmpty()) {
                    showAlertDialog("Bạn quên chưa điền Tài khoản!");
                } else if (Mk.isEmpty()) {
                    showAlertDialog("Bạn quên chưa điền Mật khẩu!");
                } else if (Mk2.isEmpty()) {
                    showAlertDialog("Bạn quên chưa nhắc lại Mật khẩu!");
                } else {
                    try {
                        Cursor cursor = database.GetData("SELECT * FROM QLTK where TK = '" + Tk + "'");
                        if (cursor.getCount() > 0) {
                            showAlertDialog("Tài khoản đã được đăng ký. Vui lòng sử dụng tài khoản khác!");
                        } else {
                            if (Mk.equals(Mk2)) {
                                database.QueryData("INSERT INTO QLTK(TK, MK) VALUES('" + Tk + "', '" + Mk + "')");
                                database.close();
                                showAlertDialog("Đăng ký tài khoản thành công. Vui lòng đăng nhập để sử dụng!");
                            } else {
                                showAlertDialog("Mật khẩu nhập lại PHẢI trùng với mật khẩu trước đó");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Tk = edtTk_Dn.getText().toString().trim();
                String Mk = edtMk_Dn.getText().toString().trim();
                String Mk_ad = edtMk_admin.getText().toString().trim();

                if (!Tk.isEmpty() && !Mk.isEmpty()) {
                    try {
                        Cursor cursor = database.GetData("SELECT * FROM QLTK where TK = '" + Tk + "' AND MK = '" + Mk + "'");
                        if (cursor.getCount() > 0) {
                            saveLoggedInUser(Tk);
                            saveLoggedInAdmin(Mk_ad);
                            Toast.makeText(QLTKActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(QLTKActivity.this, WelcomeActivity.class);
                            database.close();
                            startActivity(intent);
                        } else {
                            showAlertDialog("Tài khoản hoặc mật khẩu không chính xác!");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (Tk.isEmpty()) {
                    showAlertDialog("Không được bỏ trống Tài khoản!");
                }
                if (Mk.isEmpty()) {
                    showAlertDialog("Không được bỏ trống Mật khẩu!");
                }
            }
        });
    }

    private void saveLoggedInUser(String Tk) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(QLTKActivity.this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("LoggedInUser", Tk);
        editor.apply();
    }

    private void saveLoggedInAdmin(String Mk_ad) {
        SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(QLTKActivity.this);
        SharedPreferences.Editor editor = sharedPreferences1.edit();
        editor.putString("IsAdminLoggedIn", Mk_ad);
        editor.apply();
    }
}