package com.xl.canary.engine.launcher.impl;

import com.xl.canary.engine.event.entry.EntryEvent;
import com.xl.canary.engine.launcher.IEventLauncher;
import com.xl.canary.exception.InvalidEventException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by gqwu on 2018/4/4.
 */
@Component("entryEventLauncher")
public class EntryEventLauncherImpl implements IEventLauncher<EntryEvent> {

    private static final Logger logger = LoggerFactory.getLogger(EntryEventLauncherImpl.class);


    @Override
    public void launch (EntryEvent event) throws Exception {

        if (event == null || StringUtils.isBlank(event.getBatchId()) ) {
            throw new InvalidEventException("不是合法事件，事件=" + event);
        }

    }
}
