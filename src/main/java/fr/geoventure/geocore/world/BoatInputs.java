package fr.geoventure.geocore.world;

/** The boat's private rider-input flags, exposed by {@code MixinBoatControl}. */
public interface BoatInputs {

    boolean geocore$inputLeft();

    boolean geocore$inputRight();

    boolean geocore$inputUp();

    boolean geocore$inputDown();
}
