package model;

import java.time.LocalDateTime;

public class Reservation {
    private final Customer customer;
    private  int theaterNum;
    private final Seat seat;
    private final LocalDateTime reservationTime;

    public Reservation(Customer customer, Seat seat, int theater) {
        this.customer = customer;
        this.seat = seat;
        this.theaterNum = theater;
        this.reservationTime = LocalDateTime.now();
    }

    public Customer getCustomer() {
        return customer;
    }

    public Seat getSeat() {
        return seat;
    }

    public int getTheater() {
        return theaterNum;
    }
    
    public String getReservationTime() {
    	int month = reservationTime.getMonthValue();   // 몇 월
        int day = reservationTime.getDayOfMonth();     // 몇 일
        int hour = reservationTime.getHour();          // 몇 시 (24시간제)
        int minute = reservationTime.getMinute();      // 몇 분
        String res = month + "월 " + day + "일 " + hour + "시 " + minute + "분"; 
        return(res);

    }
}
