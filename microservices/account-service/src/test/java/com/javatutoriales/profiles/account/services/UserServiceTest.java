package com.javatutoriales.profiles.account.services;

import com.javatutoriales.profiles.account.model.User;
import com.javatutoriales.profiles.account.model.mappers.UserEntityMapper;
import com.javatutoriales.profiles.account.persistence.UserEntity;
import com.javatutoriales.profiles.account.persistence.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(SpringExtension.class)
class UserServiceTest {
    @Spy
    UserRepository userRepository;

    @Spy
    UserEntityMapper userEntityMapper;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    @DisplayName("Delete existing user")
    void givenAnExistingUser_whenCallDeleteService_thenDeletedIsTrueAndDeletedDateIsToday() {
        //Given - Arrange
        UserEntity userEntity = UserEntity.builder().id(1L).deleted(false).deletedOn(null).build();
        Mono<UserEntity> userEntityMono = Mono.just(userEntity);

        given(userRepository.findById(1L)).willReturn(userEntityMono);
        given(userRepository.save(userEntity)).willReturn(userEntityMono);

        // When - Act
        Mono<Void> deletedOperation = userService.deleteUser(1L);


        //Then - Assert / Verify
        StepVerifier.create(deletedOperation)
                .verifyComplete();

        assertThat(userEntity.isDeleted()).isTrue();
        assertThat(userEntity.getDeletedOn()).isBeforeOrEqualTo(ZonedDateTime.now());

        then(userRepository).should().findById(1L);
        then(userRepository).should().save(userEntity);
        then(userRepository).should(VerificationModeFactory.times(0)).delete(any(UserEntity.class));
        then(userRepository).shouldHaveNoMoreInteractions();
        then(userEntityMapper).shouldHaveNoInteractions();
    }

    @Test
    void givenAnNonExistingUser_whenCallDeleteService_thenNothingHappens() {
        given(userRepository.findById(1L)).willReturn(Mono.empty());

        Mono<Void> deleteOperation = userService.deleteUser(1L);

        StepVerifier.create(deleteOperation)
                .verifyComplete();

        then(userRepository).should().findById(1L);
        then(userRepository).should(VerificationModeFactory.times(0)).delete(any(UserEntity.class));
        then(userRepository).should(VerificationModeFactory.times(0)).save(any(UserEntity.class));
        then(userRepository).shouldHaveNoMoreInteractions();
        then(userEntityMapper).shouldHaveNoInteractions();
    }


    @Test
    void givenAnExistingAndEnabledUser_whenCallDisableService_thenDisabledIsTrueAndDisabledDateIsToday() {
        UserEntity userEntity = UserEntity.builder().id(1L).deleted(false).deletedOn(null).disabled(false).disabledOn(ZonedDateTime.now()).build();
        Mono<UserEntity> userEntityMono = Mono.just(userEntity);

        given(userRepository.findById(1L)).willReturn(userEntityMono);
        given(userRepository.save(userEntity)).willReturn(userEntityMono);

        Mono<Void> disableOperation = userService.disableUser(1L, true);

        StepVerifier.create(disableOperation)
                .verifyComplete();

        assertThat(userEntity.isDeleted()).isFalse();
        assertThat(userEntity.isDisabled()).isTrue();
        assertThat(userEntity.getDisabledOn()).isBeforeOrEqualTo(ZonedDateTime.now());

        then(userRepository).should(VerificationModeFactory.atMost(1)).findById(1L);
        then(userRepository).should(VerificationModeFactory.atMost(1)).save(userEntity);
        then(userRepository).shouldHaveNoMoreInteractions();
        then(userEntityMapper).shouldHaveNoInteractions();
    }

    @Test
    void givenAnExistingDisabledUser_whenCallEnableService_thenDisabledIsFalseAndDisabledDateIsNull() {
        UserEntity userEntity = UserEntity.builder().id(1L).deleted(false).deletedOn(null).disabled(false).disabledOn(ZonedDateTime.now()).build();
        Mono<UserEntity> userEntityMono = Mono.just(userEntity);

        given(userRepository.findById(1L)).willReturn(userEntityMono);
        given(userRepository.save(userEntity)).willReturn(userEntityMono);

        Mono<Void> disableOperation = userService.disableUser(1L, false);

        StepVerifier.create(disableOperation)
                .verifyComplete();

        assertThat(userEntity.isDeleted()).isFalse();
        assertThat(userEntity.isDisabled()).isFalse();
        assertThat(userEntity.getDisabledOn()).isNull();

        then(userRepository).should(VerificationModeFactory.atMost(1)).findById(1L);
        then(userRepository).should(VerificationModeFactory.atMost(1)).save(userEntity);
        then(userRepository).shouldHaveNoMoreInteractions();
        then(userEntityMapper).shouldHaveNoInteractions();
    }

    @Test
    void givenANonExistingOrDeletedUser_whenCallDisableService_thenNothingHappens() {
        given(userRepository.findById(1L)).willReturn(Mono.empty());

        Mono<Void> disableOperation = userService.disableUser(1L, false);

        StepVerifier.create(disableOperation)
                .verifyComplete();

        then(userRepository).should(VerificationModeFactory.atMost(1)).findById(1L);
        then(userRepository).should(VerificationModeFactory.times(0)).save(any(UserEntity.class));
        then(userRepository).shouldHaveNoMoreInteractions();
        then(userEntityMapper).shouldHaveNoInteractions();
    }


    @Test
    void givenFiveUsersInTheDatabase_whenCallGetAll_thenEntitiesAreConvertedIntoModelObject() {
        Flux<UserEntity> users = Flux.from(Flux.range(1, 5).map(count -> UserEntity.builder().id(Long.valueOf(count)).username("user_" + count).build()));
        List<User> userList = IntStream.rangeClosed(0, 5).boxed().toList().stream().map(count -> User.builder().id(Long.valueOf(count)).username("user_" + count).build()).toList();

        given(userRepository.findAll()).willReturn(users);
        given(userEntityMapper.entityToModel(any(UserEntity.class))).will(invocation -> userList.get((((UserEntity) invocation.getArgument(0)).getId()).intValue()));

        Flux<User> userFlux = userService.getAll();

        StepVerifier.create(userFlux).expectNextCount(5).verifyComplete();

        then(userRepository).should(VerificationModeFactory.times(1)).findAll();
        then(userEntityMapper).should(VerificationModeFactory.times(5)).entityToModel(any(UserEntity.class));
        then(userRepository).shouldHaveNoMoreInteractions();
        then(userEntityMapper).shouldHaveNoMoreInteractions();
    }

    @Test
    void givenTheIdOfAnExistingUser_whenCallGetUserById_thenAUserEntityIsConvertedIntoAModelObject() {
        UserEntity userEntity = UserEntity.builder().id(5L).username("user_5").build();
        Mono<UserEntity> userEntityMono = Mono.just(userEntity);
        User user = User.builder().id(5L).username("user_5").build();

        given(userRepository.findById(5L)).willReturn(userEntityMono);
        given(userEntityMapper.entityToModel(userEntity)).willReturn(user);

        Mono<User> userMono = userService.getUser(5L);

        StepVerifier.create(userMono).expectNextCount(1).verifyComplete();

        then(userRepository).should(VerificationModeFactory.times(1)).findById(5L);
        then(userRepository).shouldHaveNoMoreInteractions();
        then(userEntityMapper).should(VerificationModeFactory.times(1)).entityToModel(userEntity);
        then(userEntityMapper).shouldHaveNoMoreInteractions();
    }

    @Test
    void givenTheIdOfAnDeletedOrNonExistingUser_whenCallGetUserById_thenNoConversionIsPerformed() {

        given(userRepository.findById(-5L)).willReturn(Mono.empty());

        Mono<User> userMono = userService.getUser(-5L);

        StepVerifier.create(userMono).expectNextCount(0).verifyComplete();

        then(userRepository).should(VerificationModeFactory.times(1)).findById(-5L);
        then(userRepository).shouldHaveNoMoreInteractions();
        then(userEntityMapper).shouldHaveNoInteractions();
    }


    @Test
    void givenTheUsernameOfAnExistingUser_whenCallGetUserByUsername_thenAUserEntityIsConvertedIntoAModelObject() {
        UserEntity userEntity = UserEntity.builder().id(5L).username("user_5").build();
        Mono<UserEntity> userEntityMono = Mono.just(userEntity);
        User user = User.builder().id(5L).username("user_5").build();

        given(userRepository.findByUsername("user_5")).willReturn(userEntityMono);
        given(userEntityMapper.entityToModel(userEntity)).willReturn(user);

        Mono<User> userMono = userService.getUser("user_5");

        StepVerifier.create(userMono).expectNextCount(1).verifyComplete();

        then(userRepository).should(VerificationModeFactory.times(1)).findByUsername("user_5");
        then(userRepository).shouldHaveNoMoreInteractions();
        then(userEntityMapper).should(VerificationModeFactory.times(1)).entityToModel(userEntity);
        then(userEntityMapper).shouldHaveNoMoreInteractions();
    }

    @Test
    void whenCallSaveNewUser_thenModelIsConvertedIntoEntityObject() {
        User user = User.builder().id(null).username("username").build();
        UserEntity userEntity = UserEntity.builder().username("username").build();
        Mono<User> userMono = Mono.just(user);
        Mono<UserEntity> userEntityMono = Mono.just(userEntity);


        given(userEntityMapper.modelToEntity(user)).willReturn(userEntity);
        given(userRepository.save(userEntity)).willReturn(userEntityMono);
        given(userEntityMapper.entityToModel(userEntity)).willReturn(user);

        Mono<User> savedUser = userService.saveUser(userMono);

        StepVerifier.create(savedUser).expectNextCount(1).verifyComplete();

        InOrder inOrder = Mockito.inOrder(userRepository, userEntityMapper);

        then(userEntityMapper).should(inOrder, VerificationModeFactory.times(1)).modelToEntity(user);
        then(userRepository).should(inOrder, VerificationModeFactory.times(1)).save(userEntity);
        then(userEntityMapper).should(inOrder, VerificationModeFactory.times(1)).entityToModel(userEntity);
        then(userRepository).shouldHaveNoMoreInteractions();
        then(userEntityMapper).shouldHaveNoMoreInteractions();
    }


    @Test
    void whenCallUpdatedWithAnExistingUser_thenTheNewUserHasTheOldId() {

        UserEntity oldUserEntity = UserEntity.builder().id(1L).username("oldUsername").build();
        UserEntity newUserEntity = UserEntity.builder().username("newUserName").build();
        User newUser = User.builder().username("newUserName").build();

        Mono<UserEntity> oldUserEntityMono = Mono.just(oldUserEntity);
        Mono<User> userMono = Mono.just(newUser);
        Mono<UserEntity> newUserEntityMono = Mono.just(newUserEntity);

        given(userRepository.findById(1L)).willReturn(oldUserEntityMono);
        given(userEntityMapper.modelToEntity(newUser)).willReturn(newUserEntity);
        given(userRepository.save(newUserEntity)).willReturn(newUserEntityMono);
        given(userEntityMapper.entityToModel(newUserEntity)).willReturn(newUser);

        Mono<User> updatedUser = userService.updateUser(1L, userMono);

        StepVerifier.create(updatedUser)
                .expectNextCount(1)
                .verifyComplete();

        assertThat(newUserEntity.getId()).isEqualTo(1L);

        then(userRepository).should(VerificationModeFactory.times(1)).findById(1L);
        then(userRepository).should(VerificationModeFactory.times(1)).save(newUserEntity);
        then(userRepository).shouldHaveNoMoreInteractions();

        then(userEntityMapper).should(VerificationModeFactory.times(1)).modelToEntity(newUser);
        then(userEntityMapper).should(VerificationModeFactory.times(1)).entityToModel(newUserEntity);
        then(userEntityMapper).shouldHaveNoMoreInteractions();

    }
}
