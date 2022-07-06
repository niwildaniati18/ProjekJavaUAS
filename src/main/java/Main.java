import frame.BukuViewFrame;
import frame.PinjambukuViewFrame;
//import helpers.Koneksi;

public class Main {
    public static void main(String[] args) {
//        Koneksi.getConnection();
//        BukuViewFrame viewFrame = new BukuViewFrame();
        PinjambukuViewFrame viewFrame = new PinjambukuViewFrame();
        viewFrame.setVisible(true);
    }
}
