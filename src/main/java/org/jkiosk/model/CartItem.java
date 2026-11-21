package org.jkiosk.model;

import java.util.Objects;

// 장바구니에 담긴 메뉴 한 줄 (결제하면 order_item 테이블 한 줄이 됨)
public class CartItem {

    public static final int OPTION_PRICE = 500;  // 라지, 샷 1개, 시럽 추가금

    private final Menu menu;
    private final String temp;   // "HOT", "ICE" (디저트는 null)
    private final String size;   // "R", "L" (디저트는 null)
    private final int shot;      // 샷 추가 횟수 (디저트는 0)
    private final String syrup;  // "VANILLA", "HAZELNUT", "CARAMEL" (없으면 null)
    private int quantity;        // 수량

    public CartItem(Menu menu, String temp, String size, int shot, String syrup, int quantity) {
        this.menu = menu;
        this.temp = temp;
        this.size = size;
        this.shot = shot;
        this.syrup = syrup;
        this.quantity = quantity;
    }

    // 옵션 추가금까지 포함한 1개 가격 (order_item.price에 저장할 값)
    public int getUnitPrice() {
        int price = menu.getPrice();

        if ("L".equals(size)) {
            price = price + OPTION_PRICE;   // 라지
        }
        price = price + shot * OPTION_PRICE; // 샷 개수만큼
        if (syrup != null) {
            price = price + OPTION_PRICE;   // 시럽
        }
        return price;
    }

    // 1개 가격 x 수량
    public int getTotalPrice() {
        return getUnitPrice() * quantity;
    }

    // 같은 메뉴 + 같은 옵션인지 (같으면 장바구니에서 새 줄 대신 수량만 더함)
    // Objects.equals는 둘 다 null이어도 안전하게 비교해줌
    public boolean isSameAs(CartItem other) {
        return menu.getMenuId() == other.menu.getMenuId()
                && Objects.equals(temp, other.temp)
                && Objects.equals(size, other.size)
                && shot == other.shot
                && Objects.equals(syrup, other.syrup);
    }

    // 장바구니 화면에 보여줄 옵션 글씨: "ICE · 레귤러 · 샷 1 추가 · 바닐라"
    public String getOptionText() {
        if (temp == null) {
            return "옵션 없음";  // 디저트
        }

        String text = temp;
        if ("L".equals(size)) {
            text = text + " · 라지";
        } else {
            text = text + " · 레귤러";
        }
        if (shot > 0) {
            text = text + " · 샷 " + shot + " 추가";
        }
        if (syrup != null) {
            text = text + " · " + syrupName(syrup);
        }
        return text;
    }

    // DB 값 → 한글 이름
    public static String syrupName(String syrup) {
        if ("VANILLA".equals(syrup)) {
            return "바닐라";
        } else if ("HAZELNUT".equals(syrup)) {
            return "헤이즐넛";
        } else if ("CARAMEL".equals(syrup)) {
            return "카라멜";
        } else {
            return "없음";
        }
    }

    public Menu getMenu() { return menu; }
    public String getTemp() { return temp; }
    public String getSize() { return size; }
    public int getShot() { return shot; }
    public String getSyrup() { return syrup; }
    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}