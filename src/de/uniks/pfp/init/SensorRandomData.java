package de.uniks.pfp.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SensorRandomData {

	int [] sensorValues = new int[5];
	boolean stopped = false;
	String sensorName;
	
	public SensorRandomData(String sensorName) {
		this.sensorName = sensorName;
	}
	
	public Runnable getSzen(String sensorName) {
		switch (sensorName) {
			case("drehTischSensor"): return new Runnable() {
				@Override
				public void run() {
					while (!stopped);
				}
			};
			case("einspeiseSensor"): return new Runnable() {
				@Override
				public void run() {
					while (!stopped);
				}
			};
			default: return null;
		}
	}
	
}
