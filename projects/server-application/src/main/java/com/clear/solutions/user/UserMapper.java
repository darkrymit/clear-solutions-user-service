package com.clear.solutions.user;


import com.clear.solutions.payload.user.UserResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.hateoas.Link;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

  @BeanMapping(qualifiedByName = "addLinks")
  UserResponse toResponse(User user);

  @AfterMapping
  @Named("addLinks")
  default UserResponse addLinks(User user, @MappingTarget UserResponse response) {
    response.add(Link.of("/users/" + user.getId()).withSelfRel());
    response.add(Link.of("/users/" + user.getId()).withRel("update"));
    response.add(Link.of("/users/" + user.getId()).withRel("update-partial"));
    response.add(Link.of("/users/" + user.getId()).withRel("delete"));
    return response;
  }
}
