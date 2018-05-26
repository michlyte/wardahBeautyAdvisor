package com.gghouse.wardah.wardahba.dummy;

import com.gghouse.wardah.wardahba.model.ProductHighlight;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaelhalim on 2/26/17.
 */

public class ProductHighlightDumy {
    public static final List<ProductHighlight> ITEMS = new ArrayList<ProductHighlight>();

    private static final int COUNT = 3;

    static {
        for (int i = 0; i < COUNT; i++) {
            ITEMS.add(new ProductHighlight((long) i, "Name: " + i));
        }
    }
}
