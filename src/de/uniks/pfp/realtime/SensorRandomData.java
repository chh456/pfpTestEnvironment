package de.uniks.pfp.realtime;

import java.util.ArrayList;

// objects from this class provide random data for the sensor objects in this package 
public class SensorRandomData implements Runnable {

	// the sensor you want to manipulate
	Sensor sensor;
	
	// the sensor will change state at these times in ms
	private ArrayList<Integer> stateChanges;
	private static ArrayList<Integer> DEFAULTSTATES = new ArrayList<>();
	
	// you can provide your own state changes here or init with null to take default values
	public SensorRandomData(Sensor sensor, ArrayList <Integer> myStates) {
		if (myStates != null && myStates.size() > 0)
			stateChanges = myStates;
		else stateChanges = init();
		this.sensor = sensor;
	}

	@Override
	public void run() {
		// iterate over all stateChanges and set the sensor accordingly
		int index = 0;
		boolean lastState = false; // we assume sensor is off in the beginning. Maybe change that in productive environment.
		while (index < stateChanges.size()) {
			
			try {
				Thread.sleep(stateChanges.get(index)); // wait listElement milliseconds
				lastState = !sensor.getSensorState();
				sensor.setSensorState(lastState); // toggle sensor state after waiting
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			index++;
			
		}
		sensor.setSensorState(!lastState);
		sensor.deactivate();
	}
	
	// default stateChanges
	private static ArrayList<Integer> init() {
		if (DEFAULTSTATES.size() > 0) return DEFAULTSTATES;
		ArrayList<Integer> changes = new ArrayList<>();
		changes.add(1000); // set sensor on after 1 second
		changes.add(3000); // set sensor off after 3 seconds
		changes.add(1000); // ..
		changes.add(5000);
		changes.add(2000);
		return changes;
	}
	
}
