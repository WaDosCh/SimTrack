package ch.awae.simtrack.core.ui;

import java.util.function.Supplier;

import ch.awae.simtrack.core.Graphics;

public class LabelWithUpdatedText extends Label {

	private Supplier<String> supplier;

	public LabelWithUpdatedText(Supplier<String> supplier, boolean isTitle) {
		super(supplier.get(), isTitle);
		this.supplier = supplier;
	}
	
	@Override
	public void render(Graphics g) {
		this.title = this.supplier.get();
		super.render(g);
	}
	
	@Override
	protected String getTextForSizeCalculation() {
		return this.supplier.get();
	}
}
