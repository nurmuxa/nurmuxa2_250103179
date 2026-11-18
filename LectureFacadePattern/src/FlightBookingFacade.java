
public class FlightBookingFacade {
    private final SeatInventorySystem seat;
    private final BaggagePricingEngine baggage;
    private final CurrencyConverter curr;
    private final PaymentProcessor payp;
    private final TicketIssuer ticket;

    public FlightBookingFacade() {
        this.seat = new SeatInventorySystem();
        this.baggage = new BaggagePricingEngine();
        this.curr = new CurrencyConverter();
        this.payp = new PaymentProcessor();
        this.ticket = new TicketIssuer();
    }

    public String bookFlight(String passengerName,
                             String flightCode,
                             String seatNumber,
                             double baseFareUSD,
                             int bagCount,
                             double bagWeightKg,
                             String currency,
                             String cardNumber) {

        String lockToken = null;

        try {
            seat.connectToReservationServer();

            lockToken = seat.lockSeat(flightCode, seatNumber, passengerName);

            double baggageFee = baggage.calculateBaggageFeeUSD(bagCount, bagWeightKg);
            double totalUSD = baseFareUSD + baggageFee;

            double convertedTotal = curr.convertFromUSD(totalUSD, currency);

            payp.initializeGateway();

            String receiptId = payp.chargeCard(cardNumber, convertedTotal, currency);

            return ticket.generateTicket(passengerName, flightCode, lockToken, receiptId);

        } catch (RuntimeException ex) {
            if (lockToken != null) {
                seat.releaseSeatLock(lockToken);
            }

            System.out.println("Booking failed: " + ex.getMessage());
            return "BOOKING_FAILED: " + ex.getMessage();

        } finally {
            seat.disconnect();
        }
    }
}
