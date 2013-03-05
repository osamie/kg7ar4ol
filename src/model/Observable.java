package model;

import java.util.Vector;

public class Observable {
	Vector<Observer> observers;
	
	public Observable() {
		observers = new Vector<Observer>();
	}
	
	public void addObserver(Observer observer){
		observers.add(observer);
	}
	
	public void removeObserver(Observer observer){
		observers.remove(observer);
	}
	
	public void updateObservers(long pollID,int[]count,int state){
		for(Observer observer:observers){
			observer.update(pollID,count,state);
		}
	}
}
