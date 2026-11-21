package org.jkiosk.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// 메뉴 화면 맨 아래 장바구니 바
// 담은 게 없으면 회색 "담은 메뉴가 없어요", 있으면 파란 "장바구니 보기 + 금액"
public class CartBar extends JPanel {

    private final RoundPanel button;
    private final RoundPanel countCircle;
    private final JLabel countLabel;
    private final JLabel textLabel;
    private final JLabel totalLabel;

    private boolean hasItems = false;  // 담은 메뉴가 있는지

    public CartBar(KioskFrame frame) {
        setLayout(null);
        setBackground(Theme.WHITE);
        setBounds(0, 860, 540, 100);

        // 위쪽에만 1px 회색 선
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.LINE));

        // ----- 큰 버튼 -----
        button = new RoundPanel(Theme.DISABLED_BG, 14);
        button.setBounds(20, 18, 500, 64);
        add(button);

        // ----- 왼쪽 흰 동그라미 안의 개수 -----
        countCircle = new RoundPanel(Theme.WHITE, 13);
        countCircle.setBounds(22, 19, 26, 26);

        countLabel = Theme.label("0", Theme.bold(14), Theme.BLUE);
        countLabel.setHorizontalAlignment(SwingConstants.CENTER);
        countLabel.setBounds(0, 0, 26, 26);
        countCircle.add(countLabel);
        button.add(countCircle);

        // ----- 가운데 글씨 -----
        textLabel = Theme.label("", Theme.bold(18), Theme.WHITE);
        button.add(textLabel);

        // ----- 오른쪽 금액 (오른쪽 정렬) -----
        totalLabel = Theme.label("", Theme.bold(18), Theme.WHITE);
        totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        totalLabel.setBounds(278, 19, 200, 26);
        button.add(totalLabel);

        // ----- 버튼을 눌렀을 때 (담은 게 있을 때만) -----
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (hasItems) {
                    frame.showCart();
                }
            }
        });

        // 처음에는 빈 상태
        update(0, 0);
    }

    // 개수와 총 금액을 받아서 모양 바꾸기
    public void update(int count, int total) {
        hasItems = count > 0;

        if (hasItems) {
            // 담은 메뉴가 있을 때: 파란 버튼
            button.setBackground(Theme.BLUE);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));

            countCircle.setVisible(true);
            countLabel.setText(String.valueOf(count));

            textLabel.setText("장바구니 보기");
            textLabel.setForeground(Theme.WHITE);
            textLabel.setBounds(58, 19, 200, 26);

            totalLabel.setText(Theme.won(total));
        } else {
            // 담은 메뉴가 없을 때: 회색 버튼
            button.setBackground(Theme.DISABLED_BG);
            button.setCursor(Cursor.getDefaultCursor());

            countCircle.setVisible(false);

            textLabel.setText("담은 메뉴가 없어요");
            textLabel.setForeground(Theme.TAB_GRAY);
            textLabel.setBounds(22, 19, 300, 26);

            totalLabel.setText("");
        }
        repaint();
    }
}