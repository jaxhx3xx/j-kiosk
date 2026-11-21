package org.jkiosk.ui;

import org.jkiosk.model.Cart;
import org.jkiosk.model.CartItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// 08 장바구니 화면
public class CartScreen extends JPanel {

    private static final int ROW_HEIGHT = 113;  // 담은 메뉴 한 줄의 높이

    private final KioskFrame frame;
    private final Header header;

    private final JLabel countLabel;       // 제목 옆 파란 숫자
    private final JPanel listPanel;        // 담은 메뉴 목록이 들어가는 곳
    private final JLabel totalCountLabel;  // 총 수량
    private final JLabel totalPriceLabel;  // 총 결제 금액
    private final JButton payButton;       // 결제하기 버튼

    public CartScreen(KioskFrame frame) {
        this.frame = frame;

        setLayout(null);
        setBackground(Theme.WHITE);

        // ----- 헤더 -----
        header = new Header(frame);
        add(header);

        // ----- 제목 "장바구니 4" -----
        JLabel title = Theme.label("장바구니", Theme.bold(28), Theme.TEXT);
        int titleWidth = title.getPreferredSize().width;
        title.setBounds(24, 100, titleWidth, 40);
        add(title);

        countLabel = Theme.label("", Theme.bold(28), Theme.BLUE);
        countLabel.setBounds(24 + titleWidth + 8, 100, 80, 40);
        add(countLabel);

        // ----- 오른쪽 "전체 삭제" -----
        JLabel clearLabel = Theme.label("전체 삭제", Theme.regular(15), Theme.SUB_TEXT);
        clearLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        clearLabel.setBounds(400, 108, 116, 24);
        clearLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clearAll();
            }
        });
        add(clearLabel);

        // ----- 담은 메뉴 목록 (많아지면 스크롤) -----
        listPanel = new JPanel(null);
        listPanel.setBackground(Theme.WHITE);

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBounds(24, 164, 492, 560);
        scroll.setBorder(BorderFactory.createEmptyBorder());  // 스크롤 테두리 없애기
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);  // 가로 스크롤 없음
        scroll.getVerticalScrollBar().setUnitIncrement(16);   // 마우스 휠 스크롤 속도
        add(scroll);

        // ----- 합계 상자 -----
        RoundPanel totalBox = new RoundPanel(Theme.LIGHT_BG, 16);
        totalBox.setBounds(24, 741, 492, 95);

        JLabel countText = Theme.label("총 수량", Theme.regular(15), Theme.SUB_TEXT);
        countText.setBounds(20, 16, 200, 22);
        totalBox.add(countText);

        totalCountLabel = Theme.label("", Theme.bold(15), Theme.TEXT);
        totalCountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        totalCountLabel.setBounds(272, 16, 200, 22);
        totalBox.add(totalCountLabel);

        JLabel priceText = Theme.label("총 결제 금액", Theme.bold(17), Theme.TEXT);
        priceText.setBounds(20, 46, 200, 34);
        totalBox.add(priceText);

        totalPriceLabel = Theme.label("", Theme.bold(26), Theme.BLUE);
        totalPriceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        totalPriceLabel.setBounds(172, 44, 300, 36);
        totalBox.add(totalPriceLabel);

        add(totalBox);

        // ----- 아래 버튼 2개 -----
        JPanel bottomBar = new JPanel(null);
        bottomBar.setBackground(Theme.WHITE);
        bottomBar.setBounds(0, 860, 540, 100);
        bottomBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.LINE));  // 위쪽 선

        JButton moreButton = Theme.secondaryButton("메뉴 더 담기");
        moreButton.setBounds(20, 20, 139, 60);
        moreButton.addActionListener(e -> frame.backToMenu());
        bottomBar.add(moreButton);

        payButton = Theme.primaryButton("");
        payButton.setBounds(169, 20, 351, 60);
        payButton.addActionListener(e -> frame.showPayment());
        bottomBar.add(payButton);

        add(bottomBar);
    }

    // 이 화면에 들어올 때마다 호출
    public void onShow() {
        header.setOrderType(frame.getOrderType());
        refresh();
    }

    // 장바구니 내용에 맞게 화면 전체를 다시 그리기
    private void refresh() {
        Cart cart = frame.getCart();

        // 다 지워서 장바구니가 비었으면 메뉴 화면으로 돌아가기
        if (cart.getItems().isEmpty()) {
            frame.backToMenu();
            return;
        }

        // 1. 제목 옆 개수
        countLabel.setText(String.valueOf(cart.getTotalCount()));

        // 2. 메뉴 목록 다시 만들기
        listPanel.removeAll();

        int y = 0;
        for (CartItem item : cart.getItems()) {
            listPanel.add(makeRow(item, y));
            y = y + ROW_HEIGHT;
        }

        // 목록 전체 높이를 알려줘야 스크롤이 생김
        listPanel.setPreferredSize(new Dimension(492, y));
        listPanel.revalidate();
        listPanel.repaint();

        // 3. 합계와 결제 버튼
        int total = cart.getTotalPrice();
        totalCountLabel.setText(cart.getTotalCount() + "개");
        totalPriceLabel.setText(Theme.won(total));
        payButton.setText(Theme.won(total) + " 결제하기");
    }

    // 담은 메뉴 한 줄 만들기
    private JPanel makeRow(CartItem item, int y) {
        JPanel row = new JPanel(null);
        row.setBackground(Theme.WHITE);
        row.setBounds(0, y, 492, ROW_HEIGHT);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.LINE));  // 아래쪽 선

        // ----- 이미지 -----
        RoundPanel imageBox = new RoundPanel(Theme.LIGHT_BG, 12);
        imageBox.setBounds(0, 18, 76, 76);

        JLabel image = new JLabel(Theme.menuImage(item.getMenu().getMenuId(), 62));
        image.setBounds(7, 7, 62, 62);
        imageBox.add(image);
        row.add(imageBox);

        // ----- 이름 -----
        JLabel name = Theme.label(item.getMenu().getName(), Theme.regular(17), Theme.TEXT);
        name.setBounds(90, 24, 280, 24);
        row.add(name);

        // ----- 옵션 ("ICE · 레귤러 · 샷 1 추가") -----
        JLabel option = Theme.label(item.getOptionText(), Theme.regular(13), Theme.SUB_TEXT);
        option.setBounds(90, 50, 280, 18);
        row.add(option);

        // ----- 삭제 -----
        JLabel delete = Theme.label("삭제", Theme.regular(13), Theme.TAB_GRAY);
        delete.setBounds(90, 70, 40, 20);
        delete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        delete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.getCart().remove(item);
                refresh();
            }
        });
        row.add(delete);

        // ----- 가격 (1개 가격 x 수량, 오른쪽 정렬) -----
        JLabel price = Theme.label(Theme.won(item.getTotalPrice()), Theme.bold(17), Theme.TEXT);
        price.setHorizontalAlignment(SwingConstants.RIGHT);
        price.setBounds(290, 24, 202, 24);
        row.add(price);

        // ----- 수량 조절 (1개 ~ 9개) -----
        QuantityControl quantity = new QuantityControl(item.getQuantity(), 1, 9);
        quantity.setBounds(390, 56, 102, 32);
        quantity.setOnChange(() -> {
            item.setQuantity(quantity.getValue());
            refresh();
        });
        row.add(quantity);

        return row;
    }

    // 전체 삭제 (확인창에서 "예"를 누르면 비우기)
    private void clearAll() {
        int answer = JOptionPane.showConfirmDialog(this,
                "장바구니를 비울까요?\n담은 메뉴가 모두 삭제돼요.",
                "전체 삭제",
                JOptionPane.YES_NO_OPTION);

        if (answer == JOptionPane.YES_OPTION) {
            frame.getCart().clear();
            frame.backToMenu();
        }
    }
}