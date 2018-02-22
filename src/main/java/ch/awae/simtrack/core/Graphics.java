package ch.awae.simtrack.core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Delegate;

public class Graphics extends Graphics2D {

	public static interface GraphicsStack {
	};

	@AllArgsConstructor
	private final static @Data class PrivateGraphicsStack implements GraphicsStack {

		final AffineTransform Tx;
		final Stroke stroke;
		final Color color;
		final Font font;
		final Shape clip;
		final PrivateGraphicsStack tail;

		public PrivateGraphicsStack() {
			Tx = null;
			stroke = null;
			color = null;
			font = null;
			tail = null;
			clip = null;
		}

		public PrivateGraphicsStack prep(AffineTransform T, Stroke stroke, Color color, Font font, Shape clip) {
			return new PrivateGraphicsStack(T, stroke, color, font, clip, this);
		}
	}

	@Delegate
	private final Graphics2D backer;
	private PrivateGraphicsStack stack = new PrivateGraphicsStack();

	public Graphics(Graphics2D backer) {
		this.backer = backer;
	}

	// STACK MANAGEMENT
	public void pop() {
		if (stack.Tx != null) {
			peek();
			stack = stack.tail;
		}
	}

	public void push() {
		stack = stack.prep(backer.getTransform(), backer.getStroke(), backer.getColor(), backer.getFont(),
				backer.getClip());
	}

	public void peek() {
		if (stack.Tx != null) {
			backer.setTransform(stack.Tx);
			backer.setColor(stack.color);
			backer.setFont(stack.font);
			backer.setStroke(stack.stroke);
			backer.setClip(stack.clip);
		}
	}

	public GraphicsStack getStack() {
		push();
		return stack;
	}

	public void setStack(GraphicsStack stack) {
		if (stack instanceof PrivateGraphicsStack) {
			this.stack = (PrivateGraphicsStack) stack;
			pop();
		} else {
			throw new IllegalArgumentException("unsupported stack type");
		}
	}

}
