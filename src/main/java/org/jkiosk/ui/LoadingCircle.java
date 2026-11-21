package org.jkiosk.ui;

import javax.swing.*;
import java.awt.*;

// 빙글빙글 도는 로딩 원 (13 결제 중 화면). 크기 140 x 140
public class LoadingCircle extends JPanel {

    private int angle = 0;      // 지금 파란 부분의 각도
    private final Timer timer;

    public LoadingCircle() {
        setOpaque(false);

        // 0.03초마다 각도를 8도씩 돌리고 다시 그림 → 돌아가는 것처럼 보임
        timer = new Timer(30, e -> {
            angle = (angle + 8) % 360;
            repaint();
        });
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        // 부드럽게 그리기 + 두께 12px, 끝은 둥글게
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(12, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        // 연한 파랑 바탕 원
        g2.setColor(Theme.BADGE_BG);
        g2.drawOval(6, 6, 128, 128);

        // 파란 부분 (원의 1/4 길이)
        g2.setColor(Theme.BLUE);
        g2.drawArc(6, 6, 128, 128, 90 - angle, -90);

        g2.dispose();
    }
}