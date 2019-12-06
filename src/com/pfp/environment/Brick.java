package com.pfp.environment;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.pfp.exception.BrickInitException;
import com.pfp.exception.IllegalPortException;
import com.pfp.exception.IllegalTypeException;

public class Brick {

	String ip;
	
	// open / close
	boolean open = false, close = false;
	
	// types
	static ArrayList<Character> m_types = new ArrayList<>(
			List.of('M', 'L')
	);
	
	static ArrayList<String> s_types = new ArrayList<>(
			
	);
	
	// ports
	static ArrayList<String> m_ports = new ArrayList<>(
			List.of("A", "B", "C", "D")
	);
	static ArrayList<String> s_ports = new ArrayList<>(
			List.of("S1", "S2", "S3", "S4")
	);
	
	// connectivity
	static Pattern ip_mask;
	
	public Brick(String ip) throws BrickInitException {
		if (ip == null) throw new BrickInitException(ip);
		else;
		open = true;
	}
	
	public Motor createRegulatedMotor(String port, char type) throws IllegalPortException, IllegalTypeException {
		if (!m_ports.contains(port)) throw new IllegalPortException(port, "motor");
		if (!m_types.contains(type)) throw new IllegalTypeException(Character.toString(type), "motor");
		Motor m = new Motor(port, type);
		return m;
	}
	
	public Sensor createSampleProvider(String port, String type, String mode) throws IllegalPortException {
		if (!s_ports.contains(port)) throw new IllegalPortException(port, "sensor");
		Sensor s = new Sensor(port, type, mode);
		return s;
	}
	
	@Override
	protected void finalize() throws Throwable {
		System.out.println("I'm getting destroyed!");
	}
}
