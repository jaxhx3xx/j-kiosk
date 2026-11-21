package org.jkiosk.ui;

import org.jkiosk.model.Cart;

import javax.swing.*;

// 14 주문 완료 화면 (대기번호 + 주문 정보, 10초 뒤 처음 화면으로)
public class DoneScreen extends JPanel {

    private final KioskFrame frame;
    private final Header header;

    private final JLabel subText;        // "영수증을 꼭 챙겨 주세요"
    private final JLabel numberLabel;    // "A-027"
    private final JLabel typeValue;      // 주문 유형
    private final JLabel menuValue;      // 주문 메뉴
    private final JLabel priceValue;     // 결제 금액
    private final JLabel countdownLabel; // "10초 뒤 처음 화면으로 돌아가요"

    private Timer countdownTimer;
    private int secondsLeft;

    public DoneScreen(KioskFrame frame) {
        this.frame = frame;

        setLayout(null);
        setBackground(Theme.WHITE);

        // ----- 헤더 -----
        header = new Header(frame);
        add(header);

        // ----- 체크 아이콘 -----
        JLabel doneIcon = new JLabel(Theme.icon("done", 72));
        doneIcon.setBounds(234, 120, 72, 72);
        add(doneIcon);

        // ----- 제목 + 안내 (가운데 정렬) -----
        JLabel title = Theme.label("주문이 완료되었어요", Theme.bold(28), Theme.TEXT);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBounds(0, 210, 540, 40);
        add(title);

        subText = Theme.label("", Theme.regular(15), Theme.SUB_TEXT);
        subText.setHorizontalAlignment(SwingConstants.CENTER);
        subText.setBounds(0, 256, 540, 22);
        add(subText);

        // ----- 대기번호 상자 -----
        RoundPanel box = new RoundPanel(Theme.LIGHT_BG, 20);
        box.setBounds(24, 312, 492, 322);

        JLabel numberTitle = Theme.label("대기번호", Theme.regular(16), Theme.SUB_TEXT);
        numberTitle.setHorizontalAlignment(SwingConstants.CENTER);
        numberTitle.setBounds(0, 28, 492, 22);
        box.add(numberTitle);

        numberLabel = Theme.label("", Theme.bold(88), Theme.BLUE);
        numberLabel.setHorizontalAlignment(SwingConstants.CENTER);
        numberLabel.setBounds(0, 50, 492, 110);
        box.add(numberLabel);

        JPanel line = new JPanel();
        line.setBackground(Theme.LINE);
        line.setBounds(28, 161, 436, 1);
        box.add(line);

        // 주문 정보 4줄
        typeValue = addInfoRow(box, "주문 유형", 186);
        menuValue = addInfoRow(box, "주문 메뉴", 216);
        priceValue = addInfoRow(box, "결제 금액", 246);
        JLabel waitValue = addInfoRow(box, "예상 대기 시간", 276);
        waitValue.setText("약 5분");

        add(box);

        // ----- 남은 시간 안내 -----
        countdownLabel = Theme.label("", Theme.regular(14), Theme.SUB_TEXT);
        countdownLabel.setHorizontalAlignment(SwingConstants.CENTER);
        countdownLabel.setBounds(0, 822, 540, 20);
        add(countdownLabel);

        // ----- 아래 "처음으로" 버튼 -----
        JPanel bottomBar = new JPanel(null);
        bottomBar.setBackground(Theme.WHITE);
        bottomBar.setBounds(0, 860, 540, 100);
        bottomBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.LINE));  // 위쪽 선

        JButton homeButton = Theme.primaryButton("처음으로");
        homeButton.setBounds(20, 20, 500, 60);
        homeButton.addActionListener(e -> frame.goHome());
        bottomBar.add(homeButton);

        add(bottomBar);
    }

    // 주문 정보 한 줄 만들기 (왼쪽 제목 + 오른쪽 값). 값 라벨을 돌려줌
    private JLabel addInfoRow(RoundPanel box, String title, int y) {
        JLabel titleLabel = Theme.label(title, Theme.regular(15), Theme.SUB_TEXT);
        titleLabel.setBounds(28, y, 200, 22);
        box.add(titleLabel);

        JLabel valueLabel = Theme.label("", Theme.bold(15), Theme.TEXT);
        valueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        valueLabel.setBounds(236, y, 228, 22);
        box.add(valueLabel);

        return valueLabel;
    }

    // 이 화면에 들어올 때 호출 (주문 저장이 끝난 뒤)
    public void onShow(String waitingNumber, String payMethod) {
        header.setOrderType(frame.getOrderType());
        numberLabel.setText(waitingNumber);

        // 결제 수단에 따라 안내 문구 다르게
        if ("CARD".equals(payMethod)) {
            subText.setText("영수증을 꼭 챙겨 주세요");
        } else {
            subText.setText("카운터에서 현금으로 결제해 주세요");
        }

        // 주문 유형
        if ("STORE".equals(frame.getOrderType())) {
            typeValue.setText("매장 식사");
        } else {
            typeValue.setText("포장");
        }

        // 주문 메뉴 ("3종류 · 4개"), 결제 금액
        Cart cart = frame.getCart();
        menuValue.setText(cart.getItems().size() + "종류 · " + cart.getTotalCount() + "개");
        priceValue.setText(Theme.won(cart.getTotalPrice()));

        startCountdown();
    }

    // 10초 세기 시작 (1초마다 글씨 바꾸고, 0이 되면 처음 화면으로)
    private void startCountdown() {
        stopTimer();
        secondsLeft = 10;
        countdownLabel.setText(secondsLeft + "초 뒤 처음 화면으로 돌아가요");

        countdownTimer = new Timer(1000, e -> {
            secondsLeft = secondsLeft - 1;

            if (secondsLeft <= 0) {
                frame.goHome();
            } else {
                countdownLabel.setText(secondsLeft + "초 뒤 처음 화면으로 돌아가요");
            }
        });
        countdownTimer.start();
    }

    // 10초 세기 멈추기 (처음 화면으로 갈 때 KioskFrame에서 호출)
    public void stopTimer() {
        if (countdownTimer != null) {
            countdownTimer.stop();
        }
    }
}