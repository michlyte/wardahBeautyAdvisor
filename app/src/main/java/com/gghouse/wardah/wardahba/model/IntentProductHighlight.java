package com.gghouse.wardah.wardahba.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by michaelhalim on 5/13/17.
 */

public class IntentProductHighlight implements Serializable {
    private List<ProductHighlight> objects;

    public IntentProductHighlight(List<ProductHighlight> objects) {
        this.objects = objects;
    }

    public List<ProductHighlight> getObjects() {
        return objects;
    }

    public void setObjects(List<ProductHighlight> objects) {
        this.objects = objects;
    }
}
