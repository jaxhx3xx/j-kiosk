package org.jkiosk.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// 키오스크 창. 화면들을 카드처럼 겹쳐두고 하나씩 보여줌
public class KioskFrame extends JFrame {

    // 화면 이름 (화면을 바꿀 때 이 이름으로 부름)
    public static final String START = "start";  // 01 시작
    public static final String PLACE = "place";  // 02 매장·포장 선택

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel screens = new JPanel(cardLayout);

    public KioskFrame() {
        setTitle("J의 키오스크");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);  // 키오스크라 크기 고정

        // 피그마 화면 크기 540 x 960
        screens.setPreferredSize(new Dimension(540, 960));

        // 화면 등록
        screens.add(new StartScreen(this), START);
        screens.add(makeTempScreen("02 매장·포장 선택 (만드는 중)"), PLACE);

        setContentPane(screens);
        pack();                       // 창 크기를 화면 크기에 맞춤
        setLocationRelativeTo(null);  // 모니터 가운데에 띄움
    }

    // 다른 화면으로 바꾸기
    public void showScreen(String name) {
        cardLayout.show(screens, name);
    }

    // 아직 안 만든 화면 자리에 임시로 넣는 화면 (누르면 시작 화면으로 돌아감)
    private JPanel makeTempScreen(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.WHITE);

        JLabel label = Theme.label(text, Theme.bold(24), Theme.TEXT);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showScreen(START);
            }
        });
        return panel;
    }
}