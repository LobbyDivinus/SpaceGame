package info.flowersoft.spacegame;

import info.flowersoft.appframe.R;
import info.flowersoft.gameframe.AppRenderer;
import info.flowersoft.gameframe.BlittingEngine;
import info.flowersoft.gameframe.TouchButton;
import info.flowersoft.gameframe.description.FontDescription;
import info.flowersoft.gameframe.shape.ShapeFactory;
import info.flowersoft.gameframe.touch.TouchPoint;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Object3D;
import com.threed.jpct.RGBColor;
import com.threed.jpct.util.Overlay;

import android.content.Context;
import android.os.Bundle;

public class MyApp extends AppRenderer {
	
	private Overlay overlayBackground;
	private Overlay overlayParallax1;
	private Overlay overlayParallax2;
	private Overlay overlayColor;
	
	private float scale;
	
	private float camX;
	private float camY;
	
	private FontDescription font;
	
	private ShapeFactory shapeFactory;
	private BlittingEngine blitting;
	
	private TouchButton leftButton;
	private TouchButton rightButton;
	
	private Ship ship;
	
	public MyApp(Bundle savedInstance, Context con) {
		super(savedInstance, con);
	}
	
	@Override
	protected void onCreate(int w, int h) {
		loadTexture("bg", R.raw.bg);
		loadTexture("parallax1", R.raw.parallax1, true);
		loadTexture("parallax2", R.raw.parallax2, true);
		loadTexture("colorbg", R.raw.colorbg);
		loadTexture("ship", R.raw.ship, true);
		loadTexture("particle", R.raw.particle, true);
		loadTexture("button", R.raw.button, true);
		loadTexture("button_touched", R.raw.button_touched, true);
		loadTexture("font", R.raw.font, true);
		
		font = new FontDescription("font", true, 22, 47, ' ', Character.MAX_VALUE);
		
		shapeFactory = new ShapeFactory(getWorld(), getBuffer());
		shapeFactory.createTextLine(font, "Hallo Welt", 0, 100);
		
		blitting = new BlittingEngine(getBuffer());
		
		scale = h / 512f;
		
		
		
		leftButton = new TouchButton(getWorld());
		leftButton.setTexture("button", 0, 0, 256, 148);
		leftButton.setPressedTexture("button_touched", 0, 0, 256, 148);
		leftButton.setShape(0, h - (int) (0.75 * scale * 148), (int) (0.75 * scale * 256), (int) (0.75 * scale * 148));
		
		rightButton = new TouchButton(getWorld());
		rightButton.setTexture("button", 0, 0, 256, 148);
		rightButton.setPressedTexture("button_touched", 0, 0, 256, 148);
		rightButton.setShape(w - (int) (0.75 * scale * 256), h - (int) (0.75 * scale * 148),
				(int) (0.75 * scale * 256), (int) (0.75 * scale * 148));
		
		
		
		overlayBackground = new Overlay(getWorld(), 0, 0, 0, 0, "bg", true);
		overlayParallax1 = new Overlay(getWorld(), 0, 0, 0, 0, "parallax1", true);
		overlayParallax2 = new Overlay(getWorld(), 0, 0, 0, 0, "parallax2", true);
		overlayColor = new Overlay(getWorld(), 0, 0, 0, 0, "colorbg", true);
		
		overlayBackground.getObject3D().setSortOffset(4);
		overlayParallax1.getObject3D().setSortOffset(3);
		overlayParallax2.getObject3D().setSortOffset(2);
		overlayColor.getObject3D().setSortOffset(1);
		
		overlayParallax1.setTransparency(15);
		overlayParallax2.setTransparency(15);
		overlayColor.setTransparencyMode(Object3D.TRANSPARENCY_MODE_ADD);
		overlayColor.setTransparency(15);
		
		overlayBackground.setNewCoordinates(0, 0, w, h);
		overlayParallax1.setNewCoordinates(0, 0, w, h);
		overlayParallax2.setNewCoordinates(0, 0, w, h);
		overlayColor.setNewCoordinates(0, 0, w, h);
		
		
		
		ship = new Ship(getWorld());
	}
	
	@Override
	protected void onResolutionChange(int w, int h) {
	}

	@Override
	protected void update(double time) {
		// Update buttons
		TouchPoint newPoint = getNewTouchPoint();
		TouchPoint removedPoint = getRemovedTouchPoint();
		leftButton.update(newPoint, removedPoint);
		rightButton.update(newPoint, removedPoint);
		
		// Update ship
		ship.update(time, leftButton.isPressed(), rightButton.isPressed());
	}

	@Override
	protected void beforeRendering() {
		FrameBuffer buffer = getBuffer();
		
		// Clear buffer
		buffer.clear(RGBColor.BLACK);
		
		// Move ship
		camX = - ship.getX() + getWidth() / scale / 2;
		camY = - ship.getY() + getHeight() / scale / 2;
		ship.draw(camX, camY, scale);
		
		// Move background layers
		float baseX = - scale * camX;
		float baseY = - scale * camY;
		float w = getWidth() / scale / 2;
		float h = getHeight() / scale / 2;
		
		overlayBackground.setSourceCoordinates(
				baseX,
				baseY,
				baseX + w,
				baseY + h);
		
		overlayParallax1.setSourceCoordinates(
				1.1f * baseX,
				1.1f * baseY,
				1.1f * baseX + w,
				1.1f * baseY + h);
		
		overlayParallax2.setSourceCoordinates(
				1.2f * baseX,
				1.2f * baseY,
				1.2f * baseX + w,
				1.2f * baseY + h);
		
		overlayColor.setSourceCoordinates(
				0.1f * baseX,
				0.1f * baseY,
				0.1f * baseX + w / 10,
				0.1f * baseY + h / 10);
	}

	@Override
	protected void afterRendering() {
		blitting.setScale(scale, scale);
		blitting.blitTextLine(font, String.valueOf((int) getFPS()) + "FPS", 0, 0);
	}

	@Override
	public void onPause() {
	}

	@Override
	public void onResume() {
	}

}
