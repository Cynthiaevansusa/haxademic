package com.haxademic.sketch.visualgorithms;

import com.haxademic.core.app.P;
import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.constants.PBlendModes;
import com.haxademic.core.constants.PRenderers;
import com.haxademic.core.draw.context.DrawUtil;
import com.haxademic.core.draw.filters.pshader.BlurHFilter;
import com.haxademic.core.draw.filters.pshader.BlurProcessingFilter;
import com.haxademic.core.draw.filters.pshader.BlurVFilter;
import com.haxademic.core.draw.filters.pshader.GrainFilter;
import com.haxademic.core.draw.filters.pshader.LeaveWhiteFilter;
import com.haxademic.core.draw.image.ImageUtil;

import processing.core.PGraphics;

public class BloomEffect
extends PAppletHax {
	public static void main(String args[]) { PAppletHax.main(Thread.currentThread().getStackTrace()[1].getClassName()); }
	
	protected PGraphics glowBuffer;

	public void setupFirstFrame() {
		glowBuffer = p.createGraphics(pg.width, pg.height, PRenderers.P2D);
	}

	public void drawApp() {
		// draw cube to buffer
		pg.beginDraw();
		pg.clear();
//		pg.background(0);
		DrawUtil.setCenterScreen(pg);
		DrawUtil.setBetterLights(pg);
		pg.fill(180 + 55f * P.sin(p.frameCount * 0.02f), 180 + 55f * P.sin(p.frameCount * 0.03f), 180 + 55f * P.sin(p.frameCount * 0.04f), 255);
		pg.stroke(0);
		pg.rotateX(p.frameCount * 0.01f);
		pg.rotateY(p.frameCount * 0.02f);
		pg.box(200 + 170f * P.sin(p.frameCount * 0.04f), 200 + 50f * P.sin(p.frameCount * 0.01f), 200 + 50f * P.sin(p.frameCount * 0.02f));
		pg.endDraw();
		
		// copy image & create glow version
		ImageUtil.copyImage(pg, glowBuffer);
		LeaveWhiteFilter.instance(p).setMix(0.7f);
		LeaveWhiteFilter.instance(p).applyTo(glowBuffer);
		BlurHFilter.instance(p).setBlurByPercent(p.mousePercentX() * 5f, glowBuffer.width);
		BlurVFilter.instance(p).setBlurByPercent(p.mousePercentY() * 5f, glowBuffer.height);
		for (int i = 0; i < 10; i++) {
			BlurHFilter.instance(p).applyTo(glowBuffer);
			BlurVFilter.instance(p).applyTo(glowBuffer);
		}

		// debug display
		p.background(0);
		p.image(pg, 0, 0, 320, 240);
		p.image(glowBuffer, 320, 0, 320, 240);
		p.image(pg, 0, 240, 320, 240);
		p.blendMode(PBlendModes.MULTIPLY);
		p.blendMode(PBlendModes.DARKEST);
		p.blendMode(PBlendModes.SCREEN);
		p.image(glowBuffer, 0, 240, 320, 240);
		p.blendMode(PBlendModes.BLEND);
		
		// draw back to source buffer
		pg.beginDraw();
		pg.blendMode(PBlendModes.SCREEN);
		pg.image(glowBuffer, 0, 0);
		pg.blendMode(PBlendModes.BLEND);
		pg.endDraw();
		
		p.image(pg, 320, 240, 320, 240);

		
		// post 
		GrainFilter.instance(p).setTime(p.frameCount * 0.01f);
		GrainFilter.instance(p).setCrossfade(0.03f);
		GrainFilter.instance(p).applyTo(p);
	}
}
