/*
 *   SimpleToggleSprint
 *   Copyright (C) 2021  My-Name-Is-Jeff
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package mynameisjeff.simpletogglesprint

import cc.polyfrost.oneconfig.libs.universal.UMinecraft
import cc.polyfrost.oneconfig.utils.commands.CommandManager
import mynameisjeff.simpletogglesprint.commands.SimpleToggleSprintCommand
import mynameisjeff.simpletogglesprint.core.SimpleToggleSprintConfig
import mynameisjeff.simpletogglesprint.core.checkKeyCode
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent

@Mod(
    modid = SimpleToggleSprint.MODID,
    name = SimpleToggleSprint.MOD_NAME,
    version = SimpleToggleSprint.VERSION,
    clientSideOnly = true,
    modLanguageAdapter = "cc.polyfrost.oneconfig.utils.KotlinLanguageAdapter"
)
object SimpleToggleSprint {

    const val MODID = "@ID@"
    const val MOD_NAME = "@NAME@"
    const val VERSION = "@VER@"
    val player
        get() = UMinecraft.getPlayer()
    val gameSettings
        get() = UMinecraft.getSettings()

    var sprintHeld = false
    var sneakHeld = false

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        SimpleToggleSprintConfig
        MinecraftForge.EVENT_BUS.register(this)
    }

    @Mod.EventHandler
    fun onPostInit(event: FMLPostInitializationEvent) {
        CommandManager.INSTANCE.registerCommand(SimpleToggleSprintCommand())
    }


    @SubscribeEvent
    fun onInput(event: InputEvent) {
        if (!SimpleToggleSprintConfig.enabled) return
        val sprint = gameSettings.keyBindSprint.keyCode
        val sneak = gameSettings.keyBindSneak.keyCode
        if (!SimpleToggleSprintConfig.keybindToggleSprint && checkKeyCode(sprint)) {
            if (SimpleToggleSprintConfig.enabled && SimpleToggleSprintConfig.toggleSprint && !sprintHeld) {
                SimpleToggleSprintConfig.toggleSprintState = !SimpleToggleSprintConfig.toggleSprintState
                SimpleToggleSprintConfig.save()
            }
            sprintHeld = true
        } else {
            sprintHeld = false
        }
        if (!SimpleToggleSprintConfig.keybindToggleSneak && checkKeyCode(sneak)) {
            if (SimpleToggleSprintConfig.enabled && SimpleToggleSprintConfig.toggleSneak && !sneakHeld) {
                SimpleToggleSprintConfig.toggleSneakState = !SimpleToggleSprintConfig.toggleSneakState
                SimpleToggleSprintConfig.save()
            }
            sneakHeld = true
        } else {
            sneakHeld = false
        }
    }
}