package listeners

import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

object CactusShaveListener : Listener {
    @EventHandler
    fun onCactusShave(event: PlayerInteractEvent) {
        val player = event.player
        val item = event.item ?: return
        val block = event.clickedBlock ?: return
        val hand = event.hand
        if (item.type == Material.SHEARS && block.type == Material.CACTUS) {
            block.type = Material.GREEN_CONCRETE
            player.world.playSound(
                player.location,
                Sound.ENTITY_SHEEP_SHEAR,
                0.5f,
                1.0f,
            )
            event.setUseItemInHand(Event.Result.ALLOW)
            player.world.dropItemNaturally(player.location, ItemStack(Material.STICK, Random.nextInt(1, 4)))
            if (hand != null) player.swingHand(hand)
        }
    }
}
