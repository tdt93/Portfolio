package swing.sample;

import org.apache.log4j.Logger; 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import java.awt.*;
import javax.swing.DefaultListModel;

public class sqliteDB {
	Connection conn = null;
	Statement stat = null;
	base ref;
	private static Logger log = Logger.getLogger(base.class.getName());	
	
	sqliteDB(base _ref){
		ref = _ref;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:./Properties/FlightDB.sqlite");	
			stat = conn.createStatement();
			log.debug("Connection to databases is established");
			//closeConnection();
		}
		catch(Exception e) {
			log.error(e.getMessage(), e);
			System.exit(1);
		}
	}
	
	//Closing databases:
	public void closeConnection() {
		try {
			conn.close();
			log.debug("Connection to databases was closed");
		}
		catch(SQLException ex) {
			log.error(ex.getMessage(), ex);
		}
	}
	
	public void showDatesList(int flightId) {
		ref.sqlDates.clear();
		try {			
			ResultSet rsDates = stat.executeQuery("select * from Dates WHERE flight_id = " + flightId + ";");
			// add query to show list of flights
			while(rsDates.next()) {
				ref.sqlDates.add("" + rsDates.getString("date") + "  " + rsDates.getString("departure_time") + " - " + rsDates.getString("arrival_time"));
				log.debug("Updating dates table");
			}
			stat.close();
		}
		catch(SQLException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public void showFlightsList() {
		ref.sqlFlights.clear();
		ref.sqlFlight_id.clear();
		try {			
			ResultSet rsFlights = stat.executeQuery("select * from Flights");
			// add query to show list of flights
			while(rsFlights.next()) {
				ref.sqlFlight_id.add(rsFlights.getInt("flight_id"));
				ref.sqlFlights.add(rsFlights.getString("source"));
				ref.sqlFlights.add(rsFlights.getString("destination"));
				log.debug("Updating flights table");
				log.debug("Updating flight_id table");
			}
			stat.close();
		}
		catch(SQLException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public void showBookingList() {
		ref.sqlBooking_id.clear();
		ref.sqlBooking.clear();
		ref.sqlSubscriber.clear();
		try {
			ResultSet rsBooking = stat.executeQuery("select * from Clients");
			while (rsBooking.next()) {
				ref.sqlBooking_id.add(rsBooking.getInt("userID"));				
				ref.sqlBooking.add(rsBooking.getString("email"));
				ref.sqlSubscriber.add(rsBooking.getInt("newsletter"));
				log.debug("Updating booking table");				
			}
			stat.close();
		}
		catch(SQLException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	// Booking ticket:
	public void bookingTicket(String name, String secondName, String lastName, String email, int ticketAmount, boolean newsLetter, int flightId, String date) {
		try{
			// add data to Clients table
			String dbquery1 = "INSERT INTO Clients (name, secondName, lastName, email, ticketAmount, newsletter)" + "VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement pstat = conn.prepareStatement(dbquery1);
			pstat.setString(1, name);
			pstat.setString(2, secondName);
			pstat.setString(3, lastName);
			pstat.setString(4, email);
			pstat.setInt(5, ticketAmount);
			pstat.setBoolean(6, newsLetter);
			pstat.executeUpdate();
			
			// add data to BookingList table
			ResultSet rs = stat.executeQuery("select max(userID) from Clients");
			int id = rs.getInt("max(userID)");
			stat.close();
			String dbquery2 = "INSERT INTO ClientsFlights (user_id, flight_id, date)" + "VALUES (?, ?, ?)";
			pstat = conn.prepareStatement(dbquery2);
			pstat.setInt(1, id);
			pstat.setInt(2, flightId);
			pstat.setString(3, date);
			pstat.executeUpdate();
			pstat.close();
		}
		catch(SQLException e){
			log.error(e.getMessage(), e);
		}
		
	}
	
	// Delete ticker order from BookingList table
	public void cancelTicket(int num) {
		try {
			String query = "DELETE FROM Clients WHERE userID = " + num;
			
			stat.executeUpdate(query);
			stat.close();
		}
		catch(SQLException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	// add flights (admin only) add data to FlightSource, FlightDestination, Aircraft tables
	public void addFlight(String source, String destination, String date, String departure, String arrival) {
		try {
			// add to FlightSource table:
			
			String query1 = "INSERT INTO Flights (source, destination)" + "VALUES ( ?, ?)";
			PreparedStatement pstat = conn.prepareStatement(query1);
			pstat.setString(1, source);
			pstat.setString(2, destination);
			pstat.executeUpdate();		
			
			ResultSet rs = stat.executeQuery("select max(flight_id) from Flights");
			int id = rs.getInt("max(flight_id)");
			stat.close();
			// add to FlightDestination table:
			String query2 = "INSERT INTO Dates (flight_id, date, departure_time, arrival_time)" + "VALUES (?, ?, ?, ?)";
			pstat = conn.prepareStatement(query2);
			pstat.setInt(1, id);
			pstat.setString(2, date);
			pstat.setString(3, departure);
			pstat.setString(4, arrival);
			pstat.executeUpdate();
			pstat.close();
		}
		catch(SQLException e) {
			log.error(e.getMessage(), e);
		}	
	}
	
	public void addDate(int flight_id, String date, String departure, String arrival) {
		try {
			// add to Dates table:
			String query2 = "INSERT INTO Dates (flight_id, date, departure_time, arrival_time)" + "VALUES (?, ?, ?, ?)";
			PreparedStatement pstat = conn.prepareStatement(query2);
			pstat.setInt(1, flight_id);
			pstat.setString(2, date);
			pstat.setString(3, departure);
			pstat.setString(4, arrival);
			pstat.executeUpdate();
			pstat.close();
		}
		catch(SQLException e) {
			log.error(e.getMessage(), e);
		}	
	}
	
	public void deleteFlight(int flight_id) {
		try {
			String query2 = "DELETE FROM Dates WHERE flight_id = " + flight_id;
			String query1 = "DELETE FROM Flights WHERE flight_id = " + flight_id;
			stat.executeUpdate(query1);
			stat.executeUpdate(query2);
			stat.close();						
		}
		catch(SQLException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	// remove flights: delete data from FlightSource, FlightDestination
	public void deleteDate(int flight_id, String date) {
		try {
			String query1 = "DELETE FROM ClientsFlights WHERE flight_id = " + flight_id + " AND  date =" + date ;
			String query2 = "DELETE FROM Dates WHERE flight_id = " + flight_id + " AND  date =" + date ;
			stat.executeUpdate(query1);
			stat.executeUpdate(query2);
			stat.close();						
		}
		catch(SQLException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	
	
}
