package z8.mctrl.controller.qr

import com.google.common.io.BaseEncoding
import io.nayuki.qrcodegen.*
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.faces.view.ViewScoped
import javax.imageio.ImageIO
import javax.inject.Named


@Named("QRCodeController")
@ViewScoped
class QRCodeController {

    companion object {
        private fun toImage(qr: QrCode, scale: Int, border: Int, lightColor: Int, darkColor: Int): BufferedImage? {
            require(!(scale <= 0 || border < 0)) { "Value out of range" }
            require(!(border > Int.MAX_VALUE / 2 || qr.size + border * 2L > Int.MAX_VALUE / scale)) { "Scale or border too large" }
            val result = BufferedImage(
                (qr.size + border * 2) * scale,
                (qr.size + border * 2) * scale,
                BufferedImage.TYPE_INT_RGB
            )
            for (y in 0 until result.height) {
                for (x in 0 until result.width) {
                    val color = qr.getModule(x / scale - border, y / scale - border)
                    result.setRGB(x, y, if (color) darkColor else lightColor)
                }
            }
            return result
        }

    }

    val content: String? = ""
    var image: String? = "data:image/png;base64"

    init {
        update()
    }

    fun update() {
        val qr = QrCode.encodeText(content, QrCode.Ecc.MEDIUM)
        val qrImage = toImage(qr, 6, 2, 0xFFFFFF, 0x000000)
        val os = ByteArrayOutputStream()
        ImageIO.write(qrImage, "png", os)
        image = "data:image/png;base64," + BaseEncoding.base64().encode(os.toByteArray())
    }

    fun generate(content: String): String {
        val qr = QrCode.encodeText(content, QrCode.Ecc.MEDIUM)
        val qrImage = toImage(qr, 6, 2, 0xFFFFFF, 0x000000)
        val os = ByteArrayOutputStream()
        ImageIO.write(qrImage, "png", os)
        return "data:image/png;base64," + BaseEncoding.base64().encode(os.toByteArray())
    }

}