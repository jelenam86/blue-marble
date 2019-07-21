package hellofx;

import java.time.LocalDate;

import org.curiousworks.BlueMarble;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class BlueMarbleController {

	@FXML
	private Button naturalButton;

	@FXML
	private Button quitButton;

	@FXML
	private Button enhancedButton;

	@FXML
	private Button blackWhiteButton;

	@FXML
	private Button grayButton;

	@FXML
	private DatePicker datePicker;

	@FXML
	private ImageView image;

	private static BlueMarble blueMarble = new BlueMarble();

	@FXML
	void blackWhitePressed(ActionEvent event) {
		if (loadImage(blueMarble)) {
			Image img = new Image(blueMarble.getImage());
			WritableImage writableImage = new WritableImage(img.getPixelReader(), (int) img.getWidth(),
					(int) img.getHeight());
			PixelWriter pixelWriter = writableImage.getPixelWriter();
			PixelReader pixelReader = writableImage.getPixelReader();
			for (int i = 0; i < writableImage.getHeight(); i++) {
				for (int j = 0; j < writableImage.getWidth(); j++) {
					Color c = pixelReader.getColor(j, i);
					if (c.getOpacity() < 1)
						pixelWriter.setColor(j, i, Color.BLACK);
					double luminance = (c.getRed() * 0.2126) + (c.getGreen() * 0.7152) + (c.getBlue() * 0.0722);
					if (luminance > 0.5)
						pixelWriter.setColor(j, i, Color.WHITE);
					else
						pixelWriter.setColor(j, i, Color.BLACK);
				}
			}
			image.setImage(writableImage);
		}
	}

	@FXML
	void grayPressed(ActionEvent event) {
		if (loadImage(blueMarble)) {
			Image img = new Image(blueMarble.getImage());
			WritableImage writableImage = new WritableImage(img.getPixelReader(), (int) img.getWidth(),
					(int) img.getHeight());
			PixelWriter pixelWriter = writableImage.getPixelWriter();
			PixelReader pixelReader = writableImage.getPixelReader();
			for (int i = 0; i < writableImage.getHeight(); i++) {
				for (int j = 0; j < writableImage.getWidth(); j++) {
					Color c = pixelReader.getColor(j, i);
					pixelWriter.setColor(j, i, c.grayscale());
				}
			}
			image.setImage(writableImage);
		}
	}

	@FXML
	void enhancedPressed(ActionEvent event) {
		blueMarble.setEnhanced(true);
		loadImage(blueMarble);
		blueMarble.setEnhanced(false);
	}

	@FXML
	void quitPressed(ActionEvent event) {
		Platform.exit();
	}

	@FXML
	void naturalPressed(ActionEvent event) {
		loadImage(blueMarble);
	}

	@FXML
	void updateDate(ActionEvent event) {

		blueMarble.setDate(String.format("%d-%02d-%02d", datePicker.getValue().getYear(),
				datePicker.getValue().getMonthValue(), datePicker.getValue().getDayOfMonth()));
		if (alert()) {
			setDisabled(true);
			return;
		}
		setDisabled(false);
		image.setBlendMode(BlendMode.DIFFERENCE);
	}

	private void setDisabled(boolean b) {
		naturalButton.setDisable(b);
		enhancedButton.setDisable(b);
		grayButton.setDisable(b);
		blackWhiteButton.setDisable(b);
	}

	private boolean alert() {

		boolean s = datePicker.getValue().isBefore(LocalDate.of(2015, 7, 1));
		String message = s ? "The oldest EPIC photo is from July 2015...\nSelect another date!"
				: "Time travel into the future is not possible yet...\nSelect another date!";
		if (s || datePicker.getValue().isAfter(LocalDate.now())) {
			createAlert(message);
			return true;
		}
		return false;
	}

	private boolean loadImage(BlueMarble bm) {
		try {
			Image img = new Image(bm.getImage());
			image.setImage(img);
			return true;
		} catch (Exception e) {
			setDisabled(true);
			String message = "No image at the specified date!\nSelect another date.";
			if (bm.getQuality().equals("enhanced")) {
				setDisabled(false);
				enhancedButton.setDisable(true);
				message = "No enhanced photo at the specified date!";
			}
			if (blueMarble.getDate() == null)
				message = "Please choose a date first!";
			createAlert(message);
			return false;
		}
	}

	private void createAlert(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
