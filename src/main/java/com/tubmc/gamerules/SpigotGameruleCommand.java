package com.tubmc.gamerules;

import org.jetbrains.annotations.ApiStatus.Internal;

import com.tubmc.commons.identifiers.Identifier;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import net.md_5.bungee.api.chat.TranslatableComponent;

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
 * Impl for overriding /gamerule
 * 
 * @author BradBot_1
 * @since 1.0.0
 * @version 1.0.0
 * @see Gamerule
 */
@Internal
final class SpigotGameruleCommand {
	/**
	 * Removes and re-adds the gamerule command
	 * @since 1.0.0
	 */
	@Internal
	static final void update() {
		CommandAPI.unregister("gamerule");
		final CommandAPICommand command = new CommandAPICommand("gamerule").withPermission(CommandPermission.OP);
		for (final Identifier identifier : Gamerule.getGamerules()) {
			final String gameruleName = Gamerule.get(identifier, Gamerule.getTypeOfGamerule(identifier)).commandName();
			command.withSubcommands(
					new CommandAPICommand(gameruleName)
						.executes((info) -> {
							info.sender().spigot().sendMessage(new TranslatableComponent("commands.gamerule.query", gameruleName, AbstractImplementation.IMPLEMENTATION.getValueFor(identifier)));
						}),
					new CommandAPICommand(gameruleName)
						.withArguments(
							Gamerule.getTypeOfGamerule(identifier).getSpigotCommandAPIArgument()
						)
						.executes((info) -> {
							AbstractImplementation.IMPLEMENTATION.setValueFor(identifier, info.args().getRaw("value"));
							info.sender().spigot().sendMessage(new TranslatableComponent("commands.gamerule.set", gameruleName, AbstractImplementation.IMPLEMENTATION.getValueFor(identifier)));
						})
			);
		}
		command.register();
	}
}