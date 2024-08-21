package items

import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerItemDamageEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import org.bukkit.util.Vector
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

object CliffBootsItem : AbstractRLItem {
    private const val DURABILITY_TICKS = 20 * 60 * 5
    private const val IRON_BOOTS_MAX_DAMAGE = 194 // for reference

    override val baseItem: Material = Material.IRON_BOOTS
    override val model: Int = 44302
    override val id: String = "cliff_boots"
    override val itemGetterAction: (ItemStack, ItemMeta, PersistentDataContainer) -> Unit = { _, resultMeta, _ ->
        resultMeta.displayName(displayName)
        resultMeta.lore(lore)
        resultMeta.persistentDataContainer.set(durabilityKey, PersistentDataType.INTEGER, DURABILITY_TICKS)
    }

    private val mm = MiniMessage.miniMessage()
    private val displayName = mm.deserialize("Ботинки скалолаза").decoration(TextDecoration.ITALIC, false)
    private val lore =
        listOf(
            "Позволяют залезать на стены, пока крадётесь",
        ).map { mm.deserialize(it).decoration(TextDecoration.ITALIC, false) }

    private val durabilityKey = NamespacedKey("rle", "damage")

    private fun setBootsDurability(
        boots: ItemStack,
        amount: Int,
    ) {
        val bootsMeta = (boots.itemMeta as Damageable)
        val newDurability = max(amount, 0).toDouble()
        bootsMeta.persistentDataContainer.set(durabilityKey, PersistentDataType.INTEGER, amount)
        bootsMeta.damage = IRON_BOOTS_MAX_DAMAGE - (IRON_BOOTS_MAX_DAMAGE * min(newDurability / DURABILITY_TICKS, 1.0)).toInt()
        boots.setItemMeta(bootsMeta)
    }

    private fun getBootsDurability(boots: ItemStack): Int =
        boots.itemMeta.persistentDataContainer.get(durabilityKey, PersistentDataType.INTEGER)!!

    private fun checkIfInAir(p: Player): Boolean {
        val blockUnderFeet = p.location.block.getRelative(BlockFace.DOWN)
        return !blockUnderFeet.isSolid || blockUnderFeet.isPassable
    }

    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent) {
        if (e.isCancelled) return
        val p: Player = e.player
        val intPlayerX: Int = p.location.blockX
        val intPlayerY: Int = p.location.blockY
        val intPlayerZ: Int = p.location.blockZ
        val dubPlayerX: Double = p.location.x
        val dubPlayerZ: Double = p.location.z
        val boots = p.inventory.boots ?: return

        val isCliffClimbing = p.isSneaking && compare(p.inventory.boots ?: return) && checkIfInAir(p)
        val durabilityCheck = getBootsDurability(boots) > 0

        val blockCheckDiffVector =
            when {
                dubPlayerX - intPlayerX.toDouble() >= 0.69 -> Vector(-1, 0, 0)
                dubPlayerX - intPlayerX.toDouble() <= 0.31 -> Vector(1, 0, 0)
                dubPlayerZ - intPlayerZ.toDouble() >= 0.69 -> Vector(0, 0, -1)
                dubPlayerZ - intPlayerZ.toDouble() <= 0.31 -> Vector(0, 0, 1)
                else -> return
            }
        val blockCheckVector = Vector(intPlayerX.toDouble(), intPlayerY.toDouble(), intPlayerZ.toDouble()).subtract(blockCheckDiffVector)
        val blockCheckLocation = Location(p.world, blockCheckVector.x, blockCheckVector.y, blockCheckVector.z)
        val checkedBlock = p.world.getBlockAt(blockCheckLocation).type
        val blockCheck = checkedBlock.isSolid && checkedBlock.isOccluding

        if (isCliffClimbing && blockCheck && durabilityCheck && Random.nextInt(1, 12) < 10) {
            p.velocity = Vector(0.0, 0.3, 0.0)
            setBootsDurability(boots, getBootsDurability(boots) - 1)
        }
    }

    @EventHandler
    fun onBootsDamage(event: PlayerItemDamageEvent) {
        if (event.isCancelled) return
        val item = event.item
        if (compare(item)) event.isCancelled = true
    }
}
