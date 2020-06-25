package swing.sample;

import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.String;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridLayout;

public class adminLogin extends JFrame {
	
	private JButton btnLogin;
	private JPanel contentPane;
	private JTextField nameField1;
	private final JPasswordField nameField2;
	
	private static Logger log = Logger.getLogger(base.class.getName());	
	//static base ref = null;
	
	public static void main(String[] args) {
		BasicConfigurator.configure();
		
		EventQueue.invokeLater(new Runnable() {			
			public void run() {
				try {						
					//adminLogin frame = new adminLogin(ref);									
					//frame.setLocationRelativeTo(null);
					//frame.setVisible(true);
				} catch (Exception e) {
					log.error(e.getMessage(),e);
				}
			}
		});
		
	} 
	
	
	public adminLogin(base _ref) {
		//ref = _ref;
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 250);
				
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel left = new JPanel();
		contentPane.add(left, BorderLayout.CENTER);
		GridBagLayout gbl_left = new GridBagLayout();
		gbl_left.columnWidths = new int[]{14, 96, 0};
		gbl_left.rowHeights = new int[]{21, 0, 29, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_left.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_left.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		left.setLayout(gbl_left);
		
		JLabel lblName1 = new JLabel("Username");
		//lblName1.setFont(new Font("Serif", Font.BOLD, 18));
		GridBagConstraints gbc_lblName1 = new GridBagConstraints();
		gbc_lblName1.anchor = GridBagConstraints.EAST;
		gbc_lblName1.insets = new Insets(0, 0, 5, 5);
		gbc_lblName1.gridx = 0;
		gbc_lblName1.gridy = 2;
		left.add(lblName1, gbc_lblName1);
		
		nameField1 = new JTextField();
		nameField1.setPreferredSize(new Dimension(20, 30));
		nameField1.setFont(new Font("Verdana", Font.PLAIN, 15));
		GridBagConstraints gbc_nameField1 = new GridBagConstraints();
		gbc_nameField1.anchor = GridBagConstraints.WEST;
		gbc_nameField1.insets = new Insets(0, 0, 5, 0);
		gbc_nameField1.gridx = 1;
		gbc_nameField1.gridy = 2;
		left.add(nameField1, gbc_nameField1);
		nameField1.setColumns(15);
		
		JLabel lblName2 = new JLabel("Password");
		//lblName2.setFont(new Font("Serif", Font.BOLD, 18));
		GridBagConstraints gbc_lblName2 = new GridBagConstraints();
		gbc_lblName2.anchor = GridBagConstraints.EAST;
		gbc_lblName2.insets = new Insets(0, 0, 5, 5);
		gbc_lblName2.gridx = 0;
		gbc_lblName2.gridy = 4;
		left.add(lblName2, gbc_lblName2);
		
		nameField2 = new JPasswordField();
		nameField2.setPreferredSize(new Dimension(20, 30));
		nameField2.setFont(new Font("Verdana", Font.PLAIN, 15));
		GridBagConstraints gbc_nameField2 = new GridBagConstraints();
		gbc_nameField2.anchor = GridBagConstraints.WEST;
		gbc_nameField2.insets = new Insets(0, 0, 5, 0);
		gbc_nameField2.gridx = 1;
		gbc_nameField2.gridy = 4;
		left.add(nameField2, gbc_nameField2);
		nameField2.setColumns(15);
		
		btnLogin = new JButton("Login");
		btnLogin.setPreferredSize(new Dimension(75, 40));
		GridBagConstraints gbc_btnLogin = new GridBagConstraints();
		gbc_btnLogin.insets = new Insets(20, 0, 5, -250);
		gbc_btnLogin.gridx = 0;
		gbc_btnLogin.gridy = 8;
		left.add(btnLogin, gbc_btnLogin);
		btnLogin.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String admin = nameField1.getText();
				String pass = nameField2.getText();
				
				if (admin.equals("admin") && pass.equals("password")) {
					// open admin window
					try{
						dispose();
						log.debug("Login to admin window success");
						//ref.adminAccess = true;
						adminControl adminWindow = new adminControl(_ref);
						adminWindow.setLocationRelativeTo(null);
						adminWindow.setVisible(true);
					}
					catch(Exception er) {
						log.error(er.getMessage(), er);
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "Invalid username or password");
				}
			
			}
		});
		
	}
	
	
	
}
