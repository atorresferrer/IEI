package application;

import java.util.ArrayList;
import java.util.List;

public class Cafetera {
	private String marca;
	private String modelo;
	private String tipo;
	private List<Comercio> listaComercios = new ArrayList<Comercio>();
	
	public final static String CAFETERA_TRADICIONAL = "Cafetera Tradicional";
	public final static String CAFETERA_DE_CAPSULAS = "Cafetera de capsulas";
	public final static String CAFETERA_SUPERAUTOMATICA = "Cafetera Superautomatica";
	public final static String CAFETERA_DE_GOTEO = "Cafetera de Goteo";
	public final static String CAFETERA_EXPRESS = "Cafetera Express";
	public final static String CAFETERA_ESPRESSO_AUTOMATICA = "Cafetera Espresso Automática";
	public final static String CAFETERA_ITALIANA_ELECTRICA = "Cafetera Italiana Electrica";
	public final static String OTRAS = "Otras cafeteras";
	public final static String CAFETERA_EMBOLO = "Cafetera de émbolo";
	public final static String MAQUINA_CAFE = "Máquina de café";
	
	
	public Cafetera(String marca, String modelo, String tipo) {
		this.marca = marca;
		this.modelo = modelo;
		this.tipo = tipo;
		this.listaComercios.add(new Comercio(Comercio.AMAZON));
		this.listaComercios.add(new Comercio(Comercio.EL_CORTE_INGLES));
		this.listaComercios.add(new Comercio(Comercio.MEDIAMARKT));
		this.listaComercios.add(new Comercio(Comercio.FNAC));
	}
	
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public String getModelo() {
		return modelo;
	}
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	public List<Comercio> getListaComercios() {
		return listaComercios;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public void setListaComercios(List<Comercio> listaComercios) {
		this.listaComercios = listaComercios;
	}
	private int indiceLista(String nombre) throws Exception {
		int index = 0;
		while(!nombre.equals(listaComercios.get(index).getNombre()) && listaComercios.size()> index)
			index++;
		if(index == listaComercios.size()) throw new Exception("No se encuentra el comercio");
		return index;
	}
	
	public void setPrecio(String comercio, String precio) {
		
		try {
			listaComercios.get(indiceLista(comercio)).setPrecio(precio);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getPrecio(String comercio) {
		String precio = "";
		try {
			precio = listaComercios.get(indiceLista(comercio)).getPrecio();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	}
		return precio;
	}
	
	public static String[] getTiposDeCafeteras() {
		String[] tiposDeCafeteras = new String[] { CAFETERA_DE_CAPSULAS, CAFETERA_DE_GOTEO, CAFETERA_EMBOLO,
				CAFETERA_ESPRESSO_AUTOMATICA, CAFETERA_EXPRESS, CAFETERA_ITALIANA_ELECTRICA, CAFETERA_SUPERAUTOMATICA,
				CAFETERA_TRADICIONAL, MAQUINA_CAFE };
		return tiposDeCafeteras;
	}
}
