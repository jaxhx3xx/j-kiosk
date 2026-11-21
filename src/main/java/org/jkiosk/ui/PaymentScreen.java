package org.jkiosk.ui;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// 11 결제 수단 선택 화면 (카드 / 현금)
public class PaymentScreen extends JPanel {

    private final KioskFrame frame;
    private final Header header;

    private final JLabel amountLabel;          // 결제 금액
    private final PayMethodCard cardOption;    // 카드 칸
    private final PayMethodCard cashOption;    // 현금 칸
    private final JButton payButton;

    // 고른 결제 수단 (DB orders.pay_method 값과 똑같이 "CARD" / "CASH")
    private String payMethod = "CARD";

    public PaymentScreen(KioskFrame frame) {
        this.frame = frame;

        setLayout(null);
        setBackground(Theme.WHITE);

        // ----- 헤더 -----
        header = new Header(frame);
        add(header);

        // ----- 제목 -----
        JLabel title = Theme.label("결제 수단을 선택해 주세요", Theme.bold(28), Theme.TEXT);
        title.setBounds(24, 108, 492, 40);
        add(title);

        // ----- "결제 금액 13,000원" -----
        JLabel amountText = Theme.label("결제 금액", Theme.regular(16), Theme.SUB_TEXT);
        int textWidth = amountText.getPreferredSize().width;
        amountText.setBounds(24, 154, textWidth, 24);
        add(amountText);

        amountLabel = Theme.label("", Theme.bold(16), Theme.BLUE);
        amountLabel.setBounds(24 + textWidth + 8, 154, 200, 24);
        add(amountLabel);

        // ----- 카드 -----
        cardOption = new PayMethodCard("card", "신용·체크카드", "IC칩 카드를 꽂아 주세요");
        cardOption.setBounds(24, 212, 492, 88);
        cardOption.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                select("CARD");
            }
        });
        add(cardOption);

        // ----- 현금 -----
        cashOption = new PayMethodCard("cash", "현금", "카운터에서 결제해 주세요");
        cashOption.setBounds(24, 312, 492, 88);
        cashOption.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                select("CASH");
            }
        });
        add(cashOption);

        // ----- 아래 버튼 2개 -----
        JPanel bottomBar = new JPanel(null);
        bottomBar.setBackground(Theme.WHITE);
        bottomBar.setBounds(0, 860, 540, 100);
        bottomBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.LINE));  // 위쪽 선

        JButton backButton = Theme.secondaryButton("이전");
        backButton.setBounds(20, 20, 82, 60);
        backButton.addActionListener(e -> frame.showCart());
        bottomBar.add(backButton);

        payButton = Theme.primaryButton("");
        payButton.setBounds(112, 20, 408, 60);
        payButton.addActionListener(e -> frame.startPaying(payMethod));
        bottomBar.add(payButton);

        add(bottomBar);
    }

    // 이 화면에 들어올 때마다 호출
    public void onShow() {
        header.setOrderType(frame.getOrderType());

        int total = frame.getCart().getTotalPrice();
        amountLabel.setText(Theme.won(total));
        payButton.setText(Theme.won(total) + " 결제하기");

        select("CARD");  // 처음엔 카드 선택
    }

    // 결제 수단 고르기
    private void select(String method) {
        payMethod = method;
        cardOption.setSelected("CARD".equals(method));
        cashOption.setSelected("CASH".equals(method));
    }
}