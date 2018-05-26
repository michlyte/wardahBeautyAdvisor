package com.gghouse.wardah.wardahba.dummy;

import com.gghouse.wardah.wardahba.model.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by michaelhalim on 2/25/17.
 */

public class TestHistoryDummy {
    public static final List<Test> ITEMS = new ArrayList<Test>();

    private static final int COUNT = 10;

    static {
        for (int i = 0; i < COUNT; i++) {
            ITEMS.add(new Test(i * 10, "Tes ke " + (i + 1), i, new Date().getTime()));
        }
    }
}
