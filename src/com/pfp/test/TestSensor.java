package com.pfp.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TestSensor {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		ArrayList<String> sen = new ArrayList<>(
				List.of("_", "_", "_", "-", "-")
		);
		
		LinkedList<Character> str = new LinkedList<>();
		
		
		System.out.print("S1: ");
		for (int i = 0; i < 50; i++) {
			boolean flip = Math.random() < 0.5;
			Character nextChar = flip ? '_' : '-';
			str.add(nextChar);
			if (str.size() > 20)
				str.removeFirst();
				
			System.out.print(printList(str));
			// System.out.println("\r");
			Thread.sleep(1000);
		}
		
		
	}
	
	public static String printList(LinkedList<Character> l) {
		StringBuffer strB = new StringBuffer();
		for (Character c : l) {
			strB.append(c);
		}
		return strB.toString();
	}

}
