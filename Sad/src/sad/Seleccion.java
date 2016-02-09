package sad;

import java.util.Random;

import weka.attributeSelection.BestFirst;
import weka.attributeSelection.CfsSubsetEval;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
//AQUI ARRIBA VEMOS LA RUTA DEL FILTRO QUE VAMOS A USAR
public class Seleccion {
		
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
}
