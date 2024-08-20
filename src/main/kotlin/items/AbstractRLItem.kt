package items

import listeners.RLEngineListeners
import org.bukkit.Material
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataContainer

interface AbstractRLItem : Listener {
    @Suppress("SameReturnValue")
    val baseItem: Material
    val model: Int
    val id: String
    val itemGetterAction: (result: ItemStack, resultMeta: ItemMeta, resultPDC: PersistentDataContainer) -> Unit

    fun createItem() {
        RLEngineItems.registerItem(id, this)
        RLEngineListeners.register(this)
    }

    fun getItem(
        result: ItemStack = ItemStack(baseItem),
        resultMeta: ItemMeta = result.itemMeta,
        resultPDC: PersistentDataContainer = resultMeta.persistentDataContainer,
    ): ItemStack {
        resultMeta.setCustomModelData(model)
        itemGetterAction(result, resultMeta, resultPDC)
        result.setItemMeta(resultMeta)
        return result
    }

    fun compare(other: ItemStack): Boolean {
        if (!other.hasItemMeta()) return false
        val otherMeta = other.itemMeta

        if (!otherMeta.hasCustomModelData()) return false
        val otherModel = otherMeta.customModelData

        return other.type == baseItem && otherModel == model
    }
}
