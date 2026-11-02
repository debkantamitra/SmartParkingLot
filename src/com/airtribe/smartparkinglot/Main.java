package com.airtribe.smartparkinglot;

import com.airtribe.smartparkinglot.entity.ParkingReceipt;
import com.airtribe.smartparkinglot.entity.ParkingSpot;
import com.airtribe.smartparkinglot.entity.ParkingTicket;
import com.airtribe.smartparkinglot.entity.SpotType;
import com.airtribe.smartparkinglot.entity.Vehicle;
import com.airtribe.smartparkinglot.entity.VehicleType;
import com.airtribe.smartparkinglot.repository.InMemoryRepository;
import com.airtribe.smartparkinglot.repository.Repository;
import com.airtribe.smartparkinglot.service.ParkingLotService;
import com.airtribe.smartparkinglot.service.allocation.BestFitStrategy;

import java.time.Clock;
import java.util.Scanner;

public class Main {
    private final ParkingLotService parkingLot;
    private final Scanner scanner;

    public Main(ParkingLotService parkingLot, Scanner scanner) {
        this.parkingLot = parkingLot;
        this.scanner = scanner;
    }

    public static void main(String[] args) {
        Main app = new Main(createParkingLot(), new Scanner(System.in));
        System.out.println("Welcome to Smart Parking Lot");
        app.run();
    }

    private static ParkingLotService createParkingLot() {
        Repository<ParkingSpot> spots = new InMemoryRepository<>();
        addSpot(spots, new ParkingSpot("F1-M1", 1, 1, SpotType.MOTORCYCLE));
        addSpot(spots, new ParkingSpot("F1-C1", 1, 2, SpotType.COMPACT));
        addSpot(spots, new ParkingSpot("F1-C2", 1, 3, SpotType.COMPACT));
        addSpot(spots, new ParkingSpot("F1-L1", 1, 4, SpotType.LARGE));
        addSpot(spots, new ParkingSpot("F2-L1", 2, 1, SpotType.LARGE));

        return new ParkingLotService(
                spots,
                new InMemoryRepository<>(),
                new BestFitStrategy(),
                Clock.systemUTC()
        );
    }

    private static void addSpot(Repository<ParkingSpot> repository, ParkingSpot spot) {
        repository.add(spot.getId(), spot);
    }

    private void run() {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = readText("Choose an option: ");

            try {
                switch (choice) {
                    case "1" -> checkIn();
                    case "2" -> checkOut();
                    case "3" -> showAvailability();
                    case "0" -> running = false;
                    default -> System.out.println("Please choose a valid option.");
                }
            } catch (RuntimeException exception) {
                System.out.println("Could not complete action: " + exception.getMessage());
            }
        }
        System.out.println("Goodbye.");
    }

    private void printMenu() {
        System.out.println("\n1. Check in vehicle");
        System.out.println("2. Check out vehicle");
        System.out.println("3. View availability");
        System.out.println("0. Exit");
    }

    private void checkIn() {
        String plate = readText("License plate: ");
        String typeText = readText("Vehicle type (MOTORCYCLE/CAR/BUS): ");
        Vehicle vehicle = new Vehicle(plate, VehicleType.valueOf(typeText.toUpperCase()));
        ParkingTicket ticket = parkingLot.checkIn(vehicle);
        System.out.println("Ticket " + ticket.id() + " assigned spot " + ticket.spotId());
    }

    private void checkOut() {
        String ticketId = readText("Ticket ID: ").toUpperCase();
        ParkingReceipt receipt = parkingLot.checkOut(ticketId);
        System.out.println("Checked out " + receipt.licensePlate()
                + "; duration " + receipt.duration().toMinutes() + " minute(s)"
                + "; fee ₹" + receipt.fee());
    }

    private void showAvailability() {
        parkingLot.availableSpots()
                .forEach((type, count) -> System.out.println(type + ": " + count));
    }

    private String readText(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
