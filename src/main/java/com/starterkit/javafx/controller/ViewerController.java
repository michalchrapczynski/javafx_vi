package com.starterkit.javafx.controller;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;

/**
 * Controller for the person search screen.
 * <p>
 * The JavaFX runtime will inject corresponding objects in the @FXML annotated
 * fields. The @FXML annotated methods will be called by JavaFX runtime at
 * specific points in time.
 * </p>
 *
 * @author Leszek
 */
public class ViewerController {

	/**
	 * Resource bundle loaded with this controller. JavaFX injects a resource
	 * bundle specified in {@link FXMLLoader#load(URL, ResourceBundle)} call.
	 * <p>
	 * NOTE: The variable name must be {@code resources}.
	 * </p>
	 */
	@FXML
	private ResourceBundle resources;

	/**
	 * URL of the loaded FXML file. JavaFX injects an URL specified in
	 * {@link FXMLLoader#load(URL, ResourceBundle)} call.
	 * <p>
	 * NOTE: The variable name must be {@code location}.
	 * </p>
	 */
	@FXML
	private URL location;

	/**
	 * JavaFX injects an object defined in FXML with the same "fx:id" as the
	 * variable name.
	 */
	@FXML
	private TextField nameField;

	@FXML
	private Button searchButton;

	@FXML
	private TextField testPath;

	@FXML
	private ImageView testImage;

	@FXML
	private ListView<File> listImagies;

	@FXML
	private Slider slider;

	@FXML
	private void initialize() {

	}

	@FXML
	private void searchButtonAction(ActionEvent event) {

		DirectoryChooser dir = new DirectoryChooser();

		File selectedFile = dir.showDialog(null);

		if (selectedFile != null) {
			testPath.setText(selectedFile.getAbsolutePath());
			lookForImagies(selectedFile);
		}

	}

	@SuppressWarnings("unchecked")
	private void lookForImagies(File absolutePath) {
		File[] files;

		Path dir = absolutePath.toPath();
		FilenameFilter filtr = new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".jpg");
			}
		};
		files = dir.toFile().listFiles(filtr);
		List<File> filesList = new ArrayList<>(Arrays.asList(files));
		initializeFileField(filesList);

	}

	@SuppressWarnings("unchecked")
	private <T> void initializeFileField(List<File> files) {

		for (File file : files) {
			listImagies.getItems().add(file);
		}

		listImagies.setCellFactory(new Callback<ListView<File>, ListCell<File>>() {

			@Override
			public ListCell<File> call(ListView<File> param) {

				return new ListCell<File>() {

					@Override
					protected void updateItem(File item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							return;
						}
						String nameFile = item.getName();
						setText(nameFile);
						String pathImage = "file:" + item.getAbsolutePath();
						Image img = new Image(pathImage);
						Image img2 = new Image(pathImage, 20, 20, true, true);
						testImage.setImage(img);
						Label label = new Label();
						ImageView iv = new ImageView(img2);
						HBox bar = new HBox(iv, label);
						bar.setAlignment(Pos.CENTER_LEFT);
						setGraphic(bar);
					};

				};
			}

		});
		listImagies.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<File>() {

			@Override
			public void changed(ObservableValue<? extends File> observable, File oldValue, File newValue) {
				String pathImage = "file:" + newValue.getAbsolutePath();
				Image img = new Image(pathImage);
				testImage.setImage(img);
			}
		});

		slider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				testImage.setScaleX(newValue.doubleValue());
				testImage.setScaleY(newValue.doubleValue());
			}
		});

	}

}
