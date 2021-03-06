package com.haxademic.demo.ui;

import com.haxademic.core.app.PAppletHax;

public class Demo_PrefsSliders 
extends PAppletHax {
	public static void main(String args[]) { PAppletHax.main(Thread.currentThread().getStackTrace()[1].getClassName()); }
	
	protected String R = "R";
	protected String G = "G";
	protected String B = "B";
	
	public void setupFirstFrame () {
		prefsSliders.addSlider(R, 255, 0, 255, 0.5f);
		prefsSliders.addSlider(G, 255, 0, 255, 0.5f);
		prefsSliders.addSlider(B, 255, 0, 255, 0.5f);
	}
	
	public void drawApp() {
		p.background(
			prefsSliders.value(R),
			prefsSliders.value(G),
			prefsSliders.value(B)
		);
	}
	
}
