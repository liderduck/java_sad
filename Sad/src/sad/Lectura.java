package sad;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.Randomize;

public class Lectura {

	public Instances cargarDatos() throws Exception{
		/////////////////////////////////////////////////////////////
		// 1. LOAD DATA FILE
		//  HACER!!!! Bloque 1: como sub-clase
		// 1.1. Get the path of the .arff (instances) from the command line
		/*
		if( args.length < 1 ){
			System.out.println("OBJETIVO: Seleccionar atributos (AttributeSelection<-CfsSubsetEval, search<-BestFirst) y Evaluar clasificador NaiveBayes con 10-fold cross-validation.");
			System.out.println("ARGUMENTOS:");
			System.out.println("\t1. Path del fichero de entrada: datos en formato .arff");
			return; 
		}
		*/		
		
		// 1.2. Open the file
		FileReader fi=null;
		try {
			fi= new FileReader("C:\\Users\\jonathan\\Downloads\\breast-cancer.arff"); //(args[0]) <-> ("~/software/weka-3-6-9/data/breast-cancer.arff" )
		} catch (FileNotFoundException e) {
				System.out.println("ERROR: Revisar path del fichero de datos:"/*+ruta*/);
		}
		
		// 1.3. Load the instances
		Instances data=null;
		try {
			data = new Instances(fi);
		} catch (IOException e) {
			System.out.println("ERROR: Revisar contenido del fichero de datos: "/*+ruta*/);
		}
		
		// 1.4. Close the file
		try {
			fi.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		
	
		// 1.5. Aplica el filtro randomize
	
		Randomize filtroRand = new Randomize();//creamos el filtro
		filtroRand.setInputFormat(data);//le asignamos los datos a filtrar
		Instances datosRan = Filter.useFilter(data, filtroRand);//creamos las nuevas instances usando el filtro.<
	
		
		// 1.6. Specify which attribute will be used as the class: the last one, in this case 
		datosRan.setClassIndex(data.numAttributes()-1);
				
		return datosRan;
	}

}
