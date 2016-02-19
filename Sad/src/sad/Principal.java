package sad;


	/*
	 GOAL: Load data from .arff files, preprocess the data, train a model and assess it either using 10-fold cross-validation or hold-out
	 
	 Compile:
	 javac DataMiningExample.java

	 Run Interpret:
	 java DataMiningExample
	 
	 HACER!!!
		- Hacer modular
		- El programa no puede tener dependencias con datos!
		- Generar un .jar y ejecutar desde la línea de comandos

	 */


import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.classifiers.lazy.IBk;


	public class Principal {
		
	    public static void main(String[] args) throws Exception {
			Seleccion sel=new Seleccion();
	  // 	Results resultados = new Results();
	  //  	Escribir esc=new Escribir();
	    	
	    	/////////////Lectura de datos y aplica el filtro RANDOMIZE/////////////
	    	Lectura lect= new Lectura();
	    	Instances data;
	    	data = lect.cargarDatos();
	    	
	    	
	    	/////////////Seleccion del Sub-set/////////////
			//tambien aplica el filtro selectAtributes ojo
	    	Instances dataSel = sel.selSubSet(data);
	    			
	    	/////////////Creamos el clasificador/////////////
	    	//NaiveBayes estimador = new NaiveBayes();//Naive Bayes
	    	IBk estimador = new IBk();
	    	
	    	
	    	/////////////Creamos el evaluador kfold de 10 valores, pasandole los datos y el  clasificador
	    	Evaluation evaluator =sel.evalKFold(dataSel, estimador);
	    	sel.mejorK(estimador,dataSel,evaluator);//caso particular para el IBk
	    	
	    	/////////////Imprime resultados por pantalla./////////////
		//	resultados.imprimirResultados(dataSel, evaluator);//en estimado entra ahora nayvebayes pero debera cambiar.

/*			parte del hold out
System.out.println("///////////////////////////////");
			
			/////////////ahora usando un Hold out al 70%/////////////
			int trainSize = (int) Math.round(dataSel.numInstances() * 0.7);
			int testSize = dataSel.numInstances() - trainSize;
			
			/////////////creamos las instances de entrenamiento y de testeo/////////////
			Instances train = new Instances(dataSel, 0, trainSize);
			Instances test = new Instances(dataSel, trainSize, testSize);
			
			/////////////creamos un fichero con las instances de test/////
			esc.escribir(null,test);
			
			/////////////Construimos el entrenador con una parte de las instancias/////////////
			estimador.buildClassifier(train);
			
			/////////////Creamos el evaluador con holdout/////////////
			Evaluation evaluator2=sel.holdOut(test, estimador);
					
			/////////////Saca en un fichero las clases de cada instance/////////////
			esc.escClases(test, evaluator2, estimador);
			
			/////////////Imprime los resultados del hold out/////////////
	//		resultados.imprimirResultados(test, evaluator2);
*/
	    }
}


