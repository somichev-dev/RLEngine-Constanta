package items

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataContainer

object ToolboxItem : AbstractRLItem {
    override val baseItem: Material = Material.IRON_INGOT
    override val model: Int = 44401
    override val id: String = "tool_box"
    override val itemGetterAction: (
        result: ItemStack,
        resultMeta: ItemMeta,
        resultPDC: PersistentDataContainer,
    ) -> Unit = { _, resultMeta, _ ->
        resultMeta.displayName(Component.text("Ящик с инструментами").decoration(TextDecoration.ITALIC, false))
    }
}
