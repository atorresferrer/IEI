package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;

public class VistaPrincipalController implements Initializable {

	@FXML
	private ListView<CheckBox> listaArticulos;

	@FXML
	private CheckBox seleccionAmazon;

	@FXML
	private CheckBox seleccionCorteIngles;

	@FXML
	private CheckBox seleccionMediaMarkt;

	@FXML
	private CheckBox seleccionFnac;

	@FXML
	private Button botonBuscar;

	@FXML
	private ListView<CheckBox> listaMarcas;

	private ObservableList<CheckBox> articulos;
	private ObservableList<CheckBox> marcas;
	private static ObservableList<Cafetera> cafeteras = FXCollections.observableArrayList();
	//private ArrayList<Cafetera> cafeteras = new ArrayList<>();

	private ArrayList<String> articulosSeleccionados = new ArrayList<>();
	private ArrayList<String> marcasSeleccionadas = new ArrayList<>();
	private ArrayList<String> comerciosSeleccionados = new ArrayList<>();

	private String textoBusqueda = "Cafetera";

	private String[] nombreArticulos = new String[] { "Cafeteras de capsulas", "Cafeteras de goteo",
			"Cafeteras expresso manual", "Cafeteras italianas", "Cafeteras de capsulas", "Cafeteras super automaticas",
			"Cafetera expres", "Cafetera sin categoria" };

	private String[] marcasCafeteras = new String[] { "Bosch", "Breville", "De Longhi", "Digrato", "Electrolux", "Illy",
			"Jata", "Jura", "Kenwood", "Krups", "Lavazza", "Marcilla", "Menz & Konecke", "Miele", "Moulinex",
			"Nescafe Dolce Gusto", "OK", "Orbegozo", "Oster", "Philips", "Russell Hobbs", "Saeco", "Saivod", "Scanpart",
			"Severin", "Smeg", "Solac", "Tassimo", "Taurus", "Tecnhogar", "Tecnnhogar", "Tristar", "Ufesa", "Whirlpool",
			"Wmf" };

	@FXML
	void buscar(ActionEvent event) {
		articulosSeleccionados = obtenerSeleccion(articulos);
		marcasSeleccionadas = obtenerSeleccion(marcas);
		comerciosSeleccionados = comerciosSeleccionados();

		if (comerciosSeleccionados.size() < 1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Informacion necesaria");
			alert.setHeaderText("Comercio no seleccionado");
			alert.setContentText("Se debe seleccionar al menos un comercio");

			alert.showAndWait();
		} else {
			abrirResultado();
		}

	}

	
	public static ObservableList<Cafetera> getCafeteras() { return cafeteras; }
	 

	private void abrirResultado() {
		try {
			FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/application/VistaResultado.fxml"));
			AnchorPane root = (AnchorPane) miCargador.load();
			VistaResultadoController conf = miCargador.<VistaResultadoController>getController();
			// conf.iniciar();
			Scene scene = new Scene(root, 1000, 800);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.showAndWait();
		} catch (IOException e) {
		}
	}

	private ArrayList<String> obtenerSeleccion(ObservableList<CheckBox> list) {
		ArrayList<String> listaSeleccion = new ArrayList<>();

		for (CheckBox cb : list) {
			if (cb.isSelected())
				listaSeleccion.add(cb.getText());
		}
		return listaSeleccion;
	}

	private ArrayList<String> comerciosSeleccionados() {
		ArrayList<String> seleccion = new ArrayList<>();

		if (seleccionAmazon.isSelected())
			seleccion.add(Comercio.AMAZON);
		if (seleccionCorteIngles.isSelected())
			seleccion.add(Comercio.EL_CORTE_INGLES);
		if (seleccionMediaMarkt.isSelected())
			seleccion.add(Comercio.MEDIAMARKT);
		if (seleccionFnac.isSelected())
			seleccion.add(Comercio.FNAC);
		return seleccion;
	}

	private ObservableList<CheckBox> llenarObservableList(String[] listaStrings) {
		ObservableList<CheckBox> lista = FXCollections.observableArrayList();

		for (int i = 0; i < listaStrings.length; i++) {
			lista.add(new CheckBox(listaStrings[i]));
		}

		return lista;

	}

	private void busquedaMediaMarkt() {
		//String exePath = "C:\\Users\\Toni\\eclipse\\geckodriver-master\\geckodriver.exe";
		String exePath = "//IEI_Selenium//geckodriver-master//geckodriver.exe";
		System.setProperty("webdriver.gecko.driver", exePath);
		DesiredCapabilities capabilities = DesiredCapabilities.firefox();
		capabilities.setCapability("marionette", true);
		WebDriver driver = new FirefoxDriver(capabilities);
		WebDriverWait waiting = new WebDriverWait(driver, 5);
		driver.get("https://www.mediamarkt.es/");

		WebElement campoDeBusqueda = driver.findElement(By.id("header__search--input"));
		campoDeBusqueda.click();
		campoDeBusqueda.sendKeys(textoBusqueda);

		waiting.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("/html/body/div[5]/footer/div/div[2]/div/div/div/div/eb-results/div[1]/div/a[1]")));

		WebElement modoLista = driver.findElement(
				By.xpath("/html/body/div[5]/footer/div/div[2]/div/div/div/div/eb-results/div[1]/div/a[1]"));
		modoLista.click();

		// WebElement ventanaBusqueda = driver.findElement(By.id("eb-body"));
		EventFiringWebDriver eventFiringWebDriver = new EventFiringWebDriver(driver);
		waiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"homepage-content\"]")));
		eventFiringWebDriver.executeScript("document.querySelector('#homepage-content').scrollTop=3000");
		/*
		 * JavascriptExecutor jse = (JavascriptExecutor) driver;
		 * jse.executeScript("self.scrollBy(0,-1000)");
		 * //((JavascriptExecutor)driver).executeScript(
		 * "arguments[0].scrollIntoView(true)", ventanaBusqueda); window./
		 */
	}

	private void busquedaElCorteIngles() {
		String exePath = "C:\\Users\\Toni\\eclipse\\geckodriver-master\\geckodriver.exe";
		System.setProperty("webdriver.gecko.driver", exePath);
		DesiredCapabilities capabilities = DesiredCapabilities.firefox();
		capabilities.setCapability("marionette", true);
		WebDriver driver = new FirefoxDriver(capabilities);
		WebDriverWait waiting = new WebDriverWait(driver, 10);

		// Cargar pagina con busqueda de cafeteras
		driver.get("http://www.elcorteingles.es/search/?itemsPerPage=36&s=cafetera");

		//Contamos el numero de elementos li de la barra de paginas para saber en que posicion esta el boton siguiente
		ArrayList<WebElement> elementos = (ArrayList<WebElement>) driver
				.findElements(By.xpath("/html/body/div[1]/div[3]/div[2]/div[1]/div[3]/ul/li"));
		int numElementos = elementos.size();

		//Inicializamos el elemento siguiente
		WebElement siguiente = driver
				.findElement(By.xpath("/html/body/div[1]/div[3]/div[2]/div[1]/div[3]/ul/li[" + numElementos + "]"));
		String windowHandler = driver.getWindowHandle();
		
		
		
		boolean ultimaPagina = false;
		//Mientras siguiente tenga una etiqueta "a" iran poasando las paginas
		while (!ultimaPagina) {
			//Esperamos a que se cargue el boton siguiente
			//waiting.until(ExpectedConditions.visibilityOf(siguiente));
			driver.switchTo().activeElement();
			waiting.withTimeout(5, TimeUnit.SECONDS);
			WebElement productList = driver.findElement(By.id("product-list"));
			List<WebElement> elementosLi = productList.findElements(By.xpath("./ul/li"));
			//List<WebElement> elementosLi = driver.findElements(By.xpath("/html/body/div[2]/div[3]/div[2]/div[1]/ul/li"));
			
			Iterator<WebElement> iter = elementosLi.iterator();
			while (iter.hasNext()) {
				WebElement itemActual = iter.next();
				
				// Sacamos la informacion de la cafetera
				String marca = itemActual.findElement(By.xpath("./div/div[2]/div[1]/h4")).getText();
				String informacion = itemActual.findElement(By.xpath("./div/div[2]/div[1]/h3/a[1]"))
						.getAttribute("title");
				String precio = itemActual.findElement(By.xpath("./div/div[2]/div[2]/span[1]")).getText();
				

				// Guardamos la informacion en la lista de cafeteras
				Cafetera cafetera = new Cafetera(marca, informacion, "tipo");
				cafetera.setPrecio(Comercio.EL_CORTE_INGLES, precio);
				cafeteras.add(cafetera);

			}
			
			/* Mientras no estas en la ultima pagina iras haciendo click en siguiente
			  Cuando hagas el click para entrar en la ultima pagina, hara una ultima iteracion y cambia ultimaPagina a true
			  Se hace asi para poder sacar la informacion de la ultima pagina */
			if(siguiente.findElement(By.xpath(".//*")).getTagName().equals("span")) ultimaPagina = true;
			else {
				siguiente.click();
				siguiente = driver
						.findElement(By.xpath("/html/body/div[1]/div[3]/div[2]/div[1]/div[3]/ul/li[" + numElementos + "]"));
			}
		}

		driver.quit();

		// System.out.println(elementosLi.get(1).findElement(By.xpath("./div[1]/div[1]/a/img")).click(););
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// busquedaMediaMarkt();
		busquedaElCorteIngles();
		articulos = llenarObservableList(nombreArticulos);
		listaArticulos.setItems(articulos);
		marcas = llenarObservableList(marcasCafeteras);
		listaMarcas.setItems(marcas);

	}

}