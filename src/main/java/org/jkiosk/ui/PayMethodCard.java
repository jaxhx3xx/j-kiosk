package org.jkiosk.ui;

import javax.swing.*;
import java.awt.*;

// 결제 수단 한 칸 (아이콘 + 이름 + 설명 + 오른쪽 동그라미). 크기 492 x 88
public class PayMethodCard extends RoundPanel {

    private final RoundPanel iconBox;
    private final JLabel radio;

    public PayMethodCard(String iconName, String title, String desc) {
        super(Theme.WHITE, 14);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // ----- 왼쪽 아이콘 -----
        iconBox = new RoundPanel(Theme.LIGHT_BG, 12);
        iconBox.setBounds(20, 20, 48, 48);

        JLabel icon = new JLabel(Theme.icon(iconName, 28));
        icon.setBounds(10, 10, 28, 28);
        iconBox.add(icon);
        add(iconBox);

        // ----- 이름 + 설명 -----
        JLabel titleLabel = Theme.label(title, Theme.bold(18), Theme.TEXT);
        titleLabel.setBounds(84, 20, 300, 26);
        add(titleLabel);

        JLabel descLabel = Theme.label(desc, Theme.regular(13), Theme.SUB_TEXT);
        descLabel.setBounds(84, 48, 300, 18);
        add(descLabel);

        // ----- 오른쪽 동그라미 -----
        radio = new JLabel();
        radio.setBounds(448, 32, 24, 24);
        add(radio);

        setSelected(false);
    }

    // 선택 표시 바꾸기
    public void setSelected(boolean selected) {
        if (selected) {
            setBackground(Theme.BADGE_BG);
            setBorderColor(Theme.BLUE);
            setBorderWidth(2);
            iconBox.setBackground(Theme.WHITE);
            radio.setIcon(Theme.icon("radio_on", 24));
        } else {
            setBackground(Theme.WHITE);
            setBorderColor(Theme.LINE);
            setBorderWidth(1);
            iconBox.setBackground(Theme.LIGHT_BG);
            radio.setIcon(Theme.icon("radio_off", 24));
        }
    }
}