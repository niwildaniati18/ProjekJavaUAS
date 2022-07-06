package frame;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import helpers.ComboBoxItem;
import helpers.Koneksi;

import javax.swing.*;
import java.sql.*;

public class PinjambukuInputFrame extends JFrame{
    private JPanel mainPanel;
    private JTextField idTextField;
    private JTextField namaTextField;
    private JComboBox bukuComboBox;
    private JButton simpanButton;
    private JButton batalButton;
    private JRadioButton romanceRadioButton;
    private JRadioButton comedyRadioButton;
    private JTextField emailTextField;
    private DatePicker tanggalPinjamDataPicker;

    private ButtonGroup jenisBukuButtonGroup;

    private int id;

    public void setId(int id){
        this.id = id;
    }

    public PinjambukuInputFrame(){

        simpanButton.addActionListener(e -> {
            String nama = namaTextField.getText();
            if (nama.equals("")) {
                JOptionPane.showMessageDialog(null,
                        "Isi Nama Peminjam",
                        "Validasi kata kunci kosong",
                        JOptionPane.WARNING_MESSAGE);
                namaTextField.requestFocus();
                return;
            }
            ComboBoxItem item = (ComboBoxItem) bukuComboBox.getSelectedItem();
            int bukuId = item.getValue();
            if (bukuId == 0){
                JOptionPane.showMessageDialog(null,
                        "Pilih Buku",
                        "Validasi Combobox",
                        JOptionPane.WARNING_MESSAGE);
                bukuComboBox.requestFocus();
                return;
            }

            String jenisBuku = "";
            if (romanceRadioButton.isSelected()){
                jenisBuku = "Romance";
            } else if (comedyRadioButton.isSelected()){
                jenisBuku = "Comedy";
            } else {
                JOptionPane.showMessageDialog(null,
                        "Pilih Jenis Buku",
                        "Validasi Data Kosong",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String email = emailTextField.getText();
            if (!email.contains("@") || !email.contains(".")) {
                JOptionPane.showMessageDialog(null,
                        "Isi dengan Email Valid",
                        "Validasi Email",
                        JOptionPane.WARNING_MESSAGE);
                emailTextField.requestFocus();
                return;
            }

            String tanggalPinjam = tanggalPinjamDataPicker.getText();
            if (tanggalPinjam.equals("")){
                JOptionPane.showMessageDialog(null,
                        "Isi Tanggal Pinjam",
                        "Validasi Data Kosong",
                        JOptionPane.WARNING_MESSAGE);
                tanggalPinjamDataPicker.requestFocus();
                return;
            }

            Connection c = Koneksi.getConnection();
            PreparedStatement ps;
            try {
                if (id == 0) {
                    String cekSQL = "SELECT * FROM pinjambuku WHERE nama = ? AND id != ?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, id);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null,
                                "Data yang anda masukkan suda ada");
                    } else {
                        String insertSQL = "INSERT INTO pinjambuku (id, nama, buku_id, jenis_buku, email, " +
                                "tanggalpinjam) VALUES (NULL, ?, ?, ?, ?, ?)";
                        ps = c.prepareStatement(insertSQL);
                        ps.setString(1, nama);
                        ps.setInt(2, bukuId);
                        ps.setString(3, jenisBuku);
                        ps.setString(4, email);
                        ps.setString(5, tanggalPinjam);
                        ps.executeUpdate();
                        dispose();
                    }
                } else {
                    String updateSQL = "UPDATE pinjambuku SET nama = ?, buku_id = ?, jenis_buku = ?, email = ?,  " +
                            "tanggalpinjam = ? WHERE id = ?";
                    ps = c.prepareStatement(updateSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, bukuId);
                    ps.setString(3, jenisBuku);
                    ps.setString(4, email);
                    ps.setString(5, tanggalPinjam);
                    ps.setInt(6, id);
                    ps.executeUpdate();
                    dispose();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        batalButton.addActionListener(e -> {
            dispose();
        });
        kustomisasiKomponen();
        init();
    }

    public void init(){
        setContentPane(mainPanel);
        setTitle("Input Data Pinjam Buku");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void isiKomponen(){
        Connection c = Koneksi.getConnection();
        String findSQL = "SELECT * FROM pinjambuku WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                idTextField.setText(String.valueOf(rs.getInt("id")));
                namaTextField.setText(rs.getString("nama"));
                int bukuId = rs.getInt("buku_id");
                for (int i = 0; i < bukuComboBox.getItemCount(); i++) {
                    bukuComboBox.setSelectedIndex(i);
                    ComboBoxItem item = (ComboBoxItem) bukuComboBox.getSelectedItem();
                    if (bukuId == item.getValue()) {
                        break;
                    }
                }
                String jenisBuku = rs.getString("jenis_buku");
                if (jenisBuku != null) {
                    if (jenisBuku.equals("Romance")) {
                        romanceRadioButton.setSelected(true);
                    } else if (jenisBuku.equals("Comedy")) {
                        comedyRadioButton.setSelected(true);
                    }
                }
                emailTextField.setText(rs.getString("email"));
                tanggalPinjamDataPicker.setText(rs.getString("tanggalpinjam"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void kustomisasiKomponen() {
        Connection c = Koneksi.getConnection();
        String selectSQL = "SELECT * FROM buku ORDER BY nama";
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);
            bukuComboBox.addItem(new ComboBoxItem(0, "Pilih Buku"));
            while (rs.next()) {
                bukuComboBox.addItem(new ComboBoxItem(
                        rs.getInt("id"),
                        rs.getString("nama")));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        jenisBukuButtonGroup = new ButtonGroup();
        jenisBukuButtonGroup.add(romanceRadioButton);
        jenisBukuButtonGroup.add(comedyRadioButton);

        DatePickerSettings dps = new DatePickerSettings();
        dps.setFormatForDatesCommonEra("yyyy-MM-dd");
        tanggalPinjamDataPicker.setSettings(dps);
    }
}
