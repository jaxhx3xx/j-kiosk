package org.jkiosk.ui;

import org.jkiosk.dao.CategoryDao;
import org.jkiosk.dao.MenuDao;
import org.jkiosk.model.Cart;
import org.jkiosk.model.CartItem;
import org.jkiosk.model.Category;
import org.jkiosk.model.Menu;

import javax.swing.*;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// 03~05 메뉴 화면 (카테고리 탭 + 메뉴 카드 + 장바구니 바 + 옵션 창)
public class MenuScreen extends JPanel {

    private final KioskFrame frame;
    private final Header header;
    private final CartBar cartBar;

    private final JLabel categoryTitle;  // "커피"
    private final JLabel categoryCount;  // "4개"
    private final JPanel menuArea;       // 메뉴 카드들이 들어가는 곳

    private final CategoryDao categoryDao = new CategoryDao();
    private final MenuDao menuDao = new MenuDao();

    private List<Category> categories = new ArrayList<>();
    private final List<JLabel> tabLabels = new ArrayList<>();  // 탭 글씨들
    private final List<JPanel> tabLines = new ArrayList<>();   // 탭 아래 파란 선들
    private int currentIndex = 0;  // 지금 보고 있는 탭 번호

    private OptionSheet optionSheet;  // 열려 있는 옵션 창 (없으면 null)
    private RoundPanel toast;         // "장바구니에 담았어요" 알림
    private Timer toastTimer;         // 알림을 1.5초 뒤에 없애는 타이머

    public MenuScreen(KioskFrame frame) {
        this.frame = frame;

        setLayout(null);
        setBackground(Theme.WHITE);

        // ----- 헤더 -----
        header = new Header(frame);
        add(header);

        // ----- 카테고리 탭 (DB에서 읽어서 만들기) -----
        loadCategories();

        int x = 24;  // 첫 번째 탭 위치
        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);

            // 탭 글씨
            JLabel tab = Theme.label(category.getName(), Theme.bold(19), Theme.TAB_GRAY);
            int width = tab.getPreferredSize().width;
            tab.setBounds(x, 90, width, 26);
            tab.setCursor(new Cursor(Cursor.HAND_CURSOR));

            final int index = i;  // 아래 클릭 코드 안에서 쓰려면 final이어야 함
            tab.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showCategory(index);
                }
            });
            add(tab);
            tabLabels.add(tab);

            // 선택된 탭 아래 파란 선 (처음엔 숨김)
            JPanel underline = new JPanel();
            underline.setBackground(Theme.BLUE);
            underline.setBounds(x, 125, width, 3);
            underline.setVisible(false);
            add(underline);
            tabLines.add(underline);

            x = x + width + 28;  // 다음 탭은 28px 띄워서
        }

        // 탭 아래 회색 선 (파란 선이 위에 보이도록 탭보다 나중에 add)
        JPanel line = new JPanel();
        line.setBackground(Theme.LINE);
        line.setBounds(0, 127, 540, 1);
        add(line);

        // ----- 카테고리 제목 + 개수 -----
        categoryTitle = Theme.label("", Theme.bold(22), Theme.TEXT);
        add(categoryTitle);

        categoryCount = Theme.label("", Theme.regular(15), Theme.SUB_TEXT);
        add(categoryCount);

        // ----- 메뉴 카드 영역 -----
        menuArea = new JPanel(null);
        menuArea.setOpaque(false);
        menuArea.setBounds(0, 200, 540, 540);
        add(menuArea);

        // ----- 장바구니 바 -----
        cartBar = new CartBar(frame);
        add(cartBar);
    }

    // DB에서 카테고리 목록 읽기
    private void loadCategories() {
        try {
            categories = categoryDao.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "카테고리를 불러오지 못했어요.\n" + e.getMessage());
        }
    }

    // 이 화면에 들어올 때마다 호출
    public void onShow() {
        closeOptionSheet();
        header.setOrderType(frame.getOrderType());
        updateCartBar();
        showCategory(0);
    }

    // index번째 카테고리를 보여주기 (0 = 커피, 1 = 음료, 2 = 디저트)
    private void showCategory(int index) {
        if (categories.isEmpty()) {
            return;  // DB 연결 실패 등으로 카테고리가 없으면 아무것도 안 함
        }
        currentIndex = index;

        // 1. 탭 색 바꾸기 (선택된 탭만 진한 글씨 + 파란 선)
        for (int i = 0; i < tabLabels.size(); i++) {
            boolean selected = (i == index);

            if (selected) {
                tabLabels.get(i).setForeground(Theme.TEXT);
            } else {
                tabLabels.get(i).setForeground(Theme.TAB_GRAY);
            }
            tabLines.get(i).setVisible(selected);
        }

        // 2. DB에서 이 카테고리의 메뉴 읽기
        Category category = categories.get(index);
        List<Menu> menus;
        try {
            menus = menuDao.findByCategory(category.getCategoryId());
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "메뉴를 불러오지 못했어요.\n" + e.getMessage());
            menus = new ArrayList<>();
        }

        // 3. 제목과 개수 ("커피 4개")
        categoryTitle.setText(category.getName());
        int titleWidth = categoryTitle.getPreferredSize().width;
        categoryTitle.setBounds(24, 150, titleWidth, 32);

        categoryCount.setText(menus.size() + "개");
        categoryCount.setBounds(24 + titleWidth + 8, 156, 60, 22);

        // 4. 메뉴 카드 다시 그리기 (2줄 x 2칸)
        menuArea.removeAll();
        Cart cart = frame.getCart();

        for (int i = 0; i < menus.size(); i++) {
            Menu menu = menus.get(i);
            int col = i % 2;  // 0이면 왼쪽, 1이면 오른쪽
            int row = i / 2;  // 0이면 윗줄, 1이면 아랫줄

            boolean inCart = cart.contains(menu.getMenuId());
            MenuCard card = new MenuCard(menu, inCart, this);
            card.setBounds(20 + col * 258, row * 278, 242, 262);  // 카드 242 + 간격 16
            menuArea.add(card);
        }

        menuArea.revalidate();
        menuArea.repaint();
    }

    // ===== 옵션 창 =====

    // 메뉴 카드를 누르면 옵션 창 열기
    public void openOptionSheet(Menu menu) {
        closeOptionSheet();
        optionSheet = new OptionSheet(this, menu);
        add(optionSheet, 0);  // 0번 = 맨 위에 그려짐
        revalidate();
        repaint();
    }

    // 옵션 창 닫기
    public void closeOptionSheet() {
        if (optionSheet != null) {
            remove(optionSheet);
            optionSheet = null;
            repaint();
        }
    }

    // 옵션 창에서 "담기"를 누르면
    public void addToCart(CartItem item) {
        frame.getCart().add(item);
        closeOptionSheet();
        updateCartBar();
        showCategory(currentIndex);  // 담은 메뉴 카드에 파란 테두리 표시
        showToast();
    }

    // 장바구니 바를 지금 장바구니 상태에 맞게 바꾸기
    public void updateCartBar() {
        Cart cart = frame.getCart();
        cartBar.update(cart.getTotalCount(), cart.getTotalPrice());
    }

    // ===== "장바구니에 담았어요" 알림 (1.5초 뒤 사라짐) =====
    private void showToast() {
        // 이전 알림이 남아 있으면 먼저 지우기
        if (toast != null) {
            remove(toast);
            toastTimer.stop();
        }

        toast = new RoundPanel(Theme.TEXT, 12);  // 진한 남색 배경
        toast.setBounds(174, 780, 192, 44);

        JLabel check = new JLabel(Theme.icon("check", 20));
        check.setBounds(16, 12, 20, 20);
        toast.add(check);

        JLabel text = Theme.label("장바구니에 담았어요", Theme.regular(15), Theme.WHITE);
        text.setBounds(44, 0, 140, 44);
        toast.add(text);

        add(toast, 0);
        repaint();

        // 1.5초(1500ms) 뒤에 알림 지우기
        toastTimer = new Timer(1500, e -> {
            remove(toast);
            toast = null;
            repaint();
        });
        toastTimer.setRepeats(false);  // 한 번만 실행
        toastTimer.start();
    }
}