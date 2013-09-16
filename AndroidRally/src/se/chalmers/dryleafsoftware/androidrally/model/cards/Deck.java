package se.chalmers.dryleafsoftware.androidrally.model.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
	private List<Card> cards;
	private List<Card> drawnCards;
	
	public Deck() {
		cards = new ArrayList<Card>();
		cards = new ArrayList<Card>();
		
		for (int i = 10; i <= 60; i+=10) {
			cards.add(new Turn(i, TurnType.UTURN));
		}
		for (int i = 70; i <= 410; i+=20) {
			cards.add(new Turn(i, TurnType.LEFT));
		}
		for (int i = 80; i <= 420; i+=20) {
			cards.add(new Turn(i, TurnType.RIGHT));
		}
		for (int i = 430; i <= 480; i+=10) {
			cards.add(new Move(i, -1));
		}
		for (int i = 490; i <= 660; i+=10) {
			cards.add(new Move(i, 1));
		}
		for (int i = 670; i <= 780; i+=10) {
			cards.add(new Move(i, 2));
		}
		for (int i = 790; i <= 840; i+=10) {
			cards.add(new Move(i, 3));
		}
		
		//Remove locked cards from deck
		for (int i = 0; i < cards.size(); i++) {
			if (cards.get(i).isLocked()) {
				cards.remove(i);
			}
		}
		
		shuffleDeck();
	}
	
	public void shuffleDeck() {
		Collections.shuffle(cards);
	}
	
	public Card drawCard() {
		//TODO Check if cards is empty
		Card drawnCard = cards.get(0);
		cards.remove(0);
		drawnCards.add(drawnCard);
		return drawnCard;
	}
}
