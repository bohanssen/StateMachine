package me.d2o.persistence.model.game;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;

import me.d2o.config.statemachine.States;
import me.d2o.statemachine.StateMachine;

/**
 * The Class Game.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:31 PM
 */
@Entity
public class Game {

	@Id
	private String id;

	@OneToOne(cascade=CascadeType.ALL)
	private StateMachine state;

	private char[][] board = new char[][]{{'.','.','.'},{'.','.','.'},{'.','.','.'}};
	
	@PrePersist
	public void init() {
		this.state = new StateMachine(States.INITIAL);
		this.id = state.getMachineId();
	}

	public String getId() {
		return id;
	}

	public StateMachine getState() {
		return state;
	}

	public void printBoard(){
		System.out.println("  A B C");
		System.out.println(1+" "+board[0][0]+" "+board[0][1]+" "+board[0][2]);
		System.out.println(2+" "+board[1][0]+" "+board[1][1]+" "+board[1][2]);
		System.out.println(3+" "+board[2][0]+" "+board[2][1]+" "+board[2][2]);
	}
	
	public boolean pin(String c, String r, char m){
		int column = c.equalsIgnoreCase("A") ? 0 : c.equalsIgnoreCase("B") ? 1 : 2; 
		int row = Integer.parseInt(r)-1;
		if (board[row][column] == '.'){
			board[row][column] = m;
			return true;
		} else {
			return false;
		}
	}
	
	private boolean checkUtil(boolean check, char m, char x1, char x2, char x3){
		return (check || (m==x1 && m==x2 && m==x3));
	}
	public boolean checkVictory(char m){
		boolean victory = false;
		
		victory = checkUtil(victory, m, board[0][0], board[0][1], board[0][2]);
		victory = checkUtil(victory, m, board[1][0], board[1][1], board[1][2]);
		victory = checkUtil(victory, m, board[2][0], board[2][1], board[2][2]);
		
		victory = checkUtil(victory, m, board[0][0], board[1][0], board[2][0]);
		victory = checkUtil(victory, m, board[0][1], board[1][1], board[2][1]);
		victory = checkUtil(victory, m, board[0][2], board[1][2], board[2][2]);
		
		victory = checkUtil(victory, m, board[0][0], board[1][1], board[2][2]);
		victory = checkUtil(victory, m, board[0][2], board[1][1], board[2][0]);
		return victory;
	}
}
