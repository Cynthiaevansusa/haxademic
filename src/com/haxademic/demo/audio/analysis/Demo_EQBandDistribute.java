package com.haxademic.demo.audio.analysis;

import com.haxademic.core.app.P;
import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.constants.AppSettings;

public class Demo_EQBandDistribute 
extends PAppletHax {
	public static void main(String args[]) { PAppletHax.main(Thread.currentThread().getStackTrace()[1].getClassName()); }
	
	protected void overridePropsFile() {
		p.appConfig.setProperty( AppSettings.INIT_ESS_AUDIO, false);
		p.appConfig.setProperty( AppSettings.INIT_MINIM_AUDIO, true);
		p.appConfig.setProperty( AppSettings.INIT_BEADS_AUDIO, true);
	}

	public void drawApp() {
		background(0);
		p.noStroke();
		float numElements = p.width;

		float eqStep = 512f / numElements;
		float barW = numElements / 512f;
		int eqIndex = 0;
		for(int i=0; i < numElements; i++) {
			eqIndex = P.floor(i * eqStep);
			float eq = p.audioFreq(eqIndex);
			p.fill(255f * eq);
			p.rect(i * barW, 0, barW, p.height);
		}

	}
}
