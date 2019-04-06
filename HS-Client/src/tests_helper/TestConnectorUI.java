package tests_helper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Tests Specific UI for changing server and database connection data.
 * @author Amir
 *
 */
public class TestConnectorUI extends JFrame{
	JButton performNewConnectionButton;
	JTextField portTextField;
	JTextField hostNameTextField;
	JLabel currentServer;

	JLabel dbStatus;
	private static TestConnectorUI instance;
	boolean isListeningEstablished;
	JTextField dbHostTextField;
	JTextField dbUserTextField;
	JPasswordField dbPasswordTextField;
	private RefreshConnectionIF refreshConnectionIF;

	public RefreshConnectionIF getRefreshConnectionIF() {
		return refreshConnectionIF;
	}

	public void setRefreshConnectionIF(RefreshConnectionIF refreshConnectionIF) {
		this.refreshConnectionIF = refreshConnectionIF;
	}

	public TestConnectorUI(){
		super("High School Test-Client Connector by G12");
		this.refreshConnectionIF = refreshConnectionIF;
		setLayout(new FlowLayout());
		currentServer = new JLabel("Couldnt Connect please type correct server info.");
		currentServer.setForeground(Color.red);
		
		currentServer.setFont(new Font("Serif", Font.BOLD, 18));

		JLabel portLabel = new JLabel("port: ");
		portLabel.setPreferredSize(new Dimension(40,60));
		
		JLabel hostLabel = new JLabel("host: ");
		portLabel.setPreferredSize(new Dimension(40,60));
		
		currentServer.setPreferredSize(new Dimension(570,60));

		hostNameTextField = new JTextField("");
		hostNameTextField.setPreferredSize(new Dimension(400,50));
		hostNameTextField.setHorizontalAlignment(JTextField.CENTER);		
		
		portTextField = new JTextField("");
		portTextField.setPreferredSize(new Dimension(60,50));
		portTextField.setHorizontalAlignment(JTextField.CENTER);

		dbStatus = new JLabel("OFF");


		performNewConnectionButton = new JButton("Reconnect");
		performNewConnectionButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshConnectionIF.onClickRefresh(hostNameTextField.getText(), portTextField.getText(), dbHostTextField.getText(), dbUserTextField.getText(), dbPasswordTextField.getText());
	
			}
		});
		performNewConnectionButton.setPreferredSize(new Dimension(170,40));

		add(currentServer);
		
		
		add(hostLabel);
		add(hostNameTextField);
		add(portLabel);
		add(portTextField);
		


		Panel pane = new Panel();
		pane.setLayout(new FlowLayout());
		pane.setSize(580,100);

		JLabel dbLabel = new JLabel("DB info: ");
		dbLabel.setSize(new Dimension(500,40));
		pane.add(dbLabel);

		dbHostTextField = new JTextField("",20);
		dbUserTextField = new JTextField("",5);
		dbPasswordTextField = new JPasswordField("",5);

		pane.add(dbHostTextField);
		pane.add(dbUserTextField);
		pane.add(dbPasswordTextField);
		pane.add(new JLabel("status:"));
		pane.add(dbStatus);

		pane.repaint();
		add(pane);
		add(performNewConnectionButton);
		setSize(600,260);
		setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		//setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
	}

	public static TestConnectorUI getInstance(){
		if(instance == null){
			instance = new TestConnectorUI();
		}
		return instance;
	}
	
	

	public void setConnectionToClient(String hostname,Integer port,boolean isConnected){
		hostNameTextField.setText(hostname);
		portTextField.setText(port.toString());
		if (!isConnected){
			currentServer.setText("Test-Client has failed to connect to server " + hostname + " on port " + port.toString());
			currentServer.setForeground(Color.red);
		}else{
			currentServer.setText("Test-Client connected successfuly to server " + hostname + " on port " + port.toString());
			currentServer.setForeground(Color.getColor("0X007a00"));
		}
	}
	
	public void setConnectedToDatabase(String hostname,String username,String password,boolean isDBConnected){
		dbHostTextField.setText(hostname);
		dbUserTextField.setText(username);
		dbPasswordTextField.setText(password);
		if (isDBConnected){
			dbStatus.setText("ON");
			dbStatus.setForeground(Color.getColor("0X007a00"));
		}else{
			dbStatus.setText("OFF");
			dbStatus.setForeground(Color.red);			
		}
	}	

}
