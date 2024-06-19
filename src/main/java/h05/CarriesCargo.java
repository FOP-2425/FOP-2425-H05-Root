package h05;

public interface CarriesCargo {
    void loadContainer(int cargoWeight);

    boolean hasFreightLoaded();
    int unloadNextContainer();
}
