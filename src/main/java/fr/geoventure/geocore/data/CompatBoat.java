package fr.geoventure.geocore.data;

import fr.geoventure.geocore.world.BoatControl;
import fr.geoventure.geocore.world.BoatInputs;
import java.util.function.Supplier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.vehicle.boat.Boat;

/**
 * Facade for custom {@code Boat} subclasses that steer themselves.
 *
 * <p>26.1's boat takes its drop item as a constructor supplier; older boats answer {@code getDropItem}
 * from a variant. The constructor here accepts the 26.1 shape on every node. Steering: vanilla's
 * {@code controlBoat} is private, so a subclass implements {@link #controlBoat()} and GeoCore's boat
 * mixin hands the vanilla call to it; the rider's input flags are read through the {@code input*()}
 * accessors.
 */
public abstract class CompatBoat extends Boat implements BoatControl {

    //? if <26.1 {
    /*private final Supplier<Item> dropItem;
    *///?}

    protected CompatBoat(EntityType<? extends Boat> type, Level level, Supplier<Item> dropItem) {
        //? if >=26.1 {
        super(type, level, dropItem);
        //?} else {
        /*super(type, level);
        this.dropItem = dropItem;
        *///?}
    }

    //? if <26.1 {
    /*@Override
    public Item getDropItem() {
        return dropItem.get();
    }
    *///?}

    /** Replaces vanilla's steering for this tick. Called only while a rider controls the boat. */
    protected abstract void controlBoat();

    @Override
    public final void geocore$controlBoat() {
        controlBoat();
    }

    protected final boolean inputLeft() {
        return ((BoatInputs) this).geocore$inputLeft();
    }

    protected final boolean inputRight() {
        return ((BoatInputs) this).geocore$inputRight();
    }

    protected final boolean inputUp() {
        return ((BoatInputs) this).geocore$inputUp();
    }

    protected final boolean inputDown() {
        return ((BoatInputs) this).geocore$inputDown();
    }
}
