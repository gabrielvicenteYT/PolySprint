/*
 * PolySprint - Toggle sprint and sneak with a keybind.
 *  Copyright (C) 2023  Polyfrost
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package cc.polyfrost.polysprint.core

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.HUD
import cc.polyfrost.oneconfig.config.annotations.KeyBind
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.annotations.Text
import cc.polyfrost.oneconfig.config.core.OneKeyBind
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.config.migration.VigilanceMigrator
import cc.polyfrost.oneconfig.hud.TextHud
import cc.polyfrost.oneconfig.libs.universal.UKeyboard
import cc.polyfrost.polysprint.PolySprint
import cc.polyfrost.polysprint.core.PolySprintConfig.ToggleSprintHud.DisplayState.Companion.activeDisplay
import net.minecraft.entity.player.EntityPlayer
import java.io.File

object PolySprintConfig : Config(
    Mod(
        "PolySprint",
        ModType.PVP,
        "/polysprint_dark.svg",
        VigilanceMigrator(File("./config/simpletogglesprint.toml").absolutePath),
        ),
    "polysprint.json"
) {

    @Switch(
        name = "Toggle Sprint"
    )
    var toggleSprint = true

    @Switch(
        name = "Toggle Sneak"
    )
    var toggleSneak = false

    @Switch(
        name = "Disable W-Tap Sprint"
    )
    var disableWTapSprint = true

    @JvmField
    var toggleSprintState = false

    @JvmField
    var toggleSneakState = false

    @Switch(
        name = "Seperate Keybind for Toggle Sprint",
        subcategory = "Toggle Sprint",
        description = "Use a seperate keybind for Toggle Sprint."
    )
    var keybindToggleSprint = false

    @KeyBind(
        name = "Toggle Sprint Keybind",
        subcategory = "Toggle Sprint"
    )
    var keybindToggleSprintKey = OneKeyBind(UKeyboard.KEY_NONE)

    @Switch(
        name = "Seperate Keybind for Toggle Sneak",
        subcategory = "Toggle Sneak",
        description = "Use a seperate keybind for Toggle Sneak."
    )
    var keybindToggleSneak = false

    @KeyBind(
        name = "Toggle Sneak Keybind",
        subcategory = "Toggle Sneak"
    )
    var keybindToggleSneakKey = OneKeyBind(UKeyboard.KEY_NONE)

    @HUD(
        name = "HUD",
        subcategory = "HUD"
    )
    var hud = ToggleSprintHud()

    init {
        initialize()
        addDependency("keybindToggleSprint", "toggleSprint")
        addDependency("keybindToggleSneak", "toggleSneak")
        addDependency("keybindToggleSprintKey", "keybindToggleSprint")
        addDependency("keybindToggleSneakKey", "keybindToggleSneak")
        registerKeyBind(keybindToggleSprintKey) {
            if (keybindToggleSprint) {
                if (enabled && toggleSprint && !PolySprint.sprintHeld) {
                    toggleSprintState = !toggleSprintState
                    PolySprintConfig.save()
                }
                PolySprint.sprintHeld = !PolySprint.sprintHeld
            }
        }
        registerKeyBind(keybindToggleSneakKey) {
            if (keybindToggleSneak) {
                if (enabled && toggleSneak && !PolySprint.sneakHeld) {
                    toggleSneakState = !toggleSneakState
                    PolySprintConfig.save()
                }
                PolySprint.sneakHeld = !PolySprint.sneakHeld
            }
        }
    }

    class ToggleSprintHud : TextHud(true, 0, 1080 - 19) {
        @Switch(name = "Brackets")
        private var brackets = true

        @Text(
            name = "Descending Held Text",
            category = "Display",
            subcategory = "Text"
        )
        var descendingHeld = "Descending (key held)"

        @Text(
            name = "Descending Toggled Text",
            category = "Display",
            subcategory = "Text"
        )
        var descendingToggled = "Descending (toggled)"

        @Text(
            name = "Descending Text",
            category = "Display",
            subcategory = "Text"
        )
        var descending = "Descending (vanilla)"

        @Text(
            name = "Flying Text",
            category = "Display",
            subcategory = "Text"
        )
        var flying = "Flying"

        @Text(
            name = "Riding Text",
            category = "Display",
            subcategory = "Text"
        )
        var riding = "Riding"

        @Text(
            name = "Sneak Held Text",
            category = "Display",
            subcategory = "Text"
        )
        var sneakHeld = "Sneaking (key held)"

        @Text(
            name = "Sneak Toggle Text",
            category = "Display",
            subcategory = "Text"
        )
        var sneakToggle = "Sneaking (toggled)"

        @Text(
            name = "Sneaking Text",
            category = "Display",
            subcategory = "Text"
        )
        var sneak = "Sneaking (vanilla)"

        @Text(
            name = "Sprint Held Text",
            category = "Display",
            subcategory = "Text"
        )
        var sprintHeld = "Sprinting (key held)"

        @Text(
            name = "Sprint Toggle Text",
            category = "Display",
            subcategory = "Text"
        )
        var sprintToggle = "Sprinting (toggled)"

        @Text(
            name = "Sprinting Text",
            category = "Display",
            subcategory = "Text"
        )
        var sprint = "Sprinting (vanilla)"

        override fun getLines(lines: MutableList<String>, example: Boolean) {
            getCompleteText(activeDisplay)?.let { lines.add(it) }
        }

        private fun getCompleteText(text: String?) = if (brackets && text?.isNotEmpty() == true) "[$text]" else text

        private enum class DisplayState(val displayText: ToggleSprintHud.() -> String, val displayCheck: (EntityPlayer) -> Boolean) {
            DESCENDINGHELD({ descendingHeld }, { it.capabilities.isFlying && it.isSneaking && PolySprint.sneakHeld }),
            DESCENDINGTOGGLED({ descendingToggled }, { it.capabilities.isFlying && PolySprintConfig.enabled && toggleSprint && toggleSneakState }),
            DESCENDING({ descending }, { it.capabilities.isFlying && it.isSneaking }),
            FLYING({ flying }, { it.capabilities.isFlying }),
            RIDING({ riding }, { it.isRiding }),
            SNEAKHELD({ sneakHeld }, { it.isSneaking && PolySprint.sneakHeld }),
            TOGGLESNEAK({ sneakToggle }, { PolySprintConfig.enabled && toggleSneak && toggleSneakState }),
            SNEAKING({ sneak }, { it.isSneaking }),
            SPRINTHELD({ sprintHeld }, { it.isSprinting && PolySprint.sprintHeld }),
            TOGGLESPRINT({ sprintToggle }, { PolySprintConfig.enabled && toggleSprint && toggleSprintState }),
            SPRINTING({ sprint }, { it.isSprinting });

            val isActive: Boolean
                get() = displayCheck(PolySprint.player!!)

            companion object {
                val ToggleSprintHud.activeDisplay: String?
                    get() {
                        if (PolySprint.player == null) return null
                        return values().find { it.isActive }?.displayText?.invoke(this)
                    }
            }
        }
    }
}