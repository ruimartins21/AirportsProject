package airportsProject.gui;

import airportsProject.Airline;
import airportsProject.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class NewAirlineDialogController {
    @FXML
    private TextField airlineName;
    @FXML
    private TextField airlineCountry;

    private String airlineInitialName;
    private boolean isEdit = false;

    @FXML
    void getNewAirlineInput(){
        if(!isEdit){
            if(!airlineName.getText().isEmpty() && !airlineCountry.getText().isEmpty()){
                Utils.getInstance().newAirline(new Airline(airlineName.getText(), airlineCountry.getText()));
            }
        }else{
            if(!airlineName.getText().isEmpty() && !airlineCountry.getText().isEmpty()){
                Utils.getInstance().editAirline(airlineInitialName, airlineName.getText(), airlineCountry.getText());
            }
        }
        // update local airline initial name in case the user chooses to edit again without leaving the page
        this.airlineInitialName = airlineName.getText();
        // close the dialog window
        airlineName.getParent().getParent().getScene().getWindow().hide();
    }

    public void setInputs(String name, String country){
        airlineName.setText(name);
        airlineCountry.setText(country);
        this.airlineInitialName = name;
        this.isEdit = true;
    }

    public String getInitialName(){
        return airlineInitialName;
    }
}
