package items

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.util.Vector
import kotlin.random.Random

object CliffBootsItem : AbstractRLItem {
    private val blockBlacklist =
        listOf(
            Material.AIR,
            Material.WATER,
            Material.FIRE,
            Material.FLOWER_POT,
            Material.IRON_TRAPDOOR,
            Material.LADDER,
            Material.LAVA,
            Material.LEVER,
            Material.MELON_SEEDS,
            Material.MELON_STEM,
            Material.PAINTING,
            Material.POWERED_RAIL,
            Material.ACTIVATOR_RAIL,
            Material.DETECTOR_RAIL,
            Material.POWERED_RAIL,
            Material.RED_MUSHROOM,
            Material.BROWN_MUSHROOM,
            Material.SNOW,
//            Material.STATIONARY_WATER,
//            Material.SAPLING,
//            Material.CARPET,
//            Material.CROPS,
//            Material.DOUBLE_PLANT,
//            Material.LONG_GRASS,
//            Material.NETHER_STALK,
//            Material.NETHER_WARTS,
//            Material.RAILS,
//            Material.SAPLING,
//            Material.TRAP_DOOR,
//            Material.SKULL,
//            Material.SKULL,
//            Material.STATIONARY_LAVA,
//            Material.BANNER,
//            Material.STANDING_BANNER,
//            Material.WALL_SIGN,
//            Material.WALL_BANNER,
        )
    private val mm = MiniMessage.miniMessage()
    private val displayName = mm.deserialize("Ботинки скалолаза")
    private val lore =
        listOf(
            "Позволяют залезать на стены, пока крадётесь",
        ).map { mm.deserialize(it) }
    override val baseItem: Material = Material.IRON_BOOTS
    override val model: Int = 44302
    override val id: String = "cliff_boots"
    override val itemGetterAction: (ItemStack, ItemMeta, PersistentDataContainer) -> Unit = { result, resultMeta, _ ->
        resultMeta.displayName(displayName)
        resultMeta.lore(lore)
    }

    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent) {
        val p: org.bukkit.entity.Player = e.player
        val intPlayerX: Int = p.location.blockX
        val intPlayerY: Int = p.location.blockY
        val intPlayerZ: Int = p.location.blockZ
        val dubPlayerX: Double = p.location.x
        val dubPlayerZ: Double = p.location.z

        val isCliffClimbing = p.isSneaking && compare(p.inventory.boots ?: return) && !p.isOnGround

        val blockCheckDiffVector =
            when {
                dubPlayerX - intPlayerX.toDouble() >= 0.69 -> Vector(1, 0, 0)
                dubPlayerX - intPlayerX.toDouble() <= 0.31 -> Vector(-1, 0, 0)
                dubPlayerZ - intPlayerZ.toDouble() >= 0.69 -> Vector(0, 0, 1)
                dubPlayerZ - intPlayerZ.toDouble() <= 0.31 -> Vector(0, 0, -1)
                else -> return
            }
        val blockCheckVector = Vector(intPlayerX.toDouble(), intPlayerY.toDouble(), intPlayerZ.toDouble()).subtract(blockCheckDiffVector)
        val blockCheckLocation = Location(p.world, blockCheckVector.x, blockCheckVector.y, blockCheckVector.z)
        val blockCheck = p.world.getBlockAt(blockCheckLocation).type !in blockBlacklist

        if (isCliffClimbing && blockCheck && Random.nextInt(1, 12) < 10) {
            p.velocity = Vector(0.0, 0.3, 0.0)
        }
    }
}
