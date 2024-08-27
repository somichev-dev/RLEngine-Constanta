import commands.AboutCommandExecutor
import commands.GetCommand
import items.RLEngineItems
import listeners.RLEngineListeners
import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import util.RLEngineTaskManager

class RadioLampEngine : JavaPlugin() {
    private val commandExecutors =
        mapOf<String, CommandExecutor>(
            "get" to GetCommand(),
            "radiolamp" to AboutCommandExecutor(),
        )

    override fun onEnable() {
        // Command registration
        commandExecutors.forEach {
            val command = this.getCommand(it.key)
            if (command != null) {
                command.setExecutor(it.value)
            } else {
                logger.warning("Command ${it.key} not found in plugin.yml file!")
            }
        }

        instance = this
        RLEngineTaskManager
        RLEngineListeners
        RLEngineItems
    }

    override fun onDisable() {
        // cancel all tasks
        Bukkit.getScheduler().cancelTasks(this)
    }

    companion object {
        lateinit var instance: Plugin
    }
}
