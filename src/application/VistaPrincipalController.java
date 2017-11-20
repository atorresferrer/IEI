package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
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
	
	@FXML
	private ListView<CheckBox> listaArticulos;

	private ObservableList<CheckBox> articulos;
	private ObservableList<CheckBox> marcas;
	
	// Informacion filtros
	private ArrayList<String> articulosSeleccionados = new ArrayList<>();
	private ArrayList<String> marcasSeleccionadas = new ArrayList<>();
	private ArrayList<String> comerciosSeleccionados = new ArrayList<>();
	
	//Almacenamiento de todas as cafeteras
	private ArrayList<Cafetera> cafeteras = new ArrayList<>();
	private static ArrayList<Cafetera> cafeterasFiltradas = new ArrayList<>();

	private ArrayList<String> nombreArticulos = Cafetera.getTiposDeCafeteras();

	private ArrayList<String> marcasCafeteras = new ArrayList<>();

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		busquedaMediaMarkt();
		busquedaElCorteIngles();
		cargarMarcasCafeteras();
		articulos = llenarObservableList(nombreArticulos);
		listaArticulos.setItems(articulos);
		marcas = llenarObservableList(marcasCafeteras);
		listaMarcas.setItems(marcas);

	}
	
	/*
	 * Cuando pinchemos en el boton...
	 */
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
			cafeterasFiltradas = filtrarBusqueda(comerciosSeleccionados, marcasSeleccionadas, articulosSeleccionados, cafeteras);
			abrirResultado();
		}

	}
	

	/*
	 * Devuelve las cafeteras para poder recuperarlas desde la ventana de resultados,
	 * lo hacemos estatico para no tener que crear una instancia de VistaPrincipalController
	 */
	public static ArrayList <Cafetera> getCafeterasFiltradas() { return cafeterasFiltradas ; } 

	private void abrirResultado() {
		try {
			FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/application/VistaResultado.fxml"));
			AnchorPane root = (AnchorPane) miCargador.load();
			Scene scene = new Scene(root, 1000, 800);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.showAndWait();
		}
		catch (IOException e) {}
		catch (NullPointerException e) {}
		
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

	private ObservableList<CheckBox> llenarObservableList(ArrayList<String> listaStrings) {
		ObservableList<CheckBox> lista = FXCollections.observableArrayList();

		
		for(String elemento : listaStrings)
			lista.add(new CheckBox(elemento));
		
		return lista;
	}
	
	/*
	 * ***************************************************************************************
	 *                     WRAPER MEDIA MARKT
	 * ***************************************************************************************
	 * 
	 */

	private void busquedaMediaMarkt() {
		String exePath = "chromedriver//chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", exePath);
		ChromeOptions opciones = new ChromeOptions();
		opciones.addArguments("--start-maximized");
		WebDriver driver = new ChromeDriver(opciones);
		WebDriverWait waiting = new WebDriverWait(driver, 10);
		Actions actions = new Actions(driver);

		driver.get("https://www.mediamarkt.es/");
		WebElement barraBusqueda = driver.findElement(By.id("header__search--input"));
		barraBusqueda.click();
		barraBusqueda.sendKeys("cafeteras\n");

		waiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"eb-results-view\"]/a[1]")));
		WebElement botonLista = driver.findElement(By.xpath("//*[@id=\"eb-results-view\"]/a[1]"));
		botonLista.click();

		ArrayList<WebElement> elementos = (ArrayList<WebElement>) driver.findElements(By.xpath("//*[@id=\"eb-results\"]/data-eb-result"));
		int numeroElementosActual = elementos.size();
		//Iteramos hasta que al buscar el ultimo elemento de la iteracion anterior + 1, no lo localiza y sale del bucle
		try {
			do {
				actions.keyDown(Keys.CONTROL).sendKeys(Keys.END).perform(); /*scroll*/
				String ruta = "//*[@id=\"eb-results\"]/data-eb-result["+ (numeroElementosActual + 1) + "]";
				waiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath(ruta)));
				numeroElementosActual = driver.findElements(By.xpath("//*[@id=\"eb-results\"]/data-eb-result")).size();

			}while(true);
			//Capturamos la excepcion de no localizar el elemento para que no aborte el programa y continuamos
		}catch(Exception e){System.out.println(numeroElementosActual);}

		elementos = (ArrayList<WebElement>) driver.findElements(By.xpath("//*[@id=\"eb-results\"]/data-eb-result"));
		for(int i = 0; i < elementos.size(); i++) {
			//Sacamos la informacion iterando en las cafeteras
			String informacion = elementos.get(i).findElement(By.xpath("./div/a[2]/span[1]")).getText();
			String marca = elementos.get(i).findElement(By.xpath("./div/a[1]/div[1]/img")).getAttribute("alt");
			String precio = elementos.get(i).findElement(By.xpath("./div/div[2]/div/div/div")).getText();

			
			//Normalizamos toda la informacion de la cafetera
			marca = normalizarString(marca);
			String tipo = asignarTipoMediaMarkt((extraerCategoriaMediaMarkt(informacion)));
			String modelo = extraerModeloMediaMarkt(informacion, marca);
			System.out.println("marca  = " + marca + " \nTipo = " + tipo + " \nModelo = " + modelo + "\nPrecio = " + precio + "\n");
			
			
			// Guardamos la informacion en la lista de cafeteras
			Cafetera cafetera = new Cafetera(marca, modelo, tipo);
			cafetera.setPrecio(Comercio.MEDIAMARKT, precio);
			cafeteras.add(cafetera);
		}
		driver.quit();
	}
	
	
	/*
	 * Extractor de categorias de la informacion de Media Markt
	 */
	
	private String extraerCategoriaMediaMarkt(String info) {
			String separador = "-";
			String categoria = info;
			
			// Sacamos el substring hasta el guion, si no tiene devolvemos toda la informacion
			if(info.indexOf(separador) != -1)
				categoria = info.substring(0,info.indexOf(separador));
			
		return categoria;
	}
	
	/*
	 * Extractor de categorias de la informacion de Mediamarkt
	 */
	
	private String extraerModeloMediaMarkt(String info, String marca) {
			if(marca.equals("")) marca = "Sin Marca";
			//Lo pasamos todo a minusculas
			String infoAux = info.toLowerCase();
			marca = marca.toLowerCase();
			// Sacamos el substring hasta la marca
			String modelo = info;
			//Caso especifico
			if(marca.equals("Menz & Konecke"))
				marca = "Menz and Konecke";
			if(infoAux.indexOf(marca) != -1)
				modelo = info.substring(infoAux.indexOf(marca) + marca.length());
			
		return modelo;
	}
	
	
	/*
	 * Asignar el tipo especifico de la marca a un tipo de cafetera unificado
	 */
	
	private String asignarTipoMediaMarkt(String tipo) {
		tipo = normalizarString(tipo);
		if(tipo.contains("Superautomática")) return Cafetera.CAFETERA_SUPERAUTOMATICA;
		else if(tipo.contains("Express")) return Cafetera.CAFETERA_EXPRESS;
		else if(tipo.contains("Exprés")) return Cafetera.CAFETERA_EXPRESS;
		else if(tipo.contains("Goteo")) return Cafetera.CAFETERA_DE_GOTEO;
		else if(tipo.contains("Tradicional")) return Cafetera.CAFETERA_TRADICIONAL;
		else if(tipo.contains("Cápsulas")) return Cafetera.CAFETERA_DE_CAPSULAS;
		else if(tipo.startsWith("Cafetera")) return Cafetera.CAFETERA_DE_CAPSULAS;
		else return "SIN CATEGORIA";
	}
	
	
	

	/*
	 * ***************************************************************************************
	 *                     WRAPER EL CORTE INGLES 
	 * ***************************************************************************************
	 * 
	 */
	
	
	private void busquedaElCorteIngles() {

		String exePath = "geckodriver-master//geckodriver.exe";
		System.setProperty("webdriver.gecko.driver", exePath);
		DesiredCapabilities capabilities = DesiredCapabilities.firefox();
		capabilities.setCapability("marionette", true);
		@SuppressWarnings("deprecation")
		WebDriver driver = new FirefoxDriver(capabilities);
		WebDriverWait waiting = new WebDriverWait(driver, 10);

		// Cargar pagina con busqueda de cafeteras
		driver.get("http://www.elcorteingles.es/search/?itemsPerPage=36&s=cafetera");

		//Contamos el numero de elementos li de la barra de paginas para saber en que posicion esta el boton siguiente
		ArrayList<WebElement> elementos = (ArrayList<WebElement>) driver
				.findElements(By.xpath("/html/body/div[1]/div[3]/div[2]/div[1]/div[3]/ul/li"));
		int numElementos = elementos.size();

		//Inicializamos el elemento siguiente
		WebElement siguiente = driver.findElement(By.xpath("/html/body/div[1]/div[3]/div[2]/div[1]/div[3]/ul/li[" + numElementos + "]"));

		boolean ultimaPagina = false;
		//Mientras siguiente tenga una etiqueta "a" iran poasando las paginas
		while (!ultimaPagina) {
			//Esperamos a que se cargue el boton siguiente
			driver.switchTo().activeElement();
			waiting.withTimeout(5, TimeUnit.SECONDS);
			WebElement productList = driver.findElement(By.id("product-list"));
			List<WebElement> elementosLi = productList.findElements(By.xpath("./ul/li"));
			
			Iterator<WebElement> iter = elementosLi.iterator();
			while (iter.hasNext()) {
				WebElement itemActual = iter.next();
				
				// Sacamos la informacion de la cafetera
				String marca = itemActual.findElement(By.xpath("./div/div[2]/div[1]/h4")).getText();
				String informacion = itemActual.findElement(By.xpath("./div/div[2]/div[1]/h3/a[1]"))
						.getAttribute("title");
				String precio = itemActual.findElement(By.xpath("./div/div[2]/div[2]/span[1]")).getText();
				if(precio.equals("Desde"))
					precio = itemActual.findElement(By.xpath("./div/div[2]/div[2]/span[2]")).getText();

				//Normalizamos toda la informacion de la cafetera
				marca = normalizarString(marca);
				
				//Caso especifico
				if(marca.toLowerCase().equals("de'longhi"))
					marca = "De Longhi";
				
				String tipo = asignarTipoCorteIngles(extraerCategoriaElCorteIngles(informacion, marca));
				String modelo = extraerModeloElCorteIngles(informacion, marca);
				
				//Cremaos un objeto cafetera
				Cafetera cafetera = new Cafetera(marca, modelo, tipo);
				cafetera.setPrecio(Comercio.EL_CORTE_INGLES, precio);
				
				// Guardamos la cafetera en la lista de cafeteras
				cafeteras.add(cafetera);
			}
			
			/* Mientras no estas en la ultima pagina iras haciendo click en siguiente.
			  Cuando hagas el click para entrar en la ultima pagina, hara una ultima iteracion y cambia ultimaPagina a true
			  Se hace asi para poder sacar la informacion de la ultima pagina */
			if(siguiente.findElement(By.xpath(".//*")).getTagName().equals("span"))
				ultimaPagina = true;
			else {
				siguiente.click();
				siguiente = driver
						.findElement(By.xpath("/html/body/div[1]/div[3]/div[2]/div[1]/div[3]/ul/li[" + numElementos + "]"));
			}
		}

		driver.quit();
	}
	
	
	/*
	 * Extractor de categorias de la informacion de El Corte Ingles
	 */
	
	private String extraerCategoriaElCorteIngles(String info, String marca) {
			if(marca.equals("")) marca = "Bra";
			//Lo pasamos todo a minusculas
			String infoAux = info.toLowerCase();
			marca = marca.toLowerCase();
			// Sacamos el substring hasta la marca
			String categoria = info;
			if(infoAux.indexOf(marca) != -1)
				categoria = info.substring(0,infoAux.indexOf(marca));
			
		return categoria;
	}
	
	/*
	 * Extractor de categorias de la informacion de El Corte Ingles
	 */
	
	private String extraerModeloElCorteIngles(String info, String marca) {
			if(marca.equals("")) marca = "Bra";
			//Lo pasamos todo a minusculas
			String infoAux = info.toLowerCase();
			marca = marca.toLowerCase();
			// Sacamos el substring hasta la marca
			String modelo = info;
			if(infoAux.indexOf(marca) != -1)
				modelo = info.substring(infoAux.indexOf(marca) + marca.length());
			
		return modelo;
	}
	
	/*
	 * Asignar el tipo especifico de la marca a un tipo de cafetera unificado
	 */
	
	private String asignarTipoCorteIngles(String tipo) {
		if(tipo.contains("super")) return Cafetera.CAFETERA_SUPERAUTOMATICA;
		else if(tipo.contains("goteo")) return Cafetera.CAFETERA_DE_GOTEO;
		else if(tipo.contains("italiana eléctrica")) return Cafetera.CAFETERA_ITALIANA_ELECTRICA;
		else if(tipo.contains("moka")) return Cafetera.CAFETERA_ITALIANA_ELECTRICA;
		else if(tipo.contains("cápsulas")) return Cafetera.CAFETERA_DE_CAPSULAS;
		else if(tipo.contains("espresso automática")) return Cafetera.CAFETERA_DE_CAPSULAS;
		else if(tipo.contains("espresso manual")) return Cafetera.CAFETERA_EXPRESS;
		else if(tipo.contains("émbolo")) return Cafetera.CAFETERA_EMBOLO;
		else if(tipo.contains("Máquina de café")) return Cafetera.MAQUINA_CAFE;
		else if(tipo.contains("italiana")) return Cafetera.CAFETERA_TRADICIONAL;
		else if(tipo.startsWith("Cafetera")) return Cafetera.OTRAS;
		else return "SIN CATEGORIA";	
	}
	
	
	/*
	 * Normalizamos los Strings para dejar la primera letra de cada palabra en mayusculas y las demas minusculas
	 * 
	 */
	private String normalizarString(String linea) {
		if(!linea.equals("")) {
			linea = linea.toLowerCase();
			String[] palabras = linea.split(" ");

			linea = "";

			for(int i = 0; i < palabras.length; i++ ) {
				palabras[i].trim(); // Quitamos posibles espacios en blanco antes y despues de la palabra
				palabras[i] = palabras[i].substring(0, 1).toUpperCase()  //Cojo la primera letra y la pongo en mayuscula
						+ palabras[i].substring(1); // concateno el resto de la palabra en minuscula
				linea = linea + palabras[i] + " ";
			}
		}

		return linea;
	}
	
	
	/*
	 * Filtra la busqueda en funcion de los filtros seleccionados en la ventana principal
	 */
	
	public ArrayList<Cafetera> filtrarBusqueda(ArrayList<String> comerciosSeleccionados,
			ArrayList<String> marcasSeleccionadas, ArrayList<String> articulosSeleccionados,
			ArrayList<Cafetera> cafeteras) {

		cafeteras = filtroComercios(cafeteras, comerciosSeleccionados);
		cafeteras = filtroMarcas(cafeteras, marcasSeleccionadas);
		cafeteras = filtroArticulos(cafeteras, articulosSeleccionados);

		return cafeteras;
	}
	
	
	/* Filtro para comercios*/
	
	private ArrayList<Cafetera> filtroComercios(ArrayList<Cafetera> cafeteras, ArrayList<String> comerciosSeleccionados){
		ArrayList<Cafetera> resultado = new ArrayList<>();
		
		if(comerciosSeleccionados.size() != 0) { // Si no hay comercios seleccionados entonces nos quedamos con todas las cafeteras
			for(Cafetera cafetera : cafeteras)  //para cada cafetera 
				for(String comercio : comerciosSeleccionados) // recorremos los comercios seleccionados
					for(Comercio comercioCafetera : cafetera.getListaComercios()) // y los comercios de la cafetera
						if (comercioCafetera.isExiste() && comercioCafetera.getNombre().equals(comercio)) // si la cafetera tiene precio en el comercio seleccionado
							resultado.add(cafetera); //la añadimos a la lista final
		}
		else
			resultado = cafeteras;
		
		return resultado;
	}
	
	
	/* Filtro para marcas*/
	
	private ArrayList<Cafetera> filtroMarcas(ArrayList<Cafetera> cafeteras, ArrayList<String> marcasSeleccionadas){
		ArrayList<Cafetera> resultado = new ArrayList<>();
		
		if(marcasSeleccionadas.size() != 0) { // Si no hay marcas seleccionadas entonces nos quedamos con todas las cafeteras
			for(Cafetera cafetera : cafeteras)  //para cada cafetera 
				for(String marca : marcasSeleccionadas) // recorremos las marcas seleccionadas
					if(cafetera.getMarca().equals(marca)) // preguntando si coincide con la de la cafetera
						resultado.add(cafetera); //la añadimos a la lista final
		}
		else
			resultado = cafeteras;

		return resultado;
	}
	
	
	
	/* Filtro para articulos*/
	
	private ArrayList<Cafetera> filtroArticulos(ArrayList<Cafetera> cafeteras, ArrayList<String> articulosSeleccionados){
		ArrayList<Cafetera> resultado = new ArrayList<>();
		
		if(articulosSeleccionados.size() != 0) { // Si no hay articulos seleccionados entonces nos quedamos con todas las cafeteras
			for(Cafetera cafetera : cafeteras)  //para cada cafetera 
				for(String articulo : articulosSeleccionados) // recorremos los articulos seleccionados
					if(cafetera.getTipo().equals(articulo)) // preguntando si coincide con el de la cafetera
						resultado.add(cafetera); //la añadimos a la lista final
		}
		else
			resultado = cafeteras;

		return resultado;
	}
	
	
	private boolean comprobarSiExiste(String marca) {
		boolean existe = false;
		for(String cafetera : marcasCafeteras)
			if(marca.equals(cafetera)){
				existe = true; break;
			}
		return existe;
	}
	
	private void cargarMarcasCafeteras() {
		for(Cafetera cafetera : cafeteras) 
			if(!comprobarSiExiste(cafetera.getMarca()))
				marcasCafeteras.add(cafetera.getMarca());
	}

}
