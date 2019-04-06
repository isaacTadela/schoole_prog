package hs_server;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class HSServerFrameGUI extends JFrame{
	ServerApp abstractServer;
	JButton performNewConnectionButton;
	JTextField portTextField;
	JLabel currentServer;
	JLabel lastMessageRecieved;
	JLabel lastMessageLog;
	JLabel dbStatus;
	boolean isListeningEstablished;

	/**
	 * constructor of server GUI
	 * @param abstractServer ServerApp which is the class that inherits AbstractServer in order to handle connections
	 * @param isListeningEstablished boolean indicating if the server listening was established or not
	 */
	public HSServerFrameGUI(ServerApp abstractServer,boolean isListeningEstablished){
		super("High School Server by G12");
		this.abstractServer = abstractServer;
		this.isListeningEstablished = isListeningEstablished;
		setLayout(new FlowLayout());
		Integer port = abstractServer.getPort();
		if (isListeningEstablished){
			currentServer = new JLabel("Server is Listening on port "+ abstractServer.getPort());
			currentServer.setForeground(Color.green);
		}else{
			currentServer = new JLabel("Server is not Running");
			currentServer.setForeground(Color.red);
		}
		currentServer.setFont(new Font("Serif", Font.BOLD, 22));

		JLabel portLabel = new JLabel("port: ");
		portLabel.setPreferredSize(new Dimension(50,60));

		currentServer.setPreferredSize(new Dimension(450,60));

		portTextField = new JTextField(port.toString(),6);
		portTextField.setPreferredSize(new Dimension(200,60));
		portTextField.setHorizontalAlignment(JTextField.CENTER);


		dbStatus = new JLabel("OFF");


		performNewConnectionButton = new JButton("Reconnect server");
		performNewConnectionButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				if (abstractServer.isListening()){
					abstractServer.stopListening();
					try {
						abstractServer.close();
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				}
				currentServer.setText("Server is not Running");
				abstractServer.setServerPort(Integer.parseInt(portTextField.getText()));
				while(abstractServer.isListening()){

				}
				try {
					abstractServer.listen();
					Integer port = abstractServer.getPort();
					currentServer.setText("Server is Listening on port "+ port);
					currentServer.setForeground(Color.green);
					abstractServer.updateValueInIni("server","port",port.toString());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					currentServer.setText("Server has failed to run on port "+abstractServer.getPort());
					currentServer.setForeground(Color.red);
				}
			}
		});
		performNewConnectionButton.setPreferredSize(new Dimension(170,40));

		add(currentServer);
		add(portLabel);
		add(portTextField);
		add(performNewConnectionButton);


		Panel logPane = new Panel();
		logPane.setLayout(new FlowLayout());

		lastMessageLog = new JLabel("Last message recieved log: ");
		lastMessageLog.setPreferredSize(new Dimension(180,40));
		lastMessageRecieved = new JLabel("No messages recieved yet.");
		lastMessageRecieved.setPreferredSize(new Dimension(400,40));
		logPane.add(lastMessageLog);
		logPane.add(lastMessageRecieved);
		add(logPane);



		Panel pane = new Panel();
		pane.setLayout(new FlowLayout());
		pane.setSize(580,100);

		JLabel dbLabel = new JLabel("DB info: ");
		dbLabel.setSize(new Dimension(500,40));
		pane.add(dbLabel);

		JTextField dbHostTextField = new JTextField(ServerApp.DB_HOST,20);
		JTextField dbUserTextField = new JTextField(ServerApp.DB_USER,5);
		JPasswordField dbPasswordTextField = new JPasswordField(ServerApp.DB_PASSWORD,5);

		pane.add(dbHostTextField);
		pane.add(dbUserTextField);
		pane.add(dbPasswordTextField);
		pane.add(new JLabel("status:"));
		pane.add(dbStatus);

		JButton reconnectDB = new JButton("reconnect db");
		reconnectDB.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				abstractServer.updateValueInIni("db-server","host",dbHostTextField.getText());
				abstractServer.updateValueInIni("db-server","user",dbUserTextField.getText());
				abstractServer.updateValueInIni("db-server","pass",String.valueOf(dbPasswordTextField.getPassword()));
				abstractServer.connectWithDatabase(dbHostTextField.getText(), dbUserTextField.getText(), String.valueOf(dbPasswordTextField.getPassword()));
			}
		});

		pane.add(reconnectDB);
		pane.repaint();
		add(pane);


		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
	}

	/**
	 * method used to show messages on GUI which is meant for as logger as response to receiving info from client.
	 * @param msg the message as String
	 */
	public void setLastMessageLog(Object msg){
		lastMessageRecieved.setText(msg.toString());
	}

	/**
	 * method updates the GUI indicating if connection with DB was established or not.
	 * @param b boolean indicating if connection with database was successful or not
	 */
	public void updateDBConnectionUi(boolean b) {
		if (dbStatus != null){
			if (b){
				dbStatus.setText("ON");
				dbStatus.setForeground(Color.green);
			}else{
				dbStatus.setText("OFF");
				dbStatus.setForeground(Color.red);
			}
		}
	}


}
