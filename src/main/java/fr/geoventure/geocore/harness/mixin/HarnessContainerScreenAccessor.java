package fr.geoventure.geocore.harness.mixin;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Harness: container-screen layout for slot hit positions. */
@Mixin(AbstractContainerScreen.class)
public interface HarnessContainerScreenAccessor {

    @Accessor("leftPos")
    int geocore$leftPos();

    @Accessor("topPos")
    int geocore$topPos();

    @Accessor("imageWidth")
    int geocore$imageWidth();

    @Accessor("imageHeight")
    int geocore$imageHeight();

    @Accessor("hoveredSlot")
    Slot geocore$hoveredSlot();
}
