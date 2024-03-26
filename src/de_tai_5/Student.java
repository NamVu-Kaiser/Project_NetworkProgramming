// Student.java
package de_tai_5;

import java.sql.Date;
import java.io.Serializable;

public class Student implements Serializable{
    private String masv;
    private String tensv;
    private boolean gioitinh;
    private Date ngaysinh;
    private String noisinh;
    private int khoa;
    private String malop;
    private String tenkhoa;
    private String tennganh;

    public Student(String masv, String tensv, boolean gioitinh, Date ngaysinh, String noisinh, int khoa, String malop, String tenkhoa, String tennghanh) {
        this.masv = masv;
        this.tensv = tensv;
        this.gioitinh = gioitinh;
        this.ngaysinh = ngaysinh;
        this.noisinh = noisinh;
        this.khoa = khoa;
        this.malop = malop;
        this.tennganh = tennghanh;
        this.tenkhoa = tenkhoa;
        
    }

    public String getMasv() {
        return masv;
    }

    public String getTensv() {
        return tensv;
    }

    public boolean isGioitinh() {
        return gioitinh;
    }

    public Date getNgaysinh() {
        return ngaysinh;
    }

    public String getNoisinh() {
        return noisinh;
    }

    public int getKhoa() {
        return khoa;
    }

    public String getMalop() {
        return malop;
    }

    public String getTenkhoa() {
        return tenkhoa;
    }

    public void setTenkhoa(String tenkhoa) {
        this.tenkhoa = tenkhoa;
    }

    public String getTennganh() {
        return tennganh;
    }

    public void setTennganh(String tennganh) {
        this.tennganh = tennganh;
    }
    
}