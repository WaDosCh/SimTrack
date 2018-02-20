package ch.awae.simtrack.view;

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

	public static interface Stack {
	};

	@AllArgsConstructor
	private final static @Data class TStack implements Stack {

		final AffineTransform Tx;
		final Stroke stroke;
		final Color color;
		final Font font;
		final Shape clip;
		final TStack tail;

		public TStack() {
			Tx = null;
			stroke = null;
			color = null;
			font = null;
			tail = null;
			clip = null;
		}

		public TStack prep(AffineTransform T, Stroke stroke, Color color, Font font, Shape clip) {
			return new TStack(T, stroke, color, font, clip, this);
		}
	}

	@Delegate
	private final Graphics2D backer;
	private TStack stack = new TStack();

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

	public Stack getStack() {
		push();
		return stack;
	}

	public void setStack(Stack stack) {
		if (stack instanceof TStack) {
			this.stack = (TStack) stack;
			pop();
		} else {
			throw new IllegalArgumentException("unsupported stack type");
		}
	}

}
