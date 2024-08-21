package items

import RadioLampEngine

object RLEngineItems {
    private val RLEItems = mutableMapOf<String, AbstractRLItem>()
    private val registryElements =
        listOf(
            /*
            / And now, the list of ALL items for registration. Cuz kotlin can't have self-instantiating singletons
            / & I don't have time to fuck around with reflections & patterns
            / - somichev
             */
            GliderItem,
            CliffBootsItem,
            ToolboxItem,
        )

    init {
        registryElements.forEach { it.createItem() }
    }

    fun registerItem(
        id: String,
        item: AbstractRLItem,
    ) {
        RLEItems[id] = item
        RadioLampEngine.instance.logger.info("Registered item: $id (model: ${item.model}")
    }

    fun fetchItem(id: String): AbstractRLItem? = RLEItems[id]

    fun getItems() = RLEItems.keys.toList()
}
