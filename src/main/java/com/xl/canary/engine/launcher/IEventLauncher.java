package com.xl.canary.engine.launcher;


import com.xl.canary.engine.event.IEvent;

/**
 * Created by gqwu on 2018/4/5.
 */
public interface IEventLauncher<V extends IEvent> {
    void launch(V event) throws Exception;
}
