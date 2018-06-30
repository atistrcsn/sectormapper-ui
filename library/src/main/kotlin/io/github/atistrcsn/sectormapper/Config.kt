package io.github.atistrcsn.sectormapper

import java.io.File

class Config(
        /**
         * Background image as the default image layer. Works as a blueprint under the overlay sector map.
         */
        val backgroundImage: File?,

        /**
         * The SVG layer over the background image. Shows the mapping of the sectors.
         */
        val overlaySVG: File,

        /**
         * The prefix in the overlay SVG before the sector number (Integer).
         * @sample <path id="sector-overlay-2" style="...> than this value is "sector-overlay-"
         */
        val overlaySectorIdPrefix: String


)