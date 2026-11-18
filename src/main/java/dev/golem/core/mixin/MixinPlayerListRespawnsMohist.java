package dev.golem.core.mixin;

import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
//? if <1.21 && forge {
/*import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.golem.core.world.Respawns;
import dev.golem.core.world.WorldAttributes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
*///?}

/**
 * Variante Mohist 1.20.1 de {@link MixinPlayerListRespawns} (activee uniquement lorsque
 * {@link dev.golem.core.compat.MohistCompatPlugin} detecte Mohist).
 *
 * <p>Mohist remplace {@code MinecraftServer.overworld()} par {@code server.getLevel(Level.OVERWORLD)}
 * (2e appel a {@code getLevel}, {@code ordinal = 1}) dans {@code PlayerList.respawn(ServerPlayer, boolean)},
 * et ajoute deux surcharges Bukkit {@code respawn(...)}. Cibler la signature explicite Vanilla/Forge et
 * {@code getLevel(..., ordinal = 1)} conserve intactes les regles {@link dev.golem.core.world.Respawns}
 * et {@link dev.golem.core.world.WorldAttributes#BED_SPAWN}.
 */
@Mixin(PlayerList.class)
public abstract class MixinPlayerListRespawnsMohist {
    //? if <1.21 && forge {
    /*@Inject(method = "respawn(Lnet/minecraft/server/level/ServerPlayer;Z)Lnet/minecraft/server/level/ServerPlayer;",
            at = @At("HEAD"))
    private void golem$chooseRespawn(ServerPlayer player, boolean keepEverything,
            CallbackInfoReturnable<ServerPlayer> result,
            @Share("golem$destination") LocalRef<Respawns.Destination> destination) {
        destination.set(Respawns.RULES.invoker().destination(player));
    }

    @ModifyExpressionValue(
            method = "respawn(Lnet/minecraft/server/level/ServerPlayer;Z)Lnet/minecraft/server/level/ServerPlayer;",
            at = @At(value = "INVOKE", ordinal = 0,
                    target = "Lnet/minecraft/server/level/ServerPlayer;getRespawnPosition()Lnet/minecraft/core/BlockPos;"))
    private @Nullable BlockPos golem$lookupPoint(@Nullable BlockPos point,
            @Share("golem$destination") LocalRef<Respawns.Destination> destination) {
        return destination.get() == null ? point : null;
    }

    @WrapOperation(
            method = "respawn(Lnet/minecraft/server/level/ServerPlayer;Z)Lnet/minecraft/server/level/ServerPlayer;",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;findRespawnPositionAndUseSpawnBlock(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;FZZ)Ljava/util/Optional;"))
    private Optional<Vec3> golem$storedBed(ServerLevel level, BlockPos pos, float angle, boolean forced,
            boolean keepEverything, Operation<Optional<Vec3>> original,
            @Share("golem$lookedUp") LocalBooleanRef lookedUp) {
        lookedUp.set(true);
        var state = level.getBlockState(pos);
        if (state.getBlock() instanceof BedBlock) {
            boolean works = BedBlock.canSetSpawn(level);
            if (WorldAttributes.BED_SPAWN.invoker().apply(level, pos, works) != works) {
                return works ? Optional.empty() : BedBlock.findStandUpPosition(EntityType.PLAYER, level, pos,
                        state.getValue(BedBlock.FACING), angle);
            }
        }
        return original.call(level, pos, angle, forced, keepEverything);
    }

    // Mohist remplace server.overworld() (ordinal vanilla unique) par server.getLevel(Level.OVERWORLD)
    // apres l'appel initial pour la dimension sauvegardee du joueur (ordinal 0).
    @ModifyExpressionValue(
            method = "respawn(Lnet/minecraft/server/level/ServerPlayer;Z)Lnet/minecraft/server/level/ServerPlayer;",
            at = @At(value = "INVOKE", ordinal = 1,
                    target = "Lnet/minecraft/server/MinecraftServer;getLevel(Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/server/level/ServerLevel;"))
    private @Nullable ServerLevel golem$destination(@Nullable ServerLevel vanilla,
            @Local(argsOnly = true) ServerPlayer player,
            @Share("golem$destination") LocalRef<Respawns.Destination> destination,
            @Share("golem$lookedUp") LocalBooleanRef lookedUp,
            @Share("golem$fallback") LocalBooleanRef fallback) {
        var choice = destination.get();
        if (choice == null && lookedUp.get()) {
            choice = Respawns.FALLBACKS.invoker().destination(player);
            fallback.set(choice instanceof Respawns.WorldSpawn);
        }
        return choice instanceof Respawns.WorldSpawn world ? world.level() : vanilla;
    }

    @Inject(
            method = "respawn(Lnet/minecraft/server/level/ServerPlayer;Z)Lnet/minecraft/server/level/ServerPlayer;",
            at = @At(value = "INVOKE", shift = At.Shift.AFTER,
                    target = "Lnet/minecraft/server/level/ServerPlayer;restoreFrom(Lnet/minecraft/server/level/ServerPlayer;Z)V"))
    private void golem$preservePoint(ServerPlayer player, boolean keepEverything,
            CallbackInfoReturnable<ServerPlayer> result,
            @Local(ordinal = 1) ServerPlayer replacement,
            @Share("golem$destination") LocalRef<Respawns.Destination> destination,
            @Share("golem$fallback") LocalBooleanRef fallback) {
        if (destination.get() != null) {
            replacement.setRespawnPosition(player.getRespawnDimension(), player.getRespawnPosition(),
                    player.getRespawnAngle(), player.isRespawnForced(), false);
        } else if (!fallback.get()) {
            return;
        }
        replacement.setYRot(replacement.serverLevel().getSharedSpawnAngle());
        replacement.setXRot(0F);
    }
    *///?}
}
