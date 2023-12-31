package com.example.qlnv.QLNV;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.qlnv.Database;
import com.example.qlnv.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SuaActivity extends AppCompatActivity {
    ImageView imgHinh;
    TextView edtMaNv;
    EditText edtHoTen;
    EditText edtChucVu;
    EditText edtGioiTinh;
    EditText edtDiaChi;
    EditText edtSDT;
    Button btnSua, btnThoat;
    final int REQUEST_CODE_CAMERA = 123;
    final int REQUEST_CODE_FOLDER = 456;
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua);

        // Khởi tạo và mở cơ sở dữ liệu
        database = new Database(SuaActivity.this, "QLNV.sqlite", null, 1);
        database.getWritableDatabase();

        AnhXa();

        /// Lấy thông tin từ Intent
        Intent intent = getIntent();
        String maNv = intent.getStringExtra("MaNv");
        String hoTen = intent.getStringExtra("HoTen");
        String gt = intent.getStringExtra("GioiTinh");
        String dc = intent.getStringExtra("DiaChi");
        String sdt = intent.getStringExtra("SDT");
        String cv = intent.getStringExtra("ChucVu");
        byte[] hinh = intent.getByteArrayExtra("HinhAnh");

        edtMaNv.setText(maNv);
        edtHoTen.setText(hoTen);
        edtGioiTinh.setText(gt);
        edtDiaChi.setText(dc);
        edtSDT.setText(sdt);
        edtChucVu.setText(cv);

        if (hinh != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(hinh, 0, hinh.length);
            imgHinh.setImageBitmap(bitmap);
        }

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //chuyen data imageview sang byte
                BitmapDrawable bitmapDrawable = (BitmapDrawable) imgHinh.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
                byte[] hinhAnh = byteArray.toByteArray();

                String maNv = edtMaNv.getText().toString().trim();
                String hoTen = edtHoTen.getText().toString().trim();
                String gioiTinh = edtGioiTinh.getText().toString().trim();
                String diaChi = edtDiaChi.getText().toString().trim();
                String sdt = edtSDT.getText().toString().trim();
                String chucVu = edtChucVu.getText().toString().trim();

                Cursor cursor = database.GetData("SELECT MaNv FROM QLNV WHERE MaNv = '" + maNv + "'");
                if (cursor.getCount() > 0) {
                    // Mã Nhân viên không thay đổi, tiến hành cập nhật thông tin
                    database.UPDATE_NHANVIEN(maNv, hoTen, gioiTinh, diaChi, sdt, chucVu, hinhAnh);
                    database.close();
                    Toast.makeText(SuaActivity.this, "Đã cập nhật Nhân viên", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SuaActivity.this, QLNVActivity.class));
                }
            }
        });
        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SuaActivity.this, QLNVActivity.class));
            }
        });
        imgHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SuaActivity.this);
                builder.setTitle("Bạn muốn tải ảnh lên bằng gì?");
                builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(
                                SuaActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                REQUEST_CODE_CAMERA
                        );
                    }
                });
                builder.setNegativeButton("Thư viện ảnh", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(
                                SuaActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_CODE_FOLDER
                        );
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Mở camera để chụp ảnh
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CODE_CAMERA);
                } else {
                    Toast.makeText(this, "Bạn vừa hủy quyền truy cập camera", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CODE_FOLDER:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Mở thư viện ảnh để chọn ảnh
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_CODE_FOLDER);
                } else {
                    Toast.makeText(this, "Bạn vừa hủy quyền truy cập thư viện ảnh", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgHinh.setImageBitmap(bitmap);
        }
        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgHinh.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

    private void AnhXa() {
        btnSua = (Button) findViewById(R.id.btnSua1);
        btnThoat = (Button) findViewById(R.id.btnThoat1);

        edtMaNv = (TextView) findViewById(R.id.edtMaNv1);
        edtHoTen = (EditText) findViewById(R.id.edtHoTen1);
        edtGioiTinh = (EditText) findViewById(R.id.edtGioiTinh1);
        edtDiaChi = (EditText) findViewById(R.id.edtDiaChi1);
        edtSDT = (EditText) findViewById(R.id.edtSDT1);
        edtChucVu = (EditText) findViewById(R.id.edtChucVu1);
        imgHinh = (ImageView) findViewById(R.id.imgHinh1);
    }
}