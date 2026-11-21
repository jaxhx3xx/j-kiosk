package org.jkiosk.ui;

import javax.swing.*;
import java.awt.*;

// 옵션 버튼. 선택되면 연한 파랑 + 파란 테두리, 아니면 흰색 + 회색 테두리
public class OptionButtom extends RoundPanel {

    private final JLabel titleLabel;

    // extraPrice: "+500원"처럼 아래 작은 글씨 (없으면 null)
    public OptionButtom(String title, String extraPrice, int width, int height) {
        super(Theme.WHITE, 12);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        titleLabel = Theme.label(title, Theme.regular(16), Theme.TEXT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        if (extraPrice == null) {
            // 글씨 한 줄 → 버튼 가운데
            titleLabel.setBounds(0, 0, width, height);
        } else {
            // 글씨 두 줄 (이름 + 추가 금액)
            titleLabel.setBounds(0, 13, width, 22);

            JLabel priceLabel = Theme.label(extraPrice, Theme.regular(12), Theme.SUB_TEXT);
            priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
            priceLabel.setBounds(0, 36, width, 16);
            add(priceLabel);
        }
        add(titleLabel);

        setSelected(false);
    }

    // 선택 표시 바꾸기
    public void setSelected(boolean selected) {
        if (selected) {
            setBackground(Theme.BADGE_BG);
            setBorderColor(Theme.BLUE);
            setBorderWidth(2);
            titleLabel.setForeground(Theme.BLUE);
            titleLabel.setFont(Theme.bold(16));
        } else {
            setBackground(Theme.WHITE);
            setBorderColor(Theme.LINE);
            setBorderWidth(1);
            titleLabel.setForeground(Theme.TEXT);
            titleLabel.setFont(Theme.regular(16));
        }
    }
}