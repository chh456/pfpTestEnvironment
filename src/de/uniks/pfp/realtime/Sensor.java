package de.uniks.pfp.realtime;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
// import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Sensor implements Runnable {

	String name;
	
	// time this sensor was started and stopped
	Long startTime;
	Long endTime;
	
	// sensor will quit when this is set to false
	boolean activated;

	// time in ms when this sensor will check it's state = sample frequency
	int tick;
	
	// in the test environment impulse is used to set this sensor's history
	boolean impulse;
	
	// saves the last states of the sensor
	private LinkedList <Boolean> sensorHistory = new LinkedList<>(); 

	// this list will keep track of the time this sensor changed it's state
	private ArrayList<Long> stateChanges = new ArrayList<>();
	
	// default sample frequency in ms
	private static int SAMPLINGFREQUENCY = 200; 	
	
	// for statistical issues
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SS");
	
	// TODO
	final Lock lock = new ReentrantLock();
	
	// initialize and activate a sensor with default values
	public Sensor(String name) {
		this.name = name;
		this.tick = SAMPLINGFREQUENCY; // set default sample frequency
		System.out.println("Sensor " + name + ": activate");
		activated = true;
	}
	
	// sets a custom sample frequency. be careful especially in productive environment!
	public Sensor(String name, int sampleFrequency) {
		this(name);
		if (sampleFrequency > 0)
			tick = sampleFrequency;
	}

	
	// checks whether sensor was in sensorState (on|off) over timeFrame ticks
	synchronized public boolean checkSensorState(int timeFrame, boolean sensorState) {
	
		// caller is interested in the last sensor state only or there was no valid timeFrame provided
		if (timeFrame <= 1) return sensorHistory.getLast().equals(sensorState);
		
		// timeFrame exceeds sensor's runtime
		if (timeFrame > sensorHistory.size()) return false;
		
		lock.lock();
		
		// we copy the list so we don't have to care about concurrency issues		
		LinkedList<Boolean> localList = new LinkedList<Boolean>(sensorHistory);
		
		// we get the last (timeFrame) states of the sensor's history
		ListIterator<Boolean> it = localList.listIterator(localList.size() - timeFrame);
	
		// the iterator now contains timeFrame states from sensor's history
		while(it.hasNext())
			if (it.next() != sensorState) return false; // if one of them is not the desired sensorState return false.
		
		lock.unlock();
		
		// sensor was in sensorState over timeFrame ticks
		return true; 
	}
	
	// default: check whether sensor was on for timeFrame ticks
	public boolean checkSensorState(int timeFrame) {
		return checkSensorState(timeFrame, true);
	}

	@Override
	public void run() {

		// we save the startTime when this sensor was started
		startTime = System.currentTimeMillis();
			
		while (activated) {
			// System.out.print(impulse ? '-' : '_');
			if (impulse())
				propertyChanged();
			try {
				Thread.sleep(SAMPLINGFREQUENCY);
			} catch (InterruptedException e) {
				// at this point our thread pool tries to interrupt because the sensor was not shut down
				activated = false;
				System.out.println("Sensor " + name + ": Unexpected shutdown");
				break;
				// e.printStackTrace();
			}
		}
		
		// sensor was shut down successfully
		endTime = System.currentTimeMillis();
		
		// print some stats. this will happen in a different method later.
		System.out.println("Sensor " + name + ": Activated " + sdf.format(startTime) + " in Thread " + Thread.currentThread().getName());
		
		boolean startState = false;
		
		// iterate over stateChanges 
		for (Long timeStateChanged : stateChanges) {
			System.out.println(name + " went from " + startState + " to " + !startState + " at " + sdf.format(timeStateChanged));
			startState = (startState ? false : true); // toggle startState
		}
		
		System.out.println("Sensor " + name + ": Shutdown at " + sdf.format(endTime));
		
	}
	

	
	// returns the current value of the sensor
	public boolean getSensorState() {
		return sensorHistory.getLast();
	}
	
	public void setSensorState(Boolean state) {
		impulse = state;
	}

	// in the test environment this function will add an impulse to sensor's state history
	private boolean impulse() {
		
		// when the last state differs from current state save the time
		if (sensorHistory.size() > 0 && sensorHistory.getLast() != impulse)
			stateChanges.add(System.currentTimeMillis());
		
		// in a productive environment this will be set from the sensor directly. here we set it manually from outside
		sensorHistory.add(impulse);
		
		// this will be used in a gui later as property change event
		return impulse;
	}

	public void deactivate() {
		activated = false;
	}	
	
	// ignore. this will be used in a gui later.
	private void propertyChanged() {

	}
	
	// ignore
	private PropertyChangeSupport changes = new PropertyChangeSupport(this);	
	
	// ignore
	public void addPropertyChangeListener(PropertyChangeListener l) {
		changes.addPropertyChangeListener(l);
	}

	// ignore
	public void removePropertyChangeListener(PropertyChangeListener l) {
		changes.removePropertyChangeListener(l);
	}
	
}
