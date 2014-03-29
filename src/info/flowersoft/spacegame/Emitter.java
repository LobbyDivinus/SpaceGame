package info.flowersoft.spacegame;

import java.util.LinkedList;
import java.util.List;

import com.threed.jpct.Object3D;
import com.threed.jpct.World;
import com.threed.jpct.util.Overlay;

public class Emitter {

	private class Particle {
		Overlay image;
		float x;
		float y;
		float xSpeed;
		float ySpeed;
		float angle;
		double startTime;
		double time;
	}
	
	private World world;
	
	private String tex;
	
	private int texWidth;
	
	private int texHeight;
	
	private List<Particle> particles;
	
	private List<Particle> removeParticles;
	
	private int x;
	
	private int y;
	
	private float angle;
	
	private float speed;
	
	private double lifetime;
	
	private double rate;
	
	private boolean enabled;
	
	private double lastRate;
	
	private List<Particle> pool;
	
	public Emitter(String texture, World world) {
		tex = texture;
		this.world = world;
		
		particles = new LinkedList<Particle>();
		removeParticles = new LinkedList<Particle>();
		pool = new LinkedList<Particle>();
	}
	
	public void setSize(int width, int height) {
		texWidth = width;
		texHeight = height;
	}
	
	public void setPosition(int xpos, int ypos) {
		x = xpos;
		y = ypos;
	}
	
	public void setDirection(float angle) {
		this.angle = angle;
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public void setLifetime(double time) {
		lifetime = time;
	}
	
	public void setRate(double rate) {
		this.rate = rate;
	}
	
	public void setEnabled(boolean state) {
		enabled = state;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void update(double time) {
		removeParticles.clear();
		for (Particle p:particles) {
			updateParticle(p, time);
		}
		for (Particle p:removeParticles) {
			p.image.setVisibility(false);
			particles.remove(p);
			pool.add(p);
		}
		
		lastRate -= 2 * time * Math.random();
		if (lastRate <= 0 && enabled) {
			lastRate = rate;
			addParticle();
		}
	}
	
	public void draw(float scale) {
		for (Particle p:particles) {
			drawParticle(p, scale);
		}
	}
	
	public void flush() {
		for (Particle p:particles) {
			pool.add(p);
		}
		particles.clear();
	}
	
	public void dispose() {
		flush();
		
		for (Particle p:pool) {
			p.image.dispose();
		}
		pool.clear();
	}
	
	private void addParticle() {
		Particle p;
		
		if (pool.isEmpty()) {
			p = new Particle();
			
			p.image = new Overlay(world, 0, 0, 0, 0, tex);
			p.image.setTransparencyMode(Object3D.TRANSPARENCY_MODE_ADD);
		} else {
			p = pool.remove(0);
			
			p.image.setVisibility(true);
		}
		
		p.x = 0;
		p.y = 0;
		
		double angle = this.angle + 0.3 * (Math.random() - 0.5) * Math.PI;
		p.xSpeed = speed * (float) Math.cos(angle);
		p.ySpeed = speed * (float) Math.sin(angle);
		
		p.angle = (float) (Math.random() * 2 * Math.PI);
		
		p.time = lifetime * (0.5 + 0.5 * Math.random());
		p.startTime = p.time;
		
		p.image.setTransparency(0);
		p.image.setRotation(p.angle);
		
		particles.add(p);
	}
	
	private void updateParticle(Particle p, double time) {
		p.time -= time;
		if (p.time < 0) {
			removeParticles.add(p);
		}
		
		p.x += time * p.xSpeed;
		p.y += time * p.ySpeed;
	}
	
	public void drawParticle(Particle p, float scale) {
		p.image.setNewCoordinates((int) (scale * (p.x - texWidth / 2)) + x,
				(int) (scale * (p.y - texHeight / 2)) + y,
				(int) (scale * (p.x + texWidth - texWidth / 2)) + x,
				(int) (scale * (p.y + texHeight - texHeight / 2)) + y);
		
		p.image.setTransparency((int) (15 * p.time / p.startTime));
	}
}
