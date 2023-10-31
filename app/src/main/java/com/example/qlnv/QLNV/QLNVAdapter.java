package com.example.qlnv.QLNV;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qlnv.R;
import java.util.List;

public class QLNVAdapter extends BaseAdapter {
    private QLNVActivity context;
    private int layout;
    private static List<QLNV> qlnvList;

    public QLNVAdapter(QLNVActivity context, int layout, List<QLNV> qlnvList) {
        this.context = context;
        this.layout = layout;
        this.qlnvList = qlnvList;
    }

    @Override
    public int getCount() {
        return qlnvList.size();
    }

    @Override
    public Object getItem(int position) {
        return qlnvList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return qlnvList.get(position).getMaNv();
    }

    private class ViewHoler {
        TextView txtName, txtCv, txtMaNv;
        ImageView imgNv;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHoler holer;

        if(view == null){
            holer = new ViewHoler();
            LayoutInflater inflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holer.imgNv     = (ImageView) view.findViewById(R.id.imgNv);
            holer.txtName   = (TextView) view.findViewById(R.id.txtName);
            holer.txtCv     = (TextView) view.findViewById(R.id.txtCv);
            holer.txtMaNv   = (TextView) view.findViewById(R.id.txtMaNv);

            view.setTag(holer);
        }
        else {
            holer = (ViewHoler) view.getTag();
        }

        QLNV qlnv = qlnvList.get(position);

        //chuyen byte sang bitmap
        byte[] hinhAnh = qlnv.getHinh();
        Bitmap bitmap = BitmapFactory.decodeByteArray(hinhAnh, 0 , hinhAnh.length);
        holer.imgNv.setImageBitmap(bitmap);

        holer.txtName.setText(qlnv.getHoTen());
        holer.txtCv.setText(qlnv.getChucVu());
        holer.txtMaNv.setText(String.valueOf(qlnv.getMaNv()));
        return view;
    }
}
