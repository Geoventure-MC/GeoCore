package fr.geoventure.geocore.harness.mixin;

import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/** Harness: synthetic mouse events through the vanilla handler. */
@Mixin(MouseHandler.class)
public interface HarnessMouseHandlerAccessor {

    //? if >=26.1 {
    @Invoker("onButton")
    void geocore$onButton(long window, net.minecraft.client.input.MouseButtonInfo button, int action);
    //?} else {
    /*@Invoker("onPress")
    void geocore$onPress(long window, int button, int action, int modifiers);
    *///?}

    @Invoker("onMove")
    void geocore$onMove(long window, double x, double y);

    @Invoker("onScroll")
    void geocore$onScroll(long window, double xOffset, double yOffset);
}
