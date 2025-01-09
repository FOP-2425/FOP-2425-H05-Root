package h05;

public class WeatherBalloon implements Flying{

    private final int balloonNumber;

    public WeatherBalloon(int balloonNumber){
        this.balloonNumber = balloonNumber;
    }

    public void start(){
        Airspace.get().register(this);
    }

    public void pop(){
        Airspace.get().deregister(this);
    }

    @Override
    public String getIdentifier() {
        return "WeatherBalloon " + balloonNumber;
    }
}
