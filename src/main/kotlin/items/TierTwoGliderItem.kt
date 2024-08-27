package items

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Lightable
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import util.RLEngineTaskManager
import kotlin.random.Random

object TierTwoGliderItem : AbstractRLItem {
    override val baseItem: Material = Material.IRON_CHESTPLATE
    override val model: Int = 44302
    override val id: String = "better_glider"
    override val itemGetterAction =
        { _: ItemStack, resultMeta: ItemMeta, _: PersistentDataContainer ->
            resultMeta.displayName(
                Component.text("Улучшенный параглайдер", TextColor.color(250, 250, 250)).decoration(
                    TextDecoration.ITALIC,
                    false,
                ),
            )
            resultMeta.lore(
                listOf(
                    Component.text("летает", TextColor.color(250, 250, 250)),
                ),
            )
        }

    init {
        RLEngineTaskManager.runTask({
            Bukkit
                .getOnlinePlayers()
                .filter {
                    val chestPiece = it.inventory.chestplate
                    chestPiece != null && compare(chestPiece)
                }.forEach { onInventoryTick(it) }
        }, 2L, 10L)
    }

    private fun onInventoryTick(player: Player) {
        player.addPotionEffect(PotionEffect(PotionEffectType.SLOW_FALLING, 30, 0, false, false, false))

        val blockUnderFeet = player.location.block.getRelative(BlockFace.DOWN)
        if (blockUnderFeet.type != Material.CAMPFIRE) return
        if (blockUnderFeet.blockData !is Lightable) return
        if (!(blockUnderFeet.blockData as Lightable).isLit) return
        player.addPotionEffect(PotionEffect(PotionEffectType.LEVITATION, 20 * 9, 6, false, false, false))
    }

    @EventHandler
    fun onTntUse(event: PlayerInteractEvent) {
        val item = event.item ?: return
        val action = event.action
        val player = event.player
        val chestplate = player.inventory.chestplate ?: return

        if (
            item.type == Material.TNT &&
            action in listOf(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK) &&
            compare(chestplate) &&
            player.hasPotionEffect(PotionEffectType.LEVITATION)
        ) {
            player.world.playSound(
                player.location,
                Sound.ENTITY_GENERIC_EXPLODE,
                2.0f,
                Random.nextFloat() * 0.4f + 0.8f,
            )
            player.velocity = player.velocity.add(Vector(0.0, 1.0, 0.0))
            item.amount--
        }
    }
}
