package model;

public interface Observer {
	public abstract void update(long pollID,int[]count,int pollState);
}
