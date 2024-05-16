package com.sunny.flavorfinder.listenersJ;

import com.sunny.flavorfinder.modelJ.Step;

public interface EquipmentDetailsListener {

    void didFetch(Step response, String message);
    void didError(String message);
}
