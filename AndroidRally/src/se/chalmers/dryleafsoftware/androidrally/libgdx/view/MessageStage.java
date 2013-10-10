package se.chalmers.dryleafsoftware.androidrally.libgdx.view;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import se.chalmers.dryleafsoftware.androidrally.libgdx.gameboard.RobotView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MessageStage extends Stage { 
	
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	private final Table container;
	private final Image backGroundImage;
	private final Image inputCatcher;
	private final TextButtonStyle buttonStyle;
	private final Table exitPanel;
	private final Texture backGround;
	private final LabelStyle labelStyle;
	
	public static final String EVENT_OK = "messageOk";
	
	public MessageStage() {
		super();
		
		backGround = new Texture(Gdx.files.internal("textures/messageBackGround.png"));
		backGround.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		// Default camera
		OrthographicCamera cardCamera = new OrthographicCamera(480, 800);
		cardCamera.zoom = 1.0f;
		cardCamera.position.set(240, 400, 0f);
		cardCamera.update();
		setCamera(cardCamera);

		Texture buttonTexture = new Texture(Gdx.files.internal("textures/button.png"));
		buttonTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		buttonStyle = new TextButtonStyle(
				new TextureRegionDrawable(new TextureRegion(buttonTexture, 0, 0, 32, 32)),
				new TextureRegionDrawable(new TextureRegion(buttonTexture, 0, 32, 32, 32)),
				null);
		buttonStyle.font = new BitmapFont();
		
		labelStyle = new LabelStyle(new BitmapFont(), Color.WHITE);		
		
		this.container = new Table();
		container.debug();
		container.setSize(480, 800);
		container.setLayoutEnabled(false);
		addActor(container);
		
		ClickListener gl = new ClickListener();
		
		inputCatcher = new Image();
        inputCatcher.setSize(480, 800);
        inputCatcher.addListener(gl);
				
		backGroundImage = new Image(new TextureRegion(backGround));
		backGroundImage.addListener(gl);
		        
        exitPanel = new Table();
        exitPanel.setBackground(new TextureRegionDrawable(new TextureRegion(backGround)));
        exitPanel.setVisible(true);
        exitPanel.setSize(280, 200);
        exitPanel.setPosition(100, 500);
        exitPanel.debug();
        exitPanel.setLayoutEnabled(false);
        
        Label label = new Label("If you exit now you will not be able to change your cards."
        		+ "Do you want to exit anyway?", labelStyle);
        Table labelContainer = new Table();
        labelContainer.debug();
        labelContainer.setSize(200, 200);
        labelContainer.add(label);
        exitPanel.add(labelContainer);
        
        TextButton exit = new TextButton("Yes", buttonStyle);
        exit.setSize(100, 30);
        exit.setPosition(0, 0);
        exitPanel.add(exit); // Border
        exit.addListener(new ClickListener() {
    		@Override
    		public void clicked(InputEvent event, float x, float y) {
    			pcs.firePropertyChange(EVENT_OK, 0, 1);
    		}
    	});
        
        TextButton noExit = new TextButton("No", buttonStyle);
        noExit.setSize(100, 30);
        noExit.setPosition(100, 0);
        exitPanel.add(noExit); // Border
        noExit.addListener(new ClickListener() {
    		@Override
    		public void clicked(InputEvent event, float x, float y) {
    			container.clear();
    		}
    	});
	}
	
	public void addListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}
	
	public void removeListener(PropertyChangeListener listener) {
		this.pcs.removePropertyChangeListener(listener);
	}
	
	/**
	 * Will show the game over screen.
	 * @param robots The robots in the game.
	 */
	public void dispGameOver(List<RobotView> robots) {
		closeAll();
		
		Table panel = new Table();
		panel.setSize(280, 200);
		panel.setPosition(100, 500);
		panel.debug();
		panel.setBackground(new TextureRegionDrawable(new TextureRegion(backGround)));
		panel.setVisible(true);
			
		Label message = new Label("", labelStyle);
		panel.add(message);     
		
        TextButton gameOverButton = new TextButton("Return to menu", buttonStyle);
        panel.row();
        panel.add(gameOverButton); // Border
        gameOverButton.addListener(new ClickListener() {
    		@Override
    		public void clicked(InputEvent event, float x, float y) {
    			pcs.firePropertyChange(EVENT_OK, 0, 1);
    		}
    	});
		
		setInputCatcher(panel.getX(), panel.getY(), 
				panel.getWidth(), panel.getHeight());	
		
		int scorePos = 0;
		for(RobotView r : robots) {
			if(r.isGameDead()) {
				scorePos++;
			}
		}
		message.setText("You were the " + convertToText(scorePos+1) + " to die!");
		panel.layout();
		
		container.add(panel);
	}
	
	public void dispCloseMessage() {
		closeAll();
		setInputCatcher(0, 0, 480, 800);		
		container.add(exitPanel);
	}
	
	public void closeAll() {
		container.clear();
	}
	
	private void setInputCatcher(float x, float y, float w, float h) {
		inputCatcher.setSize(w, h);
		inputCatcher.setPosition(x, y);
		container.add(inputCatcher);
	}
	
	private String convertToText(int i) {
		return new String[] {"first", "second", 
				"third", "fourth", "fifth", 
				"sixth", "seventh", "eighth"}[i-1];
	}
	
	/**
	 * Will show the screen which tells the user who won.
	 * @param robots The robots in the game.
	 */
	public void dispGameWon(List<RobotView> robots) {
		closeAll();
		
		Table panel = new Table();
		panel.setSize(280, 200);
		panel.setPosition(100, 500);
		panel.debug();
		panel.setBackground(new TextureRegionDrawable(new TextureRegion(backGround)));
		panel.setVisible(true);
		
		LabelStyle labelStyle = new LabelStyle(new BitmapFont(), Color.WHITE);				
		Label message = new Label("", labelStyle);
		panel.add(message);     
		
        TextButton gameOverButton = new TextButton("Return to menu", buttonStyle);
        panel.row();
        panel.add(gameOverButton); // Border
        gameOverButton.addListener(new ClickListener() {
    		@Override
    		public void clicked(InputEvent event, float x, float y) {
    			pcs.firePropertyChange(EVENT_OK, 0, 1);
    		}
    	});
		
		setInputCatcher(panel.getX(), panel.getY(), 
				panel.getWidth(), panel.getHeight());	
		
		for(RobotView r : robots) {
			if(r.hasFinished()) {
				message.setText("\"" + r.getName() +"\" just won!");
				break;
			}
		}				
		panel.layout();
		container.add(panel);
	}
}
