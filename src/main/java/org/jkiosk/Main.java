package org.jkiosk;

import com.formdev.flatlaf.FlatLightLaf;
import org.jkiosk.ui.KioskFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // 윈도우 화면 배율(125%, 150%)을 무시하고 실제 크기로 띄우기 (창이 작아짐)
        // 너무 작으면 "1.0"을 "1.25"로 바꿔보기
        System.setProperty("sun.java2d.uiScale", "1.0");

        // FlatLaf 디자인 적용 (화면을 만들기 전에 해야 함)
        FlatLightLaf.setup();

        // Swing 화면은 SwingUtilities.invokeLater 안에서 만드는 게 규칙
        SwingUtilities.invokeLater(() -> {
            KioskFrame frame = new KioskFrame();
            frame.setVisible(true);
        });
    }
}