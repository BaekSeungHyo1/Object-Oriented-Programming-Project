package repository;

import model.Reservation;

public class ReservationRepository {
	
    private static final int MAX_RESERVATIONS = 100;
    private final Reservation[] reservations = new Reservation[MAX_RESERVATIONS];
    private int count = 0; // 현재 예약 수

    // 예약 추가
    public void save(Reservation reservation) {
        if (count >= MAX_RESERVATIONS) {
            System.out.println("[오류] 예약 저장 공간이 부족합니다.");
            return;
        }
        reservations[count++] = reservation;
    }

    // 모든 예약 조회
    public Reservation[] findAll() {
        Reservation[] result = new Reservation[count];
        for (int i = 0; i < count; i++) {
            result[i] = reservations[i];
        }
        return result;
    }

    // 예약 삭제
    public void delete(Reservation reservation) {
        for (int i = 0; i < count; i++) {
            if (reservations[i].equals(reservation)) {

                for (int j = i; j < count - 1; j++) {
                    reservations[j] = reservations[j + 1];
                }
                reservations[--count] = null;
                return;
            }
        }
    }

    // 고객 이름으로 예약 찾기
    public Reservation[] findByCustomerName(String name) {
        int matchCount = 0;
        Reservation[] temp = new Reservation[count];
        
        for (int i = 0; i < count; i++) 
        {
            if (reservations[i].getCustomer().getName().equals(name)) 
            {
                temp[matchCount++] = reservations[i];
            }
        }

        // 정확한 크기의 결과 배열 생성
        Reservation[] result = new Reservation[matchCount];
        
        for (int i = 0; i < matchCount; i++) 
        {
            result[i] = temp[i];
        }

        return result;
    }
}
