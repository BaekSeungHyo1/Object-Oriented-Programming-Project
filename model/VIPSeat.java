package model;

public class VIPSeat extends Seat 
{
    private final int extraCharge;	 						// 추가 요금

    public VIPSeat(int row, int number, int extraCharge) 
    {
        super(row, number); 								// 부모 호출
        this.extraCharge = extraCharge;
        SetVIP(true);
    }

    public int getExtraCharge() 
    {
        return extraCharge;
    }
}
