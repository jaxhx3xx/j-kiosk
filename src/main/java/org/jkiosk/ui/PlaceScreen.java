package org.jkiosk.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// 02 매장·포장 선택 화면
public class PlaceScreen extends JPanel {

    private final KioskFrame frame;

    public PlaceScreen(KioskFrame frame) {
        this.frame = frame;

        setLayout(null);
        setBackground(Theme.WHITE);

        // ----- 헤더 -----
        add(new Header(frame));

        // ----- 제목 -----
        JLabel title = Theme.label("어디에서 드시나요?", Theme.bold(28), Theme.TEXT);
        title.setBounds(24, 112, 492, 40);
        add(title);

        JLabel subTitle = Theme.label("식사 장소를 선택해 주세요", Theme.regular(16), Theme.SUB_TEXT);
        subTitle.setBounds(24, 156, 492, 22);
        add(subTitle);

        // ----- 카드 2개 -----
        // "STORE", "TAKEOUT"은 DB orders.order_type 값과 똑같이 맞춤
        add(makeCard(24, "cup", "매장 식사", "다회용 컵에 담아 드려요", "STORE"));
        add(makeCard(278, "bag", "포장", "일회용 컵에 담아 드려요", "TAKEOUT"));
    }

    // 회색 둥근 카드 하나 만들기
    private RoundPanel makeCard(int x, String iconName, String title, String desc, String orderType) {
        RoundPanel card = new RoundPanel(Theme.LIGHT_BG, 20);
        card.setBounds(x, 228, 238, 420);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 흰 동그라미 + 아이콘
        RoundPanel circle = new RoundPanel(Theme.WHITE, 52);
        circle.setBounds(28, 36, 104, 104);

        JLabel icon = new JLabel(Theme.icon(iconName, 72));
        icon.setBounds(16, 16, 72, 72);
        circle.add(icon);
        card.add(circle);

        // 제목
        JLabel titleLabel = Theme.label(title, Theme.bold(28), Theme.TEXT);
        titleLabel.setBounds(28, 296, 160, 40);
        card.add(titleLabel);

        // 설명
        JLabel descLabel = Theme.label(desc, Theme.regular(14), Theme.SUB_TEXT);
        descLabel.setBounds(28, 346, 200, 20);
        card.add(descLabel);

        // 오른쪽 > 화살표
        JLabel arrow = new JLabel(Theme.icon("arrow", 28));
        arrow.setBounds(190, 304, 28, 28);
        card.add(arrow);

        // 카드를 누르면: 매장/포장을 기억하고 → 메뉴 화면으로
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.setOrderType(orderType);
                frame.showScreen(KioskFrame.MENU);
            }
        });

        return card;
    }
}