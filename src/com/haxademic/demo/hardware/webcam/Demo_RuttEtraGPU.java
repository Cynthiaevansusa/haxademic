package com.haxademic.demo.hardware.webcam;

import com.haxademic.core.app.P;
import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.constants.AppSettings;
import com.haxademic.core.constants.PRenderers;
import com.haxademic.core.draw.context.DrawUtil;
import com.haxademic.core.draw.filters.pshader.BlendTowardsTexture;
import com.haxademic.core.draw.filters.pshader.BrightnessFilter;
import com.haxademic.core.draw.image.ImageUtil;
import com.haxademic.core.draw.shapes.PShapeUtil;
import com.haxademic.core.draw.shapes.pshader.LinesDeformAndTextureFilter;
import com.haxademic.core.file.DemoAssets;
import com.haxademic.core.file.FileUtil;
import com.haxademic.core.hardware.webcam.IWebCamCallback;

import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PShape;
import processing.opengl.PShader;

public class Demo_RuttEtraGPU 
extends PAppletHax
implements IWebCamCallback {
	public static void main(String args[]) { PAppletHax.main(Thread.currentThread().getStackTrace()[1].getClassName()); }

	protected PGraphics webcamBuffer;
	protected PGraphics webcamLerped;

	protected PShape shape;
	protected float shapeExtent = 100;

	protected void overridePropsFile() {
		p.appConfig.setProperty(AppSettings.LOOP_FRAMES, 160);
		p.appConfig.setProperty(AppSettings.WEBCAM_INDEX, 3);
	}
	
	protected void setupFirstFrame() {
		// set up webcam
		p.webCamWrapper.setDelegate(this);
		webcamBuffer = p.createGraphics(p.width, p.height, PRenderers.P2D);
		webcamLerped = p.createGraphics(p.width, p.height, PRenderers.P2D);

		// build sheet mesh
		shape = p.createShape(P.GROUP);
		int rows = 100;
		int cols = 200;
		for (int y = 0; y < rows; y++) {
			PShape line = P.p.createShape();
			line.beginShape();
			line.stroke(255);
			line.strokeWeight(1);
			line.noFill();
			for (int x = 0; x < cols; x++) {
				line.vertex(x * 10f, y * 10f, 0);
			}
			line.endShape();
			shape.addChild(line);
		}
		PShapeUtil.centerShape(shape);
		PShapeUtil.scaleShapeToHeight(shape, p.height * 0.5f);
		PShapeUtil.addTextureUVToShape(shape, webcamBuffer);
		shapeExtent = PShapeUtil.getMaxExtent(shape);
		shape.disableStyle();

		shape.setTexture(webcamBuffer);
		p.debugView.setValue("shape.getVertexCount();", PShapeUtil.vertexCount(shape));
	}

	public void drawApp() {
		background(0);
		
		// rotate
		DrawUtil.setCenterScreen(p);
		DrawUtil.basicCameraFromMouse(p.g, 0.1f);

		// draw shader-displaced mesh
		LinesDeformAndTextureFilter.instance(p).setDisplacementMap(webcamLerped);
		LinesDeformAndTextureFilter.instance(p).setColorMap(webcamLerped);
		LinesDeformAndTextureFilter.instance(p).setWeight(p.mousePercentX() * 10f);
		LinesDeformAndTextureFilter.instance(p).setModelMaxExtent(shapeExtent * 2.1f);
		LinesDeformAndTextureFilter.instance(p).setColorThicknessMode((p.mousePercentY() > 0.5f));
		if(p.mousePercentX() > 0.5f) {
			LinesDeformAndTextureFilter.instance(p).setSheetMode(true);
			LinesDeformAndTextureFilter.instance(p).setDisplaceAmp(p.mousePercentY() * pg.height * 0.7f);
		} else {
			LinesDeformAndTextureFilter.instance(p).setSheetMode(false);
			LinesDeformAndTextureFilter.instance(p).setDisplaceAmp(p.mousePercentY() * pg.height * 0.01f);
		}
		//		p.shader(displacementShader, P.LINES);
		LinesDeformAndTextureFilter.instance(p).applyTo(p);

		// draw shape
		p.stroke(255);
		p.shape(shape);
		p.resetShader();
	}
	
	@Override
	public void newFrame(PImage frame) {
		// copy webcam and create motion detection at size of cropped webcam (and downscaling)
		ImageUtil.cropFillCopyImage(frame, webcamBuffer, true);
		ImageUtil.flipH(webcamBuffer);
		ImageUtil.flipV(webcamBuffer);
		
//		InvertFilter.instance(p).applyTo(webcamBuffer);
		
		BlendTowardsTexture.instance(p).setSourceTexture(webcamBuffer);
		BlendTowardsTexture.instance(p).setBlendLerp(0.35f);
		BlendTowardsTexture.instance(p).applyTo(webcamLerped);
		
		BrightnessFilter.instance(p).setBrightness(2f);
		BrightnessFilter.instance(p).applyTo(webcamBuffer);
//		ThresholdFilter.instance(p).applyTo(webcamBuffer);
				
		// set textures for debug view
		p.debugView.setTexture(frame);
		p.debugView.setTexture(webcamBuffer);
	}

}