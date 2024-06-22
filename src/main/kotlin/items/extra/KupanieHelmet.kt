package items.extra

import items.AbstractRLItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataContainer

object KupanieHelmet : AbstractRLItem {
    override val baseItem: Material = Material.TURTLE_HELMET
    override val model: Int = 44301
    override val id: String = "kupanie_helmet"

    override fun getItem(result: ItemStack, resultMeta: ItemMeta, resultPDC: PersistentDataContainer): ItemStack {
        resultMeta.displayName(Component.text("lamp"))

        result.setItemMeta(resultMeta)
        return result
    }
}