package de.jonasborn.zeroeight.controller.token

import de.jonasborn.zeroeight.data.token.TokenTable
import javax.inject.Named


@Named("dtBasicView")
class TokenListController {

    var tokens : List<TokenTable> = ArrayList<TokenTable>()

    constructor() {



    }
}