package items.extra

import items.AbstractRLItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataContainer

object GurocratHat: AbstractRLItem {
    override val baseItem: Material = Material.CARVED_PUMPKIN
    override val model: Int = 44301
    override val id: String = "gurocrat_hat"

    override fun getItem(result: ItemStack, resultMeta: ItemMeta, resultPDC: PersistentDataContainer): ItemStack {
        resultMeta.displayName(Component.text("red"))

        result.setItemMeta(resultMeta)
        return result
    }
}