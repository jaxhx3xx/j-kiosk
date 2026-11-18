package org.jkiosk;

import org.jkiosk.dao.MenuDao;
import org.jkiosk.model.Menu;

public class Main {
    public static void main(String[] args) throws Exception {
        MenuDao menuDao = new MenuDao();

        for (Menu m : menuDao.findByCategory(1)) {
            String mark = m.isSoldOut() ? " (품절)" : "";
            System.out.println(m.getName() + " - " + m.getPrice() + "원" + mark);
        }
    }
}