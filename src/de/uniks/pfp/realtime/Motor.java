package de.uniks.pfp.realtime;

public class Motor implements Runnable, SensorListener {

	boolean impulse = false;
	boolean activated = false;
	
	@Override
	public void sensorImpulse(boolean status) {
		// TODO Auto-generated method stub
		if (activated) {
			impulse = true;
			System.out.println("Sensor fired.");
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("motor object started.");
		activated = true;
		while(!impulse)
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
		activated = false;
		System.out.println("motor object stopped");
		
	}

}
