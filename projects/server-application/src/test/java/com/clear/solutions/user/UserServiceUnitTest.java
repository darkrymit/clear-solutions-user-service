package com.clear.solutions.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.answer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.clear.solutions.payload.range.RangeLocalDateCriteria;
import com.clear.solutions.payload.user.UserCreateRequest;
import com.clear.solutions.payload.user.UserPartialUpdateRequest;
import com.clear.solutions.payload.user.UserUpdateRequest;
import com.clear.solutions.shared.PageMapperImpl;
import com.clear.solutions.testspecific.ServiceLevelUnitTest;
import com.clear.solutions.user.exception.NoSuchUserByIdException;
import com.clear.solutions.user.exception.UserAlreadyExistsByEmailException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;

@ServiceLevelUnitTest
@ContextConfiguration(classes = {UserService.class, UserMapperImpl.class, PageMapperImpl.class})
class UserServiceUnitTest {

  @Autowired
  private UserService userService;


  @MockBean
  private UserRepository userRepository;

  @Test
  void shouldReturnUserResponseWhenCreatingUser() {
    // given
    var request = new UserCreateRequest("johndoe@gmail.com", "John", "Doe", LocalDate.MAX,
        "New address", "+1234567890");

    when(userRepository.saveAndFlush(any())).thenAnswer(answer(getFakeSaveAnswer(1L)));

    // when
    var userResponse = userService.create(request);

    assertNotNull(userResponse);
    assertEquals(request.getEmail(), userResponse.getEmail());
    assertEquals(request.getFirstName(), userResponse.getFirstName());
    assertEquals(request.getLastName(), userResponse.getLastName());
    assertEquals(request.getBirthDate(), userResponse.getBirthDate());
    assertEquals(request.getAddress(), userResponse.getAddress());
    assertEquals(request.getPhoneNumber(), userResponse.getPhoneNumber());
    assertTrue(userResponse.hasLinks());
    assertTrue(userResponse.getLinks().hasLink("self"));
    assertTrue(userResponse.getLinks().hasLink("update"));
    assertTrue(userResponse.getLinks().hasLink("update-partial"));
    assertTrue(userResponse.getLinks().hasLink("delete"));

    verify(userRepository, times(1)).saveAndFlush(any());
  }

  @Test
  void shouldThrowUserAlreadyExistsByEmailExceptionWhenCreatingUserWithExistingEmail() {
    // given
    var request = new UserCreateRequest("johndoe@gmail.com", "John", "Doe", LocalDate.MAX,
        "New address", "1234567890");

    when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

    // when && then
    assertThrows(UserAlreadyExistsByEmailException.class, () -> userService.create(request));
  }

  @Test
  void shouldReturnUserResponseWhenGetByExistingId() {
    // given
    Instant timeOfCreation = Instant.now().plus(Duration.ofHours(10));
    Instant timeOfModification = Instant.now().plus(Duration.ofHours(20));

    User userToFind = new User(1L, "johndoe@gmail.com", "John", "Doe", LocalDate.MAX, "New address",
        "1234567890", timeOfCreation, timeOfModification);

    when(userRepository.findById(1L)).thenReturn(Optional.of(userToFind));

    // when
    var userResponse = userService.getById(1L);

    // then
    assertNotNull(userResponse);
    assertEquals(userToFind.getEmail(), userResponse.getEmail());
    assertEquals(userToFind.getFirstName(), userResponse.getFirstName());
    assertEquals(userToFind.getLastName(), userResponse.getLastName());
    assertEquals(userToFind.getBirthDate(), userResponse.getBirthDate());
    assertEquals(userToFind.getAddress(), userResponse.getAddress());
    assertEquals(userToFind.getPhoneNumber(), userResponse.getPhoneNumber());
    assertEquals(userToFind.getCreatedAt(), userResponse.getCreatedAt());
    assertEquals(userToFind.getLastModifiedAt(), userResponse.getLastModifiedAt());
    assertTrue(userResponse.hasLinks());
    assertTrue(userResponse.getLinks().hasLink("self"));
    assertTrue(userResponse.getLinks().hasLink("update"));
    assertTrue(userResponse.getLinks().hasLink("update-partial"));
    assertTrue(userResponse.getLinks().hasLink("delete"));
  }

  @Test
  void shouldThrowNoSuchUserByIdExceptionWhenGetByNonExistingId() {
    // given
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    // when && then
    assertThrows(NoSuchUserByIdException.class, () -> userService.getById(1L));
  }

  @Test
  void shouldUpdateAndReturnUserResponseWhenChangingFirstName() {
    // given
    var userId = 2L;
    Instant timeOfCreation = Instant.now().plus(Duration.ofHours(10));
    Instant timeOfModification = Instant.now().plus(Duration.ofHours(20));

    User userToUpdate = new User(userId, "johnathandoe@gmail.com", "Johnathan", "Doe",
        LocalDate.MIN, "Address", "+1234567890", timeOfCreation, timeOfModification);

    var updateRequest = new UserPartialUpdateRequest("newemail@gmail.com", null, null, null, null,
        null);

    when(userRepository.findById(userId)).thenReturn(Optional.of(userToUpdate));
    when(userRepository.saveAndFlush(any())).thenAnswer(answer(getFakeSaveAnswer(userId)));

    var updatedUser = userService.update(userId, updateRequest);

    // then
    assertNotNull(updatedUser);
    assertEquals(updateRequest.getEmail(), updatedUser.getEmail());
    assertEquals(userToUpdate.getFirstName(), updatedUser.getFirstName());
    assertEquals(userToUpdate.getLastName(), updatedUser.getLastName());
    assertEquals(userToUpdate.getBirthDate(), updatedUser.getBirthDate());
    assertEquals(userToUpdate.getAddress(), updatedUser.getAddress());
    assertEquals(userToUpdate.getPhoneNumber(), updatedUser.getPhoneNumber());
    assertEquals(userToUpdate.getCreatedAt(), updatedUser.getCreatedAt());
    assertEquals(userToUpdate.getLastModifiedAt(), updatedUser.getLastModifiedAt());
    assertTrue(updatedUser.hasLinks());
    assertTrue(updatedUser.getLinks().hasLink("self"));
    assertTrue(updatedUser.getLinks().hasLink("update"));
    assertTrue(updatedUser.getLinks().hasLink("update-partial"));
    assertTrue(updatedUser.getLinks().hasLink("delete"));
  }

  @Test
  void shouldThrowNoSuchUserByIdExceptionWhenUpdatingSomeUserFieldsOfNonExistingUser() {
    var updateRequest = new UserPartialUpdateRequest("newemail@gmail.com", null, null, null, null,
        null);
    // given
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    // when && then
    assertThrows(NoSuchUserByIdException.class, () -> userService.update(1L, updateRequest));
  }

  @Test
  void shouldUpdateAndReturnUserResponseWhenChangingAllFields() {
    // given
    var userId = 3L;
    Instant timeOfCreation = Instant.now().plus(Duration.ofHours(10));
    Instant timeOfModification = Instant.now().plus(Duration.ofHours(20));

    User userToUpdate = new User(userId, "johnathandoe@gmail.com", "Johnathan", "Doe",
        LocalDate.MIN, "Address", "+1234567890", timeOfCreation, timeOfModification);

    UserUpdateRequest updateRequest = new UserUpdateRequest("newemail@gmail.com", "John", "Doe",
        LocalDate.MAX, "New address", "+1234567890");

    when(userRepository.findById(userId)).thenReturn(Optional.of(userToUpdate));
    when(userRepository.saveAndFlush(any())).thenAnswer(answer(getFakeSaveAnswer(userId)));

    var updatedUser = userService.update(userId, updateRequest);

    // then
    assertNotNull(updatedUser);
    assertEquals(updateRequest.getEmail(), updatedUser.getEmail());
    assertEquals(updateRequest.getFirstName(), updatedUser.getFirstName());
    assertEquals(updateRequest.getLastName(), updatedUser.getLastName());
    assertEquals(updateRequest.getBirthDate(), updatedUser.getBirthDate());
    assertEquals(updateRequest.getAddress(), updatedUser.getAddress());
    assertEquals(updateRequest.getPhoneNumber(), updatedUser.getPhoneNumber());
    assertEquals(userToUpdate.getCreatedAt(), updatedUser.getCreatedAt());
    assertTrue(updatedUser.hasLinks());
    assertTrue(updatedUser.getLinks().hasLink("self"));
    assertTrue(updatedUser.getLinks().hasLink("update"));
    assertTrue(updatedUser.getLinks().hasLink("update-partial"));
    assertTrue(updatedUser.getLinks().hasLink("delete"));
  }

  @Test
  void shouldThrowNoSuchUserByIdExceptionWhenUpdatingAllUserFieldsOfNonExistingUser() {
    UserUpdateRequest updateRequest = new UserUpdateRequest("newemail@gmail.com", "John", "Doe",
        LocalDate.MAX, "New address", "1234567890");
    // given
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    // when && then
    assertThrows(NoSuchUserByIdException.class, () -> userService.update(1L, updateRequest));
  }

  @Test
  void shouldReturnVoidWhenDelete() {
    //given
    var userId = 3L;
    Instant timeOfCreation = Instant.now().plus(Duration.ofHours(10));
    Instant timeOfModification = Instant.now().plus(Duration.ofHours(20));

    User user = new User(userId, "johnathandoe@gmail.com", "Johnathan", "Doe", LocalDate.MIN,
        "Address", "1234567890", timeOfCreation, timeOfModification);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    //when
    userService.delete(1L);
    //then
    verify(userRepository, times(1)).delete(user);
  }

  @Test
  void shouldThrowNoSuchUserByIdExceptionWhenDeletingNonExistingUser() {
    // given
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    // when && then
    assertThrows(NoSuchUserByIdException.class, () -> userService.delete(1L));
  }

  @Test
  void shouldReturnUserResponseListWhenGettingAllUsersByBirthDateRange() {
    // given
    User firstUser = new User(1L, "johnathandoe@gmail.com", "Johnathan", "Doe",
        LocalDate.of(2004, 4, 25), "Address", "1234567890",
        Instant.now().plus(Duration.ofHours(10)), Instant.now().plus(Duration.ofHours(20)));
    User secondUser = new User(2L, "janedoe@gmail.com", "Jane", "Doe", LocalDate.of(2003, 5, 20),
        "New Address", "1234567899", Instant.now().plus(Duration.ofHours(11)),
        Instant.now().plus(Duration.ofHours(21)));

    var from = LocalDate.of(2003, 1, 1);
    var to = LocalDate.of(2004, 12, 31);
    var range = new RangeLocalDateCriteria(from, to);
    var pageable = Pageable.ofSize(2);
    var page = new PageImpl<>(new ArrayList<>(List.of(firstUser, secondUser)), pageable, 2);

    when(userRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

    // when
    var users = userService.getAll(range, pageable);

    // then
    assertNotNull(users);
    assertEquals(2, users.getMetadata().getTotalElements());

    var actualFirstUser = users.getContent().stream().findFirst().orElseThrow();
    var actualSecondUser = users.getContent().stream().skip(1).findFirst().orElseThrow();

    assertTrue(actualFirstUser.getBirthDate().isAfter(from));
    assertTrue(actualFirstUser.getBirthDate().isBefore(to));

    assertEquals(firstUser.getEmail(), actualFirstUser.getEmail());
    assertEquals(firstUser.getBirthDate(), actualFirstUser.getBirthDate());
    assertTrue(actualFirstUser.hasLinks());

    assertTrue(actualFirstUser.getBirthDate().isAfter(from));
    assertTrue(actualSecondUser.getBirthDate().isBefore(to));

    assertEquals(secondUser.getEmail(), actualSecondUser.getEmail());
    assertEquals(secondUser.getBirthDate(), actualSecondUser.getBirthDate());
    assertTrue(actualSecondUser.hasLinks());
  }

  private Answer1<User, User> getFakeSaveAnswer(Long id) {
    return user -> {
      user.setId(id);
      return user;
    };
  }
}
