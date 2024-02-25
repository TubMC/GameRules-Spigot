package com.tubmc.gamerules;

import org.bukkit.plugin.java.JavaPlugin;

import com.tubmc.commons.identifiers.Identifier;
import com.tubmc.commons.identifiers.Namespace;

/**
 *    Copyright 2024 TubMC.com
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
/**
 * The entrypoint for spigot
 * 
 * @author BradBot_1
 * @since 1.0.0
 * @version 1.0.0
 */
public final class Entrypoint extends JavaPlugin {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onLoad() {
		// implementations autoregister themselves
		if (AbstractImplementation.IMPLEMENTATION == null) {
			new SpigotGameruleImplementation();
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEnable() {
		Gamerule.create(Identifier.of("test:int"), AbstractGameruleType.INTEGER_TYPE);
		Gamerule.create(Identifier.of("test:float"), AbstractGameruleType.FLOAT_TYPE);
		Gamerule.create(Identifier.of("test:boolean"), AbstractGameruleType.BOOLEAN_TYPE);
		Gamerule.create(Identifier.of("test:string"), AbstractGameruleType.STRING_TYPE);
		Gamerule.create(Identifier.of("test:material"), AbstractGameruleType.MATERIAL_TYPE);
		Gamerule.create(Identifier.of("test:enum"), AbstractGameruleType.createEnumType(new Namespace("test"), Testing.class));
	}
	
	public enum Testing {
		A,
		B,
		C;
	}
	
}