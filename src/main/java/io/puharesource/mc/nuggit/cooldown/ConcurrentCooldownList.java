package io.puharesource.mc.nuggit.cooldown;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ConcurrentCooldownList<E> extends CooldownList<E> {
    public ConcurrentCooldownList(long time, TimeUnit unit) {
        super(time, unit);
        cooldowns = new ConcurrentHashMap<>();
    }
}
