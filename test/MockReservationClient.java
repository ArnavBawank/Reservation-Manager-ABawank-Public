/**
* @author Daniel Gong (gong256), Krishna Vijay
* @version 2025-12-06
* Mock implementation of the ReservationClient for testing purposes.
*/
public class MockReservationClient extends ReservationClient {
    // configurable responses for testing
    public String listOpenResponse = "OK T1:4:Main,T2:6:Patio";
    public String bookResponse = "OK 12345678 T1";
    public String cancelResponse = "OK cancelled";
    public String listMyReservationsResponse = "OK 12345678:T1:BOOKED,87654321:T2:BOOKED";

    public MockReservationClient() { }
    public String login(String e, String p) { return "OK 123"; }
    public String createAccount(String e, String p) { return "OK"; }
    public String logout() { return "OK"; }
    public String listOpen(String dateTime, int partySize) { return listOpenResponse; }
    public String book(String dateTime, int partySize) { return bookResponse; }
    public String cancel(int reservationId) { return cancelResponse; }
    public String listMyReservations() { return listMyReservationsResponse; }
    public String deleteAccount() { return "OK deleted"; }
}