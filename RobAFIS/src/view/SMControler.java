package view;

public class SMControler implements Runnable {

	private String state;

	boolean fin;
	
	SMControler(){
		state=Data.ETAT_WAIT;
	}
	
	
	public void run() {
		fin=false; //variavel que tem que mudar pra dar o fim no while e parar aSM
		state=Data.ETAT_WAIT;
		while(!fin){
			// Aqui que entra o fim da SM tem que mudar 
			// TO DO
			// attente d'un nouvel �v�nement et traitement de l'�v�nement
		
		}
		System.out.println("SM has stopped");
	}
	

	


	
	

	
}
	



