package com.mycompany.mavenproject1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FXMLController implements Initializable {
    
    private boolean useDefaultData = true;
    private boolean hasFetchedOnlineData = false;
    
    private final ObservableList<String> defaultComboOpts = FXCollections.observableArrayList(
            "Item A",
            "Item B",
            "Item C",
            "Item D",
            "Item E",
            "Item F",
            "Item G"
    );
    
    private ObservableList<String> onlineComboOpts = null;
    
    @FXML
    private Button button;
    
    private ImageView btnImageView;
    private Image defaultBtnImage;
    private Image loadingStateImage;
    
    @FXML
    private ComboBox combo;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        useDefaultData = !useDefaultData;
        if(useDefaultData) {
            combo.setItems(defaultComboOpts);
        } else {
            if(hasFetchedOnlineData) {
                combo.setItems(onlineComboOpts);
            } else {
                // change button graphic with rolling loader
                btnImageView.setImage(loadingStateImage);
                
                Runnable runnable = () -> {
                    try {
                        onlineComboOpts = FXCollections.observableArrayList(parseJsonObjectOnline());
                        TimeUnit.SECONDS.sleep(2);
                        hasFetchedOnlineData = true;
                        combo.setItems(onlineComboOpts);
                    } catch (Exception e) {
                        e.printStackTrace();
                        combo.setItems(FXCollections.observableArrayList(
                            "Online Item A",
                            "Online Item B",
                            "Online Item C",
                            "Online Item D",
                            "Online Item E",
                            "Online Item F",
                            "Online Item G"
                        ));
                    } finally {
                        // change button graphic back to default
                        btnImageView.setImage(defaultBtnImage);
                    }
                };
                
                Thread thread = new Thread(runnable);
                thread.start();
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        defaultBtnImage = new Image(getClass().getResourceAsStream("/accept.png"));
        loadingStateImage = new Image(getClass().getResourceAsStream("/ajax-loader.gif"));
        btnImageView = new ImageView(defaultBtnImage);
        btnImageView.setFitWidth(16);
        btnImageView.setFitHeight(16);
        button.setGraphic(btnImageView);
        button.setTooltip(new Tooltip("This is button's tooltip"));

        combo.setItems(defaultComboOpts);
    }
    
    private List<String> parseJsonObjectOnline() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
       
        //Convert JSON to List of Person objects
    	//Define Custom Type reference for List<Person> type
    	TypeReference<List<Person>> mapType = new TypeReference<List<Person>>() {};
        InputStream inputStream = getClass().getResourceAsStream("/people.json");
        
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
        
    	List<Person> jsonToPersonList = objectMapper.readValue(textBuilder.toString(), mapType);
    	System.out.println("\n2. Convert JSON to List of person objects :");
        
    	List<String> strList = jsonToPersonList.stream()
                .map(p -> p.toString())
                .collect(Collectors.toList());
    	//Print list of person objects output using Java 8
    	return strList;
    }
}
