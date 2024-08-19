package util

import RadioLampEngine
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask

object RLEngineTaskManager {
    init {
    }

    @Suppress("Unused")
    fun runTaskLater(
        task: Runnable,
        delay: Long,
    ): BukkitTask = Bukkit.getScheduler().runTaskLater(RadioLampEngine.instance, task, delay)

    fun runTask(
        task: Runnable,
        delay: Long,
        period: Long,
    ): BukkitTask = Bukkit.getScheduler().runTaskTimer(RadioLampEngine.instance, task, delay, period)
}
