package de.uniks.pfp.realtime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// import de.uniks.pfp.init.Sensor;

/* In dieser Klasse können Motoren in Abhängigkeit von Sensorendaten getestet werden. Das
 * sieht auf den ersten Blick sehr kompliziert aus. 
 * 
 * Kommentarsprache in den Klassen ist generell Englisch
 * 
 * 
 * Fragen gerne an: christoph.hanauer@uni-kassel.de
 * */
public class StartHere {

	public static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SS");
	
	public static void main(String[] args) {
		
		// Wir erstellen einen ExecutorService der uns Threads zur Verfügung stellt
		ExecutorService executor = Executors.newCachedThreadPool();
		/* Generell zu dem Dienst: neben Klassen die das Interface Runnable implementieren, kann
		 * der Dienst auch Thread-Objekte, Callables, Futures, usw ausführen.  
		 */
		
		// Zwei Sensoren
		Sensor drehTischSensor = new Sensor("DrehtischSensor");
		Sensor einspeiseSensor = new Sensor("EinspeiseSensor");
		
		// SensorRandomData gibt dem Sensor Impulse. Im Produktivsystem lesen wir direkt von den Sensoren.
		
		// Für den Drehtischsensor verwenden wir die Standardwerte. Nachzulesen in der Klasse selbst unter init().
		SensorRandomData s1rnd = new SensorRandomData(drehTischSensor, null);
		
		// Beim Einspeisesensor wollen wir dass er nach 5 Sekunden für 10 Sekunden an ist.
		ArrayList<Integer> changes = new ArrayList<>();
		changes.add(5000);
		changes.add(10000);
		SensorRandomData s2rnd = new SensorRandomData(einspeiseSensor, changes);
		
		// Wir starten zuerst die Sensoren dann die Impulsgeber
		executor.execute(drehTischSensor);
		executor.execute(einspeiseSensor);
		
		executor.execute(s1rnd);
		executor.execute(s2rnd);
		
		// Ab diesem Punkt laufen unsere Sensoren und bekommen Impulse
		
		// In welchem Thread sind wir an der Stelle?
		System.out.println("Wir sind in Thread: " + Thread.currentThread().getName());
		
		// Wir bauen uns einen Motor. Das ist nur ein Beispiel und läßt sich auf viele verschiedene Arten erreichen.
		Runnable motor1 = new Runnable() {
			
			/* Wir erstellen ein Runnable Objekt mithilfe einer anonymen Klasse vom Typ Runnable und überschreiben 
			 * ihre run()-Methode. Das geht mit jedem Objekt und Klasse die nicht als final deklariert worden sind.
			 * 
			 * 		Sensor test = new Sensor("", "", "") {
						@Override
						public String toString() {
							return "my sensor object does things differently";
						}
						@Override
						...
					};
			 * 
			 */
			
			@Override
			public void run() {

				long startTime = System.currentTimeMillis(); // Speicher wann der Motor gestartet wurde.
				
				// TODO: motor objekt initialisieren usw
				
				/* Pseudo:
				 * Nimm ein RMIRegulatedMotor-Objekt oder eine selbst erstellte Klasse die über eines dieser Objekte verfügt, 
				 * setze die Geschwindigkeit des Motors auf x und lass dann den Motor rotieren..
				 */
				
				while(!drehTischSensor.checkSensorState(1)) { // .. solange der Sensor am Drehtisch nicht mindestens einmal gefeuert hat
					// syso STRG + LEERTASTE "ich warte"
					try {
						Thread.sleep(200);
						/* Wenn wir hier den Thread nicht schlafen lassen, fragt eher alle paar Nanosekunden beim Sensor
						 * an ob sich was geändert hat. Das heißt der Wert ist neben der SAMPLEFREQUENCY/tick im Sensor
						 * einer der Flaschenhälse. In unserem Produktivsystem muss der Wert nicht allzu klein gewählt werden.
						 */
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				/* Der Sensor hat min. einmal gefeuert. Das bedeutet, was auch immer wir mit unserem Motor transportiert haben,
				 * ist am Ziel, deswegen stoppen wir den Motor.
				 * 
				 * Schick dem RMIRegulatedMotor-Objekt ein stop()
				 */
				
				long endTime = System.currentTimeMillis();
				
				printInfo(startTime, endTime, Thread.currentThread().getName());

			}
		};
		
		// Wir bauen einen anderen Motor
		Runnable motor2 = new Runnable() {
		
			@Override
			public void run() {
				
				long startTime = System.currentTimeMillis();				

				// Hier interessiert uns ob der EinspeiseSensor 5 Ticks (Zeiteinheiten) an war.
				while(!einspeiseSensor.checkSensorState(5, true)) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
				long endTime = System.currentTimeMillis();
				
				printInfo(startTime, endTime, Thread.currentThread().getName());				
				
			}
		
		};
		
		// Wir bauen einen Motor der unabhängig von Sensoren für eine Sekunde rotiert
		Runnable motor3 = new Runnable() {
		
			@Override
			public void run() {
			
				long startTime = System.currentTimeMillis();
				
				// TODO: Wirf den Motor an
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// TODO: Stopp den Motor
				
				long endTime = System.currentTimeMillis();
				
				printInfo(startTime, endTime, Thread.currentThread().getName());
				
			}
			
		};
		
		// Wir starten beide Motoren.
		executor.execute(motor1);
		executor.execute(motor2);
		executor.execute(motor3);

		
		
		// Der Executor läuft normalerweise bis alle Threads beendet sind. Wir können ihm auch sagen nach einer gewissen Zeit allen Threads ein interrupt zu senden.
		executor.shutdown();
		
		// Kann ignoriert werden. An der Stelle sind die Sensoren noch aktiv aber wir haben versucht den Threadpool runterzufahren.			
		try {
			if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
				for (Runnable r : executor.shutdownNow())
					System.out.println("killed" + r.toString()); // Cancel currently executing tasks

			// Normalerweise sollte er hier 60 Sekunden warten 
		       if (executor.awaitTermination(60, TimeUnit.SECONDS))
		           System.err.println("Pool did not terminate");
		     }
		   } catch (InterruptedException ie) {
		     // (Re-)Cancel if current thread also interrupted
		     executor.shutdownNow();
		     // Preserve interrupt status
		     Thread.currentThread().interrupt();
		   }
		
	}

	public static void printInfo(long start, long end, String thread) {
		System.out.println("Ein einfacher Motor startete um " + sdf.format(start) + " und stoppte um " + sdf.format(end) + " in Thread " + thread);
	}
	
}
