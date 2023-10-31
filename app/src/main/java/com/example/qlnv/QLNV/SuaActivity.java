package com.example.qlnv.QLNV;

import static com.example.qlnv.QLNV.QLNVActivity.database;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.List;

public class SuaActivity extends AppCompatActivity {
    ImageView imgHinh;
    EditText edtMaNv, edtHoTen, edtChucVu, edtGioiTinh, edtDiaChi, edtSDT;
    Button btnSua, btnThoat;
    final int REQUEST_CODE_CAMERA = 123;
    final int REQUEST_CODE_FOLDER = 456;

    private List<QLNV> qlnvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua);

        AnhXa();

        int i = 0;
        edtMaNv.setText(QLNVActivity.arrayNv.get(i).getMaNv());
        edtHoTen.setText(QLNVActivity.arrayNv.get(i).getHoTen());
        edtChucVu.setText(QLNVActivity.arrayNv.get(i).getChucVu());

//        String query = ("SELECT GioiTinh, DiaChi, SDT FROM QLNV WHERE MaNv = '"+edtMaNv+"' ");
//        Cursor cursor = database.GetData(query);
//
//        if(cursor.moveToFirst()) {
//            do {
//                String gt = cursor.getString(4);
//                String dc = cursor.getString(5);
//                String sdt = cursor.getString(6);
//
//                edtGioiTinh.setText(gt);
//                edtDiaChi.setText(dc);
//                edtSDT.setText(sdt);
//
//            } while (cursor.moveToNext());
//        }cursor.close();


        //lấy dữ liệu byte và chuyển byte thành bitmap để dùng
        byte[] hinh = QLNVActivity.arrayNv.get(i).getHinh();
        Bitmap bitmap = BitmapFactory.decodeByteArray(hinh, 0, hinh.length);
        imgHinh.setImageBitmap(bitmap);

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

                database.GetData("SELECT MaNv, Hoten, ChucVu, GioiTinh, DiaChi, SDT,  HinhAnh FROM QLNV WHERE MaNv = '" + maNv + "' ");

                int id1 = QLNVActivity.arrayNv.get(i).getMaNv();
                if (maNv.equals(id1)) {
                    database.UPDATE_NHANVIEN(maNv, hoTen, gioiTinh, diaChi, sdt, chucVu, hinhAnh);

                    Toast.makeText(SuaActivity.this, "Đã sửa Nhân viên", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SuaActivity.this, QLNVActivity.class));
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SuaActivity.this);
                    builder.setMessage("Mã Nhân viên KHÔNG ĐƯỢC thay đổi.");
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

        edtMaNv = (EditText) findViewById(R.id.edtMaNv1);
        edtHoTen = (EditText) findViewById(R.id.edtHoTen1);
        edtChucVu = (EditText) findViewById(R.id.edtChucVu1);
        edtGioiTinh = (EditText) findViewById(R.id.edtGioiTinh1);
        edtDiaChi = (EditText) findViewById(R.id.edtDiaChi1);
        edtSDT = (EditText) findViewById(R.id.edtSDT1);
        imgHinh = (ImageView) findViewById(R.id.imgHinh1);
    }
}