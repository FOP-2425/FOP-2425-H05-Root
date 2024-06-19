package h05;

public enum FuelType {
    JetA(1.0), JetB(1.2), AvGas(0.99), Biokerosin (1.02);


    private final double consumptionMultiplicator;
    FuelType(double consumptionMultiplicator){
        this.consumptionMultiplicator = consumptionMultiplicator;
    }
    public double getConsumptionMultiplicator() {
        return consumptionMultiplicator;
    }
}
