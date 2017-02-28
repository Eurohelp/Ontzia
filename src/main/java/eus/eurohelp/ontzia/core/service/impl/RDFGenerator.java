/**
 * 
 */
package eus.eurohelp.ontzia.core.service.impl;

import java.util.ArrayList;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * @author Mikel Egaña Aranguren
 *
 */
public class RDFGenerator {

	/**
	 * 
	 */
	public RDFGenerator() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		StringBuilder hostUri = new StringBuilder("http://");
//		hostUri.append(environment.getProperty("amazon.host")).append("/ontzia");
		
		StringBuilder hostUri = new StringBuilder("http://ec2-54-148-16-180.us-west-2.compute.amazonaws.com/ontzia");
		
		Model model = ModelFactory.createDefaultModel();
		model.setNsPrefix("ssn", "http://purl.oclc.org/NET/ssnx/ssn#");
		model.createProperty("http://purl.oclc.org/NET/ssnx/ssn#observedBy");
		model.createProperty("http://purl.oclc.org/NET/ssnx/ssn#observationSamplingTime");
		model.createProperty("http://purl.oclc.org/NET/ssnx/ssn#observationResult");
		model.createProperty("http://purl.oclc.org/NET/ssnx/ssn#hasValue");
		model.createResource("http://purl.oclc.org/NET/ssnx/ssn#Observation");
		model.createResource("http://purl.oclc.org/NET/ssnx/ssn#SensingDevice");
		model.createResource(hostUri.toString() + "/medicion/num_kg_depositado");
		model.createResource(hostUri.toString() + "/medicion/num_m3_depositado");
		model.createResource(hostUri.toString() + "/medicion/num_nivel_bateria");
		model.createResource(hostUri.toString() + "/medicion/num_nivel_llenado");
		model.createResource(hostUri.toString() + "/medicion/num_aperturas");

		createContenedorRDF(model, new Contenedor(), hostUri.toString()); // En new Contenedor va el Bean contenedor
		
		model.write(System.out);

	}
	
	private static void createContenedorRDF (Model model, Contenedor contenedor, String hostUri){
		
		StringBuilder uriContenedorBuilder = new StringBuilder(hostUri);
		String uriContenedor = uriContenedorBuilder.append("/contenedor/"+contenedor.getId_contenedor()).toString();
		Resource SSNsensingDevice = model.getResource("http://purl.oclc.org/NET/ssnx/ssn#SensingDevice");
		Resource contenedorResource = model.createResource(uriContenedor)
				.addProperty(RDF.type, SSNsensingDevice);
		
		ArrayList<NodeMeasurement> nodeMeasurements = new ArrayList <NodeMeasurement> ();
		nodeMeasurements.add(new NodeMeasurement("2014-03-01 00:00:00","3.969","913","14","1.476","23"));
		nodeMeasurements.add(new NodeMeasurement("2014-04-01 00:00:00","5.031","1.089","59","11","14"));
		nodeMeasurements.add(new NodeMeasurement("2014-05-01 00:00:00","2.790","581","21","1.813","34"));
		
		
		Resource SSNObservation = model.getResource("http://purl.oclc.org/NET/ssnx/ssn#Observation");
		
		Property SSNobservedBy = model.getProperty("http://purl.oclc.org/NET/ssnx/ssn#observedBy");
		Property SSNobservationSamplingTime = model.getProperty("http://purl.oclc.org/NET/ssnx/ssn#observationSamplingTime");
		
		
		for(NodeMeasurement nodeMeasurement : nodeMeasurements){
			StringBuilder obsURIBuilder = new StringBuilder(hostUri);
			
			String fechaRegistro = nodeMeasurement.getFecha_registro().replaceAll(" 00:00:00","");
			String uriObservacion = obsURIBuilder.append("/observacion/"+ contenedor.getId_contenedor() + "-" + fechaRegistro).toString();
			
			Resource observacion = model.createResource(uriObservacion)
					.addProperty(RDF.type, SSNObservation)
						.addProperty(SSNobservedBy, contenedorResource)
							.addLiteral(SSNobservationSamplingTime, model.createTypedLiteral(fechaRegistro, "http://www.w3.org/2001/XMLSchema#date"));
			
			StringBuilder medicionURIBuilder = new StringBuilder(hostUri);
			String uriMedicionClase = medicionURIBuilder.append("/medicion/").toString();
			createContenedorObservationResult(uriObservacion, 
					"num_kg_depositado", 
					model.getResource(uriMedicionClase + "num_kg_depositado"),
					nodeMeasurement.getNum_kg_depositado(),  
					model);
			
			createContenedorObservationResult(uriObservacion, 
					"num_m3_depositado", 
					model.getResource(uriMedicionClase + "num_m3_depositado"),
					nodeMeasurement.getNum_m3_depositado(), 
					model);
			
			createContenedorObservationResult(uriObservacion, 
					"num_nivel_bateria", 
					model.getResource(uriMedicionClase + "num_nivel_bateria"),
					nodeMeasurement.getNum_nivel_bateria(), 
					model);
			 
			createContenedorObservationResult(uriObservacion, 
					"num_nivel_llenado", 
					model.getResource(uriMedicionClase + "num_nivel_llenado"),
					nodeMeasurement.getNum_nivel_llenado(), 
					model);
			
			createContenedorObservationResult(uriObservacion, 
					"num_aperturas", 
					model.getResource(uriMedicionClase + "num_aperturas"),
					nodeMeasurement.getNum_aperturas(), 
					model);						
		}
	}
	
	private static void createContenedorObservationResult (String uriObservacion, String resultType, Resource resultClass, String resultValue, Model model){
		
		StringBuilder resultURIBuilder = new StringBuilder(uriObservacion.toString());
		
		Property SSNobservationResult = model.getProperty("http://purl.oclc.org/NET/ssnx/ssn#observationResult");
		Property SSNhasValue = model.getProperty("http://purl.oclc.org/NET/ssnx/ssn#hasValue");
		
		String uriObservationResult = resultURIBuilder.append("-" + resultType).toString();
		Resource observationResult = model.createResource(uriObservationResult)
				.addLiteral(SSNhasValue, model.createTypedLiteral(resultValue.replace(".", ""),"http://www.w3.org/2001/XMLSchema#int"))
					.addProperty(RDF.type, resultClass);
		(model.getResource(uriObservacion)).addProperty(SSNobservationResult, observationResult);
	}
}
