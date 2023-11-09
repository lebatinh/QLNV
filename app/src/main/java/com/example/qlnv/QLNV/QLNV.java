package com.example.qlnv.QLNV;

public class QLNV {

    private String MaNv;
    private String HoTen;
    private String ChucVu;
    private String GioiTinh;
    private String DiaChi;
    private String SDT;
    private byte[] hinh;

    public String getMaNv() {
        return MaNv;
    }

    public void setMaNv(String maNv) {
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

    public QLNV(String maNv, String hoTen, String chucVu, String gioiTinh,
                String diaChi, String sdt, byte[] hinh) {
        MaNv = maNv;
        HoTen = hoTen;
        ChucVu = chucVu;
        GioiTinh = gioiTinh;
        DiaChi = diaChi;
        SDT = sdt;
        this.hinh = hinh;
    }
}
