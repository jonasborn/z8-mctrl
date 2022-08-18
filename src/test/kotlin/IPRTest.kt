import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.lenient
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import z8.mctrl.App
import z8.mctrl.CustomServletContextInitializer
import z8.mctrl.db.RDS
import z8.mctrl.handler.IncomingPaymentRequestHandler
import z8.mctrl.jooq.tables.daos.ExternalDeviceDao
import z8.mctrl.jooq.tables.pojos.ExternalDeviceObject
import z8.mctrl.rest.GetRequestPayment
import z8.mctrl.server.StaticFiles


@ExtendWith(SpringExtension::class)
@ExtendWith(MockitoExtension::class)
@WebAppConfiguration
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = [App::class]
)
@AutoConfigureMockMvc
@Import(CustomServletContextInitializer::class, StaticFiles::class)
class IPRTest {

    @Autowired
    private val webApplicationContext: WebApplicationContext? = null

    private var mvc: MockMvc? = null

    @BeforeEach
    fun before() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext!!).build()

        /* Do NOT use this
        Arrays.stream(webApplicationContext.beanDefinitionNames)
            .map { name -> webApplicationContext.getBean(name).javaClass.name }
            .sorted()
            .forEach(System.out::println)
         */
    }

    @Autowired
    val getRequestPayment: GetRequestPayment? = null

    @Autowired
    val rds: RDS? = null

    @Test
    fun testRegister() {

        val mock = mock<ExternalDeviceDao>  {

        }

        lenient().`when`(mock.toString()).doReturn(">>>>>>>>>>>>>>>>>>a")

        println(rds!!.externalDevice().toString())

    }
}