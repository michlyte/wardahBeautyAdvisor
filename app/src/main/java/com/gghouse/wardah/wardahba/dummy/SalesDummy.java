package com.gghouse.wardah.wardahba.dummy;

import com.gghouse.wardah.wardahba.model.Sales;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by michaelhalim on 2/25/17.
 */

public class SalesDummy {
    public static final List<Sales> ITEMS = new ArrayList<Sales>();

    private static final int COUNT = 5;

    static {
        for (long i = 0; i < COUNT; i++) {
            ITEMS.add(new Sales(new Date().getTime(), i, (Double.parseDouble(i + "") + 1000000), i, i == 0 ? true : false));
        }
    }
}
