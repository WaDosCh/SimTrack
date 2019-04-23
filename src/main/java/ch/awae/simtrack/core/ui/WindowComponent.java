package ch.awae.simtrack.core.ui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Rectangle;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.scene.game.view.Design;
import lombok.Getter;

public class WindowComponent extends BaseComponent {

	public String title;
	protected @Getter BasePanel content;
	/**
	 * may be used& changed externally to manage rendering and input handling order
	 */
	public int zIndex;

	protected int bannerHeight;
	protected int baselineDelta;
	protected boolean moving;
	protected Point movingMouseInitialPos;
	protected InputController input;
	protected Font font;
	protected Point movingWindowInitialPos;

	public WindowComponent(BasePanel panel, Font font, String title, InputController input) {
		super();
		this.content = panel;
		this.title = title;
		this.font = font;
		this.input = input;
		this.zIndex = Integer.MAX_VALUE;
		Canvas c = new Canvas();
		FontMetrics fm = c.getFontMetrics(font);
		this.bannerHeight = 4 + fm.getAscent() + fm.getDescent();
		this.baselineDelta = 2 + fm.getAscent();
	}

	@Override
	public void render(Graphics g) {
		if (this.moving) {
			Point mousepos = this.input.getMousePosition();
			Point delta = new Point(mousepos.x - movingMouseInitialPos.x, mousepos.y - movingMouseInitialPos.y);
			this.pos = new Point(this.movingWindowInitialPos);
			this.pos.translate(delta.x, delta.y);
			stayInsideClip(g);
			this.content.layout(this.pos.x, this.pos.y + bannerHeight, this.size.width,
					this.size.height - bannerHeight);
		}
		this.content.render(g);
		g.setColor(Color.gray);
		g.drawRect(this.pos.x, this.pos.y, this.size.width, this.size.height);
		g.fillRect(this.pos.x, this.pos.y, this.size.width, this.bannerHeight);
		g.setFont(this.font);
		g.setColor(Design.textColor);
		g.drawString(this.title, this.pos.x + Design.buttonTextMarginX, this.pos.y + this.baselineDelta);
	}

	private void stayInsideClip(Graphics g) {
		Rectangle clip = g.getClipBounds();
		if (this.pos.x < clip.x) {
			this.pos.x = clip.x;
		}
		if (this.pos.y < clip.y) {
			this.pos.y = clip.y;
		}
		if (this.pos.x + this.size.width > clip.x + clip.width) {
			this.pos.x = clip.x + clip.width - this.size.width;
		}
		if (this.pos.y + this.size.height > clip.y + clip.height) {
			this.pos.y = clip.y + clip.height - this.size.height;
		}
	}

	@Override
	public void layout(int x, int y, int w, int h) {
		super.layout(x, y, w, h);
		this.content.layout(x, y + bannerHeight, w, h - bannerHeight);
	}

	@Override
	public void handleInput(InputEvent event) {
		if (event.isAction(InputAction.SELECT)) {
			if (event.isPress() && isPointInside(event.getCurrentMousePosition())) {
				this.zIndex = Integer.MAX_VALUE;
			}
			if (isInsideBanner(event.getCurrentMousePosition()) && event.isPress()) {
				this.moving = true;
				this.movingMouseInitialPos = event.getCurrentMousePosition();
				this.movingWindowInitialPos = new Point(this.pos);
				event.consume();
				return;
			}
			if (event.isReleased() && this.moving) {
				this.moving = false;
				event.consume();
				return;
			}
		}

		if (this.content.isPointInside(event.getCurrentMousePosition())) {
			this.content.handleInput(event);
		}
		// consume any events inside this window and don't pass them to windows behind
		if (isPointInside(event.getCurrentMousePosition())) {
			event.consume();
		}
	}

	public boolean isInsideBanner(Point pos) {
		return pos.x >= this.pos.x && pos.y >= this.pos.y && pos.x <= this.pos.x + this.size.getWidth()
				&& pos.y <= this.pos.y + this.bannerHeight;
	}

	@Override
	public Dimension getPreferedDimension() {
		Dimension size = this.content.getPreferedDimension();
		size.height += bannerHeight; // top nav and border bottom
		return size;
	}

}
