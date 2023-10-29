package com.example.qlnv.QLNV;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.qlnv.Database;
import com.example.qlnv.R;

import java.sql.PreparedStatement;
import java.util.ArrayList;

public class QLNVActivity extends AppCompatActivity {

    public static Database database;
    ListView lvNv;
    static ArrayList<QLNV> arrayNv;
    QLNVAdapter adapter;
    ImageButton btnMenu;
    ImageButton btnThem;
    final int REQUEST_CODE_PHONE = 1;
    int index = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlnv);

        lvNv = (ListView) findViewById(R.id.lvNv);
        arrayNv = new ArrayList<>();

        adapter = new QLNVAdapter(this, R.layout.nv_defaut, arrayNv);
        lvNv.setAdapter(adapter);

        //tạo database QLTK
        database = new Database(QLNVActivity.this, "QLNV.sqlite", null, 1);

        //tạo bảng Quản lý tài khoản
        database.QueryData("CREATE TABLE IF NOT EXISTS QLNV(MaNv VARCHAR(10) PRIMARY KEY , HoTen VARCHAR(50)," +
                " ChucVu VARCHAR(50), GioiTinh VARCHAR(10), Diachi VARCHAR(50), SDT VARCHAR(11), HinhAnh BLOB)");

        //get data
        Cursor cursor = database.GetData("SELECT MaNv, HoTen, ChucVu, HinhAnh FROM QLNV");
        while (cursor.moveToNext()) {
            arrayNv.add(new QLNV(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getBlob(3)
            ));
            adapter.notifyDataSetChanged();
        }
        lvNv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long id) {
                index = i;
                String maNv = arrayNv.get(i).getMaNv();
                AlertDialog.Builder builder = new AlertDialog.Builder(QLNVActivity.this);
                builder.setTitle("Cảnh báo");
                builder.setMessage("Bạn muốn xóa hay sửa nhân viên có mã nhân viên "+ maNv +" này?");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (i = 0; i < arrayNv.size(); i++) {
                            String query = "DELETE FROM QLNV WHERE MaNv = '" + maNv + "'";
                            PreparedStatement pstmt = database.QueryData(query);
                            arrayNv.remove(i);
                            adapter.notifyDataSetChanged();
                            break;
                        }
                    }
                });
                builder.setNegativeButton("Sửa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(QLNVActivity.this, SuaActivity.class));
                    }
                });
                builder.setNeutralButton("Thoát", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return false;
            }
        });

        btnThem = (ImageButton) findViewById(R.id.btnThem);
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QLNVActivity.this, ThemNVActivity.class));
            }
        });

        btnMenu = (ImageButton) findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMenu();
            }
        });
    }

    private void ShowMenu() {
        PopupMenu popupMenu = new PopupMenu(QLNVActivity.this, btnMenu);
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.itemBaoLoi) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.me/leba.tinh.36vip7star.sv7.real"));
                    startActivity(intent);
                } else if (item.getItemId() == R.id.itemSdt) {
                    ActivityCompat.requestPermissions(
                            QLNVActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            REQUEST_CODE_PHONE
                    );
                    String phoneNumber = "0327181134";
                    Intent intent2 = new Intent(Intent.ACTION_DIAL);
                    intent2.setData(Uri.parse("tel:" + phoneNumber));
                    startActivity(intent2);
                } else if (item.getItemId() == R.id.itemEmail) {
                    String[] TO = {"batinh569@gmail.com"};
                    String[] CC = {"batinh569@gmail.com"};
                    Intent intent3 = new Intent(Intent.ACTION_SEND);
                    intent3.setData(Uri.parse("mailto:"));
                    intent3.setType("text/plain");
                    intent3.putExtra(Intent.EXTRA_EMAIL, TO);
                    intent3.putExtra(Intent.EXTRA_CC, CC);
                    intent3.putExtra(Intent.EXTRA_SUBJECT, "Báo lỗi phần mềm");
                    intent3.putExtra(Intent.EXTRA_TEXT, "Phần mềm của bạn đang bị lỗi");
                    startActivity(Intent.createChooser(intent3, "Send Email"));
                } else {
                    return false;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PHONE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Mở phần gọi điện thoại
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    startActivityForResult(intent, REQUEST_CODE_PHONE);
                } else {
                    Toast.makeText(QLNVActivity.this, "Bạn vừa hủy quyền truy cập điện thoại", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

}
