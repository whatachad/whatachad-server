package com.whatachad.app.common;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatachad.app.model.request.SignUpRequestDto;
import com.whatachad.app.type.UserMetaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class SignUpTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    void test() throws Exception {
        SignUpRequestDto dto = new SignUpRequestDto();
        Map<UserMetaType, String> meta = new HashMap<>();
        meta.put(UserMetaType.ROLE, "ROLE_CUSTOMER");
        dto.setId("wlsgus555");
        dto.setEmail("vmflwu123@gmail.com");
        dto.setName("jinhyeon");
        dto.setPassword("admin");
        dto.setPhone("01038383606");
        dto.setMeta(meta);
        String request = mapper.writeValueAsString(dto);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/signUp")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}
