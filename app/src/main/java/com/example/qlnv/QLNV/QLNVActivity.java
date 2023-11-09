package com.example.qlnv.QLNV;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;

import androidx.appcompat.widget.SearchView;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.qlnv.Database;
import com.example.qlnv.QLTK.QLTKActivity;
import com.example.qlnv.R;

import java.sql.PreparedStatement;
import java.util.ArrayList;

public class QLNVActivity extends AppCompatActivity {

    public static Database database;
    ListView lvNv;
    ArrayList<QLNV> arrayNv;
    QLNVAdapter adapter;
    ImageButton btnMenu;
    ImageButton btnThem;
    final int REQUEST_CODE_PHONE = 1;
    int index = -1;

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlnv);

        lvNv = findViewById(R.id.lvNv);
        arrayNv = new ArrayList<>();

        adapter = new QLNVAdapter(this, R.layout.nv_defaut, arrayNv);
        lvNv.setAdapter(adapter);

        //tạo database QLTK
        database = new Database(QLNVActivity.this, "QLNV.sqlite", null, 1);

        //tạo bảng Quản lý tài khoản
        database.QueryData("CREATE TABLE IF NOT EXISTS QLNV(MaNv VARCHAR(10) PRIMARY KEY , HoTen VARCHAR(50)," +
                " ChucVu VARCHAR(50), GioiTinh VARCHAR(10), Diachi VARCHAR(50), SDT VARCHAR(11), HinhAnh BLOB)");

        // Lấy dữ liệu
        Cursor cursor = database.GetData("SELECT MaNv, HoTen, ChucVu, GioiTinh, DiaChi, SDT, HinhAnh FROM QLNV");
        while (cursor.moveToNext()) {
            arrayNv.add(new QLNV(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getBlob(6)
            ));
            adapter.notifyDataSetChanged();
        }
        cursor.close();
        lvNv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long id) {
                index = i;
                Object item = parent.getItemAtPosition(index);
                QLNV qlnv = (QLNV) item;
                String maNv = String.valueOf(qlnv.getMaNv());
                String hoten = qlnv.getHoTen();
                byte[] hinh = qlnv.getHinh();
                String gt = qlnv.getGioiTinh();
                String dc = qlnv.getDiaChi();
                String sdt = qlnv.getSDT();
                String cv = qlnv.getChucVu();

                AlertDialog.Builder builder = new AlertDialog.Builder(QLNVActivity.this);
                builder.setTitle("Cảnh báo");
                builder.setMessage("Bạn muốn xóa hay sửa nhân viên có mã nhân viên " + maNv + " này?");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(QLNVActivity.this);
                        builder1.setTitle("Cảnh báo");
                        builder1.setMessage("Bạn có chắc chắn muốn xóa nhân viên có mã nhân viên là " + maNv + " này?");
                        builder1.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String query = "DELETE FROM QLNV WHERE MaNv = '" + maNv + "'";
                                PreparedStatement pstmt = database.QueryData(query);
                                arrayNv.remove(index);
                                database.close();
                                adapter.notifyDataSetChanged();
                            }
                        });
                        builder1.setNegativeButton("Không", null);
                        builder1.show();
                    }
                });
                builder.setNegativeButton("Sửa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(QLNVActivity.this, SuaActivity.class);

                        // Gửi thông tin nhân viên tới SuaActivity
                        intent.putExtra("MaNv", maNv);
                        intent.putExtra("HoTen", hoten);
                        intent.putExtra("ChucVu", cv);
                        intent.putExtra("GioiTinh", gt);
                        intent.putExtra("DiaChi", dc);
                        intent.putExtra("SDT", sdt);
                        // Truyền dữ liệu hình ảnh dưới dạng byte array
                        intent.putExtra("HinhAnh", hinh);

                        startActivity(intent);
                    }
                });
                builder.setNeutralButton("Thoát", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return true;
            }
        });

        btnThem = (ImageButton) findViewById(R.id.btnThem);
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QLNVActivity.this, ThemNVActivity.class));
            }
        });

        // Đọc thông tin tài khoản đăng nhập từ SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String loggedInUser = sharedPreferences.getString("LoggedInUser", null);

        btnMenu = (ImageButton) findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMenu(loggedInUser);
            }
        });

        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Thực hiện tìm kiếm khi người dùng nhấn nút tìm kiếm trên bàn phím
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Thực hiện tìm kiếm theo mỗi ký tự thay đổi trong ô tìm kiếm
                performSearch(newText);
                return true;
            }
        });
    }

    private void performSearch(String query) {
        ArrayList<QLNV> searchResults = new ArrayList<>();

        for (QLNV employee : arrayNv) {
            if (employee.getMaNv().toLowerCase().contains(query.toLowerCase()) ||
                    employee.getHoTen().toLowerCase().contains(query.toLowerCase()) ||
                    employee.getChucVu().toLowerCase().contains(query.toLowerCase())) {
                searchResults.add(employee);
            }
        }

        adapter = new QLNVAdapter(this, R.layout.nv_defaut, searchResults);
        lvNv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void ShowMenu(String loggedInUser) {
        PopupMenu popupMenu = new PopupMenu(QLNVActivity.this, btnMenu);
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
        // Thêm thông tin tài khoản đăng nhập vào menu
        Menu menu = popupMenu.getMenu();
//        menu.findItem(R.id.itemAccount).setTitle(loggedInUser);
        if (loggedInUser != null) {
            // Đã đăng nhập, hiển thị tên tài khoản trong menu
            menu.findItem(R.id.itemAccount).setTitle("Tài Khoản : " + loggedInUser).setEnabled(false);
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.itemBaoLoi) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.me/leba.tinh.36vip7star.sv7.real"));
                    startActivity(intent);
                } else if (item.getItemId() == R.id.itemExit) {
                    startActivity(new Intent(QLNVActivity.this, QLTKActivity.class));
                    return true;
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
