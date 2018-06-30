package io.github.atistrcsn.sectormapper

import com.vaadin.ui.CustomLayout
import java.io.InputStream

/**
 * @author atistrcsn - 2018
 */
class InlineSVGComponent(inputStream: InputStream, width: String, height: String) : CustomLayout() {

    init {
        super.setWidth(width)
        super.setHeight(height)
        super.initTemplateContentsFromInputStream(inputStream)
        primaryStyleName = "inline-svg"
    }
}