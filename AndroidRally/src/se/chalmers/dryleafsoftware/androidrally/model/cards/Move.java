package se.chalmers.dryleafsoftware.androidrally.model.cards;

import se.chalmers.dryleafsoftware.androidrally.model.robots.Robot;

/**
 * Move extends the class Card.
 * {@inheritDoc}
 */
public class Move extends Card implements Comparable<Move> {
	private int distance;

	/**
	 * Creates an instance of Move.
	 * 
	 * @param priority
	 *            the priority of the card
	 * @param nbrOfSteps
	 *            the number of steps the robot will move
	 */
	public Move(int priority, int nbrOfSteps) {
		super(priority);
		this.distance = nbrOfSteps;
	}

	/**
	 * Moves the robot 1 step.
	 * <p>
	 * Use the getDistance() method to multiply with the factor given in the
	 * constructor.
	 */
	@Override
	public void action(Robot robot) {
		robot.move((distance / Math.abs(distance)), robot.getDirection());
	}

	/**
	 * Used to be able to sort a list of Move cards with the highest priority
	 * first in the list.
	 */
	@Override
	public int compareTo(Move moveCard) {
		return moveCard.getDistance() - this.getDistance();
	}

	public int getDistance() {
		return distance;
	}
}