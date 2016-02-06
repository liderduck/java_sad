package sad;

import java.util.Random;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;

public class Results {
	
	public void imprimirResultados(Instances dataSel,NaiveBayes estimador) throws Exception{
		Evaluation evaluator = new Evaluation(dataSel);
		evaluator.crossValidateModel(estimador, dataSel, 10, new Random(1)); // Random(1): the seed=1 means "no shuffle" :-!
	
		double acc=evaluator.pctCorrect();
		double inc=evaluator.pctIncorrect();
		double kappa=evaluator.kappa();
		double mae=evaluator.meanAbsoluteError();    
		double rmse=evaluator.rootMeanSquaredError();
		double rae=evaluator.relativeAbsoluteError();
		double rrse=evaluator.rootRelativeSquaredError();
		double confMatrix[][]= evaluator.confusionMatrix();
		
		System.out.println("Correctly Classified Instances  " + acc);
		System.out.println("Incorrectly Classified Instances  " + inc);
		System.out.println("Kappa statistic  " + kappa);
		System.out.println("Mean absolute error  " + mae);
		System.out.println("Root mean squared error  " + rmse);
		System.out.println("Relative absolute error  " + rae);
		System.out.println("Root relative squared error  " + rrse);	
		for(int row_i=0; row_i<confMatrix.length; row_i++){
             for(int col_i=0; col_i<confMatrix.length; col_i++){
                 System.out.print(confMatrix[row_i][col_i]);
                 System.out.print("|");
             }
             System.out.println();
         }
		
	}

}
