package fr.geoventure.geocore.mixin;

import fr.geoventure.geocore.world.FallingBlockState;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/** Write access to the falling block's private state; see {@link FallingBlockState}. */
@Mixin(FallingBlockEntity.class)
public abstract class MixinFallingBlockState implements FallingBlockState {

    @Shadow
    private BlockState blockState;

    @Shadow
    private boolean cancelDrop;

    @Override
    public void geocore$setBlockState(BlockState state) {
        blockState = state;
    }

    @Override
    public boolean geocore$cancelDrop() {
        return cancelDrop;
    }
}
