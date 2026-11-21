package org.jkiosk.model;

// category 테이블 한 줄을 담는 클래스
public class Category {
    private final int categoryId;
    private final String name;

    public Category(int categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    public int getCategoryId() { return categoryId; }
    public String getName() { return name; }
}