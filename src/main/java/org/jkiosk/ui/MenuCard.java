package org.jkiosk.ui;

import org.jkiosk.model.Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// 메뉴 카드 하나 (이미지 + 이름 + 가격). 크기 242 x 262
public class MenuCard extends RoundPanel {

    public MenuCard(Menu menu, KioskFrame frame) {
        super(Theme.WHITE, 16);
        setBorderColor(Theme.LINE);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // ----- 품절이면: 반투명 흰 막 + "품절" 표시 -----
        // Swing은 먼저 add한 것이 맨 위에 그려져서, 품절 막을 가장 먼저 넣음
        if (menu.isSoldOut()) {
            RoundPanel cover = new RoundPanel(new Color(255, 255, 255, 153), 16); // 흰색 60%
            cover.setBounds(0, 0, 242, 262);

            RoundPanel soldOutBadge = new RoundPanel(Theme.BUTTON_TEXT, 8);
            soldOutBadge.setBounds(93, 76, 56, 38);

            JLabel soldOutText = Theme.label("품절", Theme.bold(15), Theme.WHITE);
            soldOutText.setHorizontalAlignment(SwingConstants.CENTER);
            soldOutText.setBounds(0, 0, 56, 38);
            soldOutBadge.add(soldOutText);

            cover.add(soldOutBadge);
            add(cover);
        }

        // ----- 이미지 (연한 회색 상자 안에 가운데) -----
        RoundPanel imageBox = new RoundPanel(Theme.LIGHT_BG, 12);
        imageBox.setBounds(8, 8, 226, 170);

        JLabel image = new JLabel(Theme.menuImage(menu.getMenuId(), 124));
        image.setBounds(51, 23, 124, 124);
        imageBox.add(image);
        add(imageBox);

        // ----- 이름 -----
        JLabel name = Theme.label(menu.getName(), Theme.regular(17), Theme.TEXT);
        name.setBounds(14, 190, 220, 24);
        add(name);

        // ----- 가격 -----
        JLabel price = Theme.label(Theme.won(menu.getPrice()), Theme.bold(18), Theme.TEXT);
        price.setBounds(14, 214, 220, 26);
        add(price);

        // ----- 카드를 눌렀을 때 -----
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (menu.isSoldOut()) {
                    // 품절 안내 (나중에 피그마 18번 팝업 모양으로 바꿀 예정)
                    JOptionPane.showMessageDialog(frame,
                            menu.getName() + "\n품절된 메뉴예요. 다른 메뉴를 선택해 주세요.");
                } else {
                    // 다음 단계에서 옵션 선택 화면으로 연결할 예정
                    System.out.println("선택한 메뉴: " + menu.getName());
                }
            }
        });
    }
}