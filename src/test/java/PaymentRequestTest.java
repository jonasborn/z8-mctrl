import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import z8.mctrl.App;
import z8.mctrl.companion.payment.PaymentRequests;
import z8.mctrl.db.KVS;
import z8.mctrl.db.RDS;
import z8.mctrl.handler.IncomingPaymentRequestHandler;
import z8.mctrl.jooq.tables.daos.ExternalDeviceDao;
import z8.mctrl.jooq.tables.daos.PaymentRequestDao;
import z8.mctrl.jooq.tables.pojos.ExternalDeviceObject;
import z8.mctrl.jooq.tables.pojos.PaymentRequestObject;
import z8.mctrl.voyager.Voyager;
import z8.mctrl.voyager.VoyagerStatus;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = App.class
)
@RunWith(SpringRunner.class)

public class PaymentRequestTest {


    @MockBean
    ExternalDeviceDao externalDeviceDao;

    @MockBean
    RDS rds;

    @MockBean
    KVS kvs;

    @MockBean
    Voyager voyager;

    @MockBean
    PaymentRequests paymentRequests;

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mvc;


    @Before
    public void before() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        ExternalDeviceDao exdo = Mockito.spy(new ExternalDeviceDao());

        Mockito.doReturn(Collections.singletonList(
                new ExternalDeviceObject(
                        "external", 1L, "internal", "secret", "title"
                )
        )).when(exdo).fetchById(Mockito.any());

        Mockito.when(rds.externalDevice()).thenReturn(exdo);

        PaymentRequestDao prdo = Mockito.spy(new PaymentRequestDao());

        Mockito.doNothing().when(prdo).insert(Mockito.any(PaymentRequestObject.class));

        Mockito.when(rds.paymentRequest()).thenReturn(prdo);

    }

    @Test
    public void testAuthSuccessful() throws Exception {

        Mockito.doReturn(new VoyagerStatus(true, "0.0.0.0", 0L)).when(voyager).getStatus(Mockito.any());

        mvc.perform(
                MockMvcRequestBuilders
                        .get("/payment/request")
                        .param("source", "external")
                        .param("secret", "secret")
                        .param("amount", "2")
        ).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void testAuthFailure() throws Exception {

        mvc.perform(
                MockMvcRequestBuilders
                        .get("/payment/request")
                        .param("source", "external")
                        .param("secret", "wrong")
                        .param("amount", "2")
        ).andDo(print()).andExpect(status().isForbidden());
    }


    @Test
    public void testQueue() throws Exception {

        Mockito.doReturn(new VoyagerStatus(true, "0.0.0.0", 0L)).when(voyager).getStatus(Mockito.any());

        mvc.perform(
                MockMvcRequestBuilders
                        .get("/payment/request")
                        .param("source", "external")
                        .param("secret", "secret")
                        .param("amount", "2")
        ).andDo(print()).andExpect(status().isOk());

        Mockito.verify(paymentRequests).addToQueue(Mockito.anyString(), Mockito.anyString());

    }
}
