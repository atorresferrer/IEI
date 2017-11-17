package application;

import java.util.ArrayList;
import java.util.List;

public class Cafetera {
	private String marca;
	private String modelo;
	private String tipo;
	private List<Comercio> listaComercios = new ArrayList<Comercio>();
	
	
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
	
}
