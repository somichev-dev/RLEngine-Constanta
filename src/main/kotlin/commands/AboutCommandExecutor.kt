package commands

import RadioLampEngine
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class AboutCommandExecutor :
    CommandExecutor,
    TabCompleter {
    private val aboutMsg =
        "<br><gold>RADIO LAMP ENGINE</gold> - <aqua>КОНСТАНТА SMP ULTRA DELUXE EDITION</aqua>" +
            "<br><click:open_url:'https://github.com/juj-team/RLEngine'><u><blue>[Оригинальный RLEngine]</blue></u></click>" +
            "<br>Автор: <click:open_url:'https://somichev.dev'><u><blue>somichev.dev</blue></u></click>" +
            "<br>Оригинальная идея: denbski, moss" +
            "<br>Особые благодарности: oiknai"

    override fun onCommand(
        sender: CommandSender,
        cmd: Command,
        alias: String,
        args: Array<String>,
    ): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Only players can use this command!")
            return true
        }
        sender.sendMessage(MiniMessage.miniMessage().deserialize(aboutMsg))
        @Suppress("UnstableApiUsage")
        sender.sendMessage(Component.text("Версия: ${RadioLampEngine.instance.pluginMeta.version}", TextColor.color(50, 200, 50)))
        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        cmd: Command,
        alias: String,
        args: Array<String>,
    ): List<String> = listOf()
}
