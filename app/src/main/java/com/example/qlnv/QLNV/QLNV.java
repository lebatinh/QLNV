package com.example.qlnv.QLNV;

public class QLNV {

    private int MaNv;
    private String HoTen;
    private String ChucVu;
    private String GioiTinh;
    private String DiaChi;
    private String SDT;

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

    public String getGioiTinh() {
        return GioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        GioiTinh = gioiTinh;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public byte[] getHinh() {
        return hinh;
    }

    public void setHinh(byte[] hinh) {
        this.hinh = hinh;
    }

    public QLNV(int maNv, String hoTen, String chucVu, String gioiTinh, String diaChi, String SDT, byte[] hinh) {
        MaNv = maNv;
        HoTen = hoTen;
        ChucVu = chucVu;
        GioiTinh = gioiTinh;
        DiaChi = diaChi;
        this.SDT = SDT;
        this.hinh = hinh;
    }

    private byte[] hinh;

}
