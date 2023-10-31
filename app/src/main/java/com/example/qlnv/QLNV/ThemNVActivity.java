package com.example.qlnv.QLNV;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.Toast;

import com.example.qlnv.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ThemNVActivity extends AppCompatActivity {

    Button btnThem, btnThoat;
    EditText edtMaNv, edtHoTen, edtChucVu, edtGioiTinh, edtDiaChi, edtSDT;
    ImageView imgHinh;
    final int REQUEST_CODE_CAMERA = 123;
    final int REQUEST_CODE_FOLDER = 456;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_nv);

        AnhXa();

        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ThemNVActivity.this, QLNVActivity.class));
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chuyen data imageview sang byte
                BitmapDrawable bitmapDrawable = (BitmapDrawable) imgHinh.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
                byte[] hinhAnh = byteArray.toByteArray();

                QLNVActivity.database.INSERT_NHANVIEN(
                        edtMaNv.getText().toString().trim(),
                        edtHoTen.getText().toString().trim(),
                        edtChucVu.getText().toString().trim(),
                        edtGioiTinh.getText().toString().trim(),
                        edtDiaChi.getText().toString().trim(),
                        edtSDT.getText().toString().trim(),
                        hinhAnh
                );
                QLNVActivity.database.close();
                Toast.makeText(ThemNVActivity.this, "Đã thêm Nhân viên", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ThemNVActivity.this, QLNVActivity.class));
            }
        });

        imgHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ThemNVActivity.this);
                builder.setTitle("Bạn muốn tải ảnh lên bằng gì?");
                builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(
                                ThemNVActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                REQUEST_CODE_CAMERA
                        );
                    }
                });
                builder.setNegativeButton("Thư viện ảnh", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(
                                ThemNVActivity.this,
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // Mở camera để chụp ảnh
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CODE_CAMERA);
                }else {
                    Toast.makeText(this, "Bạn vừa hủy quyền truy cập camera", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CODE_FOLDER:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Mở thư viện ảnh để chọn ảnh
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_CODE_FOLDER);
                }else {
                    Toast.makeText(this, "Bạn vừa hủy quyền truy cập thư viện ảnh", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgHinh.setImageBitmap(bitmap);
        }
        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null){
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
        btnThem = (Button) findViewById(R.id.btnThem);
        btnThoat = (Button) findViewById(R.id.btnThoat);
        edtMaNv = (EditText) findViewById(R.id.edtMaNv);
        edtHoTen = (EditText) findViewById(R.id.edtHoTen);
        edtChucVu = (EditText) findViewById(R.id.edtChucVu);
        edtGioiTinh = (EditText) findViewById(R.id.edtGioiTinh);
        edtDiaChi = (EditText) findViewById(R.id.edtDiaChi);
        edtSDT = (EditText) findViewById(R.id.edtSDT);
        imgHinh     = (ImageView) findViewById(R.id.imgHinh);
    }
}