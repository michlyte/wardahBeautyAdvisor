package com.gghouse.wardah.wardahba.dummy;

import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.model.Notif;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaelhalim on 2/19/17.
 */

public abstract class NotifDummy {
    public static final List<Notif> ITEMS = new ArrayList<Notif>();

    private static final int COUNT = 5;

    static {
        for (long i = 0; i < COUNT; i++) {
            ITEMS.add(new Notif(
                    i,
                    "Description is the fiction-writing mode for transmitting a mental image of the particulars of a story. Together with dialogue, narration, exposition, and summarization, description is one of the most widely recognized of the fiction-writing modes. As stated in Writing from A to Z, edited by Kirk Polking, description is more than the amassing of details; it is bringing something to life by carefully choosing and arranging words and phrases to produce the desired effect.[3] The most appropriate and effective techniques for presenting description are a matter of ongoing discussion among writers and writing coaches.",
                    "PRODUCT_LAUNCH",
                    R.drawable.ic_filter,
                    System.currentTimeMillis()));
        }
    }
}
