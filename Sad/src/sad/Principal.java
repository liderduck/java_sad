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


	public class Principal {
		
	    public static void main(String[] args) throws Exception {
			
	    	/////////////Lectura de datos y aplica el filtro RANDOMIZE //////////
	    	Lectura lect= new Lectura();
	    	Instances data;
	    	data = lect.cargarDatos();
	    	

	    	/////////////Seleccion del Sub-set PREGUNTAR QUE HACE ESTO/////
			//tambien aplica el filtro selectAtributes ojo
	    	Seleccion sel=new Seleccion();
	    	Instances dataSel = sel.selSubSet(data);
	    			

			// 3. Clasificar 
	    	/////////////En este caso se usa Nayve bayes(esto es solo para el ejemplo)////
			NaiveBayes estimador= new NaiveBayes();//Naive Bayes

			// 3.1 Imprime resultados.
			
			Results resultados = new Results();
			resultados.imprimirResultados(dataSel, estimador);//en estimado entra ahora nayvebayes pero debera cambiar.

			/*
			 // 3.2 Alternatively, assess the performance of the classifiera by means of hold-out: leaving the 30% of the data randomly selected out to test the model 
			// 3.2.a Get the test set by randomly selecting the the 30% of the instances
			int trainSize = (int) Math.round(newData.numInstances() * 0.7);
			int testSize = newData.numInstances() - trainSize;
			// HACER!!!! Salvar las instancias del test en un fichero
			Instances train = new Instances(newData, 0, trainSize);
			Instances test = new Instances(newData, trainSize, testSize);
			
			// 3.2.b Train the classifier with the 70\% of the data by means of the Naive Bayes algorithm
			estimador.buildClassifier(train);
			
			// 3.2.c Let the model predict the class for each instance in the test set
			evaluator.evaluateModel(estimador, test);
			double predictions[] = new double[test.numInstances()];
			for (int i = 0; i < test.numInstances(); i++) {
				predictions[i] = evaluator.evaluateModelOnceAndRecordPrediction((Classifier)estimador, test.instance(i));
			}
			// HACER!!!! Guardar en un fichero de salida la clase estimada por el modelo para cada instancia del test y así después podremos comparar la clase real y la estimada
			
			// 3.2.d Assess the performance on the test
			//  HACER!!!! Idéntico idéntico idéntico al 3.1: por eso es necesario que sea modular, no vamos a copiar aquí el código de nuevo!
			*/
			///////////////////////////////////////////////////////
			// Observa: http://weka.wikispaces.com/Use+Weka+in+your+Java+code
			///////////////////////////////////////////////////////
			
			
	    }
}


