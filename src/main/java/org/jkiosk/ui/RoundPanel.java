package org.jkiosk.ui;

import javax.swing.*;
import java.awt.*;

// 모서리가 둥근 패널. 피그마의 radius 값을 그대로 넣으면 됨
public class RoundPanel extends JPanel {

    private final int radius;

    public RoundPanel(Color background, int radius) {
        this.radius = radius;
        setBackground(background);
        setOpaque(false);  // 기본 네모 배경은 칠하지 않고, 아래에서 둥근 모양을 직접 칠함
        setLayout(null);   // 안에 넣는 것도 피그마 좌표 그대로 배치
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // 모서리를 부드럽게 그리기
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius * 2, radius * 2);

        g2.dispose();
        super.paintComponent(g);
    }
}