/**
 *
 */
package me.d2o.tictactoe.eventhandlers;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.config.statemachine.States;
import me.d2o.persistence.model.game.Game;
import me.d2o.persistence.model.game.GameRepository;
import me.d2o.statemachine.MachineEvent;
import me.d2o.statemachine.MachineEventHandler;

/**
 * Class: CapitolInitializeHandler
 *
 * @author bo.hanssen
 * @since Jan 28, 2017 10:20:54 AM
 *
 */
@Service
@Transactional
public class PlayHandler extends MachineEventHandler {

	@Autowired
	private GameRepository gameRepository;
	
	private Scanner sc;
	
	@PostConstruct
	private void init(){
		sc = new Scanner(System.in);
	}
	
	@PreDestroy
	private void destroy(){
		sc.close();
	}
	
	private String getInput(String player){
		while (true){
			try{
				System.out.println(player+ " please input coordinates:");
				String inputstring = sc.nextLine().trim();
				Pattern p = Pattern.compile("[ABCabc..][123..]");
				Matcher m = p.matcher(inputstring);
				if (m.find()){
					return inputstring;
				} 
			} catch(Exception ex){
				//
			}
		}
	}
	@Override
	public void handleEvent(MachineEvent event) {
		Game game = gameRepository.findOne(event.getMachineId());
		System.out.println("\nIt is "+game.getState().getState()+"'s turn:");
		game.printBoard();
		char m = game.getState().getState().equals(States.PLAYER1) ? 'x' : 'o';
		while (true){
			String[] cor = getInput(game.getState().getState()).split("");
			if (game.pin(cor[0], cor[1], m)){
				break;
			}
			System.out.println("Invalid coordinate!");
		}
		event.setPropagate(Events.NEXT_TURN);
	}

	@Override
	public String eventType() {
		return Events.PLAY;
	}

}
