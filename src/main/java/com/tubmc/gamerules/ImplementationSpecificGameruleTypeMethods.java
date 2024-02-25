package com.tubmc.gamerules;

import java.util.stream.Stream;

import org.bukkit.Material;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

import com.tubmc.commons.identifiers.Namespace;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.CustomArgument.CustomArgumentException;
import dev.jorel.commandapi.arguments.CustomArgument.MessageBuilder;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.FloatArgument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LongArgument;
import dev.jorel.commandapi.arguments.StringArgument;

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
 * Spigot specific methods for {@link AbstractGameruleType}
 * 
 * @author BradBot_1
 * @since 1.0.0
 * @version 1.0.0
 * @see AbstractGameruleType
 */
@Internal
sealed interface ImplementationSpecificGameruleTypeMethods<T> permits AbstractGameruleType<T> {
	/**
	 * {@link AbstractGameruleType} impl for {@link Material} {@link Enum}
	 * 
	 * @since 1.0.0
	 */
	public static AbstractGameruleType<Material> MATERIAL_TYPE = AbstractGameruleType.createEnumType(new Namespace("minecraft"), Material.class);
	/**
	 * @return A defined {@link Argument} that performs validation and suggestions for this {@link AbstractGameruleType}
	 * @since 1.0.0
	 */
	@SuppressWarnings("unchecked")
	public default @NotNull Argument<?> getSpigotCommandAPIArgument() {
		final @NotNull Class<T> type = ((AbstractGameruleType<T>)this).getType();
		if (type.isAssignableFrom(int.class) || type.isAssignableFrom(Integer.class)) return new IntegerArgument("value");
		if (type.isAssignableFrom(long.class) || type.isAssignableFrom(Long.class)) return new LongArgument("value");
		if (type.isAssignableFrom(float.class) || type.isAssignableFrom(Float.class)) return new FloatArgument("value");
		if (type.isAssignableFrom(double.class) || type.isAssignableFrom(Double.class)) return new DoubleArgument("value");
		if (type.isAssignableFrom(boolean.class) || type.isAssignableFrom(Boolean.class)) return new BooleanArgument("value");
		if (type.isAssignableFrom(String.class)) return new GreedyStringArgument("value");
		if (this instanceof EnumGameruleType<?> enumType) return new CustomArgument<T, String>(new StringArgument("value"), (info) -> {
			final T ret = (T) enumType.fromString(info.input());
			if (ret == null) throw CustomArgumentException.fromMessageBuilder(new MessageBuilder("Unknown enum constant: ").appendArgInput());
			return ret;
		}).replaceSuggestions(ArgumentSuggestions.strings(info -> Stream.of(enumType.getType().getEnumConstants()).map(Enum::name).toArray(String[]::new)));
		return new CustomArgument<T, String>(new GreedyStringArgument("value"), (info) -> ((AbstractGameruleType<T>)this).fromString(info.input()));
	}
}