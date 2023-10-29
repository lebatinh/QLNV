package com.example.qlnv;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

import com.example.qlnv.QLTK.QLTKActivity;

import java.sql.PreparedStatement;

public class Database extends SQLiteOpenHelper {

    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    //truy vấn không trả KQ: create, insert, update, delete....
    public PreparedStatement QueryData(String sql) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
        return null;
    }

    public void INSERT_NHANVIEN(String maNv, String hoTen, String gioiTinh, String diaChi, String sdt, String chucVu, byte[] hinhAnh){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO QLNV VALUES(?,?,?,?,?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, maNv);
        statement.bindString(2, hoTen);
        statement.bindString(3, gioiTinh);
        statement.bindString(4, diaChi);
        statement.bindString(5, sdt);
        statement.bindString(6, chucVu);
        statement.bindBlob(7, hinhAnh);
        statement.executeInsert();
    }

    public void UPDATE_NHANVIEN(String maNv, String hoTen, String gioiTinh, String diaChi, String sdt, String chucVu, byte[] hinhAnh) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE QLNV SET HoTen = ?, GioiTinh = ?, DiaChi = ?, SDT = ?, ChucVu = ?, HinhAnh = ? WHERE MaNv = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, hoTen);
        statement.bindString(2, gioiTinh);
        statement.bindString(3, diaChi);
        statement.bindString(4, sdt);
        statement.bindString(5, chucVu);
        statement.bindBlob(6, hinhAnh);
        statement.bindString(7, maNv);

        statement.executeUpdateDelete();
    }


    //truy vấn trả về KQ: select
    public Cursor GetData(String sql) {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql,null);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
