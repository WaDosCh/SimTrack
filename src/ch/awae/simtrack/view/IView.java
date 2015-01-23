/*
 * SimTrack - Railway Planning and Simulation Game
 * Copyright (C) 2015 Andreas Wälchli
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.awae.simtrack.view;

import ch.awae.simtrack.model.IModel;

/**
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-23
 * @since SimTrack 0.2.1
 */
public interface IView {

	public void setModel(IModel model);

	public void moveScene(int dx, int dy);

	public void zoom(float dzoom, int fixX, int fixY);

	public void setScreenDimensions(int width, int height);

	public void renderView();

	public void setEditorRenderer(IRenderer renderer);

	public IModel getModel();

	public int getHorizontalScreenSize();

	public int getVerticalScreenSize();

	public IViewPort getViewPort();

}
