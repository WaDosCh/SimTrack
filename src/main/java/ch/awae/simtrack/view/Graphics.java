package ch.awae.simtrack.view;

import java.awt.*;
import java.awt.RenderingHints.Key;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;
import java.util.Stack;

import lombok.AllArgsConstructor;

public class Graphics extends Graphics2D {

	@AllArgsConstructor
	public static class GraphicsContext {
		AffineTransform transform;
		Stroke stroke;
	}

	private final Graphics2D backer;
	private Stack<GraphicsContext> stack = new Stack<>();

	public Graphics(Graphics2D backer) {
		this.backer = backer;
	}

	// STACK MANAGEMENT
	public void pop() {
		if (this.stack.size() > 0) {
			GraphicsContext d = this.stack.pop();
			backer.setTransform(d.transform);
			backer.setStroke(d.stroke);
		}
	}

	public void push() {
		this.stack.push(new GraphicsContext(backer.getTransform(), backer.getStroke()));
	}

	@SuppressWarnings("unchecked")
	public Stack<GraphicsContext> getStack() {
		push();
		return (Stack<GraphicsContext>) stack.clone();
	}

	public void setStack(Stack<GraphicsContext> stack) {
		this.stack = stack;
		pop();
	}

	// ------------------------------------------------------------------
	// BACKER-BASED METHODS
	// ------------------------------------------------------------------

	@Override
	public void draw(Shape s) {
		backer.draw(s);
	}

	@Override
	public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
		return backer.drawImage(img, xform, obs);
	}

	@Override
	public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
		backer.drawImage(img, op, x, y);
	}

	@Override
	public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
		backer.drawRenderedImage(img, xform);
	}

	@Override
	public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
		backer.drawRenderableImage(img, xform);
	}

	@Override
	public void drawString(String str, int x, int y) {
		backer.drawString(str, x, y);
	}

	@Override
	public void drawString(String str, float x, float y) {
		backer.drawString(str, x, y);
	}

	@Override
	public void drawString(AttributedCharacterIterator iterator, int x, int y) {
		backer.drawString(iterator, x, y);
	}

	@Override
	public void drawString(AttributedCharacterIterator iterator, float x, float y) {
		backer.drawString(iterator, x, y);
	}

	@Override
	public void drawGlyphVector(GlyphVector g, float x, float y) {
		backer.drawGlyphVector(g, x, y);
	}

	@Override
	public void fill(Shape s) {
		backer.fill(s);
	}

	@Override
	public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
		return backer.hit(rect, s, onStroke);
	}

	@Override
	public GraphicsConfiguration getDeviceConfiguration() {
		return backer.getDeviceConfiguration();
	}

	@Override
	public void setComposite(Composite comp) {
		backer.setComposite(comp);
	}

	@Override
	public void setPaint(Paint paint) {
		backer.setPaint(paint);
	}

	@Override
	public void setStroke(Stroke s) {
		backer.setStroke(s);
	}

	@Override
	public void setRenderingHint(Key hintKey, Object hintValue) {
		backer.setRenderingHint(hintKey, hintValue);
	}

	@Override
	public Object getRenderingHint(Key hintKey) {
		return backer.getRenderingHint(hintKey);
	}

	@Override
	public void setRenderingHints(Map<?, ?> hints) {
		backer.setRenderingHints(hints);
	}

	@Override
	public void addRenderingHints(Map<?, ?> hints) {
		backer.addRenderingHints(hints);
	}

	@Override
	public RenderingHints getRenderingHints() {
		return backer.getRenderingHints();
	}

	@Override
	public void translate(int x, int y) {
		backer.translate(x, y);
	}

	@Override
	public void translate(double tx, double ty) {
		backer.translate(tx, ty);
	}

	@Override
	public void rotate(double theta) {
		backer.rotate(theta);
	}

	@Override
	public void rotate(double theta, double x, double y) {
		backer.rotate(theta, x, y);
	}

	@Override
	public void scale(double sx, double sy) {
		backer.scale(sx, sy);
	}

	@Override
	public void shear(double shx, double shy) {
		backer.shear(shx, shy);
	}

	@Override
	public void transform(AffineTransform Tx) {
		backer.transform(Tx);
	}

	@Override
	public void setTransform(AffineTransform Tx) {
		backer.setTransform(Tx);
	}

	@Override
	public AffineTransform getTransform() {
		return backer.getTransform();
	}

	@Override
	public Paint getPaint() {
		return backer.getPaint();
	}

	@Override
	public Composite getComposite() {
		return backer.getComposite();
	}

	@Override
	public void setBackground(Color color) {
		backer.setBackground(color);
	}

	@Override
	public Color getBackground() {
		return backer.getBackground();
	}

	@Override
	public Stroke getStroke() {
		return backer.getStroke();
	}

	@Override
	public void clip(Shape s) {
		backer.clip(s);
	}

	@Override
	public FontRenderContext getFontRenderContext() {
		return backer.getFontRenderContext();
	}

	@Override
	public java.awt.Graphics create() {
		return backer.create();
	}

	@Override
	public Color getColor() {
		return backer.getColor();
	}

	@Override
	public void setColor(Color c) {
		backer.setColor(c);
	}

	@Override
	public void setPaintMode() {
		backer.setPaintMode();
	}

	@Override
	public void setXORMode(Color c1) {
		backer.setXORMode(c1);
	}

	@Override
	public Font getFont() {
		return backer.getFont();
	}

	@Override
	public void setFont(Font font) {
		backer.setFont(font);
	}

	@Override
	public FontMetrics getFontMetrics(Font f) {
		return backer.getFontMetrics(f);
	}

	@Override
	public Rectangle getClipBounds() {
		return backer.getClipBounds();
	}

	@Override
	public void clipRect(int x, int y, int width, int height) {
		backer.clipRect(x, y, width, height);
	}

	@Override
	public void setClip(int x, int y, int width, int height) {
		backer.setClip(x, y, width, height);
	}

	@Override
	public Shape getClip() {
		return backer.getClip();
	}

	@Override
	public void setClip(Shape clip) {
		backer.setClip(clip);
	}

	@Override
	public void copyArea(int x, int y, int width, int height, int dx, int dy) {
		backer.copyArea(x, y, width, height, dx, dy);
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		backer.drawLine(x1, y1, x2, y2);
	}

	@Override
	public void fillRect(int x, int y, int width, int height) {
		backer.fillRect(x, y, width, height);
	}

	@Override
	public void clearRect(int x, int y, int width, int height) {
		backer.clearRect(x, y, width, height);
	}

	@Override
	public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		backer.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
	}

	@Override
	public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		backer.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
	}

	@Override
	public void drawOval(int x, int y, int width, int height) {
		backer.drawOval(x, y, width, height);
	}

	@Override
	public void fillOval(int x, int y, int width, int height) {
		backer.fillOval(x, y, width, height);
	}

	@Override
	public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		backer.drawArc(x, y, width, height, startAngle, arcAngle);
	}

	@Override
	public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		backer.fillArc(x, y, width, height, startAngle, arcAngle);
	}

	@Override
	public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
		backer.drawPolyline(xPoints, yPoints, nPoints);
	}

	@Override
	public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
		backer.drawPolygon(xPoints, yPoints, nPoints);
	}

	@Override
	public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
		backer.fillPolygon(xPoints, yPoints, nPoints);
	}

	@Override
	public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
		return backer.drawImage(img, x, y, observer);
	}

	@Override
	public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
		return backer.drawImage(img, x, y, width, height, observer);
	}

	@Override
	public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
		return backer.drawImage(img, x, y, bgcolor, observer);
	}

	@Override
	public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
		return backer.drawImage(img, x, y, width, height, bgcolor, observer);
	}

	@Override
	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2,
			ImageObserver observer) {
		return backer.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
	}

	@Override
	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2,
			Color bgcolor, ImageObserver observer) {
		return backer.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor, observer);
	}

	@Override
	public void dispose() {
		backer.dispose();
	}

}
