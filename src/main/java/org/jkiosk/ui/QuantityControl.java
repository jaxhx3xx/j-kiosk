package org.jkiosk.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// 수량 조절 ( −  1  + ). 크기 102 x 32
public class QuantityControl extends JPanel {

    private int value;        // 지금 숫자
    private final int min;    // 최소
    private final int max;    // 최대
    private final JLabel valueLabel;
    private Runnable onChange;  // 숫자가 바뀌면 실행할 코드

    public QuantityControl(int start, int min, int max) {
        this.value = start;
        this.min = min;
        this.max = max;

        setLayout(null);
        setOpaque(false);

        // − 버튼
        add(makeCircleButton("-", 0, -1));

        // 가운데 숫자
        valueLabel = Theme.label(String.valueOf(value), Theme.bold(16), Theme.TEXT);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueLabel.setBounds(36, 0, 30, 32);
        add(valueLabel);

        // + 버튼
        add(makeCircleButton("+", 70, +1));
    }

    // 회색 동그라미 버튼 만들기 (change: 누르면 더할 값, -1 또는 +1)
    private RoundPanel makeCircleButton(String text, int x, int change) {
        RoundPanel circle = new RoundPanel(Theme.DISABLED_BG, 16);
        circle.setBounds(x, 0, 32, 32);
        circle.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel label = Theme.label(text, Theme.bold(18), Theme.BUTTON_TEXT);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBounds(0, 0, 32, 32);
        circle.add(label);

        circle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setValue(value + change);
            }
        });
        return circle;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int newValue) {
        if (newValue < min || newValue > max) {
            return;  // 범위를 넘으면 무시
        }
        value = newValue;
        valueLabel.setText(String.valueOf(value));

        if (onChange != null) {
            onChange.run();
        }
    }

    public void setOnChange(Runnable onChange) {
        this.onChange = onChange;
    }
}