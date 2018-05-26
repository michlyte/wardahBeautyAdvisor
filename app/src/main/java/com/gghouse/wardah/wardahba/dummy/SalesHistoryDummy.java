package com.gghouse.wardah.wardahba.dummy;

import com.gghouse.wardah.wardahba.model.Sales;
import com.gghouse.wardah.wardahba.model.SalesHistoryHeader;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by michaelhalim on 2/26/17.
 */

public class SalesHistoryDummy {
    public static final List<Sales> ITEMS = new ArrayList<Sales>();

    private static final int COUNT = 10;

    static {
        ITEMS.add(new SalesHistoryHeader());

        for (long i = 0; i < COUNT; i++) {
            ITEMS.add(
                    new Sales(
                            new Date().getTime() - (i * (1000 * 60 * 60 * 24)),
                            i,
                            (Double.parseDouble(i + "") + 1000000),
                            i,
                            i == 0 ? true : false)
            );
        }
    }
}
