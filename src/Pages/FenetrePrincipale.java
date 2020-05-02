package Pages;

import Editor.LevelEditor;
import GameEngine.GameContent;
import IOEngine.Audio;

import javax.swing.*;
import java.awt.*;

public class FenetrePrincipale extends JFrame{

	private CardLayout panelSelection;
	private MainMenu mainMenu;
	private GameContent gameContent;
	private JPanel cardContent;
	private LevelSelector levelSelector;
	private LevelEditor levelEditor;
	private PlayersSelector playersSelector;
	private Settings settings;
	private Audio musiqueFond;

	public FenetrePrincipale(int width, int height, boolean isFullscreen){
		this.setTitle("Jeu de voiture ULTRA stylé");
		ImageIcon img = new ImageIcon(System.getProperty("user.dir")+"/res/textures/logo.png");
		setIconImage(img.getImage());
		if(isFullscreen){
			setExtendedState(JFrame.MAXIMIZED_BOTH);
			setUndecorated(true);
			Rectangle n = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
			width = (int)n.getWidth();
			height = (int)n.getHeight();
		}

		musiqueFond = new Audio(this);

		settings = new Settings(new Dimension(width, height), this);
		musiqueFond.playTheme("menu");
		// On charge les paramètres et la musique en premier de façon à ce que la musique soit lancée lors du chargement du reste
        panelSelection = new CardLayout();
        mainMenu = new MainMenu(width, height,this);
		gameContent = new GameContent(width, height,this);
		levelSelector = new LevelSelector(new Dimension(width,height),this);
		levelEditor = new LevelEditor(new Dimension(width,height),this);
		playersSelector = new PlayersSelector(new Dimension(width,height), this);

		cardContent = new JPanel();
		cardContent.setLayout(panelSelection);
		cardContent.add(mainMenu, "menu");
		cardContent.add(gameContent, "game");
		cardContent.add(levelSelector,"levelSelector");
		cardContent.add(levelEditor,"editor");
		cardContent.add(playersSelector, "playersSelector");
		cardContent.add(settings, "settings");
		setContentPane(cardContent);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setResizable(false);
		setVisible(true);
		setFocusable(true);

		panelSelection.show(cardContent, "menu");

		addKeyListener(playersSelector);
		addKeyListener(settings);
	}

	public GameContent getGameContent() {
		return gameContent;
	}

	public CardLayout getPanelSelection() {
		return panelSelection;
	}

	public JPanel getCardContent() {
		return cardContent;
	}

	public Audio getMusiqueFond(){
		return musiqueFond;
	}

	public Settings getSettings() {
		return settings;
	}

	public LevelSelector getLevelSelector() {
		return levelSelector;
	}
}
