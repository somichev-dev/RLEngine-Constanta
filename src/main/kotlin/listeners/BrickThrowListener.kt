package listeners

import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.entity.Entity
import org.bukkit.entity.Snowball
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object BrickThrowListener : Listener {
    private val key = NamespacedKey("rle", "brick")

    private fun isBrick(e: Entity): Boolean = e.persistentDataContainer.has(key)

    @EventHandler
    fun onBrickThrow(event: PlayerInteractEvent) {
        val player = event.player
        val item = event.item ?: return
        val action = event.action
        val world = player.world

        if (
            action == Action.RIGHT_CLICK_AIR &&
            item.type == Material.BRICK &&
            player.gameMode !in listOf(GameMode.SPECTATOR, GameMode.ADVENTURE)
        ) {
            val velocity =
                player.location.direction
                    .multiply(1)
                    .add(player.velocity)
            val brick = player.launchProjectile(Snowball::class.java, velocity)
            brick.item = ItemStack(Material.BRICK)
            brick.shooter = player
            brick.persistentDataContainer.set(key, PersistentDataType.BOOLEAN, true)

            if (player.gameMode == GameMode.SURVIVAL) item.amount--

            world.playSound(
                player.location,
                Sound.ENTITY_SNOWBALL_THROW,
                1.0f,
                0.5f,
            )
        }
    }

    @EventHandler
    fun onBrickHit(event: EntityDamageByEntityEvent) {
        if (event.isCancelled) return
        val damager = event.damager
        if (damager is Snowball && damager.item.type == Material.BRICK) {
            event.damage = 6.0
        }
    }
}
