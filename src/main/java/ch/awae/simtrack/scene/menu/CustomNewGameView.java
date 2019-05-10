package ch.awae.simtrack.scene.menu;

import java.awt.Dimension;

import ch.awae.simtrack.core.SceneController;
import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.core.ui.BasePanel;
import ch.awae.simtrack.core.ui.Button;
import ch.awae.simtrack.core.ui.InputField;
import ch.awae.simtrack.core.ui.Label;
import ch.awae.simtrack.core.ui.WindowComponent;
import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.scene.game.model.ModelCreationOptions;
import ch.awae.simtrack.scene.game.model.ModelFactory;
import ch.awae.simtrack.scene.game.view.Design;

public class CustomNewGameView extends WindowComponent {

	private SceneController controller;
	private InputField sizeX;
	private InputField sizeY;
	private InputField startingMoney;
	private InputField bulldozeCost;
	private InputField connections;

	public CustomNewGameView(SceneController controller, InputController input) {
		super(Design.titleFont, input);
		this.controller = controller;

		initMenu();
		setDefaults();
	}

	private void setDefaults() {
		ModelCreationOptions op = new ModelCreationOptions();
		this.sizeX.setText(op.size.width);
		this.sizeY.setText(op.size.height);
		this.startingMoney.setText(op.startingMoney);
		this.bulldozeCost.setText(op.bulldozeCost);
		this.connections.setText(op.connectionCount);
	}

	private void initMenu() {
		this.title = "New Custom Game";

		BasePanel horizontal = new BasePanel().setVertical(false);
		BasePanel labels = new BasePanel();
		BasePanel inputs = new BasePanel();
		horizontal.add(labels, inputs);

		this.sizeX = new InputField(10, this.input).setNumbersOnly();
		labels.add(new Label("Map width:"));
		inputs.add(this.sizeX);
		this.sizeY = new InputField(10, this.input).setNumbersOnly();
		labels.add(new Label("Map height:"));
		inputs.add(this.sizeY);
		this.startingMoney = new InputField(10, this.input).setNumbersOnly();
		labels.add(new Label("Player starting money:"));
		inputs.add(this.startingMoney);
		this.bulldozeCost = new InputField(10, this.input).setNumbersOnly();
		labels.add(new Label("Bulldoze cost:"));
		inputs.add(this.bulldozeCost);
		this.connections = new InputField(10, this.input).setNumbersOnly();
		labels.add(new Label("Starting connections:"));
		inputs.add(this.connections);

		addComponent(horizontal);
		addComponent(new Button("Generate Map", this.input, this::createCustomGame));
		addComponent(new Button("Cancel", this.input, this::dispose));
	}

	private void createCustomGame() {
		ModelCreationOptions op = new ModelCreationOptions();
		op.size = new Dimension(this.sizeX.getNumber(), this.sizeY.getNumber());
		op.startingMoney = this.startingMoney.getNumber();
		op.bulldozeCost = this.bulldozeCost.getNumber();
		op.connectionCount = this.connections.getNumber();

		this.controller.loadScene(Game.class, ModelFactory.getModel(op));
	}
	
	@Override
	public void handleInput(InputEvent event) {
		super.handleInput(event);
		if (!event.isConsumed && event.isPressActionAndConsume(InputAction.DESELECT))
			dispose();
	}

}
