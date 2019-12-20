package robot;

import java.io.File;

import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class Decision {

	Direction_pastille pastille = new Direction_pastille();
	Color_pastille ligne_stock = new Color_pastille();
	boolean fin_mission = false;
	boolean pre_fin_mission = false;

	//	File son = new File("C:\Users\Claire\Documents\Etudes\EMA\2A-Semestre_2-EMA
	//File son = new File(Decision.class.getClass().getResource("/Sounds/" + "game_over"));
	File game_over_sound = new File("game_over.wav");
	int position = 0;

	public Decision(){}

	boolean depart_mission = true;

	public int getPosition() {
		//0 si c'est le d�part
		//1 si on est au transtockeur
		//2 si on est au stock partag� d'entr�e
		//3 si on est au stock partag� de sortie
		//4 si on est sur la pastille rouge de croisement
		//5 si on est au stock de sortie
		//6 si on est sur la pastille verte centrale
		//7 si on est sur la 2nde pastille rouge
		Sound.beep();
		//Delay.msDelay(50);
		LCD.drawString("num_p?:"+position, 9, 4);
		return position;
	}
	public void setPosition(int position) {
		//0 si c'est le d�part
		//1 si on est au transtockeur
		//2 si on est au stock partag� d'entr�e
		//3 si on est au stock partag� de sortie
		//4 si on est sur la pastille rouge de croisement
		//5 si on est au stock de sortie
		//6 si on est sur la pastille verte centrale
		//7 si on est sur la 2nde pastille rouge
		//while(getPosition() != position){
			//this.position = position;			
		//}	
		this.position = position;	
		LCD.drawString("num_p!:"+position, 0, 4);
		
	}

	//renvoie l'action � effectuer
	//0 = rien
	//1 = vers stock �change sortie avec �change de balle
	//2 = vers stock sortie final avec �change de balle
	//3 = fin mission
	//4 = vers stock d�part
	//5 = vers stock �change entr�e
	//6 = vers stock �change sortie
	//7 = vers stock sortie final
	public int prendre_decision(int place, int ball_full, int position, int parametre_p){

		LCD.drawString("num_p*:"+position, 9, 4);
		if(fin_mission == true){
			if(position == 6){
				//Sound.twoBeeps();
				Sound.playSample(game_over_sound, 8);
				pastille.direction_pastille("droite");
				Robot.retrouve_ligne("droite");
				setPosition(0);
			}
			return 3;
		}
		else{
			//LCD.drawString("num_p2:"+getPosition(), 0, 4);
			//Delay.msDelay(4000);
			if(place == 2){ //green sticker
				//on v�rifie l'�tat du stock du robot et s'il y a bien une balle dans le robot alors
				if(ball_full != 0){ // il y a une balle dans le robot
					//on v�rifie le dernier emplacement connu
					if(position == 0){ //alors on vient du d�but
						//ici, le robot est sur un sticker vert, porte 1 balle et vient du d�part
						//IMPOSSIBLE => message d'erreur
						//LCD.clear();
						//LCD.drawString("ERREUR !", 5, 4);
						//LCD.drawString("2, true, 0", 5, 5);
						return 0;
					}
					if(position == 3 || position == 5 || position == 7){ //alors on vient du stock d'�change de sortie
						//ici, le robot est sur un sticker vert, porte 1 balle et vient du stock d'�change de sortie
						//IMPOSSIBLE => on est forc� de passer par la 4
					//	LCD.clear();
						//LCD.drawString("ERREUR !", 5, 4);
						//LCD.drawString("2, true, 3", 5, 5);
						return 0;
					}
					if(position == 4 ){//alors on vient de la pastille rouge de croisement
						//ici, le robot est sur un sticker vert, porte 1 balle et vient de la pastille rouge centrale
						//tr�s peu problable
						//on veut tout de m�me faire demi-tour
						setPosition(6);
						pastille.direction_pastille("retour");
						Robot.retrouve_ligne("droite");
						return 0;
					}
					if(position == 6){//alors on vient de la pastille verte centrale
						//alors on est devant 1 des 2 stocks d'entr�e : on cherche � prendre une balle
						//IMPOSSIBLE : on ne peut venir de 6, �tre sur 1 ou 2 et d�j� avoir une balle
						pastille.direction_pastille("avance_erreur");
						return 0;//prendre_balle
					}

				}
				//s'il n'y a pas de balle dans le robot alors 
				else{
					//on v�rifie le dernier emplacement connu
					if(position == 0){ //alors on vient du d�but
						//ici, le robot est sur un sticker vert, ne porte pas de balle et vient du d�part
						//on veut donc que le robot tourne sur sa droite
						setPosition(6);
						//LCD.drawString("num_p:6", 0, 4);
						//Delay.msDelay(2000);
						pastille.direction_pastille("droite");
						Robot.retrouve_ligne("droite");
						//Delay.msDelay(2000);
						//ligne_stock.retrouve_ligne("droite");
						//il faut encha�ner ici avec la pr�hension de la balle et le retour sur la pastille 6
						//ou cr�er un �tat qui se chargera de le faire
						return 4;
					}
					if(position == 3 || position == 5 || position == 7){ //alors on vient du stock d'�change de sortie
						//ici, le robot est sur un sticker vert, ne porte pas de balle et vient du stock d'�change de sortie
						//IMPOSSIBLE => on est forc� de passer par la postion 4
						return 0;
					}
					if(position == 4){//alors on vient de la pastille rouge de croisement
						//ici, le robot est sur un sticker vert, ne porte pas de balle et vient de la pastille rouge centrale
						setPosition(6);
						if(depart_mission == true){
							//on va chercher la 3eme balle dans le stock d'entree du debut
							//on veut aller � gauche 
							//pastille.direction_pastille("avance_6");
							pastille.direction_pastille("gauche");
							Robot.retrouve_ligne("gauche");
							//ligne_stock.retrouve_ligne("gauche");
							depart_mission = false;
							return 11;
						}
						else{
							//on veut v�rifier que le stock d'�change en entr�e est vide
							//on veut aller � droite
							pastille.direction_pastille("droite");
							Robot.retrouve_ligne("droite");
							//ligne_stock.retrouve_ligne("droite");


							//on est pr�-fin de mission : tout va d�pendre si le stock est vide
							pre_fin_mission = true;

							//---------------------On veut faire un cr�neau pour tourner � droite sans taper le stock
							//pastille.direction_pastille("creneau_droite");

							//---------------------On veut faire un cr�neau pour tourner � droite sans taper le stock
							if(pre_fin_mission == false){
								return 5;							
							}
							else{
								return 8;
							}
						}
					}
					if(position == 6){
						pastille.direction_pastille("avance_erreur");
						return 0;
					}
					return 0;				
				}
			}
			if(place == 3){ //red sticker
				if(ball_full != 0){	
					//on v�rifie le dernier emplacement connu
					if(position == 0 || position == 1 || position == 2){ //alors on vient du d�but
						//ici, le robot est sur un sticker rouge, porte 1 balle et vient du d�part
						//IMPOSSIBLE => il faut passer par la postion 6
						return 0;
					}
					if(position == 4){
						//alors on vient de la pastille rouge de croisement
						//ici, le robot est sur un sticker rouge, porte 1 balle et vient de la pastille rouge centrale
						//pour savoir si je suis en position 3 ou 7, il faut que je compare la couleur de ma balle
						//avec le param�tre
						setPosition(7);
						int balle_fond = Robot.stock_balle.get(0);
						int balle_devant = Robot.stock_balle.get(1);

						if(parametre_p == 0){
							//on veut les bleues
							if(balle_fond == 0){
								if(balle_devant == 1){
									//la balle de devant est rouge
									//Vers stock echange
									pastille.direction_pastille("demi-tour");
									Robot.retrouve_ligne("droite");
									//ligne_stock.retrouve_ligne("droite");
									return 0;
								}
								else if(balle_devant == 2){
									//la balle de devant est bleue
									//Vers stock sortie
									return 7; //deposer_balle
								}
							}
							else if(balle_fond == 1){
								//la balle du fond est rouge
								//vers stock echange sortie
								pastille.direction_pastille("demi-tour");
								Robot.retrouve_ligne("droite");
								//ligne_stock.retrouve_ligne("droite");
								return 0;
							}
							else if(balle_fond == 2){
								//la balle du fond est bleue
								//vers stock sortie
								return 2;
							}
						}
						else{
							//on veut les rouges
							if(balle_fond == 0){
								if(balle_devant == 1){
									//la balle de devant est rouge
									//Vers stock sortie
									return 7;

								}
								else if(balle_devant == 2){
									//la balle de devant est bleue
									//Vers stock echange
									pastille.direction_pastille("demi-tour");
									Robot.retrouve_ligne("droite");
									//ligne_stock.retrouve_ligne("droite");
									return 0;
								}
							}
							else if(balle_fond == 1){
								//la balle du fond est rouge
								//vers stock sortie
								return 2;
							}
							else if(balle_fond == 2){
								//la balle du fond est bleue
								//vers stock echange sortie
								pastille.direction_pastille("demi-tour");
								Robot.retrouve_ligne("droite");
								//ligne_stock.retrouve_ligne("droite");
								return 0;
							}

						}
					}
					if(position == 6){//alors on vient de la pastille verte centrale
						//depend de la couleur de la balle
						setPosition(4);
						int balle_fond = Robot.stock_balle.get(0);
						int balle_devant = Robot.stock_balle.get(1);

						if(parametre_p == 0){
							//on veut les bleues
							if(balle_fond == 0){
								if(balle_devant == 1){
									//la balle de devant est rouge
									//Vers stock echange
									pastille.direction_pastille("gauche");
									Robot.retrouve_ligne("gauche");
									//ligne_stock.retrouve_ligne("gauche");
									return 6;
								}
								else if(balle_devant == 2){
									//la balle de devant est bleue
									//Vers stock sortie
									setPosition(4);
									pastille.direction_pastille("avance");
									return 0;
								}
							}
							else if(balle_fond == 1){
								//la balle du fond est rouge
								//vers stock echange sortie
								pastille.direction_pastille("gauche");
								Robot.retrouve_ligne("gauche");
								//ligne_stock.retrouve_ligne("gauche");
								return 1;
							}
							else if(balle_fond == 2){
								//la balle du fond est bleue
								//vers stock sortie
								pastille.direction_pastille("avance");
								return 0;
							}
						}
						else{
							//on veut les rouges
							if(balle_fond == 0){
								if(balle_devant == 1){
									//la balle de devant est rouge
									//Vers stock sortie
									pastille.direction_pastille("avance");
									return 0;

								}
								else if(balle_devant == 2){
									//la balle de devant est bleue
									//Vers stock echange
									pastille.direction_pastille("gauche");
									Robot.retrouve_ligne("gauche");
									//ligne_stock.retrouve_ligne("gauche");
									return 6;
								}
							}
							else if(balle_fond == 1){
								//la balle du fond est rouge
								//vers stock sortie
								pastille.direction_pastille("avance");
								return 0;
							}
							else if(balle_fond == 2){
								//la balle du fond est bleue
								//vers stock echange sortie
								pastille.direction_pastille("gauche");
								Robot.retrouve_ligne("gauche");
								//ligne_stock.retrouve_ligne("gauche");
								return 1;
							}
						}
					}
					if(position == 7){//alors on vient de la 2nde pastille rouge
						setPosition(4);
						int balle_fond = Robot.stock_balle.get(0);
						int balle_devant = Robot.stock_balle.get(1);

						if(parametre_p == 0){
							//on veut les bleues
							if(balle_fond == 0){
								if(balle_devant == 1){
									//la balle de devant est rouge
									//Vers stock echange
									pastille.direction_pastille("droite");
									Robot.retrouve_ligne("droite");
									//ligne_stock.retrouve_ligne("droite");
									//setPosition(4);
									return 6;
								}
								else if(balle_devant == 2){
									//la balle de devant est bleue
									//Vers stock sortie
									pastille.direction_pastille("demi-tour");
									Robot.retrouve_ligne("droite");
									//ligne_stock.retrouve_ligne("droite");
									//setPosition(4);
									return 0;
								}
								else if(balle_devant == 0){
									//stock vide : on avance
									pastille.direction_pastille("avance");
									//setPosition(4);
									return 0;
								}
							}
							else if(balle_fond == 1){
								//la balle du fond est rouge
								//vers stock echange sortie
								pastille.direction_pastille("droite");
								Robot.retrouve_ligne("droite");
								//setPosition(4);
								//ligne_stock.retrouve_ligne("droite");
								return 0;
							}
							else if(balle_fond == 2){
								//la balle du fond est bleue
								//vers stock echange sortie
								pastille.direction_pastille("demi-tour");
								Robot.retrouve_ligne("droite");
								//setPosition(4);
								return 1;
							}
						}
						else{
							//on veut les rouges
							if(balle_fond == 0){
								if(balle_devant == 1){
									//la balle de devant est rouge
									pastille.direction_pastille("demi-tour");
									Robot.retrouve_ligne("droite");
									//setPosition(4);
									//ligne_stock.retrouve_ligne("droite");
									return 0;
								}
								else if(balle_devant == 2){
									//la balle de devant est bleue
									//Vers stock echange
									pastille.direction_pastille("droite");
									Robot.retrouve_ligne("droite");
									//setPosition(4);
									//ligne_stock.retrouve_ligne("droite");
									return 6;
								}
							}
							else if(balle_fond == 1){
								//la balle du fond est rouge
								//Vers stock echange
								pastille.direction_pastille("droite");
								Robot.retrouve_ligne("droite");
								//setPosition(4);
								//ligne_stock.retrouve_ligne("droite");
								return 1;
							}
							else if(balle_fond == 2){
								//la balle du fond est bleue
								pastille.direction_pastille("demi-tour");
								Robot.retrouve_ligne("droite");
								//setPosition(4);
								//ligne_stock.retrouve_ligne("droite");
								return 0;
							}
						}
					}
				}
				//s'il n'y a pas de balle dans le robot alors 
				else{
					//on v�rifie le dernier emplacement connu
					if(position == 0 || position == 1 || position == 2){ //alors on vient du d�but
						//ici, le robot est sur un sticker rouge, ne porte pas de balle et vient du d�part
						//IMPOSSIBLE => erreur
						//doit passer par la position 6
						return 0;
					}
					if(position == 4){//alors on vient de la pastille rouge centrale
						//alors on est en position 7
						//on fait demi-tour
						setPosition(7);
						pastille.direction_pastille("retour");
						Robot.retrouve_ligne("droite");
						return 0;
					}
					if(position == 6){//alors on vient de la pastille verte centrale
						//alors on est en position 4
						//on fait demi-tour
						setPosition(4);
						pastille.direction_pastille("retour");
						Robot.retrouve_ligne("droite");
						return 0;
					}
					if(position == 7){//alors on vient de la 2nde pastille rouge
						//on n'a pas de balle alors on vient forc�ment de la position 5
						setPosition(4);
						pastille.direction_pastille("avance");
						return 0;
					}
					return 0;
				}
				return 0;
			}
			else{
				return 0;
			}
		}
	}
}
