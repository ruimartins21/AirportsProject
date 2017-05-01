package airportsProject.gui;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

public class MapTestController {
    private int width = 1536;
    private int height = 768;
    @FXML
    private VBox root_vbox;
    @FXML
    private ListView<String> map_listview;
    @FXML
    private ScrollPane map_scrollpane;
    @FXML
    private Slider zoom_slider;
    @FXML
    private MenuButton map_pin;
    @FXML
    private MenuItem pin_info;
    @FXML
    private ToggleButton contrast_togglebutton;
    @FXML
    private ToggleButton size_togglebutton;

    private final HashMap<String, ArrayList<Comparable<?>>> hm = new HashMap<>();
    Group zoomGroup;

    @FXML
    void initialize() {
        double longitude, latitude;
        final Geocoder geocoder = new Geocoder();
        GeocoderRequest geocoderRequest;
        geocoderRequest = new GeocoderRequestBuilder().setAddress("Porto").setLanguage("en").getGeocoderRequest();
        try {
            GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
            latitude  = geocoderResponse.getResults().get(0).getGeometry().getLocation().getLat().doubleValue();
            longitude = geocoderResponse.getResults().get(0).getGeometry().getLocation().getLng().doubleValue();
            longitude = (width) * (180 + longitude) / 360;
            latitude  = (height) * (90 - latitude) / 180;
            hm.put("Porto", new ArrayList<>(Arrays.asList(longitude, latitude, "Code: OPO")));
        }catch (IOException e){
            e.printStackTrace();
        }
        geocoderRequest = new GeocoderRequestBuilder().setAddress("Istambul").setLanguage("en").getGeocoderRequest();
        try {
            GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
            latitude  = geocoderResponse.getResults().get(0).getGeometry().getLocation().getLat().doubleValue();
            longitude = geocoderResponse.getResults().get(0).getGeometry().getLocation().getLng().doubleValue();
            longitude = (width) * (180 + longitude) / 360;
            latitude  = (height) * (90 - latitude) / 180;
            hm.put("Istambul", new ArrayList<>(Arrays.asList(longitude, latitude, "Code: IST")));
        }catch (IOException e){
            e.printStackTrace();
        }
        geocoderRequest = new GeocoderRequestBuilder().setAddress("Washington DC").setLanguage("en").getGeocoderRequest();
        try {
            GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
            latitude  = geocoderResponse.getResults().get(0).getGeometry().getLocation().getLat().doubleValue();
            longitude = geocoderResponse.getResults().get(0).getGeometry().getLocation().getLng().doubleValue();
            longitude = (width) * (180 + longitude) / 360;
            latitude  = (height) * (90 - latitude) / 180;
            hm.put("Washington DC", new ArrayList<>(Arrays.asList(longitude, latitude, "Code: WAS")));
        }catch (IOException e){
            e.printStackTrace();
        }
        geocoderRequest = new GeocoderRequestBuilder().setAddress("Brisbane").setLanguage("en").getGeocoderRequest();
        try {
            GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
            latitude  = geocoderResponse.getResults().get(0).getGeometry().getLocation().getLat().doubleValue();
            longitude = geocoderResponse.getResults().get(0).getGeometry().getLocation().getLng().doubleValue();
            longitude = (width) * (180 + longitude) / 360;
            latitude  = (height) * (90 - latitude) / 180;
            hm.put("Australia", new ArrayList<>(Arrays.asList(longitude, latitude, "Code: AUS")));
        }catch (IOException e){
            e.printStackTrace();
        }
        geocoderRequest = new GeocoderRequestBuilder().setAddress("New Zeland").setLanguage("en").getGeocoderRequest();
        try {
            GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
            latitude  = geocoderResponse.getResults().get(0).getGeometry().getLocation().getLat().doubleValue();
            longitude = geocoderResponse.getResults().get(0).getGeometry().getLocation().getLng().doubleValue();
            longitude = (width) * (180 + longitude) / 360;
            latitude  = (height) * (90 - latitude) / 180;
            hm.put("New Zeland", new ArrayList<>(Arrays.asList(longitude, latitude, "Code: NEW")));
        }catch (IOException e){
            e.printStackTrace();
        }
//        hm.put("Porto", new ArrayList<>(Arrays.asList(731,184128, 208,3783978667, "Code: OPO")));
//        hm.put("Istambul", new ArrayList<>(Arrays.asList(890.9247402667, 209.1270485333, "Code: IST")));
//        hm.put("Washington", new ArrayList<>(Arrays.asList(437.5213909333, 217.7731754667, "Code: WAS")));
//        hm.put("Australia", new ArrayList<>(Arrays.asList(1407.3034922667, 510.3590613333, "Code: AUS")));
//        hm.put("New Zeland", new ArrayList<>(Arrays.asList(1496.5367424, 573.5497088, "Code: NEW")));

        ObservableList<String> names = FXCollections.observableArrayList();
        Set<Entry<String, ArrayList<Comparable<?>>>> set = hm.entrySet();
        Iterator<Entry<String, ArrayList<Comparable<?>>>> i = set.iterator();
        while (i.hasNext()) {
            Map.Entry<String, ArrayList<Comparable<?>>> me = i.next();
            names.add(me.getKey());
        }
        Collections.sort(names);

        map_listview.setItems(names);
        map_pin.setVisible(false);

        zoom_slider.setMin(0.8);
        zoom_slider.setMax(1.5);
        zoom_slider.setValue(1.0);
        zoom_slider.valueProperty().addListener((o, oldVal, newVal) -> zoom((Double) newVal));

        // Wrap scroll content in a Group so ScrollPane re-computes scroll bars
        Group contentGroup = new Group();
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(map_scrollpane.getContent());
        map_scrollpane.setContent(contentGroup);

        // Add large UI styling and make full screen if we are on device
        if (Platform.isSupported(ConditionalFeature.INPUT_TOUCH)) {
            System.out.println("airportapp.Controller.initialize, device detected");
            size_togglebutton.setSelected(true);
            root_vbox.getStyleClass().add("touch-sizes");
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            root_vbox.setPrefSize(bounds.getWidth(), bounds.getHeight());
        }
    }

    @FXML
    void listClicked(MouseEvent event) {
        String item = map_listview.getSelectionModel().getSelectedItem();
        List<Comparable<?>> list = hm.get(item);

        // animation scroll to new position
        double mapWidth = zoomGroup.getBoundsInLocal().getWidth();
        double mapHeight = zoomGroup.getBoundsInLocal().getHeight();
        double scrollH = (Double) list.get(0) / mapWidth;
        double scrollV = (Double) list.get(1) / mapHeight;
        final Timeline timeline = new Timeline();
        final KeyValue kv1 = new KeyValue(map_scrollpane.hvalueProperty(), scrollH);
        final KeyValue kv2 = new KeyValue(map_scrollpane.vvalueProperty(), scrollV);
        final KeyFrame kf = new KeyFrame(Duration.millis(500), kv1, kv2);
        timeline.getKeyFrames().add(kf);
        timeline.play();

        // move the pin and set it's info
        double pinW = map_pin.getBoundsInLocal().getWidth();
        double pinH = map_pin.getBoundsInLocal().getHeight();
        map_pin.setLayoutX((Double) list.get(0) - (pinW / 2));
        map_pin.setLayoutY((Double) list.get(1) - (pinH));
        pin_info.setText((String) list.get(2));
        map_pin.setVisible(true);
    }

    @FXML
    void zoomIn(ActionEvent event) {
//    System.out.println("airportapp.Controller.zoomIn");
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal += 0.1);
    }

    @FXML
    void zoomOut(ActionEvent event) {
//    System.out.println("airportapp.Controller.zoomOut");
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal + -0.1);
    }

    private void zoom(double scaleValue) {
//    System.out.println("airportapp.Controller.zoom, scaleValue: " + scaleValue);
        double scrollH = map_scrollpane.getHvalue();
        double scrollV = map_scrollpane.getVvalue();
        zoomGroup.setScaleX(scaleValue);
        zoomGroup.setScaleY(scaleValue);
        map_scrollpane.setHvalue(scrollH);
        map_scrollpane.setVvalue(scrollV);
    }

    @FXML
    void stylingContrast(ActionEvent event) {
//    System.out.println("airportapp.Controller.stylingContrast");
        if (contrast_togglebutton.isSelected() == true) {
            root_vbox.getStyleClass().add("contrast");
        } else {
            root_vbox.getStyleClass().remove("contrast");
        }
    }

    @FXML
    void stylingSizing(ActionEvent event) {
//    System.out.println("airportapp.Controller.stylingSizing");
        if (size_togglebutton.isSelected() == true) {
            root_vbox.getStyleClass().add("touch-sizes");
        } else {
            root_vbox.getStyleClass().remove("touch-sizes");
        }
    }



}