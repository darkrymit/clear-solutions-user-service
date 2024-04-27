package com.clear.solutions.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.clear.solutions.testspecific.JpaLevelTest;
import com.clear.solutions.user.exception.NoSuchUserByIdException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;

@JpaLevelTest
@Sql(scripts = {"classpath:sql/users.sql"})
class UserRepositoryIntegrationTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  void shouldSaveUser() {
    // given
    User userToSave = new User(null, "email@gmail.com", "John", "Doe", LocalDate.of(1990, 1, 1),
        "Address", "1234567890", null, null);

    // when
    User savedUser = userRepository.save(userToSave);

    // then
    assertNotNull(savedUser);
    assertNotNull(savedUser.getId());
    assertEquals(userToSave.getEmail(), savedUser.getEmail());
    assertEquals(userToSave.getFirstName(), savedUser.getFirstName());
    assertEquals(userToSave.getLastName(), savedUser.getLastName());
    assertEquals(userToSave.getBirthDate(), savedUser.getBirthDate());
    assertEquals(userToSave.getAddress(), savedUser.getAddress());
    assertEquals(userToSave.getPhoneNumber(), savedUser.getPhoneNumber());
    assertEquals(userToSave.getCreatedAt(), savedUser.getCreatedAt());
    assertEquals(userToSave.getLastModifiedAt(), savedUser.getLastModifiedAt());
  }

  @Test
  void shouldThrowExceptionWhenSavingUserWithoutEmail() {
    // given
    User userToSave = new User(null, null, "John", "Doe", LocalDate.of(1990, 1, 1), "Address",
        "1234567890", null, null);

    // when & then
    assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(userToSave));
  }

  @Test
  void shouldReturnUserWhenFindById() {
    // when
    Optional<User> optionalUser = userRepository.findById(1L);

    // then
    assertTrue(optionalUser.isPresent());
    User user = optionalUser.get();
    assertEquals(1L, user.getId());
    assertEquals("johndoe@gmail.com", user.getEmail());
    assertEquals("John", user.getFirstName());
    assertEquals("Doe", user.getLastName());
    assertEquals(LocalDate.of(2002, 2, 22), user.getBirthDate());
    assertEquals("John Doe address", user.getAddress());
    assertEquals("+1234567892", user.getPhoneNumber());
    assertEquals(Instant.parse("2024-04-24T22:22:09.266615Z"), user.getCreatedAt());
    assertEquals(Instant.parse("2024-04-24T22:28:19.266615Z"), user.getLastModifiedAt());
  }

  @Test
  void shouldUpdateUserWhenChangingFirstName() {
    // given
    var userId = 1L;
    User userToUpdate = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchUserByIdException(userId));
    userToUpdate.setFirstName("Jane");

    // when
    User updatedUser = userRepository.save(userToUpdate);

    // then
    assertEquals(userToUpdate.getId(), updatedUser.getId());
    assertEquals("Jane", updatedUser.getFirstName());
  }

  @Test
  void shouldUpdateUserWhenChangingAllFields() {
    // given
    var userId = 1L;
    User userToUpdate = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchUserByIdException(userId));
    userToUpdate.setEmail("new@gmail.com");
    userToUpdate.setFirstName("Jane");
    userToUpdate.setLastName("Smith");
    userToUpdate.setBirthDate(LocalDate.of(1990, 1, 1));
    userToUpdate.setAddress("New address");
    userToUpdate.setPhoneNumber("1234567890");

    // when
    User updatedUser = userRepository.save(userToUpdate);

    // then
    assertEquals(userToUpdate.getId(), updatedUser.getId());
    assertEquals(userToUpdate.getEmail(), updatedUser.getEmail());
    assertEquals(userToUpdate.getFirstName(), updatedUser.getFirstName());
    assertEquals(userToUpdate.getLastName(), updatedUser.getLastName());
    assertEquals(userToUpdate.getBirthDate(), updatedUser.getBirthDate());
    assertEquals(userToUpdate.getAddress(), updatedUser.getAddress());
    assertEquals(userToUpdate.getPhoneNumber(), updatedUser.getPhoneNumber());
    assertEquals(userToUpdate.getCreatedAt(), updatedUser.getCreatedAt());
  }

  @Test
  void shouldDeleteUserWhenDeletingByExistingUser() {
    // given
    User userToDelete = userRepository.findById(1L).orElseThrow();

    // when
    userRepository.delete(userToDelete);

    // then
    assertTrue(userRepository.findById(1L).isEmpty());
  }

  @Test
  void shouldDeleteUserByIdWhenDeletingByExistingId() {
    // when
    userRepository.deleteById(1L);

    // then
    assertTrue(userRepository.findById(1L).isEmpty());
  }

  @Test
  void shouldReturnTrueWhenUserExists() {
    // when
    boolean exists = userRepository.existsById(1L);

    // then
    assertTrue(exists);
  }

  @Test
  void shouldReturnNonEmptyListWhenFindAll() {
    // when
    List<User> users = userRepository.findAll();

    // then
    assertNotNull(users);
    assertFalse(users.isEmpty());
  }
}
