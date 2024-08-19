package listeners
import RadioLampEngine
import org.bukkit.Bukkit
import org.bukkit.event.Listener

object RLEngineListeners {
    private val knownListeners =
        listOf<Listener>()

    init {
        registerKnownListeners()
    }

    private fun registerKnownListeners()  {
        knownListeners.forEach { register(it) }
    }

    fun register(listener: Listener)  {
        Bukkit.getPluginManager().registerEvents(listener, RadioLampEngine.instance)
    }
}
