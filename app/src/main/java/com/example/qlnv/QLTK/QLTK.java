package com.example.qlnv.QLTK;

public class QLTK {
    private String TK;
    private String MK;

    public QLTK(String tk, String mk) {
        TK = tk;
        MK = mk;
    }

    public String getTK() {
        return TK;
    }

    public void setTK(String TK) {
        this.TK = TK;
    }

    public String getMK() {
        return MK;
    }

    public void setMK(String MK) {
        this.MK = MK;
    }
}
