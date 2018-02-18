package com.haxademic.demo.draw.shapes;

import com.haxademic.core.app.P;
import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.constants.AppSettings;
import com.haxademic.core.constants.PBlendModes;
import com.haxademic.core.draw.context.DrawUtil;
import com.haxademic.core.draw.shapes.PShapeUtil;
import com.haxademic.core.file.FileUtil;

import processing.core.PShape;

public class Demo_PShapeUtil_setShapeMaterialTransparent 
extends PAppletHax {
	public static void main(String args[]) { PAppletHax.main(Thread.currentThread().getStackTrace()[1].getClassName()); }

	protected PShape shape;
	protected boolean overrideColor = false;

	protected void overridePropsFile() {
		p.appConfig.setProperty(AppSettings.WIDTH, 768);
		p.appConfig.setProperty(AppSettings.HEIGHT, 768);
	}
	
	protected void setupFirstFrame() {
		// build shape and assign texture
		shape = p.loadShape(FileUtil.getFile("haxademic/models/pink-car.obj"));
		
		// normalize shape
		PShapeUtil.centerShape(shape);
		PShapeUtil.scaleShapeToWidth(shape, p.width * 0.4f);
		PShapeUtil.meshRotateOnAxis(shape, P.PI, P.Z);
		
		// inspect materials to set target color transparent
		PShapeUtil.setShapeMaterialTransparent(shape, 0.079f, 0.13f, 0.4f, 0.5f);
	}
			
	public void drawApp() {
		// clear the screen
		background(10);
		p.noStroke();
		DrawUtil.setBasicLights(p.g);

		// rotate camera
		p.translate(p.width/2, p.height/2);
		p.rotateX(P.map(p.mouseY, 0, p.height, 0.5f, -0.5f));
		p.rotateY(P.map(p.mouseX, 0, p.width, 0, P.TWO_PI * 2));
		
		// draw shape
		p.shape(shape);
	}
	
}