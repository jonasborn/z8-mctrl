package z8.gctrl.controller.token

import z8.gctrl.data.token.TokenTable
import javax.inject.Named


@Named("dtBasicView")
class TokenListController {

    var tokens : List<TokenTable> = ArrayList<TokenTable>()

    constructor() {



    }
}