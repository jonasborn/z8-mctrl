package z8.mctrl.util

import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.awt.image.RescaleOp
import java.io.File
import javax.imageio.ImageIO


class CardGenerator {

    companion object {

        fun prepareId(id: IntArray): String {
            return id.toList().chunked(4).joinToString(
                separator = " ", transform = {
                    it.joinToString("")
                }
            )
        }

        fun generate(): BufferedImage {
            val img = BufferedImage(1011, 637, BufferedImage.TYPE_BYTE_BINARY)
            val g = img.graphics as Graphics2D
            g.color = Color.WHITE
            g.fillRect(0, 0, img.width, img.height)
            g.color = Color.BLACK
            g.font = Font.createFont(Font.TRUETYPE_FONT, File("/home/jonas/Downloads/kredit back.ttf"))
                .deriveFont(60f)
            g.drawString("z8", 70, 100)
            g.drawString(prepareId(LuhnUtils.generateLuhn(16)), 70, 450)
            ImageIO.write(img, "png", File("out.png"))
            return img
        }

        /**
         * Returns the supplied src image brightened by a float value from 0 to 10.
         * Float values below 1.0f actually darken the source image.
         */
        fun brighten(src: BufferedImage, level: Float): BufferedImage? {
            val dst = BufferedImage(
                src.width, src.height, BufferedImage.TYPE_INT_RGB
            )
            val scales = floatArrayOf(level, level, level)
            val offsets = FloatArray(4)
            val rop = RescaleOp(scales, offsets, null)
            val g = dst.createGraphics()
            g.drawImage(src, rop, 0, 0)
            g.dispose()
            return dst
        }

        fun main(args: Array<String>) {

        }
    }

}