package io.puharesource.mc.nuggit.backend.config;

import io.puharesource.mc.nuggit.config.ConfigField;

public final class ConfigMain {
    @ConfigField(path = "debug")
    public boolean debug = false;
}
