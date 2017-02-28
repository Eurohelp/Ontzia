/**
 * 
 */
package eus.eurohelp.ontzia.core.service.impl;

/**
 * @author Mikel Egaña Aranguren
 *
 */
public class NodeMeasurement {

	/**
	 * @return the fecha_registro
	 */
	public String getFecha_registro() {
		return fecha_registro;
	}


	private String fecha_registro;
	private String Num_kg_depositado;
	private String Num_m3_depositado;
	private String Num_nivel_bateria;
	private String Num_nivel_llenado;
	private String Num_aperturas;
	
	public NodeMeasurement(String Fecha_registro,String Num_kg_depositado, String Num_m3_depositado, String Num_nivel_bateria,
			String Num_nivel_llenado, String Num_aperturas) {
		super();
		this.fecha_registro = Fecha_registro;
		this.Num_kg_depositado = Num_kg_depositado;
		this.Num_m3_depositado = Num_m3_depositado;
		this.Num_nivel_bateria = Num_nivel_bateria;
		this.Num_nivel_llenado = Num_nivel_llenado;
		this.Num_aperturas = Num_aperturas;
	}
	
	/**
	 * @return the num_kg_depositado
	 */
	public String getNum_kg_depositado() {
		return Num_kg_depositado;
	}


	/**
	 * @return the num_m3_depositado
	 */
	public String getNum_m3_depositado() {
		return Num_m3_depositado;
	}


	/**
	 * @return the num_nivel_bateria
	 */
	public String getNum_nivel_bateria() {
		return Num_nivel_bateria;
	}


	/**
	 * @return the num_nivel_llenado
	 */
	public String getNum_nivel_llenado() {
		return Num_nivel_llenado;
	}


	/**
	 * @return the num_aperturas
	 */
	public String getNum_aperturas() {
		return Num_aperturas;
	}
}
