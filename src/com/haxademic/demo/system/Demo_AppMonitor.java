package com.haxademic.demo.system;

import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.draw.context.DrawUtil;
import com.haxademic.core.system.AppMonitor;

public class Demo_AppMonitor 
extends PAppletHax {
	public static void main(String args[]) { PAppletHax.main(Thread.currentThread().getStackTrace()[1].getClassName()); }

	protected AppMonitor appMonitor;
	
//	protected void overridePropsFile() {
//		// The magic happens here - configure the 2nd screen viewer with AppSettings
//		p.appConfig.setProperty( AppSettings.APP_VIEWER_WINDOW, true );
//		p.appConfig.setProperty( AppSettings.APP_VIEWER_SCALE, 0.75f );
//	}

	public void setupFirstFrame() {
		appMonitor = new AppMonitor();
	}

	public void drawApp() {
		DrawUtil.setDrawCenter(p);
		p.background((float)mouseX/width * 255,(float)mouseY/height * 255,0);
		p.fill(255);
		
		p.pushMatrix();
		p.translate(p.width/2, p.height/2);
		p.rotate(p.frameCount * 0.01f);
		p.rect(0, 0, 100, 100);
		p.popMatrix();
		
		p.fill(255);
		p.text("CLICK TO CRASH", 20, 30);
	}
	
	public void mouseClicked() {
		super.mouseClicked();
		// crash!
		Object nulll = null;
		nulll.toString();
	}

}
