package com.xl.canary.engine.event;

import com.xl.canary.utils.IDWorker;

import java.util.Date;

/**
 * Created by gqwu on 2018/4/4.
 */
public abstract class AbstractEvent implements IEvent {

    private final String eventId;

    private final long eventTime;

    public AbstractEvent() {
        this.eventId = IDWorker.getNewID();
        this.eventTime = System.currentTimeMillis();
    }

    @Override
    public String getEventId() {
        return this.eventId;
    }

    @Override
    public long getEventTime() {
        return this.eventTime;
    }
}