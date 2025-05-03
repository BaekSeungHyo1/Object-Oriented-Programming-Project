package model;

public class Customer 
{
    private final String name;
    private final String phoneNumber;
    public int price;
    
    public Customer(String name, String phoneNumber) 
    {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.price = 0;
    }

    public String getName() 
    {
        return name;
    }

    public String getPhoneNumber() 
    {
        return phoneNumber;
    }
    
    public int getPrice() 
    {
        return price;
    }
}
