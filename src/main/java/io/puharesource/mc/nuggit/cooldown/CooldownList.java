package io.puharesource.mc.nuggit.cooldown;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CooldownList<E> implements Cooldown<E> {
    protected Map<E, Long> cooldowns;
    protected long time;

    public CooldownList(long time, TimeUnit unit) {
        this.time = unit.toMillis(time);
        cooldowns = new HashMap<>();
    }

    @Override
    public void addCooldown(E o) {
        cooldowns.put(o, System.currentTimeMillis() + time);
    }

    @Override
    public void removeCooldown(E o) {
        cooldowns.remove(o);
    }

    @Override
    public boolean isUnderCooldown(E o) {
        return cooldowns.containsKey(o);
    }

    @Override
    public long getTimeLeftMillis(E o) {
        if (!cooldowns.containsKey(o)) return 0;
        long timeLeft = cooldowns.get(o) - System.currentTimeMillis();
        return timeLeft <= 0 ? 0l : timeLeft;
    }

    @Override
    public void cleanup() {
        cooldowns.keySet().stream().filter(obj -> cooldowns.get(obj) - System.currentTimeMillis() >= 0).forEach(cooldowns::remove);
    }

    @Override
    public void clear() {
        cooldowns.clear();
    }
}
