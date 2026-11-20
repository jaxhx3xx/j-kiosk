package org.jkiosk.ui;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// 01 시작 화면 (피그마 좌표 그대로 배치)
public class StartScreen extends JPanel {

    public StartScreen(KioskFrame frame) {
        setLayout(null);  // setBounds(x, y, 너비, 높이)로 직접 배치
        setBackground(Theme.WHITE);

        // ----- 로고: 파란 네모 안에 J -----
        RoundPanel logoBox = new RoundPanel(Theme.BLUE, 10);
        logoBox.setBounds(32, 48, 40, 40);

        JLabel logoJ = Theme.label("J", Theme.bold(22), Theme.WHITE);
        logoJ.setHorizontalAlignment(SwingConstants.CENTER);
        logoJ.setBounds(0, 0, 40, 40);
        logoBox.add(logoJ);
        add(logoBox);

        JLabel logoText = Theme.label("J의 카페", Theme.bold(22), Theme.TEXT);
        logoText.setBounds(82, 48, 200, 40);
        add(logoText);

        // ----- 큰 제목 2줄 -----
        JLabel title1 = Theme.label("어서 오세요", Theme.bold(44), Theme.TEXT);
        title1.setBounds(32, 190, 476, 60);
        add(title1);

        JLabel title2 = Theme.label("J의 카페입니다", Theme.bold(44), Theme.BLUE);
        title2.setBounds(32, 252, 476, 60);
        add(title2);

        // ----- 설명 -----
        JLabel guide = Theme.label("화면을 터치하면 주문을 시작해요", Theme.regular(18), Theme.SUB_TEXT);
        guide.setBounds(32, 326, 476, 30);
        add(guide);

        // ----- 메뉴 미리보기 (회색 둥근 상자 + 그림 3개) -----
        RoundPanel preview = new RoundPanel(Theme.LIGHT_BG, 24);
        preview.setBounds(32, 410, 476, 280);

        int[] menuIds = {1, 6, 9};       // 아메리카노, 딸기 스무디, 딸기 생크림 케이크
        int[] xs = {23, 173, 323};       // 피그마에서의 x 위치
        for (int i = 0; i < menuIds.length; i++) {
            JLabel image = new JLabel(Theme.menuImage(menuIds[i], 130));
            image.setBounds(xs[i], 75, 130, 130);
            preview.add(image);
        }
        add(preview);

        // ----- 주문 시작하기 버튼 -----
        JButton startButton = Theme.primaryButton("주문 시작하기");
        startButton.setBounds(32, 808, 476, 76);
        startButton.addActionListener(e -> frame.showScreen(KioskFrame.PLACE));
        add(startButton);

        // ----- 아래 작은 안내 문구 (가운데 정렬) -----
        JLabel bottom = Theme.label("매장 식사 · 포장 모두 가능해요", Theme.regular(14), Theme.SUB_TEXT);
        bottom.setHorizontalAlignment(SwingConstants.CENTER);
        bottom.setBounds(0, 904, 540, 20);
        add(bottom);

        // ----- 화면 아무 데나 눌러도 다음으로 -----
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.showScreen(KioskFrame.PLACE);
            }
        });
    }
}