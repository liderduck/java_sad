package sad;

import java.util.Random;

import weka.attributeSelection.BestFirst;
import weka.attributeSelection.CfsSubsetEval;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.core.ChebyshevDistance;
import weka.core.EditDistance;
import weka.core.EuclideanDistance;
import weka.core.Instances;
import weka.core.ManhattanDistance;
import weka.core.Tag;
import weka.core.neighboursearch.LinearNNSearch;
import weka.filters.Filter;
import weka.core.SelectedTag;
import weka.filters.supervised.attribute.AttributeSelection;
//AQUI ARRIBA VEMOS LA RUTA DEL FILTRO QUE VAMOS A USAR
public class Seleccion {
	Results resultados = new Results();
	//esta clase es para pasar el filtro atributeselection
	public Instances selSubSet(Instances data) throws Exception{
		
		AttributeSelection filter= new AttributeSelection();
		CfsSubsetEval eval = new CfsSubsetEval();
		BestFirst search=new BestFirst();
		filter.setEvaluator(eval);
		filter.setSearch(search);
		filter.setInputFormat(data);
		// 2.1 Get new data set with the attribute sub-set
		Instances newData = Filter.useFilter(data, filter);
		
		return newData;
	}
	
	//kfold de parametro 10, hay que cmbiar el estimador que le entra, ahora esta generico, pero se puede poner IBk o NaiveBayes
	public Evaluation evalKFold (Instances dataSel,Classifier estimador) throws Exception{
		
		Evaluation evaluator = new Evaluation(dataSel);
		evaluator.crossValidateModel(estimador, dataSel, 10, new Random(1)); // Random(1): the seed=1 means "no shuffle" :-!
		
		return evaluator;
	}
	
	public Evaluation holdOut (Instances test,Classifier estimador) throws Exception{
		
		Evaluation evaluator = new Evaluation(test);
		evaluator.evaluateModel(estimador, test);
		
		return evaluator;
	}
	
	//caso particular para el ibk
	public void mejorK(IBk estimador,Instances dataSel,Evaluation evaluator) throws Exception{
		double mejorFM = 0;
		double actualFM= 0;
		int mejorK=1;
		int mejorDis=0;
		String mejorDistancia="";
		
		estimador.setKNN(1);//esto sirve para añadir los vecinos al ibk empezamos x el minimo 2
		evaluator = evalKFold(dataSel, estimador);//usamos el crosvalidation
		mejorFM=evaluator.fMeasure(0);//cogemos como mejor fmeasure la primera por defecto para ir comprobando hay que pasarle la clase prioritaria en este caso 0
		
		//creamos las 4 distancias
		EuclideanDistance euclDis= new EuclideanDistance();
		ManhattanDistance manhDis = new ManhattanDistance();
		EditDistance edDis = new EditDistance();
		ChebyshevDistance chebDis = new ChebyshevDistance();
	
		//creamos este for para ir pasando entre las distancias
		for (int z=0;z<4;z++){
			if (z==0){
				//preparamos el parametro de la distancia con una de las 4 escogidas arriba y preparamos el estimador
				LinearNNSearch distancia= new LinearNNSearch();
				distancia.setDistanceFunction(euclDis);
				estimador.setNearestNeighbourSearchAlgorithm(distancia);
			}else if (z==1){
				LinearNNSearch distancia= new LinearNNSearch();
				distancia.setDistanceFunction(manhDis);
				estimador.setNearestNeighbourSearchAlgorithm(distancia);
			}else if (z==2){
				LinearNNSearch distancia= new LinearNNSearch();
				distancia.setDistanceFunction(edDis);
				estimador.setNearestNeighbourSearchAlgorithm(distancia);
			}else if(z==3){
				LinearNNSearch distancia= new LinearNNSearch();
				distancia.setDistanceFunction(chebDis);
				estimador.setNearestNeighbourSearchAlgorithm(distancia);
			}
			
			for(int k=2;k<=dataSel.numInstances();k++){//aumenta los vecinos y los va probando
				estimador.setKNN(k);
				evaluator = evalKFold(dataSel, estimador);
				actualFM=evaluator.fMeasure(0);
				actualFM=Math.rint(actualFM*1000)/1000;//truncar a 2 decimales
				if (actualFM>mejorFM){
					mejorFM=actualFM;
					mejorK=k;
					mejorDis=z;
				}
			}
		}
		
		
		System.out.println("La mejor f-measure es: "+mejorFM+" con una K de: "+mejorK+" en la distancia: "+ mejorDistancia);
		System.out.println("estos son los datos para la K:");
		if (mejorDis==0){
			mejorDistancia="Euclidean";
			LinearNNSearch distancia= new LinearNNSearch();
			distancia.setDistanceFunction(euclDis);
			estimador.setNearestNeighbourSearchAlgorithm(distancia);
			estimador.setKNN(mejorK);
			evaluator = evalKFold(dataSel, estimador);
			resultados.imprimirResultados(dataSel, evaluator);
		}else if (mejorDis==1){
			mejorDistancia="Manhattan";
			LinearNNSearch distancia= new LinearNNSearch();
			distancia.setDistanceFunction(manhDis);
			estimador.setNearestNeighbourSearchAlgorithm(distancia);
			estimador.setKNN(mejorK);
			evaluator = evalKFold(dataSel, estimador);
			resultados.imprimirResultados(dataSel, evaluator);
		}else if (mejorDis==2){
			mejorDistancia="EditDistance";
			LinearNNSearch distancia= new LinearNNSearch();
			distancia.setDistanceFunction(edDis);
			estimador.setNearestNeighbourSearchAlgorithm(distancia);
			estimador.setKNN(mejorK);
			evaluator = evalKFold(dataSel, estimador);
			resultados.imprimirResultados(dataSel, evaluator);
		}else if (mejorDis==3){
			mejorDistancia="Chebyshev";
			LinearNNSearch distancia= new LinearNNSearch();
			distancia.setDistanceFunction(chebDis);
			estimador.setNearestNeighbourSearchAlgorithm(distancia);
			estimador.setKNN(mejorK);
			evaluator = evalKFold(dataSel, estimador);
			resultados.imprimirResultados(dataSel, evaluator);
		}
		
		
		//

	}
}
