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


	import java.util.Random;


import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.classifiers.lazy.IBk;


	public class Principal {
		
	    public static void main(String[] args) throws Exception {
			Seleccion sel=new Seleccion();
	    	Results resultados = new Results();
	    	Escribir esc=new Escribir();
	    	
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
			estimador.setKNN(1);//esto sirve para añadir los vecinos al ibk
			
			//Creamos el evaluador kfold de 10 valores, pasandole los datos y el  clasificador
			Evaluation evaluator=sel.evalKFold(dataSel, estimador);

			System.out.println(evaluator.fMeasure(0));//sacamos el fmeasure..pero creo k esta mal
			
			/////////////Imprime resultados./////////////
			resultados.imprimirResultados(dataSel, evaluator);//en estimado entra ahora nayvebayes pero debera cambiar.
			
			System.out.println("///////////////////////////////");
			
			/////////////ahora usando un Hold out al 70%/////////////
			
			int trainSize = (int) Math.round(dataSel.numInstances() * 0.7);
			int testSize = dataSel.numInstances() - trainSize;
			
			/////////////creamos las instances de entrenamiento y de testeo/////////////
			Instances train = new Instances(dataSel, 0, trainSize);
			Instances test = new Instances(dataSel, trainSize, testSize);
			
			esc.escribir(null,test);
			
			/////////////Train the classifier with the 70\% of the data by means of the Naive Bayes algorithm/////////////
			estimador.buildClassifier(train);
			
			
			/////////////Let the model predict the class for each instance in the test set/////////////
			Evaluation evaluator2=sel.holdOut(test, estimador);
					
			/////////////for para sacar las clases y escribirlas en un doc luego/////////////
			
			/*double predictions[] = new double[test.numInstances()];
				for (int i = 0; i < test.numInstances(); i++) {
					predictions[i] = evaluator.evaluateModelOnceAndRecordPrediction(estimador, test.instance(i));
				}

			/////////////Guardar en un fichero de salida la clase estimada por el modelo para cada instancia del test y así después podremos comparar la clase real y la estimada
			
			esc.escribir(predictions,null);
			*/
			
			/////////////Imprime los resultados del hold out/////////////
		
			resultados.imprimirResultados(test, evaluator2);
			///////////////////////////////////////////////////////
			// Observa: http://weka.wikispaces.com/Use+Weka+in+your+Java+code
			///////////////////////////////////////////////////////

	    }
}


