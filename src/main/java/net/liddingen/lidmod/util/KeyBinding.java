package net.liddingen.lidmod.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String KEY_CATEGORY_LIDMOD = "key.category.tutorialmod.tutorial";
    public static final String KEY_SHELL_HIDING = "key.lidmod.shell_hiding";

    public static final KeyMapping HIDING_KEY = new KeyMapping(KEY_SHELL_HIDING, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, KEY_CATEGORY_LIDMOD); //GLFW_KEY_"x"
}