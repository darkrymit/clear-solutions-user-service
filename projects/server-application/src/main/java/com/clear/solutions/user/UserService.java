package com.clear.solutions.user;

import com.clear.solutions.payload.range.RangeLocalDateCriteria;
import com.clear.solutions.payload.user.UserCreateRequest;
import com.clear.solutions.payload.user.UserPartialUpdateRequest;
import com.clear.solutions.payload.user.UserResponse;
import com.clear.solutions.payload.user.UserUpdateRequest;
import com.clear.solutions.shared.PageMapper;
import com.clear.solutions.user.exception.NoSuchUserByIdException;
import com.clear.solutions.user.exception.UserAlreadyExistsByEmailException;
import jakarta.transaction.Transactional;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  private final UserMapper userMapper;

  private final PageMapper pageMapper;

  @Transactional
  public UserResponse create(UserCreateRequest request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new UserAlreadyExistsByEmailException(request.getEmail());
    }

    User user = new User();

    user.setEmail(request.getEmail());
    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setBirthDate(request.getBirthDate());
    user.setAddress(request.getAddress());
    user.setPhoneNumber(request.getPhoneNumber());

    var savedUser = userRepository.saveAndFlush(user);

    return userMapper.toResponse(savedUser);
  }

  public UserResponse getById(Long id) {
    return userRepository.findById(id).map(userMapper::toResponse)
        .orElseThrow(() -> new NoSuchUserByIdException(id));
  }

  @Transactional
  public UserResponse update(Long id, UserPartialUpdateRequest request) {
    User user = userRepository.findById(id).orElseThrow(() -> new NoSuchUserByIdException(id));

    if (request.getEmail() != null) {
      user.setEmail(request.getEmail());
    }
    if (request.getFirstName() != null) {
      user.setFirstName(request.getFirstName());
    }
    if (request.getLastName() != null) {
      user.setLastName(request.getLastName());
    }
    if (request.getBirthDate() != null) {
      user.setBirthDate(request.getBirthDate());
    }
    if (request.getAddress() != null) {
      user.setAddress(request.getAddress());
    }
    if (request.getPhoneNumber() != null) {
      user.setPhoneNumber(request.getPhoneNumber());
    }

    var updatedUser = userRepository.saveAndFlush(user);

    return userMapper.toResponse(updatedUser);
  }

  @Transactional
  public UserResponse update(Long id, UserUpdateRequest request) {
    User user = userRepository.findById(id).orElseThrow(() -> new NoSuchUserByIdException(id));

    user.setEmail(request.getEmail());
    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setBirthDate(request.getBirthDate());

    if (request.getAddress() != null) {
      user.setAddress(request.getAddress());
    }
    if (request.getPhoneNumber() != null) {
      user.setPhoneNumber(request.getPhoneNumber());
    }

    var updatedUser = userRepository.saveAndFlush(user);

    return userMapper.toResponse(updatedUser);
  }

  @Transactional
  public void delete(Long id) {
    var user = userRepository.findById(id).orElseThrow(() -> new NoSuchUserByIdException(id));

    userRepository.delete(user);
  }

  public PagedModel<UserResponse> getAll(RangeLocalDateCriteria range, Pageable pageable) {

    Specification<User> birthDateRangeSpecification = getBirthDateRangeSpecification(range);

    var pageOfUsers = userRepository.findAll(birthDateRangeSpecification, pageable);

    return pageMapper.toResponse(pageOfUsers.map(userMapper::toResponse), URI.create("/users"));
  }

  private Specification<User> getBirthDateRangeSpecification(RangeLocalDateCriteria range) {
    return (root, query, criteriaBuilder) -> {
      var from = range.getFromTemporal();
      var to = range.getToTemporal();
      if (from == null && to == null) {
        return null;
      }
      final var birthDateField = "birthDate";
      if (from != null && to != null) {
        return criteriaBuilder.between(root.get(birthDateField), from, to);
      }
      if (from != null) {
        return criteriaBuilder.greaterThanOrEqualTo(root.get(birthDateField), from);
      }
      return criteriaBuilder.lessThanOrEqualTo(root.get(birthDateField), to);
    };
  }
}
