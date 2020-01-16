package com.gdx.bomberman.screens;

import java.io.File;
import java.net.InetAddress;
import java.util.*;

import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.scene.image.*;
import javafx.collections.*;
import javafx.application.*;
import javafx.geometry.*;


public class ClientFx extends Application {
	private Scene scene;
	/**View for Log in panel, that changed to client panel if needed.*/
	@Override
	public void start(Stage stage){
		//Loging in
		GridPane grid = new GridPane();
		Text t_name = new Text("Username:");
		TextField name = new TextField();
		name.setPromptText("Enter your username here");
		name.setPrefColumnCount(10);

		Text t_folder_path = new Text("Folder path:");
		TextField folder_path = new TextField();
		folder_path.setPromptText("Enter path to your folder");

		Label comment = new Label(" ");

		HBox buttons = new HBox();
		buttons.setSpacing(10);

		Button submit = new Button("Submit");

		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(5);
		grid.setHgap(5);
		grid.setAlignment(Pos.CENTER);
		//texts
		grid.add(t_name,0,0);
		grid.add(t_folder_path,0,1);
		//text fields
		grid.add(name,1,0);
		grid.add(folder_path,1,1);
		//buttons
		grid.add(buttons,1,2,2,1);
		//label.setAlignment(Pos.CENTER);
		grid.add(comment,0,2,2,10);

		scene = new Scene(grid ,350, 300);
		stage.setTitle("Log in");
		Color color = Color.web("#a3beed");//#3264dd
		scene.setFill(color);
		stage.setScene(scene);
		stage.show();

	}
	public static void main(String args[]){
		launch(args);
	}
}