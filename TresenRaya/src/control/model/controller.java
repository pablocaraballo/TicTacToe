package control.model;



import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

public class controller {
	
	private boolean turnos=true, win;
	private int cont;

	//MODOS DE JUEGO
	@FXML private RadioButton compu;
	@FXML private RadioButton human;
	@FXML private RadioButton humanvs;
	
	//ETIQUETAS DE TEXTO
	@FXML private Label turn;
	
	//BOTONES
	@FXML private Button comienza;
	@FXML private Button reinicia;
	@FXML private Button boton1;
	@FXML private Button boton2;
	@FXML private Button boton3;
	@FXML private Button boton4;
	@FXML private Button boton5;
	@FXML private Button boton6;
	@FXML private Button boton7;
	@FXML private Button boton8;
	@FXML private Button boton9;
	
	//ARRAY DE BOTONES COMO INDEX
	ArrayList<Button>botones2=new ArrayList<Button>();

	//MATRIZ DE CHARS CONTENEDORA DE VALORES
	private char[][] botones=new char[3][3];
	
	//BOTON START
	@FXML protected void start(ActionEvent event){
		
		inicializaPartida();
	}
	
	//METODO QUE INICIALIZA TODOS LOS VALORES
	public boolean inicializaPartida(){
		
		//Inicializamos la matriz
		for(int i=0; i<3; i++){
			for (int j=0; j<3; j++)
			botones[i][j]=0;
		}
		
		win=false;
		comienza.setDisable(true);
		
		//Metemos los botones en el ArrayList
		botones2.add(boton1);
		botones2.add(boton2);
		botones2.add(boton3);
		botones2.add(boton4);
		botones2.add(boton5);
		botones2.add(boton6);
		botones2.add(boton7);
		botones2.add(boton8);
		botones2.add(boton9);
		
		
		//MODO HUMANO VS HUMANO
		if(human.isSelected()){
			
			boolean turno=turno();
			cont = 1;
			
			click(boton1);
			click(boton2);
			click(boton3);
			click(boton4);
			click(boton5);
			click(boton6);
			click(boton7);
			click(boton8);
			click(boton9);
			
			//TURNOS DE INICIO ALEATORIOS
			if (turno){
				turn.setText("X");
				turnos=true;
			}
			else{
				turn.setText("O"); 
				turnos=false;
			}
		}
		
		//MODO HUMANO VS IA
		else if(humanvs.isSelected()){
			
			click(boton1);
			click(boton2);
			click(boton3);
			click(boton4);
			click(boton5);
			click(boton6);
			click(boton7);
			click(boton8);
			click(boton9);
		}
		
		//MODO IA VS IA
		else if(compu.isSelected()){
			turnos=true;
			boolean end=false;
			turn.setText("");
			
			while(!end){
				
				IA();
				
				if(cont==8)
					turn.setText("Empate");
				if (turn.getText().equals("Ganador: X") || turn.getText().equals("Ganador: O") || cont == 8)
					end=true;
			}
		}
		return turnos;
	}
	
	//Devuelve el turno mediante un numero aleatorio entre 0 y 1.
	public boolean turno(){
		
		boolean turn=true;
		int i=0;
		i=(int) Math.floor(Math.random()*(1-0+1))+0;
		if (i==1) turn=true;
		else if (i==0) turn=false;
		
		return turn;
	}
	
	//SE MANTIENE A LA ESPERA HASTA QUE ALGUN BOTON ES CLICkADO
	protected void click(Button boton){
		
		boton.setOnAction(new EventHandler<ActionEvent>(){
			int id;

			@Override
			public void handle(ActionEvent event) {
				
				//Humano VS Humano
				if(human.isSelected()){
					
					if(turnos){
						boton.setText("X");
						turnos=false;
						turn.setText("O");
						boton.setDisable(true);
						id = Integer.parseInt(boton.getId().substring(5, 6));
						setValue(botones, 'X', id);
						results(botones, turnos);
						cont++;
					}
					else{
						boton.setText("O");
						turnos=true;
						turn.setText("X");
						boton.setDisable(true);
						id = Integer.parseInt(boton.getId().substring(5, 6));
						setValue(botones, 'O', id);
						results(botones, turnos);
						cont++;
					}
				}
				
				//Human VS IA
				if(humanvs.isSelected()){
					
					boton.setText("X");
					turnos=false;
					turn.setText("X");
					boton.setDisable(true);
					id = Integer.parseInt(boton.getId().substring(5, 6));
					cont++;
					setValue(botones, 'X', id);
					results(botones, turnos);
					if(!win)
					IA();
				}
			}
		});
	}
	
	public void IA(){
		
		int id, botonId;
		boolean fin=false;
		
		
		if(turnos){
			do{
				id = (int) Math.floor(Math.random()*8)+1;
				
				for(int i=0; i<botones2.size(); i++){
					botonId=Integer.parseInt(botones2.get(i).getId().substring(5,6));
					if(id==botonId && !botones2.get(botonId-1).isDisabled()){
						botones2.get(botonId-1).setDisable(true);
						botones2.get(botonId-1).setText("X");
						turnos=false;
						setValue(botones, 'X', botonId);
						results(botones, turnos);
						cont++;
						fin=!fin;
						break;
					}
				}
			}while(!fin);
		}
		
		else{
			do{
				id = (int) Math.floor(Math.random()*8)+1;
				
				for(int i=0; i<botones2.size(); i++){
					botonId=Integer.parseInt(botones2.get(i).getId().substring(5,6));
					if(id==botonId && !botones2.get(botonId-1).isDisabled()){
						botones2.get(botonId-1).setDisable(true);
						botones2.get(botonId-1).setText("O");
						turnos=true;
						setValue(botones, 'O', botonId);
						results(botones, turnos);
						cont++;
						fin=!fin;
						break;
					}
				}
			}while(!fin);
		}
	}
	
	//Localiza a partir del id del boton su posicion en la matriz (By Jony)
	protected void setValue(char[][] botones, char newChar, int id) {
		id -= 1;
		int i = id/3;
		int j = id%3;
		
		botones[i][j] = newChar;
	}
	
	//COMPROBACION DE RESULTADOS
	protected void results(char[][] botones, boolean turnos){

		if(cont==9){
			turn.setText("Empate");
			win=true;
		}
			
		//Comprobacion de linea
		for(int i=0; i<3; i++){
			
			if (botones[i][0]!=0 && botones[i][1]!=0 && botones[i][2]!=0)
			{
				if(botones[i][0]==botones[i][1] && botones[i][1]==botones[i][2]){
					if(!turnos){
						turn.setText("Ganador: X");
						win=true;
					}
					else{
						turn.setText("Ganador: O");
						win=true;
					}
				}
			}
		}
		
		//Comprobacion de columnas
		for(int i=0; i<3; i++){
			
			if (botones[0][i]!=0 && botones[1][i]!=0 && botones[2][i]!=0) 
			{
				if(botones[0][i]==botones[1][i] && botones[1][i]==botones[2][i]){
					if(!turnos){
						turn.setText("Ganador: X");
						win=true;
					}
					else{
						turn.setText("Ganador: O");
						win=true;
					}
				}
			}
		}
		
		//Comprobacion Diagonal 1
		if(botones[0][0]==botones[1][1] && botones[1][1]==botones[2][2] && botones[1][1] != 0){
			if(!turnos){
				turn.setText("Ganador: X");
				win=true;
			}
			else{
				turn.setText("Ganador: O");
				win=true;
			}
		}

		//Comprobacion Diagonal 2
		if(botones[0][2]==botones[1][1] && botones[1][1]==botones[2][0] && botones[1][1] != 0){
			if(!turnos){
				turn.setText("Ganador: X");
				win=true;
			}
				
			else{
				turn.setText("Ganador: O");
				win=true;
			}
		}
		//Si hay ganador o se finaliza la partida se activa el botón reiniciar
		if (turn.getText().equals("Ganador: X") || turn.getText().equals("Ganador: O") || cont == 9){
			
			reinicia.setDisable(false);
			//Si se gana se bloquean el resto de botones
			for(int i=0; i<botones2.size(); i++){
				botones2.get(i).setDisable(true);
			}
		}
	}
	
	//REINICIA EL JUEGO 
	@FXML protected void restart(){
		
		for(int i=0; i<botones2.size(); i++){
			botones2.get(i).setDisable(false);
			botones2.get(i).setText("");
		}
		
		if(human.isSelected())
			cont=1;
		if(humanvs.isSelected() || compu.isSelected())
			cont=0;
		win=false;
		inicializaPartida();
	}
}
