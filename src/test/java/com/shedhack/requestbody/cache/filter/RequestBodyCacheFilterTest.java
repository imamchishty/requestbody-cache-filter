package com.shedhack.requestbody.cache.filter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RequestBodyCacheFilterTest {

    @Autowired //http://localhost:${local.server.port}
    private TestRestTemplate template;

    @Test
    public void sayHello() throws Exception {

        assertEquals("Hello Imam", template.getForEntity("/hello/{user}", Message.class, "Imam").getBody().getMessage());
    }


    @Test
    public void setMessage() throws Exception {

        // Arrange
        String message = "What's up ";
        String language = "German";
        String user = "Imam";

        // Act
        template.postForEntity("/hello?caps=true", new Message(message, language), Message.class);
        Message response = template.getForEntity("/hello/{user}?caps=true", Message.class, user).getBody();

        // Assert
        assertEquals(new String(message + user).toUpperCase(), response.getMessage());
        assertEquals(language.toUpperCase(), response.getLanguage());
    }
}