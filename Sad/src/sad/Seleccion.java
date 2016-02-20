package sad;

import java.util.ArrayList;
import java.util.Random;
import weka.attributeSelection.BestFirst;
import weka.attributeSelection.CfsSubsetEval;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.core.ChebyshevDistance;
import weka.core.EuclideanDistance;
import weka.core.Instances;
import weka.core.ManhattanDistance;
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
		int mejorSelTag=0;
		String mejorDistancia="";
		String mejorDisW="";
		
		LinearNNSearch distancia= new LinearNNSearch();

	//creamos para la W
		ArrayList<Integer> tagDis= new ArrayList<Integer>();//arraylist para los valores del selectedtag
		tagDis.add(1);
		tagDis.add(2);
		tagDis.add(4);
		

	for (int t=0;t<tagDis.size();t++){
		estimador= obtTagDis(t,estimador);//modulo para obtener el peso de la distancia
		
		for (int z=0;z<3;z++){//creamos este for para ir pasando entre las distancias
			distancia= obtDis(z,distancia,t);
			estimador.setNearestNeighbourSearchAlgorithm(distancia);

			for(int k=1;k<=dataSel.numInstances();k++){//aumenta los vecinos y los va probando
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
		estimador= obtTagDis(mejorSelTag,estimador);
		estimador.setKNN(mejorK);
		distancia= obtDis(mejorDis,distancia,0);
		estimador.setNearestNeighbourSearchAlgorithm(distancia);
		evaluator = evalKFold(dataSel, estimador);
		
		resultados.imprimirMejorK(mejorDis, mejorDistancia, mejorSelTag, mejorDisW, mejorFM, mejorK);
		resultados.imprimirResultados(dataSel, evaluator);

	}
	
	public IBk obtTagDis(int num,IBk estimador){
		if (num==0){
			SelectedTag sl1 = new SelectedTag(1,IBk.TAGS_WEIGHTING);//no distance
			estimador.setDistanceWeighting(sl1);
		}else if (num==1){
			SelectedTag sl2 = new SelectedTag(2,IBk.TAGS_WEIGHTING);//1/distance
			estimador.setDistanceWeighting(sl2);
		}else if(num==2){
			SelectedTag sl3 = new SelectedTag(4,IBk.TAGS_WEIGHTING);//1-distance
			estimador.setDistanceWeighting(sl3);
		}
		return estimador;
	}
	
	public LinearNNSearch obtDis(int num,LinearNNSearch distancia,int t) throws Exception{
		//creamos las 4 distancias
		EuclideanDistance euclDis= new EuclideanDistance();
		ManhattanDistance manhDis = new ManhattanDistance();
		ChebyshevDistance chebDis = new ChebyshevDistance();
		if (num==0){	
			distancia.setDistanceFunction(euclDis);
		}else if ((num==1) && (t!=2)){//como dijo josu manhattan con 1-dis da error asik lo saltamos en tal caso
			distancia.setDistanceFunction(manhDis);
		}else if(num==2){
			distancia.setDistanceFunction(chebDis);
		}
		return distancia;
	}
}
