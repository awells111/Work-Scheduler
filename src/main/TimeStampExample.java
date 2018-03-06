package main;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;




public class TimeStampExample {

	static final String serverName = "52.206.157.109";
	static final String dbName = "U03QIu";
	static final String userName = "U03QIu";
	static final String passwd = "53688051379";
	static final String url = "jdbc:mysql://"+serverName+"/"+dbName;

	public static void main(String[] args) {


		//Getting the LocalDateTime Objects from String values
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm"); 
		String txtStartTime = "2017-03-29 12:00";

		LocalDateTime ldtStart = LocalDateTime.parse(txtStartTime, df);


		//Showing how to parse the Date/Time String
		DateTimeFormatter dFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
		LocalDate localDate = LocalDate.parse(txtStartTime.substring(0, 10), dFormatter);
		System.out.println("The local date is " + localDate);
		
		DateTimeFormatter tFormatter = DateTimeFormatter.ofPattern("kk:mm"); 
		LocalTime localTime = LocalTime.parse(txtStartTime.substring(11), tFormatter);
		System.out.println("The local time is " + localTime);
		
		//Getting the day of the week
		System.out.println(ldtStart.getDayOfWeek());
		
		//Convert to a ZonedDate Time in UTC
		ZoneId zid = ZoneId.systemDefault();

		ZonedDateTime zdtStart = ldtStart.atZone(zid);
		System.out.println("Local Time: " + zdtStart);
		ZonedDateTime utcStart = zdtStart.withZoneSameInstant(ZoneId.of("UTC"));
		System.out.println("Zoned time: " + utcStart);
		ldtStart = utcStart.toLocalDateTime();
		System.out.println("Zoned time with zone stripped:" + ldtStart);
		//Create Timestamp values from Instants to update database
		Timestamp startsqlts = Timestamp.valueOf(ldtStart); //this value can be inserted into database
		System.out.println("Timestamp to be inserted: " +startsqlts);

		//insertDB(startsqlts);

		ResultSet rs = null;
		try {
			Connection c = connectDB();
			Statement stmt = c.createStatement();
			String sql;
			sql = "SELECT start FROM appointment where appointmentid = 46";
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//to retrieve from database

		try {
			while(rs.next()) {
				Timestamp tsStart = rs.getTimestamp("start");

				ZoneId newzid = ZoneId.systemDefault();

				ZonedDateTime newzdtStart = tsStart.toLocalDateTime().atZone(ZoneId.of("UTC"));

				ZonedDateTime newLocalStart = newzdtStart.withZoneSameInstant(newzid);
				
				System.out.println("From db in UTC: " + newzdtStart);
				System.out.println("From db in local time: " + newLocalStart);

			}
		} catch (SQLException e) {
			System.err.println(e);
		}


	}

	private static Connection connectDB() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url,userName,passwd);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();

		}

		return conn;
	}

	private static void insertDB(Timestamp t) {
		try {
			Connection c = connectDB();
			String query = "INSERT INTO appointment VALUES (46,?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement sql = c.prepareStatement(query);
			sql.setInt(1, 12);
			sql.setString(2, "");
			sql.setString(3, "");
			sql.setString(4, "");
			sql.setString(5, "");
			sql.setString(6, "");
			sql.setTimestamp(7, t);
			sql.setTimestamp(8, t);
			sql.setTimestamp(9, t);
			sql.setString(10, "");
			sql.setTimestamp(11, t);
			sql.setString(12, "");
			sql.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
