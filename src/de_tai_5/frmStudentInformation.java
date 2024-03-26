/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de_tai_5;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author DerKaiser
 */
public class frmStudentInformation extends javax.swing.JFrame {

    private String mssv;
    private String fullName;
    
    public frmStudentInformation() {
        initComponents();
        displayKhoaData();
    }
    
    
    public String[] getKhoaDataFromDatabase() {
        Connection connection = Connect_Database.connect();
        String query = "SELECT tenkhoa FROM Khoa";
        ArrayList<String> khoaData = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                khoaData.add(resultSet.getString("tenkhoa"));
            }

            String[] khoaDataArray = khoaData.toArray(new String[0]);
            Connect_Database.close(connection);
            return khoaDataArray;
        } catch (SQLException e) {
            System.out.println("Error fetching department data: " + e.getMessage());
            return new String[0];
        }
    }
    
    public void displayKhoaData() {
        String[] khoaData = getKhoaDataFromDatabase();
        cmbKhoa.setModel(new DefaultComboBoxModel<>(khoaData));
    }
    
    public String[] getNganhDataFromDatabase(String tenKhoa) {
        Connection connection = Connect_Database.connect();
        String query = "SELECT tennganh FROM Nganh WHERE makhoa = (SELECT makhoa FROM Khoa WHERE tenkhoa = ?)";
        ArrayList<String> nganhData = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, tenKhoa);
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                nganhData.add(resultSet.getString("tennganh"));
            }

            String[] nganhDataArray = nganhData.toArray(new String[0]);
            Connect_Database.close(connection);
            return nganhDataArray;
        } catch (SQLException e) {
            System.out.println("Error fetching department data: " + e.getMessage());
            return new String[0];
        }
    }
    
    public void displayNganhData(String tenKhoa) {
        String[] nganhData = getNganhDataFromDatabase(tenKhoa);
        cmbNganh.setModel(new DefaultComboBoxModel<>(nganhData));
    }
    
    public String[] getLopDataFromDatabase(String tenNganh) {
        Connection connection = Connect_Database.connect();
        String query = "SELECT malop FROM Lop WHERE manganh = (SELECT manganh FROM Nganh WHERE tennganh = ?)";
        ArrayList<String> lopData = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, tenNganh);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                lopData.add(resultSet.getString("malop"));
            }

            String[] lopDataArray = lopData.toArray(new String[0]);
            Connect_Database.close(connection);
            return lopDataArray;
        } catch (SQLException e) {
            System.out.println("Error fetching class data: " + e.getMessage());
            return new String[0];
        }
    }

    public void displayLopData(String tenNganh) {
        String[] lopData = getLopDataFromDatabase(tenNganh);
        cmbLop.setModel(new DefaultComboBoxModel<>(lopData));
    }
    
    public void loadTableDiem(String mssv) {
        Connection connection = Connect_Database.connect();
        String query = "SELECT MonHoc.tenmh, BangDiem.diem_quatrinh, BangDiem.diem_cuoiky, BangDiem.diem_chu FROM MonHoc "
                + "INNER JOIN BangDiem ON MonHoc.mamh = BangDiem.mamh "
                + "WHERE BangDiem.masv = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, mssv);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Create a DefaultTableModel to store data for the JTable
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("STT");
            model.addColumn("Môn Học");
            model.addColumn("Điểm Quá Trình");
            model.addColumn("Điểm Cuối Kỳ");
            model.addColumn("Điểm Chữ");
            int count = 1;

            while (resultSet.next()) {
                // Retrieve data from the result set
                String tenMonHoc = resultSet.getString("tenmh");
                float diemQuaTrinh = resultSet.getFloat("diem_quatrinh");
                float diemCuoiKy = resultSet.getFloat("diem_cuoiky");
                String diemChu = resultSet.getString("diem_chu");

                // Add the data to the table model
                model.addRow(new Object[]{count, tenMonHoc, diemQuaTrinh, diemCuoiKy, diemChu});
                count++;
            }
            
            // Set the table model to the JTable
            tbDiem.setModel(model);
            displayGPA(mssv);
            Connect_Database.close(connection);
        } catch (SQLException e) {
            System.out.println("Error loading table Diem: " + e.getMessage());
        }
    }
    
    public frmStudentInformation(String mssv, String fullname) {
        this(); // Call the default constructor to initialize components
        this.fullName = fullname;
        lb_Show_fullname.setText(fullName); 
        this.mssv = mssv;
        txtMSSV.setText(this.mssv);
        loadTableDiem(mssv);
        
    }
    
    public void setStudentInformation(String mssv, String tenSV, boolean gioiTinh, Date ngaySinh, String noiSinh,int khoa, String tenKhoa, String tenNganh, String maLop) {
        this.mssv = mssv;
        txtMSSV.setText(this.mssv);
        txtHoTen.setText(tenSV);
        if (gioiTinh) {
            cbNam.setSelected(true);
            cbNu.setSelected(false);
        } else {
            cbNam.setSelected(false);
            cbNu.setSelected(true);
        }
        Date dateNgaySinh = new Date(ngaySinh.getTime());
        dcNgaySinh.setDate(dateNgaySinh);
        txtNoiSinh.setText(noiSinh);
        txtKhoa.setText(String.valueOf(khoa));
        
        cmbKhoa.setSelectedItem(tenKhoa);
        cmbNganh.setSelectedItem(tenNganh);
        cmbLop.setSelectedItem(maLop);
        // Set other text fields or components with received data
    }
    public void displayGPA(String mssv) {
        Connection connection = Connect_Database.connect();
        String query = "SELECT COUNT(bd.mamh) AS[Tổng số môn đã học] ,SUM(bd.diem_monhoc_he10) / COUNT(bd.mamh) AS [Điểm trung bình tích lũy(hệ 10)], (SUM(bd.diem_monhoc_he10) / COUNT(bd.mamh))*0.4 AS [Điểm trung bình tích lũy(hệ 4)], sum(mh.tinchi) AS[Tổng số tín chỉ đã học] FROM BangDiem bd, MonHoc mh WHERE bd.masv = ? and bd.mamh = mh.mamh";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, mssv);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String TongSoMonDaHoc = resultSet.getString("Tổng số môn đã học");
                lbtongsomondahoc.setText(TongSoMonDaHoc);
                String DTBTLHe10 = resultSet.getString("Điểm trung bình tích lũy(hệ 10)");
                lbDTBTLHe10.setText(DTBTLHe10);
                String DTBTLHe4 = resultSet.getString("Điểm trung bình tích lũy(hệ 4)");
                lbDTBTLHe4.setText(DTBTLHe4);
                String TongSTCDaHoc = resultSet.getString("Tổng số tín chỉ đã học");
                lbSTCTL.setText(TongSTCDaHoc);
            } else {
                lbtongsomondahoc.setText("0");
                lbDTBTLHe10.setText("0");
                lbDTBTLHe4.setText("0");
                lbSTCTL.setText("0");
            }

            Connect_Database.close(connection);
        } catch (SQLException e) {
            System.out.println("Error calculating GPA: " + e.getMessage());
        }
    }



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lbHoTen = new javax.swing.JLabel();
        lbNoiSinh = new javax.swing.JLabel();
        lbGioiTinh = new javax.swing.JLabel();
        lbNgaySinh = new javax.swing.JLabel();
        lbKhoa = new javax.swing.JLabel();
        lbMSSV = new javax.swing.JLabel();
        lbLop = new javax.swing.JLabel();
        txtHoTen = new javax.swing.JTextField();
        txtMSSV = new javax.swing.JTextField();
        txtNoiSinh = new javax.swing.JTextField();
        txtKhoa = new javax.swing.JTextField();
        cbNam = new javax.swing.JCheckBox();
        cbNu = new javax.swing.JCheckBox();
        cmbLop = new javax.swing.JComboBox<>();
        lbLop1 = new javax.swing.JLabel();
        lbLop2 = new javax.swing.JLabel();
        cmbKhoa = new javax.swing.JComboBox<>();
        cmbNganh = new javax.swing.JComboBox<>();
        dcNgaySinh = new com.toedter.calendar.JDateChooser();
        lbFullName = new javax.swing.JLabel();
        lb_Show_fullname = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbDiem = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        lbtongsomondahoc = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lbDTBTLHe10 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lbDTBTLHe4 = new javax.swing.JLabel();
        lbSTCTL = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setText("THÔNG TIN SINH VIÊN");

        btnBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de_tai_5/images/btn_back.png"))); // NOI18N
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        lbHoTen.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lbHoTen.setText("Họ tên");

        lbNoiSinh.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lbNoiSinh.setText("Nơi sinh");

        lbGioiTinh.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lbGioiTinh.setText("Giới tính");

        lbNgaySinh.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lbNgaySinh.setText("Ngày sinh");

        lbKhoa.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lbKhoa.setText("Khóa");

        lbMSSV.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lbMSSV.setText("Mã số sinh viên");

        lbLop.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lbLop.setText("Lớp");

        txtHoTen.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        txtMSSV.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        txtNoiSinh.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        txtKhoa.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        cbNam.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        cbNam.setText("Nam");

        cbNu.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        cbNu.setText("Nữ");

        cmbLop.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        cmbLop.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lbLop1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lbLop1.setText("Ngành");

        lbLop2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lbLop2.setText("Khoa");

        cmbKhoa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbKhoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbKhoaActionPerformed(evt);
            }
        });

        cmbNganh.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        cmbNganh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbNganh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbNganhActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbHoTen)
                    .addComponent(lbMSSV)
                    .addComponent(lbNoiSinh)
                    .addComponent(lbKhoa)
                    .addComponent(lbGioiTinh)
                    .addComponent(lbLop)
                    .addComponent(lbLop2)
                    .addComponent(lbLop1)
                    .addComponent(lbNgaySinh))
                .addGap(50, 50, 50)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtKhoa)
                    .addComponent(txtNoiSinh)
                    .addComponent(txtMSSV)
                    .addComponent(txtHoTen)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cbNam)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 204, Short.MAX_VALUE)
                        .addComponent(cbNu))
                    .addComponent(cmbLop, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbKhoa, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbNganh, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dcNgaySinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbMSSV, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMSSV, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbNam, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbNu, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbNgaySinh, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(dcNgaySinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbNoiSinh, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNoiSinh, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbKhoa, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKhoa, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbLop, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbLop, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbLop1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbNganh, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbLop2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbKhoa, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lbFullName.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lbFullName.setText("Họ tên:");

        lb_Show_fullname.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel2.setText("Điểm học phần");

        tbDiem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tbDiem);

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setText("Tổng số môn đã học");

        lbtongsomondahoc.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel5.setText("Điểm trung bình tích lũy (hệ 10):");

        lbDTBTLHe10.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel7.setText("Điểm trung bình tích lũy (hệ 4):");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel8.setText("Số tín chỉ tích lũy:");

        lbDTBTLHe4.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N

        lbSTCTL.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbFullName)
                        .addGap(19, 19, 19)
                        .addComponent(lb_Show_fullname, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addComponent(btnBack))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel2))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lbtongsomondahoc, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lbSTCTL, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(lbDTBTLHe10, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                                .addComponent(lbDTBTLHe4, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(129, 129, 129))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbFullName, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lb_Show_fullname, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbtongsomondahoc, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbDTBTLHe10, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbDTBTLHe4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbSTCTL, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 74, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbKhoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbKhoaActionPerformed
        // TODO add your handling code here:
        String selectedKhoa = cmbKhoa.getSelectedItem().toString();
        displayNganhData(selectedKhoa);
    }//GEN-LAST:event_cmbKhoaActionPerformed

    private void cmbNganhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbNganhActionPerformed
        // TODO add your handling code here:
        String selectedNganh = cmbNganh.getSelectedItem().toString();
        displayLopData(selectedNganh);
    }//GEN-LAST:event_cmbNganhActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        // Đóng cửa sổ hiện tại
        this.dispose();

        // Hiển thị lại frmMain
        frmMain mainForm = new frmMain(fullName); // Thay 'frmMain' bằng tên lớp của frmMain nếu tên khác
        mainForm.setVisible(true);
    }//GEN-LAST:event_btnBackActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmStudentInformation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmStudentInformation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmStudentInformation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmStudentInformation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmStudentInformation().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JCheckBox cbNam;
    private javax.swing.JCheckBox cbNu;
    private javax.swing.JComboBox<String> cmbKhoa;
    private javax.swing.JComboBox<String> cmbLop;
    private javax.swing.JComboBox<String> cmbNganh;
    private com.toedter.calendar.JDateChooser dcNgaySinh;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbDTBTLHe10;
    private javax.swing.JLabel lbDTBTLHe4;
    private javax.swing.JLabel lbFullName;
    private javax.swing.JLabel lbGioiTinh;
    private javax.swing.JLabel lbHoTen;
    private javax.swing.JLabel lbKhoa;
    private javax.swing.JLabel lbLop;
    private javax.swing.JLabel lbLop1;
    private javax.swing.JLabel lbLop2;
    private javax.swing.JLabel lbMSSV;
    private javax.swing.JLabel lbNgaySinh;
    private javax.swing.JLabel lbNoiSinh;
    private javax.swing.JLabel lbSTCTL;
    private javax.swing.JLabel lb_Show_fullname;
    private javax.swing.JLabel lbtongsomondahoc;
    private javax.swing.JTable tbDiem;
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JTextField txtKhoa;
    private javax.swing.JTextField txtMSSV;
    private javax.swing.JTextField txtNoiSinh;
    // End of variables declaration//GEN-END:variables
}
