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

package mynameisjeff.simpletogglesprint.core

import cc.polyfrost.oneconfig.libs.universal.UKeyboard
import cc.polyfrost.oneconfig.libs.universal.UScreen
import mynameisjeff.simpletogglesprint.SimpleToggleSprint.gameSettings
import mynameisjeff.simpletogglesprint.mixins.accessors.AccessorKeybinding
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.common.ForgeVersion
import org.lwjgl.input.Mouse
import kotlin.reflect.full.staticProperties

val is1_12_2 by lazy { 
    (ForgeVersion::class.staticProperties.find { 
        it.name == "mcVersion"
    }!!.get() as String).startsWith("1.12")
}
fun shouldSetSprint(keyBinding: KeyBinding): Boolean {
    return (keyBinding as AccessorKeybinding).isKeyDown || UScreen.currentScreen == null && SimpleToggleSprintConfig.enabled && SimpleToggleSprintConfig.toggleSprint && SimpleToggleSprintConfig.toggleSprintState && keyBinding === gameSettings.keyBindSprint
}

fun shouldSetSneak(keyBinding: KeyBinding): Boolean {
    return (keyBinding as AccessorKeybinding).isKeyDown || UScreen.currentScreen == null && SimpleToggleSprintConfig.enabled && SimpleToggleSprintConfig.toggleSneak && SimpleToggleSprintConfig.toggleSneakState && keyBinding === gameSettings.keyBindSneak
}

fun checkKeyCode(keyCode: Int) = if (keyCode > 0) UKeyboard.isKeyDown(keyCode) else Mouse.isButtonDown(
    keyCode + 100
)