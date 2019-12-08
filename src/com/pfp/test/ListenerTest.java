package com.pfp.test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ListenerTest {

	private PropertyChangeSupport changes = new PropertyChangeSupport(this);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ListenerTest t = new ListenerTest();
		
		Object o = new Object();
		t.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				// TODO Auto-generated method stub
				o.equals(evt);
				
			}
			
		});
		
		String s = new String("hans");
		t.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				// TODO Auto-generated method stub
				System.out.println("hans");
			}
			
		});
		
		PropertyChangeListener[] l = t.changes.getPropertyChangeListeners();
		for (PropertyChangeListener u : l) {
			
			System.out.println(u.equals(o));
			System.out.println(u.equals(s));
		}
	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		changes.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		changes.removePropertyChangeListener(l);
	}	
	
}
