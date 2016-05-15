package com.mystalion.entityapi.goals.nms;

import com.mystalion.entityapi.goals.IGoalWrapper;
import com.mystalion.entityapi.goals.PathfinderGoal;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

public class GoalWrapper_v1_9_R2 implements IGoalWrapper {

	@Override
	public Object wrap(PathfinderGoal goal) {
		return new Wrap(goal);
	}

	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Wrap extends net.minecraft.server.v1_9_R2.PathfinderGoal {

		private final PathfinderGoal wrapped;

		@Override
		public boolean a() {
			return wrapped.shouldStart();
		}

		@Override
		public boolean b() {
			return wrapped.shouldContinue();
		}

		@Override
		public void c() {
			wrapped.start();
		}

		@Override
		public void d() {
			wrapped.stop();
		}

		@Override
		public void e() {
			wrapped.tick();
		}

		@Override
		public boolean g() {
			return wrapped.isContinuous();
		}
	}
}
