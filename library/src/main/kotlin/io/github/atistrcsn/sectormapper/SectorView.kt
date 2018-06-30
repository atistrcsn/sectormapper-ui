package io.github.atistrcsn.sectormapper

import com.vaadin.ui.CustomLayout
import com.vaadin.ui.JavaScript
import mu.KotlinLogging

class SectorView(val id: Int,
                 private val posX: Int,
                 private val posY: Int,
                 val sectorLevel: SectorLevel,
                 private val title: String?) {

    private val prefix = "sector"
    private val cssId = "$prefix-$id-data"
    private val overlaySelector get() = "#${config.overlaySectorIdPrefix}$id"
    internal lateinit var config: Config

    internal val component = CustomLayout().apply {
        id = cssId; primaryStyleName = prefix
        setSizeUndefined()
        setSectorPosition("#$cssId", posX, posY)
        addContextClickListener {
            SectorLevel.LEVEL2.svgStyle(overlaySelector).forEach { it.exec() }
        }
    }

    fun updateData(seatsAvailable: Int, seatsConnected: Int, sectorLevel: SectorLevel) {
        component.styleName = "$sectorLevel"
        component.templateContents = """
            <div class='title'>$title</div>
            <div class='seats'>
                <span class='available'>$seatsAvailable</span><span class='connected'>$seatsConnected</span>
            </div>
            """.trimIndent()
        toggleFontShade(true)
        sectorLevel.svgStyle(overlaySelector).forEach { it.exec() }
    }

    private fun toggleFontShade(isDark: Boolean) {
        component.apply {
            if (isDark) {
                removeStyleName("lightFont"); addStyleName("darkFont")
            } else {
                removeStyleName("darkFont"); addStyleName("lightFont")
            }
        }
    }

    private fun setSvgAttr(sectorId: Int, vararg styles: KV) {
        styles.forEach { JsStyle("${config.overlaySectorIdPrefix}$sectorId", it).exec() }
    }

    private fun setSectorPosition(selector: String, posX: Int, posY: Int) {
        JsStyle(selector, KV("position", "absolute")).exec()
        JsStyle(selector, KV("left", "${posX}px")).exec()
        JsStyle(selector, KV("top", "${posY}px")).exec()
    }

    companion object {
        private val log = KotlinLogging.logger { }
    }
}

internal class JsStyle(private val selector: String, private val style: KV) {
    fun exec() = """document.querySelector('$selector').style.${style.key}='${style.value}';""".exec()
}

internal class JsAttribute(private val selector: String, private val key: String, private val value: String) {
    fun exec() = """document.querySelector('$selector').setAttribute('$key', '$value');""".exec()
}

class KV(val key: String, val value: String)

fun String.exec() {
    JavaScript.getCurrent().execute(this)
}

enum class SectorLevel {
    LEVEL0,
    LEVEL1,
    LEVEL2,
    LEVEL3
}

internal fun SectorLevel.svgStyle(selector: String): List<JsAttribute> = when (this) {
    SectorLevel.LEVEL0 -> listOf(JsAttribute("$selector use", "fill", "#7ED321"))
    SectorLevel.LEVEL1 -> listOf(JsAttribute("$selector use", "fill", "#FFAB00"))
    SectorLevel.LEVEL2 -> listOf(JsAttribute("$selector use", "fill", "#FF5630"))
    SectorLevel.LEVEL3 -> listOf(JsAttribute("$selector use", "fill", "#EB0000"))
}