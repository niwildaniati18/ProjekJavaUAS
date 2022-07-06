package frame;

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
                        String insertSQL = "INSERT INTO pinjambuku (id, nama, buku_id) VALUES (NULL, ?, ?)";
                        ps = c.prepareStatement(insertSQL);
                        ps.setString(1, nama);
                        ps.setInt(2, bukuId);
                        ps.executeUpdate();
                        dispose();
                    }
                } else {
                    String updateSQL = "UPDATE pinjambuku SET nama = ?, buku_id = ? WHERE id = ?";
                    ps = c.prepareStatement(updateSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, bukuId);
                    ps.setInt(3, id);
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
    }
}
