package app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import common.Packet;
import common.User;
import enums.PacketId;
import enums.PacketSub;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;


public class Main extends Application {
	private static Stage primaryStage;
	final public static int DEFAULT_PORT = 5555;
	final public static String DEFAULT_HOST = "localhost";
	private static Region root;
	private static HSClient client;
	public static Integer lastAttemptedPort = DEFAULT_PORT;
	public static String lastAttemptedHost = DEFAULT_HOST;
	public static ArrayList<ControllerIF> currentController;
	public static User user;
	public static ArrayList<Node> rightPaneList;
	private static Button backButton;
	public static Stage getPrimaryStage() {
		return primaryStage;
	}
	/**
	 * method to get User info of logged user
	 * @return User of the logged in 
	 */
	public static User getUser() {
		return user;
	}
	/**
	 * method used to set user data of whom logged in
	 * @param user user class containing the datta in order to save
	 */
	public static void setUser(User user) {
		Main.user = user;
	}

	/**
	 * javafx method that will launch on javafx thread
	 * @param primaryStage default stage that gets created when first running program with javafx
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			Main.primaryStage = primaryStage;
			currentController = new ArrayList<>();
			primaryStage.setTitle("High School Client by G12");
			primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("resources/icon.png")));
			root = FXMLLoader.load(getClass().getResource("main_connected_layout.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);

			signalDisconnection();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * static method used to open ned javafx Scene
	 * @param layoutPath references to fxml of the scene required
	 */
	public static void showNewScene(String layoutPath){
		Platform.runLater(new Runnable(){
			@Override 
			public void run() {
				try {
					root = FXMLLoader.load(Main.class.getResource(layoutPath));
					Scene scene = new Scene(root);
					primaryStage.setScene(scene);
					primaryStage.setResizable(false);
					primaryStage.show();					
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}
		});
	}
	
	/**
	 * static method used to open ned javafx Scene
	 * @param layoutPath references to fxml of the scene required
	 * @param data for data to pass by for the new scene.
	 */
	public static void showNewScene(String layoutPath,Object data){
		Platform.runLater(new Runnable(){
			@Override 
			public void run() {
				try {
					DataBundle dataBundle = new DataBundle();
					dataBundle.setData(data);
					root = FXMLLoader.load(Main.class.getResource(layoutPath),dataBundle);
					Scene scene = new Scene(root);
					primaryStage.setScene(scene);
					primaryStage.setResizable(false);
					primaryStage.show();					
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}
		});
	}	
	
	/**
	 * method used to change the right pane window
	 * @param layoutPath references to fxml of the right pane required
	 */
	public static void showNewRightPane(String layoutPath){
		Platform.runLater(new Runnable(){
			@Override 
			public void run() {
				if (root instanceof SplitPane){
					try {
						Parent child = FXMLLoader.load(Main.class.getResource(layoutPath));
						SplitPane sp = (SplitPane)root;
						if (sp.getItems().size() > 1){
							Node node = sp.getItems().get(1);
							showBackButton(node);
							sp.getItems().remove(node);
						}
						sp.getItems().add(child);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}
	/**
	 * method used to change the right pane window
	 * @param layoutPath references to fxml of the right pane required
	 * @param data can be any data we need to pass along to the new controller that will handle the new right pane fxml
	 */	
	public static void showNewRightPane(String layoutPath,Object data){
		Platform.runLater(new Runnable(){
			@Override 
			public void run() {
				if (root instanceof SplitPane){
					try {
						DataBundle dBundle =  new DataBundle();
						dBundle.setData(data);

						Parent child = FXMLLoader.load(Main.class.getResource(layoutPath),dBundle);
						SplitPane sp = (SplitPane)root;
						if (sp.getItems().size() > 1){
							Node node = sp.getItems().get(1);
							showBackButton(node);
							sp.getItems().remove(node);
						}
						sp.getItems().add(child);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}



	/**
	 * method used to change the right pane window
	 * @param layoutPath references to fxml of the right pane required
	 * @param controller the controller that will handle the fxml 
	 * @param data can be any data we need to pass along to the new controller that will handle the new right pane fxml
	 */		
	public static void showNewRightPane(String layoutPath,ControllerIF controller,Object data){
		Platform.runLater(new Runnable(){
			@Override 
			public void run() {
				if (root instanceof SplitPane){
					try {
						DataBundle dBundle =  new DataBundle();
						dBundle.setData(data);
						FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(layoutPath),dBundle);
						fxmlLoader.setController(controller);
						Parent child = fxmlLoader.load();
						SplitPane sp = (SplitPane)root;
						if (sp.getItems().size() > 1){
							Node node = sp.getItems().get(1);
							showBackButton(node);
							sp.getItems().remove(node);
						}
						sp.getItems().add(child);

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}
	/**
	 * method used to return the right pane window to the initial state just after login
	 */
	public static void cleanRightPane(){
		Platform.runLater(new Runnable(){
			@Override 
			public void run() {
				if (root instanceof SplitPane){
					SplitPane sp = (SplitPane)root;
					if (sp.getItems().size() > 1){
						Node node = sp.getItems().get(1);
						sp.getItems().remove(node);
					}
					cleanBackButton();
					showNewRightPane("default_right_pane_layout.fxml");
				}
			}
		});
	}

	/**
	 * method used to change left pane window to new one
	 * @param layoutPath address of the fxml file that is needed to be set in left pane of app window
	 */
	public static void showNewLeftPane(String layoutPath){
		Platform.runLater(new Runnable(){
			@Override 
			public void run() {
				if (root instanceof SplitPane){
					try {
						Parent child = FXMLLoader.load(Main.class.getResource(layoutPath));
						SplitPane sp = (SplitPane)root;
						AnchorPane node = (AnchorPane)sp.getItems().get(0);
						node.getChildren().remove(0);
						node.getChildren().add(child);
						cleanBackButton();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}	
	/**
	 * method used to make back button dissapears when new set of paths is being chosen or no more pages left to return to
	 */
	public static void cleanBackButton(){
		Platform.runLater(new Runnable(){
			@Override 
			public void run() {	
				Main.rightPaneList = new ArrayList<>();
				if (backButton != null){
					backButton.setVisible(false);
				}
			}
		});
	}
	/**
	 * method handles what window to switch to when clicking on back button
	 */
	public static void onBackClick(){
		Platform.runLater(new Runnable(){
			@Override 
			public void run() {
				if (root instanceof SplitPane){
					SplitPane sp = (SplitPane)root;
					if (sp.getItems().size() > 1 && Main.rightPaneList.size() > 0){
						if (Main.rightPaneList.size() == 1){
							cleanRightPane();
						}else{
							Node node = sp.getItems().get(1);
							sp.getItems().remove(node);
							sp.getItems().add(Main.rightPaneList.get(Main.rightPaneList.size()-1));
							Main.rightPaneList.remove(Main.rightPaneList.size()-1);
							if (Main.rightPaneList.size() == 0){
								cleanBackButton();
							}
						}
					}
				}
			}
		});	
	}
	/**
	 * method used to make back button visible
	 * @param node new window that need to be saved as last to run
	 */
	public static void showBackButton(Node node){
		Platform.runLater(new Runnable(){
			@Override 
			public void run() {	
				backButton.setVisible(true);
				backButton.setDisable(false);
				Main.rightPaneList.add(node);
			}
		});
	}

	public static void setBackButton(Button button){
		backButton = button;	
	}
	/**
	 * main where the java app starts at
	 * @param args not needed
	 */
	public static void main(String[] args) {
		launch(args);
	}
	/*
	 * (non-Javadoc)
	 * @see javafx.application.Application#stop()
	 */
	
	@Override
	public void stop() throws Exception { 
		if (client != null && client.isConnected()){
			client.closeConnection();
		}
		primaryStage.close();
		System.exit(0);
	}
	/*
	 * method from ControllerIF interface, in which we handle the responses from server 
	 * this one will send the response from server to other controllers that actively wait response.
	 */
	public static void onResponse(Object message) {
		if (message instanceof Packet){
			Packet pck = (Packet)message;
			if (pck.getPacketId() == PacketId.REQUIRE_BOOLEAN){
				if (pck.getPacketSub() == PacketSub.REQUEST_LOGOUT){
					Platform.runLater(new Runnable(){
						@Override 
						public void run() {
							Main.setUser(null);

							Main.showNewScene("login_layout.fxml","logout");
						}
					});
					return;
				}
			}
		}

		if (currentController != null){
			for (int i = 0;i<currentController.size();i++){
				currentController.get(i).onResponse(message);
			}
		}
	}

	/**
	 * method used in order to set the right pane controller that will wait for server responses.
	 * @param controllerIF
	 */
	public static void setRightPaneController(ControllerIF controllerIF){
		if (currentController.size()>1){
			currentController.remove(1);
		}
		currentController.add(controllerIF);
	}

	/**
	 * method used in order to set the left pane controller that will wait for server responses.
	 * @param controllerIF
	 */	
	public static void setLeftPaneController(ControllerIF controllerIF){
		if (currentController.size()>0){
			currentController.remove(0);
		}
		currentController.add(0,controllerIF);
	}


	/**
	 * method used to send message to server
	 * @param message is the data that we need to send to server
	 */
	public static void sendToServer(Object message) {
		try {
			System.out.println("main 395...");
			client.sendToServer(message);
			System.out.println("main 398...");
		} catch (IOException e) {
			e.printStackTrace();
			signalDisconnection();
		}
	}
	/**
	 * method that will run when server connection gets detected that it disconnected in order to reconnect.
	 */
	public static void signalDisconnection() {
		startNewConnection(getLastUsedHost(), getLastUsedPort());	
	}

	/**
	 * method used to open popup alert message over the app main window,
	 * its used for INFORMATION,ERROR message types
	 * @param msg the message the popup needs to show.
	 * @param type message type which is one of the AlertType enum values
	 */
	public static void openPopUpWithMessage(String msg,AlertType type){
		Platform.runLater(new Runnable(){
			@Override 
			public void run() {		
				Alert alert = new Alert(type);
				alert.setTitle(currentController.get(1).getWindowTitle());
				if (type.name().equals("INFORMATION")){
					alert.setHeaderText("INFORMATION");
				}else{
					alert.setHeaderText("ERROR");
				}
				alert.setContentText(msg);
				alert.showAndWait();
			}
		});
	}


	/**
	 * method used to start connection with server
	 * @param host is server host required to connect to
	 * @param port port required to connect to server with
	 */
	public static void startNewConnection(String host, int port){
		lastAttemptedPort = port;
		lastAttemptedHost = host;	
		try 
		{
			client = new HSClient(host, port);
			showNewScene("login_layout.fxml");
		}catch(IOException exception){ 
			System.out.println("Error: Can't setup connection.");
			showNewScene("disconnect_ui.fxml");
		}  
	}
	/**
	 * methods used to obtain last used port to connect to server with, in order to save it for next time we run app.
	 * @return the port number last used for connection attempt
	 */
	public static Integer getLastUsedPort(){
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		return prefs.getInt("port", 5555);
	}
	/**
	 * method used to obtain last host server attempted to be connected to
	 * @return the host address last attempted to connect to.
	 */
	public static String getLastUsedHost(){
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		return prefs.get("host", "localhost");
	}


}


