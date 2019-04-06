package app;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
/**
 * class used to build customized button that has image on its left part
 * to use as folder in grid view
 * @author Amir
 *
 */
public class ImageButton extends Button {
	final ImageView iv;
	public ImageButton(String text,Image image) {
		super(text);
		iv = new ImageView(image);
		this.getChildren().add(iv);
		super.setGraphic(iv);
	}
}