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
	
	// default time sensor checks for new states in ms
	private static int SAMPLINGFREQUENCY = 200; 	
	
	final Lock lock = new ReentrantLock();
	
	// initialize and activate a sensor with default values
	public Sensor(String name) {
		this.name = name;
		this.tick = SAMPLINGFREQUENCY; // set default sample frequency
		System.out.println(name + ": activated");
		activated = true;
	}
	
	// sets a custom sample frequency
	public Sensor(String name, int sampleFrequency) {
		this(name);
		if (sampleFrequency > 0)
			tick = sampleFrequency;
	}
	
	// ignore. this will be used in a gui later.
	private PropertyChangeSupport changes = new PropertyChangeSupport(this);
	
	// checks whether sensor was in sensorState (on|off) over timeFrame ticks
	synchronized public boolean checkSensorState(int timeFrame, boolean sensorState) {
	
		// caller is interested in the last sensor state only or there was no valid timeFrame provided
		if (timeFrame <= 1) return sensorHistory.getLast().equals(sensorState);
		
		// timeFrame exceeds sensor's runtime
		if (timeFrame > sensorHistory.size()) return false;
		
		/*
		// get sensor's last states and check whether all of them are the desired sensorState
		Boolean lastState = sensorHistory.getLast();
		int counter = 0;
		while (lastState != null && counter < timeFrame) { // null can't happen by design but w/e 
			if (lastState != sensorState) return false;
			lastState = sensorHistory.getLast();
			counter++;
		} */
		
		// lock.lock();
		
		LinkedList<Boolean> localList = new LinkedList<Boolean>(sensorHistory);
		
		// we get the last (timeFrame) states of the sensor's history
		ListIterator<Boolean> it = localList.listIterator(localList.size() - timeFrame);
	
		// the iterator now contains timeFrame states from sensor's history
		while(it.hasNext())
			if (it.next() != sensorState) return false; // if one of them is not the desired sensorState return false.
		
		// lock.unlock();
		
		// sensor was in sensorState over timeFrame ticks
		return true; 
	}
	
	// default
	public boolean checkSensorState(int timeFrame) {
		return checkSensorState(timeFrame, true);
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExecutorService executor = Executors.newCachedThreadPool();
		
		Sensor sen = new Sensor("S1");

		executor.execute(sen);
		
// System.out.println(Thread.currentThread().getName());


		boolean b = true;
		// while(b);

		Runnable r1 = new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
					// System.out.println("Changing impulse");
					sen.impulse = true;
					Thread.sleep(3000);
					// System.out.println("Changing impulse");
					sen.impulse = false;
					Thread.sleep(1000);
					
					sen.impulse = true;
					Thread.sleep(2000);
					sen.impulse = false;
					Thread.sleep(100);					
					
					sen.activated = false;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		executor.execute(r1);
		
		Runnable r2 = new Runnable() {

			long startTime = System.currentTimeMillis();
			
			@Override
			public void run() {
				Sensor local = sen;
				while(!local.checkSensorState(10, false)) {
					System.out.println("Sensor was not 10 ticks off.");
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				long accomplishedIn = System.currentTimeMillis() - startTime;
				
				System.out.println("Sensor off 10 ticks after " + accomplishedIn / 1000 + " seconds.");
				
			}
			
		};
		
		executor.execute(r2);
		
		Runnable r3 = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (!sen.checkSensorState(5, true)) {
					System.out.println("Sensor was not 5 ticks on.");
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println("Sensor was on for 5 ticks.");
			}
			
		};
		
		executor.execute(r3);
		
		
		LinkedList<String> test = new LinkedList<>();
		test.add("hans");
		test.add("hans2");
		test.add("hans3");
		test.add("hans4");
		test.add("hans5");
		System.out.println(test.getLast());
		System.out.println(test.getLast());
		
		ListIterator it = test.listIterator(test.size() - 2);
		while(it.hasNext())
			System.out.println(it.next());
		
		ListIterator it2 = test.listIterator(test.size() - 5);
		while(it2.hasNext())
			System.out.println(it2.next());
		
		executor.shutdown();
	}

	@Override
	public void run() {
		
		System.out.print(name + ": ");
		
		// we save the startTime when this sensor was started
		startTime = System.currentTimeMillis();
		
		while (activated) {
			// System.out.print(impulse ? '-' : '_');
			if (impulse())
				propertyChanged();
			try {
				Thread.sleep(SAMPLINGFREQUENCY);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
		
		// sensor was shut down successfully
		endTime = System.currentTimeMillis();
		
		// print some stats. this will happen in a different method later.
		
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
		
		System.out.println("Sensor started at " + sdf.format(startTime));
		
		boolean startState = false;
		
		for (Long timeStateChanged : stateChanges) {
			System.out.println("Sensor went from " + startState + " to " + !startState + " at " + sdf.format(timeStateChanged));
			// startState = startState ? !startState : startState;
			if (startState)
				startState = false;
			else
				startState = true;
		}
		System.out.println("Sensor shutdown at " + sdf.format(endTime));
		
		
		
		System.out.println("\n" + name + ": shutdown");

		
	}
	
	private void propertyChanged() {

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

	// ignore
	public void addPropertyChangeListener(PropertyChangeListener l) {
		changes.addPropertyChangeListener(l);
	}

	// ignore
	public void removePropertyChangeListener(PropertyChangeListener l) {
		changes.removePropertyChangeListener(l);
	}

	
}
