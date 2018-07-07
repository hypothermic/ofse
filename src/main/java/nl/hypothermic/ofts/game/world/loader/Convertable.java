package nl.hypothermic.ofts.game.world.loader;

public interface Convertable {

    boolean isConvertable(String s);

    boolean convert(String s);
}
