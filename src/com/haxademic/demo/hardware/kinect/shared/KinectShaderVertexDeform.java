package com.haxademic.demo.hardware.kinect.shared;

import com.haxademic.core.app.P;
import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.constants.AppSettings;
import com.haxademic.core.draw.context.DrawUtil;
import com.haxademic.core.draw.context.OpenGLUtil;
import com.haxademic.core.file.FileUtil;
import com.haxademic.core.hardware.kinect.KinectSize;

import controlP5.ControlP5;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PShape;
import processing.opengl.PShader;

public class KinectShaderVertexDeform
extends PAppletHax {
	public static void main(String args[]) { PAppletHax.main(Thread.currentThread().getStackTrace()[1].getClassName()); }


	PImage texture;
	PShape obj;
	float angle;
	PShader texShader;
	float _frames = 240;

	// uv coordinates
	public float kinectLeft = 0;
	public float kinectRight = 1;
	public float kinectTop = 0;
	public float kinectBottom = 1;
	
	protected ControlP5 _cp5;
	
	protected PGraphics tex;


	protected void overridePropsFile() {
//		p.appConfig.setProperty( AppSettings.KINECT_V2_WIN_ACTIVE, true );
		p.appConfig.setProperty( AppSettings.KINECT_ACTIVE, true );
	}

	public void setup() {
		super.setup();	
		p.smooth( OpenGLUtil.SMOOTH_HIGH );

		int controlY = 0;
		int controlSpace = 12;
		_cp5 = new ControlP5(this);

		_cp5.addSlider("kinectLeft").setPosition(20,controlY+=controlSpace).setWidth(100).setRange(-1.0f,1.0f).setValue(-0.08f);
		_cp5.addSlider("kinectRight").setPosition(20,controlY+=controlSpace).setWidth(100).setRange(0f,2f).setValue(1.06f);
		_cp5.addSlider("kinectTop").setPosition(20,controlY+=controlSpace).setWidth(100).setRange(-1.0f,1.0f).setValue(-0.08f);
		_cp5.addSlider("kinectBottom").setPosition(20,controlY+=controlSpace).setWidth(100).setRange(0,2f).setValue(1.04f);

		tex = p.createGraphics(KinectSize.WIDTH, KinectSize.HEIGHT);
	}
	
	protected void setupDisplacementImg() {
		// create geometry
		// obj = createSheet(250, p.kinectWrapper.getDepthImage());
		obj = createSheet(200, tex);
		
		// load shader
		texShader = loadShader(
				FileUtil.getFile("haxademic/shaders/vertex/kinect-displace-frag-texture.glsl"), 
				FileUtil.getFile("haxademic/shaders/vertex/kinect-displace-sheet-vert.glsl")
				);
		
		texShader.set("displacementMap", p.kinectWrapper.getDepthImage());
		texShader.set("displaceStrength", 500.0f);
	}

	public void drawApp() {
		if(p.frameCount == 1) setupDisplacementImg();
		background(0);
		
		p.pushMatrix();

		// rendering
		float percentComplete = ((float)(p.frameCount%_frames)/_frames);
		angle = P.TWO_PI * percentComplete;
		
		// read audio data into offscreen texture
//		OpenGLUtil.setWireframe(p.g, false);
//		OpenGLUtil.setWireframe(p.g, true);
		
		// set center screen & rotate
		translate(width/2, height/2, -300);
//		rotateY(P.PI);
//		rotateY(p.mouseX / 100f);
		DrawUtil.basicCameraFromMouse(p.g);
//		rotateY(0.3f * P.sin(percentComplete * P.TWO_PI));

		
		// obj.texture(p.kinectWrapper.getDepthImage());
		tex.beginDraw();
		tex.image(p.kinectWrapper.getRgbImage(), tex.width * kinectLeft, tex.height * kinectTop, tex.width * kinectRight, tex.height * kinectBottom);
		tex.endDraw();
//		tex.filter(P.INVERT);
//		EdgesFilter.instance(p).applyTo(tex);
		
		// obj = createSheet(100, tex);		
		obj.texture(tex);
		
		// set shader properties & set on processing context
		texShader.set("displacementMap", p.kinectWrapper.getDepthImage());
		p.shader(texShader);  
		p.shape(obj);
		p.resetShader();
		
		p.popMatrix();
	}


	PShape createSheet(int detail, PImage tex) {
		p.textureMode(NORMAL);
		PShape sh = p.createShape();
		sh.beginShape(QUADS);
		sh.noStroke();
		sh.noFill();
		sh.texture(tex);
		float cellW = tex.width / detail;
		float cellH = tex.height / detail;
		int numVertices = 0;
		for (int col = 0; col < tex.width; col += cellW) {
			for (int row = 0; row < tex.height; row += cellH) {
				float xU = col;
				float yV = row;
				float x = -tex.width/2f + xU;
				float y = -tex.height/2f + yV;
				float z = 0;
				sh.normal(x, y, z);
				sh.vertex(x, y, z, 					P.map(xU, 0, tex.width, 0, 1f), P.map(yV, 0, tex.height, 0, 1f));
				sh.vertex(x, y + cellH, z, 			P.map(xU, 0, tex.width, 0, 1f), P.map(yV + cellH, 0, tex.height, 0, 1f));    
				sh.vertex(x + cellW, y + cellH, z,	P.map(xU + cellW, 0, tex.width, 0, 1f), P.map(yV + cellH, 0, tex.height, 0, 1f));    
				sh.vertex(x + cellW, y, z, 			P.map(xU + cellW, 0, tex.width, 0, 1f), P.map(yV, 0, tex.height, 0, 1f));
				numVertices++;
			}
		}
		P.println(numVertices, "vertices");
		sh.endShape(); 
		return sh;
	}

}

