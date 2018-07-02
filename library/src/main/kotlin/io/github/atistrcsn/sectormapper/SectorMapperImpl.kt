package io.github.atistrcsn.sectormapper

import com.vaadin.annotations.StyleSheet
import com.vaadin.server.StreamResource
import com.vaadin.ui.AbsoluteLayout
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.ComboBox
import com.vaadin.ui.Component
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Image
import com.vaadin.ui.ItemCaptionGenerator
import com.vaadin.ui.TextField
import com.vaadin.ui.UI
import mu.KotlinLogging
import java.io.InputStream
import java.util.*
import kotlin.collections.LinkedHashMap

interface SectorMapper {
    fun init()
    fun addSector(sector: SectorView): SectorView
    fun getSector(id: Int): Optional<SectorView>
}

class SectorMapperImpl(private val config: Config) : SectorMapper {

    private val secMap = LinkedHashMap<Int, SectorView>()
    private lateinit var sectorContainer: AbsoluteLayout
    private lateinit var secCombo: ComboBox<SectorView>

    override fun init() {
        RootComponent(config).let { UI.getCurrent().content = it }
    }

    override fun addSector(sector: SectorView): SectorView {
        sector.config = config
        secMap.putIfAbsent(sector.id, sector)?.let { previous ->
            sectorContainer.removeComponent(previous.component)
        }
        sectorContainer.addComponent(sector.component)

        if (config.isDevelopment) {
            secCombo.apply {
                setItems(secMap.entries.map { it.value }.sortedBy { it.id })
            }
        }

        return sector
    }

    override fun getSector(id: Int): Optional<SectorView> {
        return Optional.ofNullable(secMap[id])
    }

    @StyleSheet("style.css")
    inner class RootComponent(config: Config) : AbsoluteLayout() {

        init {
            log
            setSizeFull()

            // add background image, if passed
            config.backgroundImage?.let { bg ->
                Image(null, bg.inputStream().toStreamResource("bg.png")).let {
                    addComponent(it)
                }
            }

            // add overlay SVG
            InlineSVGComponent(config.overlaySVG.inputStream(), "100%", "100%").let {
                addComponent(it)
            }

            addComponent(AbsoluteLayout().apply { sectorContainer = this })

            if (config.isDevelopment) {
                addComponent(getDevFrom())
            }
        }
    }

    private fun getDevFrom(): Component {
        return HorizontalLayout().apply {
            log.trace { "UI id: ${UI.getCurrent().id}" }
            setMargin(true)
            setHeightUndefined()
            defaultComponentAlignment = Alignment.BOTTOM_LEFT

            secCombo = ComboBox<SectorView>().apply {
                id = "secCombo"; caption = "Choose sector"
                itemCaptionGenerator = ItemCaptionGenerator { it.title }
                pageLength = 0
            }
            secCombo.setItems(secMap.entries.map { it.value }.sortedBy { it.id })
            val available = TextField().apply {
                caption = "Available seats"
            }
            val connected = TextField().apply {
                caption = "Seats connected"
            }
            val levels = ComboBox<SectorLevel>().apply {
                caption = "Készenléti szint"
                setItems(SectorLevel.values().toList().sortedBy { it.ordinal })
                itemCaptionGenerator = ItemCaptionGenerator { it.name }
            }
            val button = Button("Beállít").apply {
                addClickListener {
                    log.trace { "UI id: ${UI.getCurrent().id}" }
                    secCombo.selectedItem.ifPresent {
                        it.updateData(
                                available.value.toInt(),
                                connected.value.toInt(),
                                levels.value
                        )
                    }
                }
            }

            addComponents(secCombo, available, connected, levels, button)
        }
    }

    companion object {
        private val log = KotlinLogging.logger { }
    }
}

fun InputStream.toStreamResource(fileName: String) =
        StreamResource(StreamResource.StreamSource { this }, fileName)


