package io.puharesource.mc.nuggit.cooldown;

public interface Cooldown<E> {
    void addCooldown(E o);

    void removeCooldown(E o);

    boolean isUnderCooldown(E o);

    long getTimeLeftMillis(E o);

    void cleanup();

    void clear();
}
