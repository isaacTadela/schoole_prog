package app;

import java.util.Enumeration;
import java.util.ResourceBundle;
/**
 * class used for sending data to new controller whenever we run new node.
 * @author Amir
 *
 */
public class DataBundle extends ResourceBundle {
	Object data;
/**
 * method for getting the stored data	
 * @return data we sent to the controller
 */
	public Object getData() {
		return data;
	}
/**
 * method used to save data
 * @param data is data needed to be sent
 */
	public void setData(Object data) {
		this.data = data;
	}

	@Override
	protected Object handleGetObject(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration<String> getKeys() {
		// TODO Auto-generated method stub
		return null;
	}

}
