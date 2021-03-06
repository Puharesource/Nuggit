package io.puharesource.mc.nuggit.command.v2;

import io.puharesource.mc.nuggit.command.AllowedSender;
import io.puharesource.mc.nuggit.command.ExecutionType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommand {
    String parent();
    String name();
    String permission() default "";
    AllowedSender allowedSender() default AllowedSender.ALL;
    String usage() default "";
    String description() default "A Nuggit Command.";
    ExecutionType executionType() default ExecutionType.NORMAL;
    String[] aliases() default {};
}
