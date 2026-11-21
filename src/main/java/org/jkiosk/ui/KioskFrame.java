package org.jkiosk.ui;

import javax.swing.*;
import java.awt.*;

// 키오스크 창. 화면들을 카드처럼 겹쳐두고 하나씩 보여줌
public class KioskFrame extends JFrame {

    // 화면 이름 (화면을 바꿀 때 이 이름으로 부름)
    public static final String START = "start";  // 01 시작
    public static final String PLACE = "place";  // 02 매장·포장 선택
    public static final String MENU = "menu";    // 03~05 메뉴

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel screens = new JPanel(cardLayout);

    private final MenuScreen menuScreen;

    // 주문 정보: 매장 식사("STORE") / 포장("TAKEOUT")
    private String orderType;

    public KioskFrame() {
        setTitle("J의 키오스크");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);  // 키오스크라 크기 고정

        // 피그마 화면 크기 540 x 960
        screens.setPreferredSize(new Dimension(540, 960));

        // 화면 만들기
        menuScreen = new MenuScreen(this);

        // 화면 등록
        screens.add(new StartScreen(this), START);
        screens.add(new PlaceScreen(this), PLACE);
        screens.add(menuScreen, MENU);

        setContentPane(screens);
        pack();                       // 창 크기를 화면 크기에 맞춤
        setLocationRelativeTo(null);  // 모니터 가운데에 띄움
    }

    // 다른 화면으로 바꾸기
    public void showScreen(String name) {
        cardLayout.show(screens, name);
    }

    // 매장/포장을 고르고 메뉴 화면으로 가기
    public void startMenu(String orderType) {
        this.orderType = orderType;
        menuScreen.onShow();  // 배지 바꾸고 커피 탭부터 보여주기
        showScreen(MENU);
    }

    // 처음 화면으로 (주문 정보도 초기화)
    public void goHome() {
        orderType = null;
        showScreen(START);
    }

    public String getOrderType() {
        return orderType;
    }
}