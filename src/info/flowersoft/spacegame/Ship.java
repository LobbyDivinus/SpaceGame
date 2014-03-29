package info.flowersoft.spacegame;

import java.util.ArrayList;
import java.util.List;

import com.threed.jpct.World;
import com.threed.jpct.util.Overlay;

public class Ship {
	
	private class Engine {
		private Emitter emitter;
		private float x;
		private float y;
		private float angle;
		private boolean enabled;
	}
	
	private World world;
	
	private Overlay img;
	private int sourceX;
	private int sourceY;
	private int sourceW;
	private int sourceH;
	
	private float x;
	private float y;
	private float angle;
	private float xSpeed;
	private float ySpeed;
	private float angleSpeed;
	
	private Engine engineLeftLeft;
	private Engine engineLeft;
	private Engine engineRight;
	private Engine engineRightRight;
	
	private List<Engine> engines;
	
	public Ship(World world) {
		this.world = world;
		engines = new ArrayList<Engine>();
		
		sourceX = 0;
		sourceY = 0;
		sourceW = 256;
		sourceH = 256;
		
		img = new Overlay(world, 0, 0, sourceX + sourceW, sourceY + sourceH, "ship", true);
		img.setSourceCoordinates(sourceX, sourceY, sourceX + sourceW, sourceY + sourceH);
		img.setTransparency(15);
		
		engineLeftLeft = addEngine(-64, 68, (float) (0.5 * Math.PI));
		engineLeft = addEngine(-32, 68, (float) (0.5 * Math.PI));
		engineRight = addEngine(32, 68, (float) (0.5 * Math.PI));
		engineRightRight = addEngine(64, 68, (float) (0.5 * Math.PI));
	}
	
	private Engine addEngine(float x, float y, float angle) {
		Engine e = new Engine();
		e.emitter = new Emitter("particle", world);
		e.emitter.setLifetime(1);
		e.emitter.setRate(0.1f);
		e.emitter.setSpeed(80);
		e.emitter.setSize(54, 54);
		e.emitter.setDirection(angle);
		e.x = x;
		e.y = y;
		e.angle = angle;
		e.enabled = false;
		engines.add(e);
		return e;
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setAngle(float angle) {
		this.angle = angle;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getAngle() {
		return angle;
	}
	
	public void update(double time, boolean left, boolean right) {;
		x += time * xSpeed;
		y += time * ySpeed;
		angle += time * angleSpeed;
		
		engineLeftLeft.enabled = left;
		engineLeft.enabled = left;
		engineRight.enabled = right;
		engineRightRight.enabled = right;
		
		float move = 0;
		float dir = 0;
		if (left) {
			move++;
			dir--;
		}
		if (right) {
			move++;
			dir++;
		}
		move *= 10 * time;
		dir *= time;
		xSpeed += Math.cos(angle + engineLeft.angle) * move;
		ySpeed += - Math.sin(angle + engineLeft.angle) * move;
		angleSpeed += dir;
		
		xSpeed *= 0.99;
		ySpeed *= 0.99;
		angleSpeed *= 0.99;
		
		for (Engine engine:engines) {
			engine.emitter.setEnabled(engine.enabled);
			engine.emitter.update(time);
		}
	}
	
	public void draw(float movex, float movey, float scale) {
		float posx = scale * (movex + x);
		float posy = scale * (movey + y);
		
		img.setNewCoordinates((int) (posx - scale * sourceW / 2),
				(int) (posy - scale * sourceH / 2),
				(int) (posx + scale * sourceW / 2),
				(int) (posy + scale * sourceH / 2));
		
		img.setRotation(angle);
		
		for (Engine engine:engines) {
			float ex = (float) (Math.cos(angle) * engine.x + Math.sin(angle) * engine.y);
			float ey = (float) (Math.cos(angle) * engine.y - Math.sin(angle) * engine.x);
			engine.emitter.setPosition(
					(int) (posx + scale * ex),
					(int) (posy + scale * ey));
			engine.emitter.setDirection(- angle + engine.angle);
			engine.emitter.draw(scale);
		}
	}
	
}
