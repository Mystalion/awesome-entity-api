package com.mystalion.entityapi;

import org.bukkit.plugin.java.JavaPlugin;

import com.mystalion.entityapi.inject.EntityTypeHandler;

public class EntityAPIPlugin extends JavaPlugin {

	@Override
	public void onLoad() {
		try {
			EntityTypeHandler.copyDefaults();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDisable() {
		try {
			EntityTypeHandler.restoreDefaults();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
