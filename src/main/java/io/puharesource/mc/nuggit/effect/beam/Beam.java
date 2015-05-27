package io.puharesource.mc.nuggit.effect.beam;

import io.puharesource.mc.nuggit.Nuggit;
import io.puharesource.mc.nuggit.effect.EffectLocation;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class Beam {

    private final float speed;
    private final Effect effect;

    public Beam() {
        this(0f);
    }

    public Beam(final float speed) {
        this(speed, Effect.CLOUD);
    }

    public Beam(final float speed, final Effect effect) {
        this.speed = speed;
        this.effect = effect;
    }

    public void shoot(final LivingEntity entity1, final LivingEntity entity2) {
        shoot(EffectLocation.MIDDLE.toLocation(entity1), EffectLocation.MIDDLE.toLocation(entity2));
    }

    public void shoot(final Location loc1, final Location loc2) {
        if (loc1.getWorld().getUID() != loc2.getWorld().getUID()) throw new IllegalArgumentException("The locations given are not in the same world!");

        Bukkit.getScheduler().runTaskAsynchronously(Nuggit.getInstance(), () -> {
            final Vector mainVector = loc2.toVector().subtract(loc1.toVector());
            Vector vector = mainVector.clone().normalize().multiply(0.1);
            final Vector cloneable = vector.clone();

            while (mainVector.length() > vector.length()) {
                Location loc = loc1.clone().add(vector.clone());
                loc.getWorld().spigot().playEffect(loc, effect, 0, 0, 0f, 0f, 0f, speed, 5, 55);
                vector = vector.add(cloneable.clone());
            }
        });
    }
}
