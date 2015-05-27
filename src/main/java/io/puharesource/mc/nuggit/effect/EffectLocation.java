package io.puharesource.mc.nuggit.effect;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public enum EffectLocation {
    ABOVE_HEAD,
    HEAD,
    MIDDLE,
    FEET;

    public Location toLocation(LivingEntity entity) {
        Location loc;
        switch (this) {
            case ABOVE_HEAD:
                loc = entity.getEyeLocation();
                loc.setY(loc.getY() + .5d);
                break;
            case HEAD:
                loc = entity.getEyeLocation();
                break;
            case MIDDLE:
                loc = entity.getLocation();
                loc.setY(loc.getY() + entity.getEyeHeight() / 2);
                break;
            case FEET:
                loc = entity.getLocation();
                loc.setY(loc.getY() + .1d);
                break;
            default:
                loc = entity.getLocation();
                break;
        }
        return loc;
    }
}
