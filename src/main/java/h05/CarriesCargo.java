package h05;

public interface CarriesCargo {
    void loadFreight(char cargoWeight);

    boolean containsMoreFreight();
    char unloadNextFreight();
}
