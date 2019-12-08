package com.pfp.test;

import de.uniks.pfp.init.*;
import de.uniks.pfp.init.exceptions.*;

public class Test {
	
	static public void main(String[] args) {

		Brick b = null;
		try {
			b = new Brick("192.168.0.1");
		} catch (BrickInitException e) {
			System.out.println(e.getMessage());
		}
		
		Motor m1;
		try {
			m1 = b.createRegulatedMotor("E", 'M'); // Illegaler Port
		} catch (IllegalPortException | IllegalTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		Motor m2;
		try {
			m2 = b.createRegulatedMotor("C", 'Z'); // Illegaler Typ
		} catch (IllegalPortException | IllegalTypeException e) {
			e.printStackTrace();
		}
		
	}
}
