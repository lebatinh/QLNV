package com.example.qlnv.QLNV;

public class QLNV {

    private int MaNv;
    private String HoTen;
    private String ChucVu;
    private byte[] hinh;

    public int getMaNv() {
        return MaNv;
    }

    public void setMaNv(int maNv) {
        MaNv = maNv;
    }

    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(String hoTen) {
        HoTen = hoTen;
    }

    public String getChucVu() {
        return ChucVu;
    }

    public void setChucVu(String chucVu) {
        ChucVu = chucVu;
    }

    public byte[] getHinh() {
        return hinh;
    }

    public void setHinh(byte[] hinh) {
        this.hinh = hinh;
    }

    public QLNV(int maNv, String hoTen, String chucVu, byte[] hinh) {
        MaNv = maNv;
        HoTen = hoTen;
        ChucVu = chucVu;
        this.hinh = hinh;
    }
}