package sad;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import weka.attributeSelection.BestFirst;
import weka.attributeSelection.CfsSubsetEval;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.lazy.IB1;
import weka.core.Capabilities;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
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
			fi= new FileReader("C:\\Users\\anahe\\Desktop\\breast-cancer.arff"); //(args[0]) <-> ("~/software/weka-3-6-9/data/breast-cancer.arff" )
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
		
	
		// 1.5. Shuffle the instances: apply Randomize filter
		//  HACER!!!!
		Randomize filter = new Randomize();
		Instances data1 = Filter.useFilter(data, filter);

		
		// 1.6. Specify which attribute will be used as the class: the last one, in this case 
		data1.setClassIndex(data.numAttributes()-1);
				
		return data1;
	}
	
	
	
	
	
}
