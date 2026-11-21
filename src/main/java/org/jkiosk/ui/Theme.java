package org.jkiosk.ui;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

// 피그마 디자인의 색, 글꼴, 이미지, 자주 쓰는 버튼을 한 곳에 모아둔 클래스
public class Theme {

    // ===== 색 (피그마 값 그대로) =====
    public static final Color BLUE = new Color(0x3182F6);       // 메인 파랑
    public static final Color TEXT = new Color(0x191F28);       // 진한 글씨
    public static final Color SUB_TEXT = new Color(0x6B7684);   // 회색 설명 글씨
    public static final Color BUTTON_TEXT = new Color(0x4E5968);// 테두리 버튼 글씨
    public static final Color LIGHT_BG = new Color(0xF5F7FA);   // 연한 회색 배경
    public static final Color LINE = new Color(0xE5E8EC);       // 구분선, 테두리
    public static final Color TAB_GRAY = new Color(0xB0B8C1);    // 선택 안 된 탭, 비활성 글씨
    public static final Color BADGE_BG = new Color(0xEAF3FE);    // 연한 파랑 배지 배경
    public static final Color DISABLED_BG = new Color(0xF2F4F6); // 비활성 버튼 배경
    public static final Color WHITE = Color.WHITE;

    // ===== 글꼴 =====
    // 피그마는 Noto Sans KR이지만, 윈도우에 기본으로 있는 맑은 고딕을 사용
    public static final String FONT_NAME = "Malgun Gothic";

    public static Font bold(int size) {
        return new Font(FONT_NAME, Font.BOLD, size);
    }

    public static Font regular(int size) {
        return new Font(FONT_NAME, Font.PLAIN, size);
    }

    // ===== 글씨 라벨 만들기 =====
    public static JLabel label(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    // ===== 파란 버튼 (피그마의 "버튼/주요") =====
    public static JButton primaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(bold(18));
        button.setBackground(BLUE);
        button.setForeground(WHITE);
        // arc = 모서리 둥글기(지름), 나머지는 테두리와 포커스 표시 없애기
        button.putClientProperty(FlatClientProperties.STYLE,
                "arc: 28; borderWidth: 0; focusWidth: 0; innerFocusWidth: 0");
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    // ===== 회색 버튼 (피그마의 "버튼/보조") =====
    public static JButton secondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(bold(18));
        button.setBackground(DISABLED_BG);
        button.setForeground(BUTTON_TEXT);
        button.putClientProperty(FlatClientProperties.STYLE,
                "arc: 28; borderWidth: 0; focusWidth: 0; innerFocusWidth: 0");
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // ===== 메뉴 이미지 불러오기 =====
    // images/menu/1.png 처럼 menu_id 번호로 저장한 파일
    public static ImageIcon menuImage(int menuId, int size) {
        return loadImage("/images/menu/" + menuId + ".png", size);
    }

    // ===== 아이콘 불러오기 =====
    // images/icon/home.png 처럼 이름으로 저장한 파일
    public static ImageIcon icon(String name, int size) {
        return loadImage("/images/icon/" + name + ".png", size);
    }

    // resources 폴더 안의 이미지를 읽어서 정사각형 size 크기로 줄임
    private static ImageIcon loadImage(String path, int size) {
        URL url = Theme.class.getResource(path);

        if (url == null) {
            return null; // 파일이 없으면 빈칸으로 둠
        }

        Image original = new ImageIcon(url).getImage();
        Image scaled = original.getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
    // 가격 글씨 만들기: 2000 → "2,000원"
    public static String won(int price) {
        return String.format("%,d원", price);
    }
}