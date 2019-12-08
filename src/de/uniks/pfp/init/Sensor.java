package de.uniks.pfp.init;

public class Sensor {

	String port;
	String type;
	String mode;
	String name;
	
	public Sensor(String port, String type, String mode) {
		this.port = port;
		this.type = type;
		this.mode = mode;
		
	}

	// this constructor is not part of the framework
	public Sensor(String port, String type, String mode, String name) {
		this(port, type, mode);
		this.name = name;
	}	
	
	public String getName() {
		return name;
	}
	
}
