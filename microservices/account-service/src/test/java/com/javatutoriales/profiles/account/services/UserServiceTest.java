package com.javatutoriales.profiles.account.services;

import com.javatutoriales.profiles.account.model.mappers.UserEntityMapper;
import com.javatutoriales.profiles.account.persistence.UserEntity;
import com.javatutoriales.profiles.account.persistence.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.ZonedDateTime;

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
    void givenAnExistingDisabledUser_whenCallEnableService_thenDisabledIsFalseAndDisabledDateIsNull(){
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
    void givenANonExistingOrDeletedUser_whenCallDisableService_thenNothingHappens(){
        given(userRepository.findById(1L)).willReturn(Mono.empty());

        Mono<Void> disableOperation = userService.disableUser(1L, false);

        StepVerifier.create(disableOperation)
                .verifyComplete();

        then(userRepository).should(VerificationModeFactory.atMost(1)).findById(1L);
        then(userRepository).should(VerificationModeFactory.times(0)).save(any(UserEntity.class));
        then(userRepository).shouldHaveNoMoreInteractions();
        then(userEntityMapper).shouldHaveNoInteractions();
    }
}
