package z8.mctrl.controller.token


import com.google.common.io.BaseEncoding
import z8.mctrl.function.sn.SecurityNumber
import z8.mctrl.function.token.TokenId
import z8.mctrl.util.CardGenerator
import java.io.ByteArrayOutputStream
import javax.faces.view.ViewScoped
import javax.imageio.ImageIO
import javax.inject.Named


@Named("NewTokenController")
@ViewScoped
class NewTokenController {

    var tokenId = TokenId()
    var securityNumber = SecurityNumber(tokenId)
    var image: String? = null;
    var images: List<String> = listOf("a", "b")
    var selected: Any? = null

    init {
        regenerate()
    }

    fun regenerate() {
        Thread.sleep(2000)
        tokenId = TokenId()
        securityNumber = SecurityNumber(tokenId)

        val os = ByteArrayOutputStream()
        ImageIO.write(CardGenerator.generate(tokenId, securityNumber), "png", os)
        image = "data:image/png;base64," + BaseEncoding.base64().encode(os.toByteArray())
    }
}