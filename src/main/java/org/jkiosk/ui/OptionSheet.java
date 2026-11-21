package org.jkiosk.ui;

import org.jkiosk.model.CartItem;
import org.jkiosk.model.Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// 06 옵션 선택 창
// 화면 전체를 어둡게 덮고, 아래에서 흰 창이 올라온 모양
public class OptionSheet extends JPanel {

    private static final int DESSERT_CATEGORY_ID = 3;  // 디저트는 옵션 없음 (category 테이블의 3번)

    private final MenuScreen menuScreen;
    private final Menu menu;
    private final boolean hasOptions;  // 옵션이 있는 메뉴인지 (커피, 음료)

    // 지금 고른 옵션 (처음 값: ICE, 레귤러, 시럽 없음)
    // 값은 DB order_item 테이블의 ENUM 값과 똑같이 맞춤
    private String temp = "ICE";
    private String size = "R";
    private String syrup = null;

    // 시럽 버튼 순서대로의 DB 값 (없음 = null)
    private final String[] syrupValues = {null, "VANILLA", "HAZELNUT", "CARAMEL"};

    // 선택 표시를 바꾸려고 버튼들을 저장해둠
    private OptionButtom hotButton;
    private OptionButtom iceButton;
    private OptionButtom regularButton;
    private OptionButtom largeButton;
    private final List<OptionButtom> syrupButtons = new ArrayList<>();

    private QuantityControl shotControl;
    private QuantityControl quantityControl;
    private JButton addButton;

    public OptionSheet(MenuScreen menuScreen, Menu menu) {
        this.menuScreen = menuScreen;
        this.menu = menu;
        this.hasOptions = (menu.getCategoryId() != DESSERT_CATEGORY_ID);

        setLayout(null);
        setOpaque(false);
        setBounds(0, 0, 540, 960);  // 화면 전체를 덮음

        // 어두운 곳을 누르면 닫기
        // (이 클릭 코드가 있어서 뒤에 있는 메뉴 카드는 눌리지 않음)
        addMouseListener(onClick(() -> menuScreen.closeOptionSheet()));

        add(makeSheet());
        updatePrice();
    }

    // 반투명 검정 배경 칠하기
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

    // 아래쪽 흰 창 만들기
    private RoundPanel makeSheet() {
        RoundPanel sheet = new RoundPanel(Theme.WHITE, 20);

        // 흰 창의 빈 곳을 눌렀을 때 창이 닫히지 않게 클릭을 여기서 막음
        sheet.addMouseListener(new MouseAdapter() {});

        // ----- 메뉴 정보 (이미지 + 이름 + 가격 + 닫기) -----
        RoundPanel imageBox = new RoundPanel(Theme.LIGHT_BG, 12);
        imageBox.setBounds(24, 28, 88, 88);

        JLabel image = new JLabel(Theme.menuImage(menu.getMenuId(), 72));
        image.setBounds(8, 8, 72, 72);
        imageBox.add(image);
        sheet.add(imageBox);

        JLabel name = Theme.label(menu.getName(), Theme.bold(24), Theme.TEXT);
        name.setBounds(128, 42, 320, 34);
        sheet.add(name);

        JLabel price = Theme.label(Theme.won(menu.getPrice()), Theme.regular(18), Theme.SUB_TEXT);
        price.setBounds(128, 76, 320, 24);
        sheet.add(price);

        JLabel close = new JLabel(Theme.icon("close", 28));
        close.setBounds(488, 58, 28, 28);
        close.setCursor(new Cursor(Cursor.HAND_CURSOR));
        close.addMouseListener(onClick(() -> menuScreen.closeOptionSheet()));
        sheet.add(close);

        // ----- 구분선 -----
        JPanel line = new JPanel();
        line.setBackground(Theme.LINE);
        line.setBounds(24, 140, 492, 1);
        sheet.add(line);

        int y = 165;  // 다음 내용을 놓을 세로 위치 (아래로 내려가면서 더함)

        if (hasOptions) {
            // ----- 온도 (필수) -----
            addGroupTitle(sheet, "온도", true, y);

            hotButton = new OptionButtom("HOT", null, 242, 51);
            hotButton.setBounds(24, y + 30, 242, 51);
            hotButton.addMouseListener(onClick(() -> {
                temp = "HOT";
                updateSelection();
            }));
            sheet.add(hotButton);

            iceButton = new OptionButtom("ICE", null, 242, 51);
            iceButton.setBounds(274, y + 30, 242, 51);
            iceButton.addMouseListener(onClick(() -> {
                temp = "ICE";
                updateSelection();
            }));
            sheet.add(iceButton);

            y = y + 105;

            // ----- 사이즈 (필수) -----
            addGroupTitle(sheet, "사이즈", true, y);

            regularButton = new OptionButtom("레귤러", null, 242, 65);
            regularButton.setBounds(24, y + 30, 242, 65);
            regularButton.addMouseListener(onClick(() -> {
                size = "R";
                updateSelection();
            }));
            sheet.add(regularButton);

            largeButton = new OptionButtom("라지", "+500원", 242, 65);
            largeButton.setBounds(274, y + 30, 242, 65);
            largeButton.addMouseListener(onClick(() -> {
                size = "L";
                updateSelection();
            }));
            sheet.add(largeButton);

            y = y + 119;

            // ----- 샷 추가 -----
            addGroupTitle(sheet, "샷 추가", false, y);

            RoundPanel shotBox = new RoundPanel(Theme.LIGHT_BG, 12);
            shotBox.setBounds(24, y + 30, 492, 52);

            JLabel shotText = Theme.label("에스프레소 샷  +500원", Theme.regular(15), Theme.BUTTON_TEXT);
            shotText.setBounds(16, 0, 250, 52);
            shotBox.add(shotText);

            shotControl = new QuantityControl(0, 0, 3);  // 0개부터 최대 3개
            shotControl.setBounds(378, 10, 102, 32);
            shotControl.setOnChange(() -> updatePrice());
            shotBox.add(shotControl);
            sheet.add(shotBox);

            y = y + 106;

            // ----- 시럽 -----
            addGroupTitle(sheet, "시럽", false, y);

            String[] syrupNames = {"없음", "바닐라", "헤이즐넛", "카라멜"};
            for (int i = 0; i < syrupNames.length; i++) {
                String extraPrice = null;
                if (i > 0) {
                    extraPrice = "+500원";  // "없음" 빼고 +500원
                }

                OptionButtom button = new OptionButtom(syrupNames[i], extraPrice, 117, 65);
                button.setBounds(24 + i * 125, y + 30, 117, 65);  // 버튼 117 + 간격 8

                final String value = syrupValues[i];
                button.addMouseListener(onClick(() -> {
                    syrup = value;
                    updateSelection();
                }));
                sheet.add(button);
                syrupButtons.add(button);
            }

            y = y + 119;
        }

        // ----- 수량 + 담기 버튼 -----
        quantityControl = new QuantityControl(1, 1, 9);  // 1개부터 최대 9개
        quantityControl.setBounds(24, y + 22, 102, 32);
        quantityControl.setOnChange(() -> updatePrice());
        sheet.add(quantityControl);

        addButton = Theme.primaryButton("");
        addButton.setBounds(142, y + 8, 374, 60);
        addButton.addActionListener(e -> menuScreen.addToCart(makeItem()));
        sheet.add(addButton);

        // 흰 창 크기와 위치
        // 아래쪽 둥근 모서리는 안 보이게 화면 밖으로 20px 더 길게 만듦
        int sheetHeight = y + 68 + 24;
        sheet.setBounds(0, 960 - sheetHeight, 540, sheetHeight + 20);

        if (hasOptions) {
            updateSelection();
        }
        return sheet;
    }

    // 옵션 그룹 제목 ("온도 필수")
    private void addGroupTitle(JPanel sheet, String title, boolean required, int y) {
        JLabel titleLabel = Theme.label(title, Theme.bold(17), Theme.TEXT);
        int width = titleLabel.getPreferredSize().width;
        titleLabel.setBounds(24, y, width, 24);
        sheet.add(titleLabel);

        if (required) {
            JLabel requiredLabel = Theme.label("필수", Theme.regular(13), Theme.BLUE);
            requiredLabel.setBounds(24 + width + 6, y + 2, 40, 20);
            sheet.add(requiredLabel);
        }
    }

    // 선택된 옵션 버튼만 파랗게 표시하고, 가격 다시 계산
    private void updateSelection() {
        hotButton.setSelected("HOT".equals(temp));
        iceButton.setSelected("ICE".equals(temp));
        regularButton.setSelected("R".equals(size));
        largeButton.setSelected("L".equals(size));

        for (int i = 0; i < syrupButtons.size(); i++) {
            boolean selected = Objects.equals(syrupValues[i], syrup);
            syrupButtons.get(i).setSelected(selected);
        }

        updatePrice();
    }

    // 지금 고른 내용으로 장바구니 항목 만들기
    private CartItem makeItem() {
        int quantity = quantityControl.getValue();

        if (hasOptions) {
            return new CartItem(menu, temp, size, shotControl.getValue(), syrup, quantity);
        } else {
            // 디저트: 옵션 없음 (DB에는 temp, size, syrup이 NULL, shot이 0으로 저장됨)
            return new CartItem(menu, null, null, 0, null, quantity);
        }
    }

    // 담기 버튼 글씨 바꾸기: "2,500원 담기"
    private void updatePrice() {
        addButton.setText(Theme.won(makeItem().getTotalPrice()) + " 담기");
    }

    // 클릭했을 때 action을 실행하는 MouseAdapter 만들기 (반복되는 코드 줄이기용)
    private MouseAdapter onClick(Runnable action) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }
        };
    }
}