package io.github.atistrcsn.sectormapper

import com.vaadin.annotations.StyleSheet
import com.vaadin.server.StreamResource
import com.vaadin.ui.AbsoluteLayout
import com.vaadin.ui.Image
import com.vaadin.ui.UI
import mu.KotlinLogging
import java.io.InputStream
import java.util.*
import kotlin.collections.LinkedHashMap

interface SectorMapper {
    fun init()
    fun addSector(sector: Sector)
    fun getSector(id: Int): Optional<Sector>
}

class SectorMapperImpl(private val config: Config) : SectorMapper {

    private val secMap = LinkedHashMap<Int, Sector>()
    private lateinit var sectorContainer: AbsoluteLayout

    override fun init() {
        RootComponent(config).let { UI.getCurrent().content = it }
    }

    override fun addSector(sector: Sector) {
        sector.config = config
        secMap.putIfAbsent(sector.id, sector)?.let { previous ->
            sectorContainer.removeComponent(previous.component)
        }
        sectorContainer.addComponent(sector.component)
    }

    override fun getSector(id: Int): Optional<Sector> {
        return Optional.ofNullable(secMap[id])
    }

    companion object {

        private val log = KotlinLogging.logger {}
    }

    @StyleSheet("style.css")
    inner class RootComponent(config: Config) : AbsoluteLayout() {

        init {
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
        }
    }
}

fun InputStream.toStreamResource(fileName: String) =
        StreamResource(StreamResource.StreamSource { this }, fileName)


