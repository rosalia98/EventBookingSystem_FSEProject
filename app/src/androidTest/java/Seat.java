public class Seat {
    private String name;
    private float price;
    private boolean is_available;

    //SETTERS
    public Seat(boolean is_available) {
        this.name = name;
        this.price = price;
        this.is_available = is_available;
    }
    //GETTERS
    public String getname(){
        return name;
    }
    public float getPrice(){
        return price;
    }
    public boolean getIs_available(){
        return is_available;
    }
}
