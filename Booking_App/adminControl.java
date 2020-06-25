package swing.sample;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import java.lang.String;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class adminControl extends JFrame{
	
	private static Logger log = Logger.getLogger(base.class.getName());
	private JPanel contentPane;
	private JLabel lblBooking;
	private JLabel lblFlights;
	private JLabel lblDates;
	private JPanel right;
	private JPanel middle;
	private JButton btnAddDate;
	private JButton btnChangeDate;
	private JButton btnDeleteDate;
	private JButton btnDeleteFlight;
	private JButton btnRemoveClient;
	private JTextField dateField;
	private JTextField sourceField;
	private JButton btnAddFlight;
	private JTextField destinationField;
	private JLabel lblSource;
	private JLabel lblDestination;
	private JSpinner depHour;
	private JSpinner depMinutes;
	private JSpinner arrHour;
	private JSpinner arrMinutes;
	private JLabel lblNewLabel;
	private JLabel lblDepartureTime;
	private JLabel lblArrivalTime;
	private JPanel panel_4;
	private JPanel panel_3;
	private SpinnerNumberModel modeltau;
	
	
	sqliteDB db;
	static base ref1 = null;
	
	public List<String> sqlDates = new ArrayList<String>();
	public List<String> sqlFlights = new ArrayList<String>();
	public List<Integer> sqlFlight_id = new ArrayList<Integer>();
	public List<Integer> sqlBooking_id = new ArrayList<Integer>();
	
	public static void main(String[] args) {
		BasicConfigurator.configure();
		
		EventQueue.invokeLater(new Runnable() {			
			public void run() {
				try {
					ref1 = new base("asd");
					adminControl adminFrame = new adminControl(ref1);									
					adminFrame.setLocationRelativeTo(null);
					adminFrame.setVisible(true);
				} catch (Exception e) {
					log.error(e.getMessage(),e);
				}
			}
		});
		
	}

	MouseListener myMouseListener = new MouseListener() {
		public void mouseClicked(MouseEvent e) {
			//System.out.println("[MouseListener] Clicked! Button = "+((JButton) e.getSource()).getText());
		}

		public void mouseEntered(MouseEvent e) {
			//System.out.println("[MouseListener] Entered! Button = "+((JButton) e.getSource()).getText());
		}

		public void mouseExited(MouseEvent e) {
			//System.out.println("[MouseListener] Exited! Button = "+((JButton) e.getSource()).getText());
		}

		public void mousePressed(MouseEvent e) {
			//System.out.println("[MouseListener] Pressed! Button = "+((JButton) e.getSource()).getText());
		}

		public void mouseReleased(MouseEvent e) {
			if (e.getSource().equals(ref1.datesList));
			else ref1.UpdateDates();
		}
		
	};
	
	
	public adminControl(base _ref) {
		ref1 = _ref;
		db = new sqliteDB(ref1);
		
		ActionListener myActionListener = new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			System.out.println("[ActionListener] Button = "+e.getActionCommand());
    			int index;
    			switch(e.getActionCommand())
    			{           			
       				case "Add Date":
       					log.info("Adding new date of flight");
       					index = ref1.flightsList.getSelectedIndex();
           				db.addDate(ref1.sqlFlight_id.get(index), dateField.getText(), "" + depHour.getValue() + ":" + depMinutes.getValue(), "" + arrHour.getValue() + ":" + arrMinutes.getValue());
           				ref1.UpdateFlights();
           				ref1.UpdateDates();
           			break;
           			
           			case "Delete date":
           				index = ref1.flightsList.getSelectedIndex();
           				int indexDate = ref1.datesList.getSelectedIndex();
           				String date = ref1.sqlDates.get(indexDate);
           				//date.trim();
           				date = date.substring(0, Math.min(date.length(), 10));
           				System.out.println(date); System.out.println(ref1.sqlFlight_id.get(index));
           				db.deleteDate(ref1.sqlFlight_id.get(index), date);
           				ref1.UpdateFlights();
           				ref1.UpdateDates();
           			break;
           			
           			case "Refresh":
           				ref1.UpdateBookings();
           				ref1.UpdateFlights();
           				ref1.UpdateDates();
           			break;
           			
           			case "Add Flight":
           				log.info("Adding new flight connection");
           				db.addFlight(sourceField.getText(), destinationField.getText(), dateField.getText(), "" + depHour.getValue() + ":" + depMinutes.getValue(), "" + arrHour.getValue() + ":" + arrMinutes.getValue());
           				ref1.UpdateFlights();
           				ref1.UpdateDates();
           			break;
           			
           			case "Delete flight":
           				index = ref1.flightsList.getSelectedIndex();
           				db.deleteFlight(ref1.sqlFlight_id.get(index));
           				log.info("Deleting flight connection at index " + ref1.flightsList.getSelectedIndex());
           				ref1.UpdateFlights();
           				ref1.UpdateDates();
               		break;
               		
           			case "Remove Client":
           				index = ref1.bookingList.getSelectedIndex();
           				db.cancelTicket(ref1.sqlBooking_id.get(index));
           				log.info("Remove clinet at index " + ref1.bookingList.getSelectedIndex());
           				ref1.UpdateBookings();
               		break;

    			}
    		}    		
    	};
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1050, 539);
						
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));				
		
		middle = new JPanel();
		contentPane.add(middle, BorderLayout.CENTER);
		GridBagLayout gbl_middle = new GridBagLayout();
		gbl_middle.columnWidths = new int[]{337, 292, 0};
		gbl_middle.rowHeights = new int[]{26, 0, 0};
		gbl_middle.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_middle.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		middle.setLayout(gbl_middle);
		
		lblBooking = new JLabel("Bookings");
		GridBagConstraints gbc_lblBooking = new GridBagConstraints();
		gbc_lblBooking.fill = GridBagConstraints.VERTICAL;
		gbc_lblBooking.insets = new Insets(0, 0, 5, 5);
		gbc_lblBooking.gridx = 0;
		gbc_lblBooking.gridy = 0;
		middle.add(lblBooking, gbc_lblBooking);
		
		GridBagConstraints gbc_BookingList = new GridBagConstraints();
		gbc_BookingList.insets = new Insets(0, 0, 0, 5);
		gbc_BookingList.fill = GridBagConstraints.BOTH;
		gbc_BookingList.gridx = 0;
		gbc_BookingList.gridy = 1;
		middle.add(ref1.bookingList, gbc_BookingList); 
		
		lblFlights = new JLabel("Flights");
		GridBagConstraints gbc_lblFlights = new GridBagConstraints();
		gbc_lblFlights.insets = new Insets(0, 0, 5, 5);
		gbc_lblFlights.gridx = 1;
		gbc_lblFlights.gridy = 0;
		middle.add(lblFlights, gbc_lblFlights);
		
		lblDates = new JLabel("Dates");
		GridBagConstraints gbc_lblDates = new GridBagConstraints();
		gbc_lblDates.insets = new Insets(0, 0, 5, 0);
		gbc_lblDates.gridx = 2;
		gbc_lblDates.gridy = 0;
		middle.add(lblDates, gbc_lblDates);
				
		GridBagConstraints gbc_flightsList = new GridBagConstraints();
		gbc_flightsList.insets = new Insets(0, 0, 0, 5);
		gbc_flightsList.fill = GridBagConstraints.BOTH;
		gbc_flightsList.gridx = 1;
		gbc_flightsList.gridy = 1;
		middle.add(ref1.flightsList, gbc_flightsList);
		
		
		GridBagConstraints gbc_datesList = new GridBagConstraints();
		gbc_datesList.insets = new Insets(0, 0, 0, 5);
		gbc_datesList.fill = GridBagConstraints.BOTH;
		gbc_datesList.gridx = 2;
		gbc_datesList.gridy = 1;
		middle.add(ref1.datesList, gbc_datesList);
		
		
		right = new JPanel();
		contentPane.add(right, BorderLayout.EAST);
		GridBagLayout gbl_right = new GridBagLayout();
		gbl_right.columnWidths = new int[]{0, 0, 0};
		gbl_right.rowHeights = new int[]{0, 0, 0, 26, 26, 0, 0, 0, 0, 0, 0};
		gbl_right.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_right.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		right.setLayout(gbl_right);
		
		lblSource = new JLabel("Source");
		GridBagConstraints gbc_lblSource = new GridBagConstraints();
		gbc_lblSource.insets = new Insets(0, 0, 5, 5);
		gbc_lblSource.anchor = GridBagConstraints.EAST;
		gbc_lblSource.gridx = 0;
		gbc_lblSource.gridy = 1;
		right.add(lblSource, gbc_lblSource);
		
		sourceField = new JTextField();
		GridBagConstraints gbc_sourceField = new GridBagConstraints();
		gbc_sourceField.insets = new Insets(0, 0, 5, 0);
		gbc_sourceField.fill = GridBagConstraints.HORIZONTAL;
		gbc_sourceField.gridx = 1;
		gbc_sourceField.gridy = 1;
		right.add(sourceField, gbc_sourceField);
		sourceField.setColumns(10);
		
		lblDestination = new JLabel("Destination");
		GridBagConstraints gbc_lblDestination = new GridBagConstraints();
		gbc_lblDestination.insets = new Insets(0, 0, 5, 5);
		gbc_lblDestination.anchor = GridBagConstraints.EAST;
		gbc_lblDestination.gridx = 0;
		gbc_lblDestination.gridy = 2;
		right.add(lblDestination, gbc_lblDestination);
		
		destinationField = new JTextField();
		destinationField.setColumns(10);
		GridBagConstraints gbc_destinationField = new GridBagConstraints();
		gbc_destinationField.insets = new Insets(0, 0, 5, 0);
		gbc_destinationField.fill = GridBagConstraints.HORIZONTAL;
		gbc_destinationField.gridx = 1;
		gbc_destinationField.gridy = 2;
		right.add(destinationField, gbc_destinationField);
		
		lblNewLabel = new JLabel("Departure date");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 3;
		right.add(lblNewLabel, gbc_lblNewLabel);
		
		dateField = new JTextField();
		GridBagConstraints gbc_dateField = new GridBagConstraints();
		gbc_dateField.insets = new Insets(0, 0, 5, 0);
		gbc_dateField.fill = GridBagConstraints.HORIZONTAL;
		gbc_dateField.gridx = 1;
		gbc_dateField.gridy = 3;
		right.add(dateField, gbc_dateField);
		dateField.setColumns(10);
		
		lblDepartureTime = new JLabel("Departure time");
		GridBagConstraints gbc_lblDepartureTime = new GridBagConstraints();
		gbc_lblDepartureTime.anchor = GridBagConstraints.EAST;
		gbc_lblDepartureTime.insets = new Insets(0, 0, 5, 5);
		gbc_lblDepartureTime.gridx = 0;
		gbc_lblDepartureTime.gridy = 4;
		right.add(lblDepartureTime, gbc_lblDepartureTime);
		
		panel_3 = new JPanel();	
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.insets = new Insets(0, 0, 5, 0);
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 1;
		gbc_panel_3.gridy = 4;
		right.add(panel_3, gbc_panel_3);
		panel_3.setLayout(new GridLayout(0, 2, 0, 0));
		
		depHour = new JSpinner();
		modeltau = new SpinnerNumberModel(0, 0, 23, 1);
		depHour.setModel(modeltau);
		panel_3.add(depHour);
		
		depMinutes = new JSpinner();
		modeltau = new SpinnerNumberModel(0, 0, 59, 1);
		depMinutes.setModel(modeltau);
		panel_3.add(depMinutes);
		
		lblArrivalTime = new JLabel("Arrival time");
		GridBagConstraints gbc_lblArrivalTime = new GridBagConstraints();
		gbc_lblArrivalTime.anchor = GridBagConstraints.EAST;
		gbc_lblArrivalTime.insets = new Insets(0, 0, 5, 5);
		gbc_lblArrivalTime.gridx = 0;
		gbc_lblArrivalTime.gridy = 5;
		right.add(lblArrivalTime, gbc_lblArrivalTime);
		
		panel_4 = new JPanel();
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.insets = new Insets(0, 0, 5, 0);
		gbc_panel_4.fill = GridBagConstraints.BOTH;
		gbc_panel_4.gridx = 1;
		gbc_panel_4.gridy = 5;
		right.add(panel_4, gbc_panel_4);
		panel_4.setLayout(new GridLayout(0, 2, 0, 0));
		
		arrHour = new JSpinner();
		modeltau = new SpinnerNumberModel(0, 0, 23, 1);
		arrHour.setModel(modeltau);
		panel_4.add(arrHour);
		
		arrMinutes = new JSpinner();
		modeltau = new SpinnerNumberModel(0, 0, 59, 1);
		arrMinutes.setModel(modeltau);
		panel_4.add(arrMinutes);
		
		btnDeleteFlight = new JButton("Delete flight");
		GridBagConstraints gbc_btnDeleteFlight = new GridBagConstraints();
		gbc_btnDeleteFlight.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnDeleteFlight.insets = new Insets(0, 0, 5, 5);
		gbc_btnDeleteFlight.gridx = 0;
		gbc_btnDeleteFlight.gridy = 6;
		right.add(btnDeleteFlight, gbc_btnDeleteFlight);
		btnDeleteFlight.addActionListener(myActionListener);
		
		btnAddFlight = new JButton("Add Flight");
		GridBagConstraints gbc_btnAddFlight = new GridBagConstraints();
		gbc_btnAddFlight.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAddFlight.insets = new Insets(0, 0, 5, 0);
		gbc_btnAddFlight.gridx = 1;
		gbc_btnAddFlight.gridy = 6;
		right.add(btnAddFlight, gbc_btnAddFlight);
		btnAddFlight.addActionListener(myActionListener);
		
		btnDeleteDate = new JButton("Delete date");
		GridBagConstraints gbc_btnDeleteDate = new GridBagConstraints();
		gbc_btnDeleteDate.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnDeleteDate.insets = new Insets(0, 0, 5, 5);
		gbc_btnDeleteDate.gridx = 0;
		gbc_btnDeleteDate.gridy = 7;
		right.add(btnDeleteDate, gbc_btnDeleteDate);
		btnDeleteDate.addActionListener(myActionListener);
		
		btnAddDate = new JButton("Add Date");
		GridBagConstraints gbc_btnAddDate = new GridBagConstraints();
		gbc_btnAddDate.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAddDate.insets = new Insets(0, 0, 5, 0);
		gbc_btnAddDate.gridx = 1;
		gbc_btnAddDate.gridy = 7;
		right.add(btnAddDate, gbc_btnAddDate);
		btnAddDate.addActionListener(myActionListener);
		
		btnChangeDate = new JButton("Refresh");
		GridBagConstraints gbc_btnChangeDate = new GridBagConstraints();
		gbc_btnChangeDate.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnChangeDate.insets = new Insets(0, 0, 5, 5);
		gbc_btnChangeDate.gridx = 1;
		gbc_btnChangeDate.gridy = 8;
		right.add(btnChangeDate, gbc_btnChangeDate);
		btnChangeDate.addActionListener(myActionListener);
		
		btnRemoveClient = new JButton("Remove Client");
		GridBagConstraints gbc_btnRemoveClient = new GridBagConstraints();
		gbc_btnRemoveClient.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnRemoveClient.insets = new Insets(0, 0, 5, 5);
		gbc_btnRemoveClient.gridx = 0;
		gbc_btnRemoveClient.gridy = 8;
		right.add(btnRemoveClient, gbc_btnRemoveClient);
		btnRemoveClient.addActionListener(myActionListener);
		
		ref1.UpdateBookings();
		ref1.UpdateFlights();
		ref1.UpdateDates();
		
		
	}
	
	
}
