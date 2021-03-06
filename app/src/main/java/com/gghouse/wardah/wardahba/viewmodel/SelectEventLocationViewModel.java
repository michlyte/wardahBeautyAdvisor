package com.gghouse.wardah.wardahba.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.location.Address;

public class SelectEventLocationViewModel extends ViewModel {

    private MutableLiveData<Address> mAddress;

    public MutableLiveData<Address> getAddress() {
        if (mAddress == null) {
            mAddress = new MutableLiveData<>();
        }
        return mAddress;
    }
}
