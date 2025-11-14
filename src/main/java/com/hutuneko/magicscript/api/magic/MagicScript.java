package com.hutuneko.magicscript.api.magic;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MagicScript {
    String value();
}
