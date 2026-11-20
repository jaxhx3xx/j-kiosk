package org.jkiosk;

import com.formdev.flatlaf.FlatLightLaf;
import org.jkiosk.ui.KioskFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // FlatLaf 디자인 적용 (화면을 만들기 전에 해야 함)
        FlatLightLaf.setup();

        // Swing 화면은 SwingUtilities.invokeLater 안에서 만드는 게 규칙
        SwingUtilities.invokeLater(() -> {
            KioskFrame frame = new KioskFrame();
            frame.setVisible(true);
        });
    }
}