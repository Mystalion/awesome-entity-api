package com.mystalion.entityapi.goals;

import com.mystalion.entityapi.Reflection;

public class GoalWrapper {

	private static IGoalWrapper wrapper;

	private static IGoalWrapper getWrapper() {
		if (wrapper == null) {
			try {
				wrapper = (IGoalWrapper) Reflection.getClass("com.mystalion.entityapi.goals.nms.GoalWrapper_" + Reflection.NMS_VERSION).newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return wrapper;
	}

	@SuppressWarnings("unchecked")
	public static <T> T wrap(PathfinderGoal goal) {
		return (T) getWrapper().wrap(goal);
	}
}
