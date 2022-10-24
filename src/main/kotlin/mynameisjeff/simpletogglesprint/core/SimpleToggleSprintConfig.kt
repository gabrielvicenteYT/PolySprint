package mynameisjeff.simpletogglesprint.core

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.HUD
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.annotations.Text
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.config.migration.VigilanceMigrator
import cc.polyfrost.oneconfig.hud.TextHud
import mynameisjeff.simpletogglesprint.SimpleToggleSprint
import mynameisjeff.simpletogglesprint.core.SimpleToggleSprintConfig.ToggleSprintHud.DisplayState.Companion.activeDisplay
import mynameisjeff.simpletogglesprint.mixins.accessors.AccessorEntityPlayer
import mynameisjeff.simpletogglesprint.mixins.accessors.AccessorPlayerCapabilities
import java.io.File

object SimpleToggleSprintConfig : Config(Mod("SimpleToggleSprint", ModType.PVP, VigilanceMigrator(File("./config/simpletogglesprint.toml").absolutePath)), "simpletogglesprint.json") {

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
        description = "Use a seperate keybind for Toggle Sprint.\nConfigure it in the In-Game Controls menu."
    )
    var keybindToggleSprint = false

    @Switch(
        name = "Seperate Keybind for Toggle Sneak",
        subcategory = "Toggle Sneak",
        description = "Use a seperate keybind for Toggle Sneak.\nConfigure it in the In-Game Controls menu."
    )
    var keybindToggleSneak = false

    @HUD(
        name = "HUD",
        subcategory = "HUD"
    )
    var hud = ToggleSprintHud()

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

        private enum class DisplayState(val displayText: ToggleSprintHud.() -> String, val displayCheck: (AccessorEntityPlayer) -> Boolean) {
            DESCENDINGHELD({ descendingHeld }, { (it.capabilities as AccessorPlayerCapabilities).isFlying && it.isSneaking && SimpleToggleSprint.sneakHeld }),
            DESCENDINGTOGGLED({ descendingToggled }, { (it.capabilities as AccessorPlayerCapabilities).isFlying && SimpleToggleSprintConfig.enabled && toggleSprint && toggleSneakState }),
            DESCENDING({ descending }, { (it.capabilities as AccessorPlayerCapabilities).isFlying && it.isSneaking }),
            FLYING({ flying }, { (it.capabilities as AccessorPlayerCapabilities).isFlying }),
            RIDING({ riding }, { it.isRiding }),
            SNEAKHELD({ sneakHeld }, { it.isSneaking && SimpleToggleSprint.sneakHeld }),
            TOGGLESNEAK({ sneakToggle }, { SimpleToggleSprintConfig.enabled && toggleSneak && toggleSneakState }),
            SNEAKING({ sneak }, { it.isSneaking }),
            SPRINTHELD({ sprintHeld }, { it.isSprinting && SimpleToggleSprint.sprintHeld }),
            TOGGLESPRINT({ sprintToggle }, { SimpleToggleSprintConfig.enabled && toggleSprint && toggleSprintState }),
            SPRINTING({ sprint }, { it.isSprinting });

            val isActive: Boolean
                get() = displayCheck(SimpleToggleSprint.player!! as AccessorEntityPlayer)

            companion object {
                val ToggleSprintHud.activeDisplay: String?
                    get() {
                        if (SimpleToggleSprint.player == null) return null
                        return values().find { it.isActive }?.displayText?.invoke(this)
                    }
            }
        }
    }
}