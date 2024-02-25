package com.tubmc.gamerules;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
 * The {@link AbstractImplementation} for spigot
 * 
 * @author BradBot_1
 * @since 1.0.0
 * @version 1.0.0
 */
@Internal
final class SpigotGameruleImplementation extends AbstractImplementation {
	
	private static final @NotNull Map<Identifier, AbstractGameruleType<?>> TYPES = new HashMap<Identifier, AbstractGameruleType<?>>();
	private static final @NotNull Map<Identifier, Gamerule<?>> GAMERULES = new HashMap<Identifier, Gamerule<?>>();
	private static final @NotNull Properties VALUES = new Properties();
	private static final @NotNull Path SAVE_PATH = Entrypoint.getPlugin(Entrypoint.class).getDataFolder().toPath().resolve("./gamerules.properties");
	
	@Internal
	SpigotGameruleImplementation() {
		for (final GameRule<?> vanillaGameRule : GameRule.values()) {
			final Namespace mcNamespace = new Namespace("minecraft");
			if (vanillaGameRule.getType() == Boolean.class) {
				this.registerGamerule(new Gamerule<Boolean>(mcNamespace.toIdentifier(vanillaGameRule.getName().toLowerCase()), AbstractGameruleType.BOOLEAN_TYPE, vanillaGameRule.getName()));
			} else {
				this.registerGamerule(new Gamerule<Integer>(mcNamespace.toIdentifier(vanillaGameRule.getName().toLowerCase()), AbstractGameruleType.INTEGER_TYPE, vanillaGameRule.getName()));
			}
		}
		this.registerGameruleType(AbstractGameruleType.BOOLEAN_TYPE);
		this.registerGameruleType(AbstractGameruleType.INTEGER_TYPE);
		this.registerGameruleType(AbstractGameruleType.STRING_TYPE);
		this.registerGameruleType(AbstractGameruleType.FLOAT_TYPE);
		final java.util.logging.Logger logger = Entrypoint.getPlugin(Entrypoint.class).getLogger();
		if (!Files.exists(SAVE_PATH)) {
			try {
				Files.createDirectories(SAVE_PATH.getParent());
				Files.createFile(SAVE_PATH);
			} catch (IOException e) {
				logger.warning("Failed to create gamerule store!");
			}
		} else {
			try {
				InputStream inputStream = Files.newInputStream(SAVE_PATH, StandardOpenOption.READ);
				VALUES.load(inputStream);
				inputStream.close();
			} catch (IOException e) {
				logger.warning("Failed to load gamerule store!");
			}
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final <T> @Nullable AbstractGameruleType<T> getGameruleType(@NotNull final Identifier identifier, @NotNull final Class<T> clazz) {
		if (!TYPES.containsKey(identifier)) return null;
		final AbstractGameruleType<?> type = TYPES.get(identifier);
		if (type.getType() != clazz) return null;
		return (AbstractGameruleType<T>) type;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final <T> void registerGameruleType(@NotNull final AbstractGameruleType<T> type) {
		TYPES.putIfAbsent(type.getIdentifier(), type);
	}
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final <T> @Nullable Gamerule<T> getGamerule(@NotNull final Identifier identifier, @NotNull final AbstractGameruleType<T> type) {
		if (!GAMERULES.containsKey(identifier)) return null;
		final Gamerule<?> gamerule = GAMERULES.get(identifier);
		if (!gamerule.type().getIdentifier().equals(type.getIdentifier())) return null;
		return (Gamerule<T>) gamerule;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final <T> void registerGamerule(@NotNull final Gamerule<T> gamerule) throws IllegalArgumentException {
		if (GAMERULES.containsKey(gamerule.getIdentifier())) throw new IllegalArgumentException("Identifier is already used by a gamerule! \"" + gamerule.getIdentifier().toString() + '"');
		GAMERULES.put(gamerule.getIdentifier(), gamerule);
		SpigotGameruleCommand.update();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final @NotNull Identifier[] getGamerules() {
		return GAMERULES.keySet().toArray(Identifier[]::new);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final @Nullable AbstractGameruleType<?> getTypeOfGamerule(@NotNull final Identifier identifier) {
		if (!GAMERULES.containsKey(identifier)) return null;
		return GAMERULES.get(identifier).type();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final @Nullable String getValueFor(@NotNull final Identifier identifier) {
		if (!GAMERULES.containsKey(identifier)) return null;
		if (!identifier.namespace().asString().equals("minecraft")) return VALUES.getProperty(identifier.toString());
		final List<World> worlds = Bukkit.getWorlds();
		if (worlds.size() == 0) return null;
		final World world = worlds.get(0);
		for (GameRule<?> vanillaGameRule : GameRule.values()) {
			if (!vanillaGameRule.getName().toLowerCase().equals(identifier.path().asString())) continue;
			return world.getGameRuleDefault(vanillaGameRule).toString();
		}
		return null;
	}
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final boolean setValueFor(@NotNull final Identifier identifier, @Nullable final String newValue) {
		if (!GAMERULES.containsKey(identifier)) return false;
		if (!identifier.namespace().asString().equals("minecraft")) {
			try {
				VALUES.put(identifier.toString(), newValue);
				VALUES.store(Files.newOutputStream(SAVE_PATH), "DO NOT EDIT THIS MANUALLY!");
				return true;
			} catch (IOException e) {
				Entrypoint.getPlugin(Entrypoint.class).getLogger().warning("Failed to save gamerule store!");
				return false;
			}
		}
		if (newValue == null) return false;
		final List<World> worlds = Bukkit.getWorlds();
		if (worlds.size() == 0) return false;
		for (GameRule<?> vanillaGameRule : GameRule.values()) {
			if (!vanillaGameRule.getName().toLowerCase().equals(identifier.path().asString())) continue;
			if (vanillaGameRule.getType() == Boolean.class) {
				worlds.forEach(w -> {
					w.setGameRule((GameRule<Boolean>)vanillaGameRule, Boolean.parseBoolean(newValue));
				});
			} else {
				worlds.forEach(w -> {
					w.setGameRule((GameRule<Integer>)vanillaGameRule, AbstractGameruleType.INTEGER_TYPE.fromString(newValue));
				});
			}
			return true;
		}
		return false;
	}
}