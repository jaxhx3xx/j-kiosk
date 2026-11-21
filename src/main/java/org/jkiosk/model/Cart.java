package org.jkiosk.model;

import java.util.ArrayList;
import java.util.List;

// 장바구니. DB가 아니라 메모리(List)에만 있다가, 결제할 때 DB에 저장함
public class Cart {

    private final List<CartItem> items = new ArrayList<>();

    // 담기: 같은 메뉴 + 같은 옵션이 이미 있으면 수량만 더하기
    public void add(CartItem newItem) {
        for (CartItem item : items) {
            if (item.isSameAs(newItem)) {
                item.setQuantity(item.getQuantity() + newItem.getQuantity());
                return;
            }
        }
        items.add(newItem);
    }

    public void remove(CartItem item) {
        items.remove(item);
    }

    public void clear() {
        items.clear();
    }

    public List<CartItem> getItems() {
        return items;
    }

    // 전체 수량 (아메리카노 2잔 + 크로플 1개 = 3)
    public int getTotalCount() {
        int count = 0;
        for (CartItem item : items) {
            count = count + item.getQuantity();
        }
        return count;
    }

    // 전체 금액
    public int getTotalPrice() {
        int total = 0;
        for (CartItem item : items) {
            total = total + item.getTotalPrice();
        }
        return total;
    }

    // 이 메뉴가 장바구니에 있는지 (메뉴 카드 파란 테두리용)
    public boolean contains(int menuId) {
        for (CartItem item : items) {
            if (item.getMenu().getMenuId() == menuId) {
                return true;
            }
        }
        return false;
    }
}