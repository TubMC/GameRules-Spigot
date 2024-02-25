package com.tubmc.gamerules;

import org.bukkit.GameRule;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.Nullable;

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
 * Spigot specific methods for {@link Gamerule}
 * 
 * @author BradBot_1
 * @since 1.0.0
 * @version 1.0.0
 * @see Gamerule
 */
@Internal
sealed interface ImplementationSpecificGameruleMethods<T> permits Gamerule<T> {
	/**
	 * @return If the {@link Gamerule} comes from the vanilla game
	 */
	public default boolean isVanilla() {
		return ((Gamerule<?>)this).getIdentifier().namespace().asString().equals("minecraft");
	}
	/**
	 * @return Gets the bukkit {@link GameRule} that this {@link Gamerule} represents
	 */
	@SuppressWarnings("unchecked")
	public default @Nullable GameRule<T> toSpigot() {
		if (!this.isVanilla()) return null;
		final String lookFor = ((Gamerule<?>)this).getIdentifier().path().asString();
		for (final GameRule<?> vanillaGameRule : GameRule.values()) {
			if (!vanillaGameRule.getName().toLowerCase().equals(lookFor)) continue;
			return (GameRule<T>) vanillaGameRule;
		}
		return null;
	}
}