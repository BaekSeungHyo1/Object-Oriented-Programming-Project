package ui;

import model.*;
import service.ReservationService;

import java.util.Scanner;

public class ReservationUI {
    private final ReservationService reservationService;
    private final Scanner scanner = new Scanner(System.in);

    public ReservationUI(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public void start(Theater[] theaters, Customer user) {
        int currentTheaterIndex = selectTheater(theaters);

        if (currentTheaterIndex == -1) {
            System.out.println("프로그램을 종료합니다.");
            return;
        }

        while (true) {
            Theater theater = theaters[currentTheaterIndex];
            System.out.println("\n==== [" + theater.getName() + "관] 예약 시스템 ====");
            System.out.println("1. 좌석 조회");
            System.out.println("2. 좌석 예약");
            System.out.println("3. 예약 취소");
            System.out.println("4. 영화관 변경");
            System.out.println("5. 종료");
            System.out.print("선택: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 엔터 소비

            switch (choice) {
                case 1:
                    showAvailableSeats(theater);
                    break;
                case 2:
                    makeReservation(theater, user);
                    break;
                case 3:
                    cancelReservation();
                    break;
                case 4:
                    currentTheaterIndex = selectTheater(theaters);
                    if (currentTheaterIndex == -1) return;
                    break;
                case 5:
                    System.out.println("프로그램을 종료합니다.");
                    return;
                default:
                    System.out.println("잘못된 입력입니다. 다시 선택하세요.");
            }
        }
    }

    private int selectTheater(Theater[] theaters) {
        System.out.println("\n[영화관 목록]");
        for (int i = 0; i < theaters.length; i++) {
            System.out.println((i + 1) + ". " + theaters[i].getName()+ " 관") ;
        }
        System.out.print("선택할 영화관 번호 입력 (종료: 0): ");
        int selected = scanner.nextInt();
        scanner.nextLine();

        if (selected == 0) return -1;
        if (selected < 1 || selected > theaters.length) {
            System.out.println("잘못된 선택입니다.");
            return selectTheater(theaters);  // 재귀로 다시 선택
        }

        return selected - 1;
    }

    private void showAvailableSeats(Theater theater) {
        showSeatMap(theater.getSeats());
    }

    private void makeReservation(Theater theater, Customer user) {
        System.out.print("예약할 열 번호 입력: ");
        int row = scanner.nextInt();

        System.out.print("예약할 좌석 번호 입력: ");
        int number = scanner.nextInt();
        scanner.nextLine(); // 엔터 소비

        try {
            Reservation reservation = reservationService.makeReservation(user, theater, row, number);
            Seat seat = reservation.getSeat();

            int totalPrice = user.getPrice();

            if (seat.isVIP() && seat instanceof VIPSeat) {
                VIPSeat vipSeat = (VIPSeat) seat;
                System.out.println("\n※ VIP 좌석 추가 요금: " + vipSeat.getExtraCharge() + "원");
            }

            System.out.println("\n[예약 성공]");
            System.out.println(user.getName() + "님이 " + theater.getName()+ "영화관의 "+seat.getRow() + "열 "
                    + seat.getNumber() + "번 좌석을 예약했습니다.");
            System.out.println("총 누적 요금: " + totalPrice + "원");

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("[예약 실패] " + e.getMessage());
        }
    }

    private void cancelReservation() {
        System.out.print("예약자 이름 입력: ");
        String name = scanner.nextLine();

        Reservation[] foundReservations = reservationService.findReservationsByCustomerName(name);

        if (foundReservations.length == 0) {
            System.out.println("[예약 취소 실패] 해당 이름의 예약이 없습니다.");
            return;
        }

        System.out.println("\n[예약 내역]");
        for (int i = 0; i < foundReservations.length; i++) {
            Reservation r = foundReservations[i];
            System.out.println((i + 1) + ". " +
            		r.getReservationTime() +"에 "+
            		r.getTheater() +"관에서 "+
                    r.getSeat().getRow() + "열 " +
                    r.getSeat().getNumber() + "좌석을 예약했습니다"+
                    " \"예약 번호 " + (i+1)+"번\"");
        }

        System.out.print("취소할 예약 번호 선택: ");
        int idx = scanner.nextInt();
        scanner.nextLine();

        if (idx < 1 || idx > foundReservations.length) {
            System.out.println("[취소 실패] 잘못된 번호입니다.");
            return;
        }

        Reservation toCancel = foundReservations[idx - 1];
        reservationService.cancelReservation(toCancel);

        System.out.println("[예약 취소 완료] " +
                toCancel.getSeat().getRow() + "열 " +
                toCancel.getSeat().getNumber() + "번 좌석");
    }

    private void showSeatMap(Seat[][] seats) {
        System.out.println("\n[좌석 배치도]");
        for (int row = 0; row < seats.length; row++) {
            System.out.print((row + 1) + "열: ");
            for (int col = 0; col < seats[row].length; col++) {
                Seat seat = seats[row][col];
                String symbol;
                if (seat.isReserved()) {
                    symbol = "X";
                } else if (seat.isVIP()) {
                    symbol = "V";
                } else {
                    symbol = "O";
                }
                System.out.print(symbol + " ");
            }
            System.out.println();
        }
    }
}
