package com.gdx.bomberman.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Animation {
	private Array<TextureRegion> frames;
	private float maxFramTime;
	private float currFrameTime;
	private int frameCount;
	private int frame;

	public Animation(TextureRegion region, int frameCount, float cycleTime){
		frames = new Array<>();
		int frameWidth = region.getRegionWidth() / frameCount;
		for(int i = 0; i < frameCount; i++){
			frames.add(new TextureRegion(region, i * frameWidth, 0, frameWidth, region.getRegionHeight()));
		}
		this.frameCount = frameCount;
		maxFramTime = cycleTime / frameCount;
		frame = 0;
	}

	public void update(float dt){
		currFrameTime += dt;
		if(currFrameTime > maxFramTime){
			frame++;
			currFrameTime = 0;
		}
		if(frame >= frameCount){
			frame = 0;
		}
	}

	public TextureRegion getFrame(){
		return frames.get(frame);
	}

}
