package fr.geoventure.geocore.world;

/**
 * A boat that steers itself. Vanilla's {@code controlBoat} is private on every version;
 * {@code MixinBoatControl} routes the vanilla call into {@link #geocore$controlBoat()} for boats
 * implementing this. The input flags it needs come from {@link BoatInputs}.
 */
public interface BoatControl {

    /** Replaces vanilla's steering for this tick. */
    void geocore$controlBoat();
}
