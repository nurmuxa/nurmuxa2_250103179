
public class Main {
    public static void main(String[] args) {

        FlightBookingFacade facade = new FlightBookingFacade();

        System.out.println("=== TEST 1: Successful Booking ===");

        String ticket1 = facade.bookFlight(
                "Amina Suleimenova",
                "KZ204",
                "14B",
                320.00,
                2,
                24.5,
                "EUR",
                "4532015112830366"
        );

        System.out.println("Result 1: " + ticket1 + "\n");


        System.out.println("=== TEST 2: Blocked Seat ===");

        String ticket2 = facade.bookFlight(
                "Daniyar Kassymov",
                "KZ204",
                "13A",
                320.00,
                1,
                15.0,
                "USD",
                "4532015112830366"
        );

        System.out.println("Result 2: " + ticket2 + "\n");


        System.out.println("=== TEST 3: Payment Failure & Rollback ===");

        String ticket3 = facade.bookFlight(
                "Timur Bekov",
                "KZ204",
                "08C",
                320.00,
                1,
                18.0,
                "KZT",
                "1234"
        );

        System.out.println("Result 3: " + ticket3 + "\n");
    }
}
