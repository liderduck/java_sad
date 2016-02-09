package sad;

import java.io.FileWriter;
import java.io.PrintWriter;

import weka.core.Instances;


public class Escribir {
	
	public void escribir(double[] predictions,Instances inst){
		if (inst==null){
			FileWriter fichero = null;
			PrintWriter pw = null;
			try{
				fichero=new FileWriter ("C:\\Users\\anahe\\Desktop\\clases.txt");
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
				fichero=new FileWriter ("C:\\Users\\anahe\\Desktop\\clases1.txt");
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
}
