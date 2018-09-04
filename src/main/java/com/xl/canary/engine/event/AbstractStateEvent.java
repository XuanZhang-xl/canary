package com.xl.canary.engine.event;

/**
 * Created by gqwu on 2018/4/4.
 */
public abstract class AbstractStateEvent extends AbstractEvent implements IStateEvent {

    private final String uniqueId;

    public AbstractStateEvent(String uniqueId) {
        super();
        this.uniqueId = uniqueId;
    }

    @Override
    public String getUniqueId() {
        return this.uniqueId;
    }
}
