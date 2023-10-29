package com.example.qlnv.QLTK;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qlnv.Database;
import com.example.qlnv.R;

public class Changepassword extends AppCompatActivity {
    Button btnDoiMk, btnQuayLai;
    Database database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepassword);

        //khai báo database
        database = new Database(Changepassword.this, "QLTK.sqlite", null, 1);
        //khai báo bảng trong database
        Cursor dataTK = database.GetData("SELECT * FROM QLTK");

        btnDoiMk = (Button) findViewById(R.id.btnDoiMk);

        btnQuayLai = findViewById(R.id.btnQuayLai);
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Changepassword.this, QLTKActivity.class));
            }
        });

        DoiMK();
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }


    private void DoiMK() {
        btnDoiMk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edtTk_change = findViewById(R.id.edtTk_change);
                EditText edtMk_old = findViewById(R.id.edtMk_old);
                EditText edtMk_new = findViewById(R.id.edtMk_new);

                String tkc = edtTk_change.getText().toString().trim();
                String mkc = edtMk_old.getText().toString().trim();
                String mkm = edtMk_new.getText().toString().trim();

                if (!tkc.isEmpty() || !mkc.isEmpty() || !mkm.isEmpty()) {
                    Cursor cursor = database.GetData("SELECT * FROM QLTK where TK = '" + tkc + "' AND MK = '" + mkc + "'");
                    if (cursor.getCount() > 0) {

                        database.QueryData("UPDATE QLTK SET MK = '" + mkm + "' WHERE TK = '" + tkc + "' ");

                        AlertDialog.Builder builder = new AlertDialog.Builder(Changepassword.this);
                        builder.setMessage("Mật khẩu đã được thay đổi. Vui lòng đăng nhập để sử dụng");
                        builder.setCancelable(true);
                        builder.setPositiveButton(
                                "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        startActivity(new Intent(Changepassword.this, QLTKActivity.class));
                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        alert.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Changepassword.this);
                        builder.setMessage("Mật khẩu cũ không chính xác!");
                        builder.setCancelable(true);
                        builder.setPositiveButton(
                                "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        alert.show();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Changepassword.this);
                    builder.setMessage("Không được bỏ trống bất kì ô nào");
                    builder.setCancelable(true);
                    builder.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    alert.show();
                }
            }
        });
    }
}