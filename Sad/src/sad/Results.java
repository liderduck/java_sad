package sad;

import weka.classifiers.Evaluation;
import weka.core.Instances;

public class Results {
	
	public void imprimirResultados(Instances dataSel,Evaluation evaluator) throws Exception{
		double confMatrix[][]= evaluator.confusionMatrix();	
		
		System.out.println(evaluator.toSummaryString());
		System.out.println(evaluator.toClassDetailsString());
		
		for(int row_i=0; row_i<confMatrix.length; row_i++){
             for(int col_i=0; col_i<confMatrix.length; col_i++){
                 System.out.print(confMatrix[row_i][col_i]);
                 System.out.print("|");
             }
             System.out.println();
         }
	}
	
	public void imprimirMejorK(int mejorDis,String mejorDistancia,int mejorSelTag,String mejorDisW,double mejorFM,int mejorK){
		
		if (mejorDis==0){
			mejorDistancia="Euclidean";
		}else if (mejorDis==1){
			mejorDistancia="Manhattan";
		}else if (mejorDis==2){
			mejorDistancia="Chebyshev";
		}
		if(mejorSelTag==0){
			mejorDisW="Non distance weighting";
		}else if(mejorSelTag==1){
			mejorDisW="1/distance";
		}else if(mejorSelTag==2){
			mejorDisW="1-distance";
		}
		
		System.out.println();
		System.out.println("La mejor WF-measure es: "+mejorFM+" con una K de: "+mejorK+" en la distancia: "+ mejorDistancia + " con una peso de distancia de:  " +mejorDisW);
		System.out.println("Estos son los datos para la K con los parametros anteriormente conseguidos:");
		System.out.println();
	}

}
