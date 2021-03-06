package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class VistaResultadoController implements Initializable {

	@FXML
	private TableView<Cafetera> tableViewResultado;

	@FXML
	private TableColumn<Cafetera, String> colMarca;

	@FXML
	private TableColumn<Cafetera, String> colModelo;
	
	 @FXML
	 private TableColumn<Cafetera, String> colTipo;

	@FXML
	private TableColumn<Cafetera, String> colAmazon;

	@FXML
	private TableColumn<Cafetera, String> colCorteIngles;

	@FXML
	private TableColumn<Cafetera, String> colMediaMarkt;

	@FXML
	private TableColumn<Cafetera, String> colFnac;

	private ObservableList<Cafetera> listaCafeteras;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//Recuperamos la informacion de las cafeteras a mostrar
		listaCafeteras = FXCollections.observableArrayList();
		inicializarListaCafeteras(VistaPrincipalController.getCafeterasFiltradas());

		//Asignamos los valores a las columnas del tableView
		colMarca.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getMarca()));
		colModelo.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getModelo()));
		colTipo.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getTipo()));
		colAmazon.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getPrecio(Comercio.AMAZON)));
		colCorteIngles.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getPrecio(Comercio.EL_CORTE_INGLES)));
		colMediaMarkt.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getPrecio(Comercio.MEDIAMARKT)));
		colFnac.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getPrecio(Comercio.FNAC)));

		//Inicializamos el tableView
		tableViewResultado.setItems(listaCafeteras);
	}
	
	private void inicializarListaCafeteras (ArrayList<Cafetera> cafeteras) {
		for(Cafetera cafetera : cafeteras)
			listaCafeteras.add(cafetera);
	}
}
