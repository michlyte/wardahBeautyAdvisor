package com.gghouse.wardah.wardahba.dummy;

import com.gghouse.wardah.wardahba.model.Pelanggan;

import java.util.ArrayList;
import java.util.List;

public class PelangganHistoryDummy {
    public static final List<Pelanggan> ITEMS = new ArrayList<Pelanggan>();

    private static final int COUNT = 10;

    static {
        for (long i = 0; i < COUNT; i++) {
            ITEMS.add(new Pelanggan(i, "Name " + (i + 1), "name" + (i + 1) + "@mmail.com"));
        }
    }
}
