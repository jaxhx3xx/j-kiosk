package org.jkiosk.model;

// menu 테이블 한 줄을 담는 클래스
public class Menu {
    private final int menuId;
    private final int categoryId;
    private final String name;
    private final int price;
    private final boolean soldOut;

    public Menu(int menuId, int categoryId, String name, int price, boolean soldOut) {
        this.menuId = menuId;
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.soldOut = soldOut;
    }

    public int getMenuId() { return menuId; }
    public int getCategoryId() { return categoryId; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public boolean isSoldOut() { return soldOut; }
}