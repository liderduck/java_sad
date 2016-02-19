package sad;

import java.util.ArrayList;
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
import weka.core.neighboursearch.LinearNNSearch;
import weka.filters.Filter;
import weka.core.SelectedTag;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.NumericToNominal;
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

	public Instances nTNOm(Instances data) throws Exception{
		
		NumericToNominal convert= new NumericToNominal();
        convert.setInputFormat(data);
        Instances newData=Filter.useFilter(data, convert);
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
		int mejorSelTag=0;
		String mejorDisW="";
		LinearNNSearch distancia= new LinearNNSearch();

		//creamos las 4 distancias
		EuclideanDistance euclDis= new EuclideanDistance();
		ManhattanDistance manhDis = new ManhattanDistance();
		EditDistance edDis = new EditDistance();
		ChebyshevDistance chebDis = new ChebyshevDistance();
		
		//creamos para la W
		ArrayList<Integer> tagDis= new ArrayList<Integer>();//arraylist para los valores del selectedtag
		tagDis.add(1);
		tagDis.add(2);
		tagDis.add(4);
		
		estimador.setKNN(1);//esto sirve para añadir los vecinos al ibk empezamos x el minimo 1
		evaluator = evalKFold(dataSel, estimador);//usamos el crosvalidation
		mejorFM=evaluator.weightedFMeasure();//cogemos como mejor fmeasure la primera por defecto para ir comprobando
		mejorFM=Math.rint(mejorFM*1000)/1000;//truncar a 3 decimales
	for (int t=0;t<tagDis.size();t++){
		if (t==0){
			SelectedTag sl1 = new SelectedTag(1,IBk.TAGS_WEIGHTING);//no distance
			estimador.setDistanceWeighting(sl1);
		}else if(t==1){
			SelectedTag sl2 = new SelectedTag(2,IBk.TAGS_WEIGHTING);//1/distance
			estimador.setDistanceWeighting(sl2);
		}else if(t==2){
			SelectedTag sl3 = new SelectedTag(4,IBk.TAGS_WEIGHTING);//1-distance
			estimador.setDistanceWeighting(sl3);
		}
		
		for (int z=0;z<4;z++){//creamos este for para ir pasando entre las distancias
			if (z==0){
				//preparamos el parametro de la distancia con una de las 4 escogidas arriba y preparamos el estimador
				distancia.setDistanceFunction(euclDis);
			}else if ((z==1) && (t!=2)){//como dijo josu manhattan con 1-dis da error asik lo saltamos en tal caso
				distancia.setDistanceFunction(manhDis);
			}else if (z==2){
				distancia.setDistanceFunction(edDis);
			}else if(z==3){
				distancia.setDistanceFunction(chebDis);
			}
			estimador.setNearestNeighbourSearchAlgorithm(distancia);

			for(int k=2;k<=dataSel.numInstances();k++){//aumenta los vecinos y los va probando
				estimador.setKNN(k);
				evaluator = evalKFold(dataSel, estimador);
				actualFM=evaluator.weightedFMeasure();		
				actualFM=Math.rint(actualFM*1000)/1000;//truncar a 3 decimales
				if (actualFM>mejorFM){
					mejorFM=actualFM;
					mejorK=k;
					mejorDis=z;
					mejorSelTag=t;
				}
			}
		}
	}
		//PAra generar los datos del resumen que veremos por pantalla debemos parametrizar el estimador
		//con los mejores parametros conseguidos anteriormente.
		if (mejorDis==0){
			mejorDistancia="Euclidean";
			distancia.setDistanceFunction(euclDis);
		}else if (mejorDis==1){
			mejorDistancia="Manhattan";
			distancia.setDistanceFunction(manhDis);
		}else if (mejorDis==2){
			mejorDistancia="EditDistance";
			distancia.setDistanceFunction(edDis);
		}else if (mejorDis==3){
			mejorDistancia="Chebyshev";
			distancia.setDistanceFunction(chebDis);
		}
			
				
		if(mejorSelTag==0){
			mejorDisW="Non distance weighting";
			SelectedTag sl1 = new SelectedTag(1,IBk.TAGS_WEIGHTING);//no distance
			estimador.setDistanceWeighting(sl1);
		}else if(mejorSelTag==1){
			mejorDisW="1/distance";
			SelectedTag sl2 = new SelectedTag(2,IBk.TAGS_WEIGHTING);//1/distance
			estimador.setDistanceWeighting(sl2);
		}else if(mejorSelTag==2){
			mejorDisW="1-distance";
			SelectedTag sl3 = new SelectedTag(4,IBk.TAGS_WEIGHTING);//1-distance
			estimador.setDistanceWeighting(sl3);
		}
		estimador.setKNN(mejorK);
		estimador.setNearestNeighbourSearchAlgorithm(distancia);
		evaluator = evalKFold(dataSel, estimador);
		
		System.out.println();
		System.out.println("La mejor WF-measure es: "+mejorFM+" con una K de: "+mejorK+" en la distancia: "+ mejorDistancia + " con una peso de distancia de:  " +mejorDisW);
		System.out.println("estos son los datos para la K con los parametros anteriormente conseguidos:");
		
		resultados.imprimirResultados(dataSel, evaluator);

	}
}
