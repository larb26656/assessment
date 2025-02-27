package com.kbtg.bootcamp.posttest.features.user.lottery;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbtg.bootcamp.posttest.features.user.lottery.model.buy_lottery.BuyLotteryResDto;
import com.kbtg.bootcamp.posttest.features.user.lottery.model.get_my_lottery.GetMyLotteryResDto;
import com.kbtg.bootcamp.posttest.features.user.lottery.model.sell_lottery.SellLotteryResDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class UserLotteryControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private UserLotteryService mockUserLotteryService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String buyLotteryUrl = "/lotteries";

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    // getAllLotteries
    @Test
    public void Buy_ShouldSuccess_WhenRequestCorrectly() throws Exception {
        // Arrange
        final String mockUserId = "1";
        final String mockTicketId = "123456";

        final BuyLotteryResDto mockRes = new BuyLotteryResDto(
                "1"
        );
        final String expectedBodyJson = objectMapper.writeValueAsString(mockRes);
        when(mockUserLotteryService.buy(any(), any())).thenReturn(mockRes);

        // Act
        MvcResult mvcResult = mockMvc.perform(post("/users/" + mockUserId + "/lotteries/" + mockTicketId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();

        // Assert
        assertEquals(expectedBodyJson, contentAsString);
        verify(mockUserLotteryService).buy(mockUserId, mockTicketId);
    }

    // getMyLottery
    @Test
    public void GetMyLottery_ShouldSuccess_WhenRequestCorrectly() throws Exception {
        // Arrange
        final String mockUserId = "1";

        final GetMyLotteryResDto mockRes = new GetMyLotteryResDto(
                List.of("123456", "123456", "333333"),
                3,
                new BigDecimal("350")
        );
        final String expectedBodyJson = objectMapper.writeValueAsString(mockRes);
        when(mockUserLotteryService.getMyLottery(any())).thenReturn(mockRes);

        // Act
        MvcResult mvcResult = mockMvc.perform(get("/users/" + mockUserId + "/lotteries")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();

        // Assert
        assertEquals(expectedBodyJson, contentAsString);
        verify(mockUserLotteryService).getMyLottery(mockUserId);
    }

    // getMyLottery
    @Test
    public void SellLottery_ShouldSuccess_WhenRequestCorrectly() throws Exception {
        // Arrange
        final String mockUserId = "1";
        final String mockTicketId = "123456";

        final SellLotteryResDto mockRes = new SellLotteryResDto(
                "123456"
        );
        final String expectedBodyJson = objectMapper.writeValueAsString(mockRes);
        when(mockUserLotteryService.sellLottery(any(), any())).thenReturn(mockRes);

        // Act
        MvcResult mvcResult = mockMvc.perform(delete("/users/" + mockUserId + "/lotteries/" + mockTicketId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();

        // Assert
        assertEquals(expectedBodyJson, contentAsString);
        verify(mockUserLotteryService).sellLottery(mockUserId, mockTicketId);
    }

}