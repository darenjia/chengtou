package com.bokun.bkjcb.chengtou.Event;

/**
 * Created by DengShuai on 2017/7/19.
 */

public class DefaultEvent {
    public static final int GET_DATA_SUCCESS = 0;
    public static final int GET_DATA_NULL = 1;
    private int state_code;

    public DefaultEvent(int state_code) {
        this.state_code = state_code;
    }

    public void setState_code(int state_code) {
        this.state_code = state_code;
    }

    public int getState_code() {
        return state_code;
    }
}
