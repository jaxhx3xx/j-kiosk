package org.jkiosk.ui;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;

// 화면 맨 위 헤더 (로고 + 매장/포장 배지 + 처음으로 버튼). 여러 화면에서 같이 사용
public class Header extends JPanel {

    private final JButton homeButton;
    private final RoundPanel badge;
    private final JLabel badgeLabel;

    public Header(KioskFrame frame) {
        setLayout(null);
        setBackground(Theme.WHITE);
        setBounds(0, 0, 540, 72);

        // 아래쪽에만 1px 회색 선
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.LINE));

        // ----- 로고: 파란 네모 안에 J -----
        RoundPanel logoBox = new RoundPanel(Theme.BLUE, 9);
        logoBox.setBounds(24, 19, 34, 34);

        JLabel logoJ = Theme.label("J", Theme.bold(19), Theme.WHITE);
        logoJ.setHorizontalAlignment(SwingConstants.CENTER);
        logoJ.setBounds(0, 0, 34, 34);
        logoBox.add(logoJ);
        add(logoBox);

        JLabel logoText = Theme.label("J의 카페", Theme.bold(20), Theme.TEXT);
        logoText.setBounds(68, 19, 200, 34);
        add(logoText);

        // ----- 오른쪽 "처음으로" 버튼 (흰 바탕 + 회색 테두리) -----
        homeButton = new JButton("처음으로", Theme.icon("home", 16));
        homeButton.setFont(Theme.regular(14));
        homeButton.setForeground(Theme.BUTTON_TEXT);
        homeButton.setBackground(Theme.WHITE);
        homeButton.setIconTextGap(6);  // 아이콘과 글씨 사이 간격
        homeButton.putClientProperty(FlatClientProperties.STYLE,
                "arc: 16; borderColor: #E5E8EC; focusWidth: 0; innerFocusWidth: 0");
        homeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 버튼 너비를 글씨+아이콘에 맞게 자동으로 계산하고, 오른쪽 끝에서 20px 띄워서 배치
        int buttonWidth = homeButton.getPreferredSize().width;
        homeButton.setBounds(540 - 20 - buttonWidth, 19, buttonWidth, 34);

        // 누르면 처음 화면으로 (나중에 "처음으로 돌아갈까요?" 팝업으로 바꿀 예정)
        homeButton.addActionListener(e -> frame.goHome());
        add(homeButton);

        // ----- 매장/포장 배지 (처음엔 숨김) -----
        badge = new RoundPanel(Theme.BADGE_BG, 8);
        badgeLabel = Theme.label("", Theme.bold(14), Theme.BLUE);
        badgeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        badge.add(badgeLabel);
        badge.setVisible(false);
        add(badge);
    }

    // 배지 바꾸기: "STORE" → 매장 식사, "TAKEOUT" → 포장, null → 숨김
    public void setOrderType(String orderType) {
        if (orderType == null) {
            badge.setVisible(false);
            return;
        }

        String text;
        if (orderType.equals("STORE")) {
            text = "매장 식사";
        } else {
            text = "포장";
        }
        badgeLabel.setText(text);

        // 글씨 길이에 맞춰 배지 너비 정하기 (양옆 여백 12px씩)
        int width = badgeLabel.getPreferredSize().width + 24;
        // "처음으로" 버튼 왼쪽에 8px 띄워서 배치
        int x = homeButton.getX() - 8 - width;

        badge.setBounds(x, 19, width, 34);
        badgeLabel.setBounds(0, 0, width, 34);
        badge.setVisible(true);
    }
}