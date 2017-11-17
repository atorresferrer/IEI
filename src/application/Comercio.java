package application;

public class Comercio {
	
	private String nombre;
	private String precio;
	private boolean existe;
	public final static String AMAZON = "Amazon";
	public final static String EL_CORTE_INGLES = "ElCorteIngles";
	public final static String MEDIAMARKT = "MediaMarkt";
	public final static String FNAC = "Fnac";
	
	
	public Comercio(String nombre) {
		this.nombre = nombre;
		this.existe = false;
	}
	public Comercio(String nombre, String precio, boolean existe) {
		this.nombre = nombre;
		this.precio = precio;
		this.existe = existe;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPrecio() {
		return precio;
	}
	public void setPrecio(String precio) {
		this.precio = precio;
	}
	public boolean isExiste() {
		return existe;
	}
	public void setExiste(boolean existe) {
		this.existe = existe;
	}
}
