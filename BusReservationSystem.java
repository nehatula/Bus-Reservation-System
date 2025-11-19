import java.io.*;
import java.util.Scanner;

class Bus {
    String busNumber;
    String from;
    String to;
    int totalSeats;
    int bookedSeats;

    public Bus(String busNumber, String from, String to, int totalSeats) {
        this.busNumber = busNumber;
        this.from = from;
        this.to = to;
        this.totalSeats = totalSeats;
        this.bookedSeats = 0;
    }

    public boolean bookTicket(int seats) {
        if (bookedSeats + seats <= totalSeats) {
            bookedSeats += seats;
            return true;
        }
        return false;
    }

    public boolean cancelTicket(int seats) {
        if (bookedSeats >= seats) {
            bookedSeats -= seats;
            return true;
        }
        return false;
    }

    public int availableSeats() {
        return totalSeats - bookedSeats;
    }

    public String toString() {
        return "Bus " + busNumber + " from " + from + " to " + to +
               " | Total Seats: " + totalSeats + " | Available: " + availableSeats();
    }
}

public class BusReservationSystem {
    Bus[] buses = new Bus[10]; // fixed-size array
    int busCount = 0;
    final String FILE_NAME = "bus_data.txt";

    public static void main(String[] args) {
        BusReservationSystem system = new BusReservationSystem();

        system.loadBusesFromFile();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Bus Reservation Menu ---");
            System.out.println("1. View Buses");
            System.out.println("2. Book Ticket");
            System.out.println("3. Cancel Ticket");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            try {
                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        system.viewBuses();
                        break;
                    case 2:
                        system.bookTicket(sc);
                        break;
                    case 3:
                        system.cancelTicket(sc);
                        break;
                    case 4:
                        system.saveBusesToFile();
                        System.out.println("Exiting... Thank you!");
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (java.util.InputMismatchException ime) {
                System.out.println("InputMismatchException: Please enter a valid number.");
                sc.nextLine();
            } catch (Exception e) {
                System.out.println("Exception: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            }
        }
    }

    void viewBuses() {
        for (int i = 0; i < busCount; i++) {
            System.out.println(buses[i]);
        }
    }

    void bookTicket(Scanner sc) {
        System.out.print("Enter Bus Number: ");
        String busNum = sc.next();
        Bus bus = findBus(busNum);
        if (bus != null) {
            try {
                System.out.print("Enter number of seats to book: ");
                int seats = sc.nextInt();
                if (bus.bookTicket(seats)) {
                    System.out.println("Booking successful!");
                } else {
                    System.out.println("Not enough seats available.");
                }
            } catch (java.util.InputMismatchException ime) {
                System.out.println("InputMismatchException: Invalid seat number.");
                sc.nextLine();
            }
        } else {
            System.out.println("Bus not found.");
        }
    }

    void cancelTicket(Scanner sc) {
        System.out.print("Enter Bus Number: ");
        String busNum = sc.next();
        Bus bus = findBus(busNum);
        if (bus != null) {
            try {
                System.out.print("Enter number of seats to cancel: ");
                int seats = sc.nextInt();
                if (bus.cancelTicket(seats)) {
                    System.out.println("Cancellation successful!");
                } else {
                    System.out.println("Invalid cancellation request.");
                }
            } catch (java.util.InputMismatchException ime) {
                System.out.println("InputMismatchException: Invalid seat number.");
                sc.nextLine();
            }
        } else {
            System.out.println("Bus not found.");
        }
    }

    Bus findBus(String busNumber) {
        for (int i = 0; i < busCount; i++) {
            if (buses[i].busNumber.equalsIgnoreCase(busNumber)) {
                return buses[i];
            }
        }
        return null;
    }

    // UPDATED METHOD WITH 3 DEFAULT BUSES
    void loadBusesFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null && busCount < buses.length) {
                String[] parts = line.split(",");
                Bus bus = new Bus(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
                bus.bookedSeats = Integer.parseInt(parts[4]);
                buses[busCount++] = bus;
            }
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException: No previous data found. Starting with default buses.");

            // 3 DEFAULT BUSES
            buses[busCount++] = new Bus("B1", "Warangal", "Hyderabad", 40);
            buses[busCount++] = new Bus("B2", "Warangal", "Karimnagar", 35);
            buses[busCount++] = new Bus("B3", "Hyderabad", "Warangal", 45);

        } catch (IOException ioe) {
            System.out.println("IOException: Error reading file.");
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException: Invalid number format in file.");
        }
    }

    void saveBusesToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < busCount; i++) {
                Bus bus = buses[i];
                bw.write(bus.busNumber + "," + bus.from + "," + bus.to + "," +
                         bus.totalSeats + "," + bus.bookedSeats);
                bw.newLine();
            }
        } catch (IOException ioe) {
            System.out.println("IOException: Error saving data.");
        }
    }
}
