package model;

public class Seat 
{
    private final int row;
    private final int number;
    private boolean reserved;
    private boolean Vip;

    public Seat(int row, int number) 
    {
        this.row = row;
        this.number = number;
        this.reserved = false;
        this.Vip= false;
    }

    public int getRow() 
    {
        return row;
    }

    public int getNumber() 
    {
        return number;
    }

    public boolean isReserved() 
    {
        return reserved;
    }

    public boolean isVIP() 
    {
        return Vip;
    }
    
    public void reserve() 
    {
        reserved = true;
    }

    public void cancel() 
    {
        reserved = false;
    }
    
    public void SetVIP(boolean How)
    {
    	this.Vip = How;
    }
}
