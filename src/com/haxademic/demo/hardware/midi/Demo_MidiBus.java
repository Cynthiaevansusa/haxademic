package com.haxademic.demo.hardware.midi;

import com.haxademic.core.app.P;
import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.constants.AppSettings;
import com.haxademic.core.hardware.midi.devices.LaunchControl;
import com.haxademic.core.hardware.midi.devices.NovationColors;

public class Demo_MidiBus
extends PAppletHax {
	public static void main(String args[]) { PAppletHax.main(Thread.currentThread().getStackTrace()[1].getClassName()); }

	protected void overridePropsFile() {
		p.appConfig.setProperty(AppSettings.MIDI_DEVICE_IN_INDEX, 0 );
		p.appConfig.setProperty(AppSettings.MIDI_DEVICE_OUT_INDEX, 1 );
	}
	
	public void drawApp() {
		p.background(0);
		// p.midi.isMidiButtonOn(LaunchControl.PAD_01)
		// print debug
		p.midiState.printButtons();
		p.midiState.printCC();
		
		// outgoing midi changes the LED color on the launch control
		p.midiState.sendMidiOut(true, 0, LaunchControl.PAD_01, P.round(P.p.audioFreq(2) * 150f));
		p.midiState.sendMidiOut(true, 0, LaunchControl.PAD_02, P.round(P.p.audioFreq(3) * 150f));
		p.midiState.sendMidiOut(true, 0, LaunchControl.PAD_03, P.round(P.p.audioFreq(4) * 150f));
		p.midiState.sendMidiOut(true, 0, LaunchControl.PAD_04, P.round(P.p.audioFreq(5) * 150f));
		p.midiState.sendMidiOut(true, 0, LaunchControl.PAD_05, P.round(P.p.audioFreq(6) * 150f));
		p.midiState.sendMidiOut(true, 0, LaunchControl.PAD_06, P.round(P.p.audioFreq(7) * 150f));
		p.midiState.sendMidiOut(true, 0, LaunchControl.PAD_07, P.round(P.p.audioFreq(8) * 150f));
		p.midiState.sendMidiOut(true, 0, LaunchControl.PAD_08, P.round(P.p.audioFreq(9) * 150f));
//		p.midiState.sendMidiOut(true, 0, LaunchControl.PAD_08, P.round(127f * p.mousePercentX()));
		
		p.midiState.sendMidiOut(true, 0, LaunchControl.PAD_08, NovationColors.colors[P.round((NovationColors.colors.length - 1) * p.mousePercentX())]);

	}
	
}