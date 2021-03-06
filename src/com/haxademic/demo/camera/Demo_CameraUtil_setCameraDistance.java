package com.haxademic.demo.camera;

import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.camera.CameraUtil;
import com.haxademic.core.constants.AppSettings;
import com.haxademic.core.draw.context.DrawUtil;

public class Demo_CameraUtil_setCameraDistance 
extends PAppletHax {
	public static void main(String args[]) { PAppletHax.main(Thread.currentThread().getStackTrace()[1].getClassName()); }

	protected String DIST = "DIST";
	protected String NEAR = "NEAR";
	
	protected void overridePropsFile() {
		p.appConfig.setProperty(AppSettings.SHOW_SLIDERS, true );
	}

	public void setupFirstFrame() {
		p.prefsSliders.addSlider(DIST, 4000, 100, 20000, 100, false);
		p.prefsSliders.addSlider(NEAR, 100, 100, 10000, 10, false);
	}

	public void drawApp() {
		p.background(0);
		DrawUtil.setCenterScreen(p);
		DrawUtil.setDrawCenter(p);
		
		int maxDist = (int) p.prefsSliders.value(DIST);
		int minDist = (int) p.prefsSliders.value(NEAR);
		CameraUtil.setCameraDistance(p.g, minDist, maxDist);
		
		p.fill(255);
		p.stroke(0);
		p.strokeWeight(1);
		int zStep = 500;
		for (int i=0; i < 20000; i += zStep) {
			p.pushMatrix();
			p.translate(0, 0, -i);
			p.rotateX(i);
			p.rotateY(i);
			p.box(i/3);
			p.popMatrix();
		}
		
		CameraUtil.resetCamera(p.g);
		p.camera();
	}
}
