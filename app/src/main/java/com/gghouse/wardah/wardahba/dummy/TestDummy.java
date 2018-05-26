package com.gghouse.wardah.wardahba.dummy;

import com.gghouse.wardah.wardahba.model.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by michaelhalim on 2/22/17.
 */

public abstract class TestDummy {
    public static final List<Test> ITEMS = new ArrayList<Test>();

    private static final int COUNT = 5;

    static {
        for (int i = 0; i < COUNT; i++) {
            ITEMS.add(new Test(100, "Tes ke " + (i + 1), i + i, new Date().getTime()));
        }
    }
}
