package com.androidproject.iottechbazaar.util;

import rx.subjects.BehaviorSubject;

public final class RxBus {

    private static final BehaviorSubject<Object> behaviorSubject
            = BehaviorSubject.create();


    public static BehaviorSubject<Object> getSubject() {
        return behaviorSubject;
    }

}
