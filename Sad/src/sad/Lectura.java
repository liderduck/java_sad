package sad;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.Randomize;

public class Lectura {

	public Instances cargarDatos() throws Exception{
	
		// 1.2. Open the file
		FileReader fi=null;
		String path = null;
		
		System.out.println("Introduce el path del fichero .arff con su extension");
		BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));   
		path = bfr.readLine(); 
		
		try {
			fi= new FileReader(path); //(args[0]) <-> ("~/software/weka-3-6-9/data/breast-cancer.arff" )
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
	
		
		// 1.6. Especificamos que la clase es el ultimo
		datosRan.setClassIndex(data.numAttributes()-1);	
		return datosRan;
	}

}
