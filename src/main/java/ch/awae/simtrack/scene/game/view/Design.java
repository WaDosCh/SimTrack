package ch.awae.simtrack.scene.game.view;

import java.awt.Color;
import java.awt.Font;

import ch.awae.simtrack.util.Properties;
import ch.awae.simtrack.util.Resource;

public class Design {

	public static final Color almostOpaque = new Color(255, 255, 255, 235);
	public static final Color grayBorder;
	public static final Color textColor;
	public static final Color buttonBackground;
	public static final Color buttonHover;
	public static final Color buttonBorder;
	public static final Color textFieldBg;
	public static final Color textFieldBorder;
	public static final Color textFieldFocus;
	public static final Color textFieldBorderFocus;

	public static final Font textFont;
	public static final Font titleFont;

	public static final int toolbarHeight;
	public static final int buttonTextMarginX;
	public static final int buttonTextMarginY;
	public static final Color checkboxSelected;
	public static final Color checkboxNotSelected;
	public static final Color menuBlackOverlay;

	static {
		Properties props = Resource.getConfigProperties("design.properties");

		toolbarHeight = props.getInt("toolbarHeight");
		buttonTextMarginX = props.getInt("buttonMarginX");
		buttonTextMarginY = props.getInt("buttonMarginY");

		grayBorder = props.getColor("border");
		textColor = props.getColor("textColor");
		buttonBackground = props.getColor("buttonBackground");
		buttonHover = props.getColor("buttonHover");
		buttonBorder = props.getColor("buttonBorder");
		checkboxSelected = props.getColor("checkboxSelected");
		checkboxNotSelected = props.getColor("checkboxNotSelected");

		textFieldBg = props.getColor("textFieldBackground");
		textFieldBorder = props.getColor("textFieldBorder");
		textFieldFocus = props.getColor("textFieldFocus");
		textFieldBorderFocus = props.getColor("textFieldBorderFocus");
		
		menuBlackOverlay = props.getColor("menuBlackOverlay");

		textFont = new Font(props.getString("textFont"), 0, props.getInt("textSize"));
		titleFont = new Font(props.getString("titleFont"), 0, props.getInt("titleSize"));
	}

}
