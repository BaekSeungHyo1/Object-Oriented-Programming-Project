package service;

import model.*; // Customer, Theater, Seat 등 모델 클래스들 가져옴
import repository.ReservationRepository; // 예약 저장소 클래스 가져옴

public class ReservationService {
    // 예약 정보를 저장할 저장소 객체 생성
    private final ReservationRepository reservationRepository = new ReservationRepository();

    // 1. 예약 생성 메서드
    public Reservation makeReservation(Customer customer, Theater theater, int row, int number) {
        // 극장에서 해당 좌석 위치(row, number)에 해당하는 좌석 찾기
        Seat seat = findSeat(theater, row, number);

        // 좌석이 존재하지 않는 경우 예외 발생
        if (seat == null) {
            throw new IllegalArgumentException("해당 좌석을 찾을 수 없습니다.");
        }

        // 이미 예약된 좌석인 경우 예외 발생
        if (seat.isReserved()) {
            throw new IllegalStateException("이미 예약된 좌석입니다.");
        }

        // 좌석 예약 처리 (reserved 상태 true로 바뀜)
        seat.reserve();

        // VIP 좌석이면 추가요금 반영
        if (seat.isVIP() && seat instanceof VIPSeat) {
            VIPSeat vip = (VIPSeat) seat;
            customer.price += 10000 + vip.getExtraCharge(); // 기본 요금 + 추가요금
        } else {
            customer.price += 10000; // 일반 좌석 요금
        }

        // 예약 객체 생성 (고객, 좌석, 극장 정보 포함)
        Reservation reservation = new Reservation(customer, seat, theater.getName());

        // 예약을 저장소에 저장
        reservationRepository.save(reservation);

        // 생성된 예약 반환
        return reservation;
    }

    // 2. 예약 취소 메서드
    public void cancelReservation(Reservation reservation) {
        // 취소할 예약이 없는 경우 예외 처리
        if (reservation == null) {
            throw new IllegalArgumentException("취소할 예약이 없습니다.");
        }

        // 해당 좌석 예약 상태를 취소 (reserved → false)
        reservation.getSeat().cancel();

        // 저장소에서도 해당 예약 삭제
        reservationRepository.delete(reservation);
    }

    // 3. 사용 가능한 좌석 목록 가져오기
    public Seat[] listAvailableSeats(Theater theater) {
        Seat[][] allSeats = theater.getSeats(); // 전체 좌석 2차원 배열
        int total = allSeats.length * allSeats[0].length; // 총 좌석 수 계산

        Seat[] temp = new Seat[total]; // 임시 배열 생성
        int idx = 0;

        // 모든 좌석을 돌면서
        for (int i = 0; i < allSeats.length; i++) {
            for (int j = 0; j < allSeats[i].length; j++) {
                // 예약되지 않은 좌석만 골라서
                if (!allSeats[i][j].isReserved()) {
                    temp[idx++] = allSeats[i][j]; // 임시 배열에 추가
                }
            }
        }

        // 필요한 크기만큼만 잘라서 새 배열 생성
        Seat[] result = new Seat[idx];
        for (int i = 0; i < idx; i++) {
            result[i] = temp[i];
        }

        // 예약 가능한 좌석 배열 반환
        return result;
    }

    // 4. 고객 이름으로 예약 검색
    public Reservation[] findReservationsByCustomerName(String name) {
        // 저장소에서 이름으로 검색된 예약 배열 반환
        return reservationRepository.findByCustomerName(name);
    }

    // 5. 좌석 찾기 (극장 안에서 특정 행/열에 있는 좌석 검색)
    private Seat findSeat(Theater theater, int row, int number) {
        Seat[][] seats = theater.getSeats(); // 2차원 좌석 배열 가져오기
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                // 좌석의 행(row)과 열(number)이 일치하는지 확인
                if (seats[i][j].getRow() == row && seats[i][j].getNumber() == number) {
                    return seats[i][j]; // 해당 좌석 반환
                }
            }
        }
        return null; // 못 찾은 경우 null 반환
    }
}
