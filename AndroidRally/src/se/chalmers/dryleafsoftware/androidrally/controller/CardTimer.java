package se.chalmers.dryleafsoftware.androidrally.controller;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.badlogic.gdx.utils.Timer;

public class CardTimer extends Timer {
	
	private final int robotID;
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	public CardTimer(long secondsInFuture, int robotID, PropertyChangeListener pcl) {
		super();
		this.robotID = robotID;
		pcs.addPropertyChangeListener(pcl);
		schedule(new Timer.Task() {
			@Override
			public void run() {
				pcs.firePropertyChange("cardTimeOut", -1, CardTimer.this.robotID);
				stop();
				clear();
			}
		}, secondsInFuture);
		start();
	}
}
