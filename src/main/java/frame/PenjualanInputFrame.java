package frame;

import helpers.Koneksi;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PenjualanInputFrame extends JFrame{
    private JPanel mainPanel;
    private JTextField idTextField;
    private JTextField judulTextField;
    private JButton simpanButton;
    private JButton batalButton;

    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public PenjualanInputFrame(){
        simpanButton.addActionListener(e -> {
            String judul = judulTextField.getText();
            Connection c = Koneksi.getConnection();
            PreparedStatement ps;
            try {
                if (id == 0) {
                    String insertSQL = "INSERT INTO buku VALUES (NULL, ?)";
                    ps = c.prepareStatement(insertSQL);
                    ps.setString(1, judul);
                    ps.executeUpdate();
                    dispose();
                } else {
                    String updateSQL = "UPDATE buku SET judul_buku = ? WHERE id = ?";
                    ps = c.prepareStatement(updateSQL);
                    ps.setString(1, judul);
                    ps.setInt(2, id);
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
        init();
    }

    public void init(){
        setContentPane(mainPanel);
        setTitle("Input buku");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void isiKomponen(){
        Connection c = Koneksi.getConnection();
        String findSQL = "SELECT * FROM buku WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                idTextField.setText(String.valueOf(rs.getInt("id")));
                judulTextField.setText(rs.getString("judul_buku"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
