package com.mystalion.entityapi.goals;

public abstract class PathfinderGoal {

	private int a;

	public abstract boolean shouldStart(); // a

	public boolean shouldContinue() { // b 
		// often { return !this.b.getNavigation().n() } 
		// for waiting until navigation has completed
		return shouldStart();
	}

	public boolean isContinuous() { // g
		return true;
	}

	public void start() { // c
	}

	public void stop() { // d
	}

	public void tick() { // e
	}

	public void setA(final int a) {
		this.a = a;
	}

	public int getA() {
		return this.a;
	}
}
