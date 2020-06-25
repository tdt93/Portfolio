package swing.sample;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;  			//log4j
import java.sql.*;							//sql

import java.lang.String;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

//import com.sun.org.apache.xpath.internal.operations.Bool;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridLayout;
import java.awt.Component;

public class base extends JFrame {

	private JPanel contentPane;
	private JTextField surnameField;
	private JTextField nameField;
	private JButton btnBookIt;
	private JLabel lblMail;
	private JTextField mailField;
	private JMenuBar menuBar;
	private JMenu mnNewMenu;
	private JMenuItem mntmNew;
	private JMenuItem mntmOpen;
	private JMenuItem mntmSave;
	private JLabel lblNumOfTickets;
	private JSeparator separator;
	private JMenuItem mntmQuit;
	private JSpinner numOfTicketsSpinner;
	private JPanel middle;
	public JList<String> datesList;
	public JList<String> flightsList;
	public JList<String> bookingList;
	private JLabel lblFlights;
	private JLabel lblDates;
	private JPanel right;
	private JButton btnAdmin;
	private JCheckBox chckbxNewsletter;
	private JLabel lblSubscribeToNewsletter;
	private JLabel lblSecondName;
	private JTextField secNameField;
	
	public boolean adminAccess = false;	//used to gain admin privileges 
	
	public List<String> sqlDates = new ArrayList<String>();
	public List<String> sqlFlights = new ArrayList<String>();
	public List<Integer> sqlFlight_id = new ArrayList<Integer>();
	public List<Integer> sqlBooking_id = new ArrayList<Integer>();
	public List<String> sqlBooking = new ArrayList<String>();
	public List<Integer> sqlSubscriber = new ArrayList<Integer>();
	
	sqliteDB db;
	
	//log4j
	private static Logger log = Logger.getLogger(base.class.getName());		
	
	static base ref = null;
	static JFrame jframe = null;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		BasicConfigurator.configure();
		
		log.debug("Create new Frame");
		
		EventQueue.invokeLater(new Runnable() {			
			public void run() {
				try {						
					base frame = new base("Booking Application");	
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					log.error(e.getMessage(),e);
				}
			}
		});
		
	} 	
	
	public void checkBtn() {
		if(nameField.getText().isEmpty() || surnameField.getText().isEmpty() || mailField.getText().isEmpty() || ((Integer) numOfTicketsSpinner.getValue() == 0 ) || datesList.isSelectionEmpty() || flightsList.isSelectionEmpty()){
			btnBookIt.setEnabled(false);
			log.debug("Button BookIt disabled");
		}
		else {
			log.debug("Button BookIt enabled");
			btnBookIt.setEnabled(true);
		}//*/
		
		/*if (!adminAccess) {
			btnAddDate.setEnabled(false);
			btnAddFlight.setEnabled(false);
			btnDeleteDate.setEnabled(false);
			btnDeleteFlight.setEnabled(false);
		}else {
			btnAddDate.setEnabled(true);
			btnAddFlight.setEnabled(true);
			btnDeleteDate.setEnabled(true);
			btnDeleteFlight.setEnabled(true);
		}*/

	}
	
	public void UpdateFlights() {
		db.showFlightsList();				//takes selected id and uses it to show dates
		DefaultListModel<String> listModelFlights = new DefaultListModel<String>();
		for (int i = 0; i < sqlFlights.size(); i+=2) {
			listModelFlights.add(i/2, "" + sqlFlights.get(i) + " - " + sqlFlights.get(i+1));
		}	
		flightsList.setModel(listModelFlights);
	}
	
	public void UpdateDates() {		
		int index = flightsList.getSelectedIndex();	
		if (index == -1) index = 0;
		db.showDatesList(sqlFlight_id.get(index));				//takes selected id and uses it to show dates
		DefaultListModel<String> listModelFlights = new DefaultListModel<String>();
		for (int i = 0; i < sqlDates.size(); i++) {
			listModelFlights.add(i, sqlDates.get(i));
		}	
		datesList.setModel(listModelFlights);
	}

	public void UpdateBookings() {
		db.showBookingList();
		DefaultListModel<String> listModelBookings = new DefaultListModel<String>();
		for (int i = 0; i < sqlBooking_id.size(); i++) {			
			if (sqlSubscriber.get(i) == 0) {
				listModelBookings.add(i, "" + sqlBooking_id.get(i) + " - " + sqlBooking.get(i) + " " );
			}else {
				listModelBookings.add(i, "" + sqlBooking_id.get(i) + " - " + sqlBooking.get(i) + " 	---SUBSCRIBER---" );
			}						
		}	
		bookingList.setModel(listModelBookings);
	}
	/**
	 * Create the frame.
	 */
	public base(String frameName){	
		super(frameName);
		
		jframe = this;
		ref = this;
		// create a new databases obj	
		db = new sqliteDB(ref);
		 
		ActionListener myActionListener = new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			System.out.println("[ActionListener] Button = "+e.getActionCommand());
    			int index;
    			switch(e.getActionCommand())
    			{
       				case "Book it":
       					String date = datesList.getSelectedValue().toString();
       					date = date.substring(0, Math.min(date.length(), 10));
       					db.bookingTicket(nameField.getText(), secNameField.getText(), surnameField.getText(), mailField.getText(), (Integer) numOfTicketsSpinner.getValue(), chckbxNewsletter.isSelected(), sqlFlight_id.get(flightsList.getSelectedIndex()), date);
       					log.info("Booked ticket on " + nameField.getText() + " " + secNameField.getText() + " " + surnameField.getText() + " for " + flightsList.getSelectedValue().toString() + " flight "  + " at " + datesList.getSelectedValue().toString() + " date ");
           			break;
           			
       				case "Add Date":
       					//log.info("Adding date " + dateField.getText() + " for flight " + flightsList.getSelectedValue().toString());
       					index = flightsList.getSelectedIndex();
           				//db.addDate(sqlFlight_id.get(index), dateField.getText(), "" + depHour.getValue() + ":" + depMinutes.getValue(), "" + arrHour.getValue() + ":" + arrMinutes.getValue());
           				UpdateFlights();
           				UpdateDates();
           			break;
           			
           			case "Delete date":
           				log.info("Deleting date " + datesList.getSelectedValue().toString() + " for flight " + flightsList.getSelectedValue().toString());
           				index = flightsList.getSelectedIndex();
           				int indexDate = datesList.getSelectedIndex();
           				date = sqlDates.get(indexDate);
           				//date.trim();
           				date = date.substring(0, Math.min(date.length(), 10));
           				System.out.println(date); System.out.println(sqlFlight_id.get(index));
           				db.deleteDate(sqlFlight_id.get(index), date);
           				UpdateFlights();
           				UpdateDates();
           			break;
           			

           			case "Refresh":
           				UpdateFlights();
           				UpdateDates();
           			break;
           			

           			case "Add Flight":
           				log.info("Adding new flight connection");
           				//db.addFlight(sourceField.getText(), destinationField.getText(), dateField.getText(), "" + depHour.getValue() + ":" + depMinutes.getValue(), "" + arrHour.getValue() + ":" + arrMinutes.getValue());
           				UpdateFlights();
           				UpdateDates();
           			break;
           			
           			case "Delete flight":
           				index = flightsList.getSelectedIndex();
           				db.deleteFlight(sqlFlight_id.get(index));
           				log.info("Deleting flight connection at index " + flightsList.getSelectedIndex());
           				UpdateFlights();
           				UpdateDates();
               		break;
               		
           			case "Admin access":
           				adminLogin admin = new adminLogin(ref);
           				admin.setLocationRelativeTo(null);
           				admin.setVisible(true);
               		break;
    			}
    		}    		
    	};
		
    	WindowListener myWindowListener = new WindowListener() {
			public void windowActivated(WindowEvent e) {
    			//System.out.println("[WindowListener] Activated!"+" visible="+jframe.isVisible()+", active="+jframe.isActive());
			}

			public void windowClosed(WindowEvent e) {
    			//System.out.println("[WindowListener] Closed!");
			}

			public void windowClosing(WindowEvent e) {
				db.closeConnection();
    			System.out.println("[WindowListener] Closing!");
			}

			public void windowDeactivated(WindowEvent e) {
    			//System.out.println("[WindowListener] Deactivated!");
    		}

			public void windowDeiconified(WindowEvent e) {
    			//System.out.println("[WindowListener] Deiconified!");
			}

			public void windowIconified(WindowEvent e) {
    			System.out.println("[WindowListener] Iconified!");
    			//jframe.setTitle("kuku");
    			jframe.setVisible(true);
    		}

			public void windowOpened(WindowEvent e) {
    			System.out.println("[WindowListener] Opened!"+" visible="+jframe.isVisible()+", active="+jframe.isActive());
			}    		
    	};
    	
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
				if (e.getSource().equals(datesList));
				else UpdateDates();
				checkBtn();
			}
    		
    	};
    	
    	DocumentListener myDocumentListener = new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				checkBtn();
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				checkBtn();
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				checkBtn();
				// TODO Auto-generated method stub
				
			}
		};
    	
    	this.addWindowListener(myWindowListener);
    	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1050, 539);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);
		
		mntmNew = new JMenuItem("New");
		mnNewMenu.add(mntmNew);
		
		mntmOpen = new JMenuItem("Open");
		mnNewMenu.add(mntmOpen);
		
		mntmSave = new JMenuItem("Save");
		mnNewMenu.add(mntmSave);
		
		separator = new JSeparator();
		mnNewMenu.add(separator);
		
		mntmQuit = new JMenuItem("Quit");
		mnNewMenu.add(mntmQuit);
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		//contentPane.addWindowListener(myWindowListener);
		//contentPane.addWindowStateListener(myWindowStateListener);
		
		JPanel left = new JPanel();
		contentPane.add(left, BorderLayout.WEST);
		GridBagLayout gbl_left = new GridBagLayout();
		gbl_left.columnWidths = new int[]{104, 96, 0};
		gbl_left.rowHeights = new int[]{21, 0, 29, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_left.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_left.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		left.setLayout(gbl_left);
		
		JLabel lblName = new JLabel("Name");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.anchor = GridBagConstraints.EAST;
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 0;
		left.add(lblName, gbc_lblName);
		
		nameField = new JTextField();
		nameField.getDocument().addDocumentListener(myDocumentListener);
		GridBagConstraints gbc_nameField = new GridBagConstraints();
		gbc_nameField.anchor = GridBagConstraints.WEST;
		gbc_nameField.insets = new Insets(0, 0, 5, 0);
		gbc_nameField.gridx = 1;
		gbc_nameField.gridy = 0;
		left.add(nameField, gbc_nameField);
		nameField.setColumns(10);
		
		lblSecondName = new JLabel("Middle name");
		GridBagConstraints gbc_lblSecondName = new GridBagConstraints();
		gbc_lblSecondName.anchor = GridBagConstraints.EAST;
		gbc_lblSecondName.insets = new Insets(0, 0, 5, 5);
		gbc_lblSecondName.gridx = 0;
		gbc_lblSecondName.gridy = 1;
		left.add(lblSecondName, gbc_lblSecondName);
		
		secNameField = new JTextField();
		secNameField.getDocument().addDocumentListener(myDocumentListener);
		secNameField.setColumns(10);
		GridBagConstraints gbc_secNameField = new GridBagConstraints();
		gbc_secNameField.insets = new Insets(0, 0, 5, 0);
		gbc_secNameField.fill = GridBagConstraints.HORIZONTAL;
		gbc_secNameField.gridx = 1;
		gbc_secNameField.gridy = 1;
		left.add(secNameField, gbc_secNameField);
		
		JLabel lblSurname = new JLabel("Surname");
		GridBagConstraints gbc_lblSurname = new GridBagConstraints();
		gbc_lblSurname.anchor = GridBagConstraints.EAST;
		gbc_lblSurname.insets = new Insets(0, 0, 5, 5);
		gbc_lblSurname.gridx = 0;
		gbc_lblSurname.gridy = 2;
		left.add(lblSurname, gbc_lblSurname);
		
		surnameField = new JTextField();
		surnameField.getDocument().addDocumentListener(myDocumentListener);
		GridBagConstraints gbc_surnameField = new GridBagConstraints();
		gbc_surnameField.anchor = GridBagConstraints.WEST;
		gbc_surnameField.insets = new Insets(0, 0, 5, 0);
		gbc_surnameField.gridx = 1;
		gbc_surnameField.gridy = 2;
		left.add(surnameField, gbc_surnameField);
		surnameField.setColumns(10);
		
		lblNumOfTickets = new JLabel("Num of tickets");
		GridBagConstraints gbc_lblNumOfTickets = new GridBagConstraints();
		gbc_lblNumOfTickets.anchor = GridBagConstraints.EAST;
		gbc_lblNumOfTickets.insets = new Insets(0, 0, 5, 5);
		gbc_lblNumOfTickets.gridx = 0;
		gbc_lblNumOfTickets.gridy = 3;
		left.add(lblNumOfTickets, gbc_lblNumOfTickets);
		
		numOfTicketsSpinner = new JSpinner();
		SpinnerModel modeltau = new SpinnerNumberModel(0, 0, 99999, 1); 			//Integer.MAX_VALUE
		numOfTicketsSpinner.setModel(modeltau);
		GridBagConstraints gbc_numOfTicketsSpinner = new GridBagConstraints();
		gbc_numOfTicketsSpinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_numOfTicketsSpinner.insets = new Insets(0, 0, 5, 0);
		gbc_numOfTicketsSpinner.gridx = 1;
		gbc_numOfTicketsSpinner.gridy = 3;
		left.add(numOfTicketsSpinner, gbc_numOfTicketsSpinner);
		
		lblMail = new JLabel("Mail");
		GridBagConstraints gbc_lblMail = new GridBagConstraints();
		gbc_lblMail.anchor = GridBagConstraints.EAST;
		gbc_lblMail.insets = new Insets(0, 0, 5, 5);
		gbc_lblMail.gridx = 0;
		gbc_lblMail.gridy = 7;
		left.add(lblMail, gbc_lblMail);
		
		mailField = new JTextField();
		mailField.getDocument().addDocumentListener(myDocumentListener);
		mailField.setColumns(10);
		GridBagConstraints gbc_mailField = new GridBagConstraints();
		gbc_mailField.insets = new Insets(0, 0, 5, 0);
		gbc_mailField.fill = GridBagConstraints.HORIZONTAL;
		gbc_mailField.gridx = 1;
		gbc_mailField.gridy = 7;
		left.add(mailField, gbc_mailField);
		
		lblSubscribeToNewsletter = new JLabel("Subscribe to newsletter");
		GridBagConstraints gbc_lblSubscribeToNewsletter = new GridBagConstraints();
		gbc_lblSubscribeToNewsletter.anchor = GridBagConstraints.WEST;
		gbc_lblSubscribeToNewsletter.insets = new Insets(0, 0, 5, 5);
		gbc_lblSubscribeToNewsletter.gridx = 0;
		gbc_lblSubscribeToNewsletter.gridy = 8;
		left.add(lblSubscribeToNewsletter, gbc_lblSubscribeToNewsletter);
		
		chckbxNewsletter = new JCheckBox("");
		GridBagConstraints gbc_chckbxNewsletter = new GridBagConstraints();
		gbc_chckbxNewsletter.anchor = GridBagConstraints.WEST;
		gbc_chckbxNewsletter.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxNewsletter.gridx = 1;
		gbc_chckbxNewsletter.gridy = 8;
		left.add(chckbxNewsletter, gbc_chckbxNewsletter);
		
		btnBookIt = new JButton("Book it");
		
		
		GridBagConstraints gbc_btnBookIt = new GridBagConstraints();
		gbc_btnBookIt.insets = new Insets(0, 0, 5, 5);
		gbc_btnBookIt.gridx = 0;
		gbc_btnBookIt.gridy = 9;
		left.add(btnBookIt, gbc_btnBookIt);
		
		btnAdmin = new JButton("Admin access");
		GridBagConstraints gbc_btnAdmin = new GridBagConstraints();
		gbc_btnAdmin.insets = new Insets(0, 0, 5, 5);
		gbc_btnAdmin.gridx = 0;
		gbc_btnAdmin.gridy = 10;
		left.add(btnAdmin, gbc_btnAdmin);
		btnAdmin.addActionListener(myActionListener);
		btnBookIt.addActionListener(myActionListener);
		
		middle = new JPanel();
		contentPane.add(middle, BorderLayout.CENTER);
		GridBagLayout gbl_middle = new GridBagLayout();
		gbl_middle.columnWidths = new int[]{142, 122, 0};
		gbl_middle.rowHeights = new int[]{0, 0, 0};
		gbl_middle.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_middle.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		middle.setLayout(gbl_middle);
		
		lblFlights = new JLabel("Flights");
		GridBagConstraints gbc_lblFlights = new GridBagConstraints();
		gbc_lblFlights.insets = new Insets(0, 0, 5, 5);
		gbc_lblFlights.gridx = 0;
		gbc_lblFlights.gridy = 0;
		middle.add(lblFlights, gbc_lblFlights);
		
		lblDates = new JLabel("Dates");
		GridBagConstraints gbc_lblDates = new GridBagConstraints();
		gbc_lblDates.insets = new Insets(0, 0, 5, 0);
		gbc_lblDates.gridx = 1;
		gbc_lblDates.gridy = 0;
		middle.add(lblDates, gbc_lblDates);
		
		
		DefaultListModel listModelFlights = new DefaultListModel();
		//listModelFlights.add(0, "test space");
		
		
		flightsList = new JList(listModelFlights);
		flightsList.addMouseListener(myMouseListener);
		
		GridBagConstraints gbc_flightsList = new GridBagConstraints();
		gbc_flightsList.insets = new Insets(0, 0, 0, 5);
		gbc_flightsList.fill = GridBagConstraints.BOTH;
		gbc_flightsList.gridx = 0;
		gbc_flightsList.gridy = 1;
		middle.add(flightsList, gbc_flightsList);
		
		
		DefaultListModel listModelDates = new DefaultListModel();
		
		datesList = new JList(listModelDates);
		datesList.addMouseListener(myMouseListener);
		
		GridBagConstraints gbc_datesList = new GridBagConstraints();
		gbc_datesList.fill = GridBagConstraints.BOTH;
		gbc_datesList.gridx = 1;
		gbc_datesList.gridy = 1;
		middle.add(datesList, gbc_datesList);
		
		DefaultListModel listModelBookings = new DefaultListModel();
		
		bookingList = new JList(listModelBookings);
		bookingList.addMouseListener(myMouseListener);
		
		right = new JPanel();
		contentPane.add(right, BorderLayout.EAST);
		GridBagLayout gbl_right = new GridBagLayout();
		gbl_right.columnWidths = new int[]{0, 0, 0};
		gbl_right.rowHeights = new int[]{0, 0, 0, 26, 26, 0, 0, 0, 0, 0, 0, 0};
		gbl_right.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_right.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		right.setLayout(gbl_right);
		modeltau = new SpinnerNumberModel(0, 0, 23, 1);
		modeltau = new SpinnerNumberModel(0, 0, 59, 1);
		modeltau = new SpinnerNumberModel(0, 0, 23, 1);
		modeltau = new SpinnerNumberModel(0, 0, 59, 1);
		
		UpdateBookings();
		UpdateFlights();
		UpdateDates();
		checkBtn();
	}
}
