package org.jkiosk.ui;

import org.jkiosk.model.Cart;

import javax.swing.*;
import java.awt.*;

// 키오스크 창. 화면들을 카드처럼 겹쳐두고 하나씩 보여줌
public class KioskFrame extends JFrame {

    // 화면 이름 (화면을 바꿀 때 이 이름으로 부름)
    public static final String START = "start";      // 01 시작
    public static final String PLACE = "place";      // 02 매장·포장 선택
    public static final String MENU = "menu";        // 03~05 메뉴
    public static final String CART = "cart";        // 08 장바구니
    public static final String PAYMENT = "payment";  // 11 결제 수단 선택
    public static final String PAYING = "paying";    // 13 결제 중
    public static final String DONE = "done";        // 14 주문 완료

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel screens = new JPanel(cardLayout);

    private final MenuScreen menuScreen;
    private final CartScreen cartScreen;
    private final PaymentScreen paymentScreen;
    private final PayingScreen payingScreen;
    private final DoneScreen doneScreen;

    // ===== 주문 정보 =====
    private String orderType;              // 매장 식사("STORE") / 포장("TAKEOUT")
    private final Cart cart = new Cart();  // 장바구니

    public KioskFrame() {
        setTitle("J의 키오스크");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);  // 키오스크라 크기 고정

        // 피그마 화면 크기 540 x 960
        screens.setPreferredSize(new Dimension(540, 960));

        // 화면 만들기
        menuScreen = new MenuScreen(this);
        cartScreen = new CartScreen(this);
        paymentScreen = new PaymentScreen(this);
        payingScreen = new PayingScreen(this);
        doneScreen = new DoneScreen(this);

        // 화면 등록
        screens.add(new StartScreen(this), START);
        screens.add(new PlaceScreen(this), PLACE);
        screens.add(menuScreen, MENU);
        screens.add(cartScreen, CART);
        screens.add(paymentScreen, PAYMENT);
        screens.add(payingScreen, PAYING);
        screens.add(doneScreen, DONE);

        setContentPane(screens);
        pack();                       // 창 크기를 화면 크기에 맞춤
        setLocationRelativeTo(null);  // 모니터 가운데에 띄움
    }

    // 다른 화면으로 바꾸기
    public void showScreen(String name) {
        cardLayout.show(screens, name);
    }

    // 매장/포장을 고르고 메뉴 화면으로 가기 (커피 탭부터)
    public void startMenu(String orderType) {
        this.orderType = orderType;
        menuScreen.onShow();
        showScreen(MENU);
    }

    // 메뉴 화면으로 돌아가기 (보던 탭 그대로)
    public void backToMenu() {
        menuScreen.refresh();
        showScreen(MENU);
    }

    // 장바구니 화면으로 가기
    public void showCart() {
        if (cart.getItems().isEmpty()) {
            return;  // 비어 있으면 가지 않음
        }
        cartScreen.onShow();
        showScreen(CART);
    }

    // 결제 수단 선택 화면으로 가기
    public void showPayment() {
        paymentScreen.onShow();
        showScreen(PAYMENT);
    }

    // 결제 시작 → 결제 중 화면
    public void startPaying(String payMethod) {
        showScreen(PAYING);
        payingScreen.start(payMethod);
    }

    // 주문 저장이 끝나면 → 완료 화면
    public void showDone(String waitingNumber, String payMethod) {
        doneScreen.onShow(waitingNumber, payMethod);
        showScreen(DONE);
    }

    // 처음 화면으로 (주문 정보와 장바구니 모두 초기화)
    public void goHome() {
        doneScreen.stopTimer();  // 완료 화면의 10초 세기가 남아 있으면 멈춤
        orderType = null;
        cart.clear();
        showScreen(START);
    }

    public String getOrderType() {
        return orderType;
    }

    public Cart getCart() {
        return cart;
    }
}