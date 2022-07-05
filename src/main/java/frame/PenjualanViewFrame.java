package frame;

import javax.swing.*;

public class PenjualanViewFrame extends JFrame{
    private JPanel mainPanel;
    private JTextField cariTextField;
    private JButton cariButton;
    private JScrollPane viewScrollPane;
    private JButton tambahButton;
    private JButton ubahButton;
    private JButton batalButton;
    private JButton hapusButton;
    private JButton batalButton1;
    private JButton tutupButton;
    private JTable viewTable;

    public PenjualanViewFrame(){
        init();
    }
    public void init(){
        setContentPane(mainPanel);
        setTitle("Data penjualan");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
