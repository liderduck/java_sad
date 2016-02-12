package sad;

import java.io.FileWriter;
import java.io.PrintWriter;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;


public class Escribir {
	
	public void escribir(double[] predictions,Instances inst){

		if (inst==null){
			FileWriter fichero = null;
			PrintWriter pw = null;
			try{
				fichero=new FileWriter ("C:\\Users\\jonathan\\Downloads\\clases.txt");
				pw= new PrintWriter(fichero);
				for (int z=0;z< predictions.length;z++){
					pw.print(predictions[z]);
					pw.println();
				}
			}catch (Exception e){
				e.printStackTrace();
			}finally{
				try{
					if (null != fichero)
						fichero.close();
				}catch (Exception e2){
					e2.printStackTrace();
				}
			}
		
		}else if(predictions==null){
			FileWriter fichero = null;
			PrintWriter pw = null;
			try{
				int z=0;
				fichero=new FileWriter ("C:\\Users\\jonathan\\Downloads\\test.txt");
				pw= new PrintWriter(fichero);
				int ultimo =  inst.numInstances();
				
				while(z != ultimo){
					pw.print(inst.instance(z));
					pw.println();
					z++;
				}
				
					
			}catch (Exception e){
				e.printStackTrace();
			}finally{
				try{
					if (null != fichero)
						fichero.close();
				}catch (Exception e2){
					e2.printStackTrace();
				}
			}
			
		}
	}
	
	public void escClases(Instances test,Evaluation evaluator2,Classifier estimador) throws Exception{
		double predictions[] = new double[test.numInstances()];
		for (int i = 0; i < test.numInstances(); i++) {
			predictions[i] = evaluator2.evaluateModelOnceAndRecordPrediction(estimador, test.instance(i));
		}
		/////////////Guardar en un fichero de salida la clase estimada por el modelo para cada instancia del test y así después podremos comparar la clase real y la estimada
		
		escribir(predictions,null);
	}
}
