package se.chalmers.dryleafsoftware.androidrally.model.gameModel;

import java.util.ArrayList;
import java.util.List;

import se.chalmers.dryleafsoftware.androidrally.model.cards.Card;
import se.chalmers.dryleafsoftware.androidrally.model.cards.Deck;
import se.chalmers.dryleafsoftware.androidrally.model.gameBoard.GameBoard;
import se.chalmers.dryleafsoftware.androidrally.model.robots.Robot;

public class GameModel {
	
	private GameBoard gameBoard;
	private List<Robot> robots;
	private Deck deck;
	
	/**
	 * Creates a game board of size 12x16 tiles. Also creates robots based
	 * on the amount of players. Creates a deck with cards that is shuffled.
	 * 
	 * @param nbrOfPlayers the number of players in the game including CPU:s
	 */
	public GameModel(int nbrOfPlayers) {
		gameBoard = new GameBoard(12, 16, nbrOfPlayers);
		for (int i = 0; i < nbrOfPlayers; i++) {
			robots.add(new Robot(i, 14));
		}
		deck = new Deck();
	}
	
	/**
	 * Give cards to all players/CPU:s.
	 */
	public void dealCards() {
		for(Robot robot : robots) {
			int health = robot.getHealth();
			List<Card> drawnCards = new ArrayList<Card>();
			for (int i = 0; i < health; i++) {
				drawnCards.add(deck.drawCard());
			}
			robot.addCards(drawnCards);
		}
	}

	/**
	 * A method that make board elements "do things" with robots, i.e.
	 * move robots that are standing on a conveyor belt and so on.
	 */
	public void activateBoardElements() {
		for (Robot robot : robots) {
			gameBoard.getTile(robot.getX(), robot.getY()).action(robot);
		}
		//INSTANTACTION TODO
	}
	
	public void fireLasers() {
		
	}
	
	/**
	 * If a robots life is equal to 0 it will be deleted from the game.
	 */
	public void deleteDeadRobots() {
		int i = 0;
		while (i < robots.size()) {
			if (robots.get(i).getLife() == 0) {
				robots.remove(i);
			} else {
				i++;
			}
		}
		if (robots.size() == 1) {
			gameOver(robots.get(0));
		}
	}
	
	public void gameOver(Robot winner) {
		//TODO add functionality
	}
	
	public void moveRobots() {
		List<Card[]> currentCards = new ArrayList<Card[]>();
		for (int i = 0; i < robots.size(); i++) {
			Card[] chosenCards = robots.get(i).getChosenCards();
			for (int j = 0; j < 5; j++) {
				currentCards.add(chosenCards);
			}
		}
		
		for (int i = 0; i < 5; i++) { //loop all 5 cards
			for(int j = 0; j < robots.size(); j++){ //for all robots
				int highestPriority = 0;
				int indexOfHighestPriority = -1; //player index in array
				for (int k = 0; k < currentCards.size(); k++) { //find highest card
					if (currentCards.get(k) != null //check if card exists and..
						&&	highestPriority //..is the highest one
							< currentCards.get(k)[i].getPriority()) {
						highestPriority = currentCards.get(k)[i].getPriority();
						indexOfHighestPriority = k;
					}
				}
				//Move the robot that has the highest priority on its card
				Robot currentRobot = robots.get(indexOfHighestPriority);
				currentCards.get(indexOfHighestPriority)[i]
						.action(currentRobot);
				gameBoard.getTile(currentRobot.getX(), currentRobot.getY())
						.instantAction(currentRobot);
				
				deleteDeadRobots();
				
				//Remove the card so it doesn't execute twice
				currentCards.get(indexOfHighestPriority)[i] = null;
			}
			activateBoardElements();
			deleteDeadRobots();
			fireLasers();
			deleteDeadRobots();

		}
		//TODO give specials to robots standing on "wrench & hammer"
	}
}
