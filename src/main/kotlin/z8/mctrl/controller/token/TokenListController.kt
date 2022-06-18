package z8.mctrl.controller.token

import z8.mctrl.data.token.TokenTable
import javax.inject.Named


@Named("dtBasicView")
class TokenListController {

    var tokens : List<TokenTable> = ArrayList<TokenTable>()

    constructor() {



    }
}