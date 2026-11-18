package fr.geoventure.geocore.harness.mixin;

import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/** Harness: synthetic keyboard events through the vanilla handler. */
@Mixin(KeyboardHandler.class)
public interface HarnessKeyboardHandlerAccessor {

    //? if >=26.1 {
    @Invoker("keyPress")
    void geocore$keyPress(long window, int action, net.minecraft.client.input.KeyEvent event);

    @Invoker("charTyped")
    void geocore$charTyped(long window, net.minecraft.client.input.CharacterEvent event);
    //?} else {
    /*@Invoker("charTyped")
    void geocore$charTyped(long window, int codepoint, int modifiers);
    *///?}
}
