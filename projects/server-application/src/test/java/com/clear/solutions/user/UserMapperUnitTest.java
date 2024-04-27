package com.clear.solutions.user;

import com.clear.solutions.payload.user.UserResponse;
import com.clear.solutions.testspecific.MapperLevelUnitTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@MapperLevelUnitTest
@ContextConfiguration(classes = {UserMapperImpl.class})
class UserMapperUnitTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    void shouldMapUserToUserResponse() {
        // given
        Instant timeOfCreation = Instant.now().plus(Duration.ofHours(10));
        Instant timeOfModification = Instant.now().plus(Duration.ofHours(20));

        User userToMap = new User(1L, "johndoe@gmail.com", "John", "Doe",
                LocalDate.MAX, "New address","1234567890", timeOfCreation, timeOfModification);
        // when
        UserResponse userResponse = userMapper.toResponse(userToMap);

        // then
        assertNotNull(userResponse);
        assertEquals(userToMap.getId(), userResponse.getId());
        assertEquals(userToMap.getEmail(), userResponse.getEmail());
        assertEquals(userToMap.getFirstName(), userResponse.getFirstName());
        assertEquals(userToMap.getLastName(), userResponse.getLastName());
        assertEquals(userToMap.getBirthDate(), userResponse.getBirthDate());
        assertEquals(userToMap.getAddress(), userResponse.getAddress());
        assertEquals(userToMap.getPhoneNumber(), userResponse.getPhoneNumber());
        assertEquals(userToMap.getCreatedAt(), userResponse.getCreatedAt());
        assertEquals(userToMap.getLastModifiedAt(), userResponse.getLastModifiedAt());
        assertTrue(userResponse.hasLinks());
        assertTrue(userResponse.getLinks().hasLink("self"));
        assertTrue(userResponse.getLinks().hasLink("update"));
        assertTrue(userResponse.getLinks().hasLink("update-partial"));
        assertTrue(userResponse.getLinks().hasLink("delete"));
    }

}
