package model;

public class Theater 
{
    private final int name;
    private final Seat[][] seats;

    public Theater(int name, Seat[][] seats) 
    {
        this.name = name;
        this.seats = seats;
    }

    public int getName() 
    {
        return name;
    }

    public Seat[][] getSeats() 
    {
        return seats;
    }

    
    public Seat getSeats(int a, int b) 
    {
        return seats[a][b];
    }
}
