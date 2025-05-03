package service;

import model.*;
import repository.ReservationRepository;

public class ReservationService {
    private final ReservationRepository reservationRepository = new ReservationRepository();

    // 예약 생성
    public Reservation makeReservation(Customer customer, Theater theater, int row, int number) {
        Seat seat = findSeat(theater, row, number);

        if (seat == null) {
            throw new IllegalArgumentException("해당 좌석을 찾을 수 없습니다.");
        }
        if (seat.isReserved()) {
            throw new IllegalStateException("이미 예약된 좌석입니다.");
        }

        seat.reserve();

        // VIP 좌석 추가요금 반영
        if (seat.isVIP() && seat instanceof VIPSeat) {
            VIPSeat vip = (VIPSeat) seat;
            customer.price += 10000 + vip.getExtraCharge();
        } else {
            customer.price += 10000;
        }

        Reservation reservation = new Reservation(customer, seat, theater.getName());
        reservationRepository.save(reservation);
        return reservation;
    }

    // 예약 취소
    public void cancelReservation(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("취소할 예약이 없습니다.");
        }

        reservation.getSeat().cancel();
        reservationRepository.delete(reservation);
    }

    // 사용 가능한 좌석 목록
    public Seat[] listAvailableSeats(Theater theater) {
        Seat[][] allSeats = theater.getSeats();
        int total = theater.getSeats().length * theater.getSeats()[0].length;

        Seat[] temp = new Seat[total];
        int idx = 0;

        for (int i = 0; i < allSeats.length; i++) {
            for (int j = 0; j < allSeats[i].length; j++) {
                if (!allSeats[i][j].isReserved()) {
                    temp[idx++] = allSeats[i][j];
                }
            }
        }

        // 정확한 크기 배열로 잘라서 반환
        Seat[] result = new Seat[idx];
        for (int i = 0; i < idx; i++) {
            result[i] = temp[i];
        }
        return result;
    }

    // 고객 이름으로 예약 찾기
    public Reservation[] findReservationsByCustomerName(String name) {
        return reservationRepository.findByCustomerName(name);
    }

    // 좌석 찾기
    private Seat findSeat(Theater theater, int row, int number) {
        Seat[][] seats = theater.getSeats();
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                if (seats[i][j].getRow() == row && seats[i][j].getNumber() == number) {
                    return seats[i][j];
                }
            }
        }
        return null;
    }
}
