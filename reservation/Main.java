package reservation;

import model.*;
import service.ReservationService;
import ui.ReservationUI;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1. 사용자 정보 입력
        System.out.print("이름을 입력하세요: ");
        String name = scanner.nextLine();
        System.out.print("전화번호를 입력하세요: ");
        String phone = scanner.nextLine();

        Customer user = new Customer(name, phone);

        // 2. 영화관 3개 생성
        Theater[] theaters = new Theater[3];

        // 3관의 좌석은 5행 × 10열
        for (int i = 0; i < theaters.length; i++) {
            Seat[][] seats = new Seat[5][10];
            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 10; col++) {
                    // VIP 조건 예시: 3열 이상 + 열 번호 8~10
                    if (row >= 2 && col >= 7) {
                        seats[row][col] = new VIPSeat(row + 1, col + 1, 5000);
                    } else {
                        seats[row][col] = new Seat(row + 1, col + 1);
                    }
                }
            }
            theaters[i] = new Theater((i + 1), seats);
        }



        // 4. 예약 시스템 실행
        ReservationService service = new ReservationService();
        ReservationUI ui = new ReservationUI(service);
        ui.start(theaters, user);
        
        scanner.close();
    }
}
