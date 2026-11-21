package org.jkiosk.ui;

import org.jkiosk.dao.OrderDao;

import javax.swing.*;
import java.sql.SQLException;

// 13 결제 진행 중 화면. 2초 뒤 DB에 주문을 저장하고 완료 화면으로
public class PayingScreen extends JPanel {

    private final KioskFrame frame;
    private final Header header;
    private final LoadingCircle loading;
    private final JLabel subText;

    private final OrderDao orderDao = new OrderDao();
    private String payMethod;

    public PayingScreen(KioskFrame frame) {
        this.frame = frame;

        setLayout(null);
        setBackground(Theme.WHITE);

        // ----- 헤더 (결제 중에는 "처음으로" 못 누르게) -----
        header = new Header(frame);
        header.setHomeEnabled(false);
        add(header);

        // ----- 로딩 원 -----
        loading = new LoadingCircle();
        loading.setBounds(200, 300, 140, 140);
        add(loading);

        // ----- 글씨 (가운데 정렬) -----
        JLabel title = Theme.label("결제 중이에요", Theme.bold(30), Theme.TEXT);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBounds(0, 470, 540, 44);
        add(title);

        subText = Theme.label("", Theme.regular(16), Theme.SUB_TEXT);
        subText.setHorizontalAlignment(SwingConstants.CENTER);
        subText.setBounds(0, 520, 540, 24);
        add(subText);
    }

    // 결제 시작 (결제 수단 화면에서 "결제하기"를 누르면 호출)
    public void start(String payMethod) {
        this.payMethod = payMethod;
        header.setOrderType(frame.getOrderType());

        if ("CARD".equals(payMethod)) {
            subText.setText("카드를 빼지 말고 잠시만 기다려 주세요");
        } else {
            subText.setText("주문을 접수하고 있어요");
        }

        loading.start();

        // 실제 카드 리더기가 없으니 2초(2000ms) 기다린 뒤 주문 저장
        Timer timer = new Timer(2000, e -> saveOrder());
        timer.setRepeats(false);  // 한 번만 실행
        timer.start();
    }

    // DB에 주문 저장 → 성공하면 완료 화면, 실패하면 결제 수단 화면으로
    private void saveOrder() {
        loading.stop();

        try {
            int orderId = orderDao.save(frame.getOrderType(), payMethod, frame.getCart());
            String waitingNumber = orderDao.getWaitingNumber(orderId);
            frame.showDone(waitingNumber, payMethod);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "주문 저장에 실패했어요.\n" + e.getMessage());
            frame.showPayment();
        }
    }
}