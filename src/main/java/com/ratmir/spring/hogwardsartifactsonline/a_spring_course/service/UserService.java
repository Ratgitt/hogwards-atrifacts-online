package com.ratmir.spring.hogwardsartifactsonline.a_spring_course.service;

import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.dto.UserCreateDto;
import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.dto.UserReadDto;
import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.entity.User;
import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.mapper.UserMapper;
import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.repository.UserRepository;
import com.ratmir.spring.hogwardsartifactsonline.rabbitmq.NotificationRequest;
import com.ratmir.spring.hogwardsartifactsonline.rabbitmq.RabbitMQMessageProducer;
import com.ratmir.spring.hogwardsartifactsonline.util.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${rabbitmq.exchanges.internal}")
    private String EXCHANGE;

    @Value("${rabbitmq.routing-keys.internal-notification}")
    private String ROUTING_KEY;

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final RabbitMQMessageProducer rabbitMQMessageProducer;

    @Transactional(readOnly = true)
    public UserReadDto findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::entityToReadDto)
                .orElseThrow(() -> new ObjectNotFoundException("spring_user", id));
    }

    @Transactional
    public UserReadDto createUser(UserCreateDto userCreateDto) {
        User userToSave = userMapper.createDtoToEntity(userCreateDto);
        User savedUser = userRepository.save(userToSave);

        log.info("saved user: {}, ready to send notification", savedUser);

        // Отправляем сообщение в RabbitMQ
        sendNotification(savedUser);

        return userMapper.entityToReadDto(savedUser);
    }

    private void sendNotification(User savedUser) {
        NotificationRequest notificationRequest = new NotificationRequest(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getFirstname(),
                savedUser.getLastname(),
                String.format("Hi %s, welcome to Site...", savedUser.getFirstname())
        );

        log.info("sending notification request to RabbitMQ: {}", notificationRequest);

        rabbitMQMessageProducer.publish(
                notificationRequest,
                EXCHANGE,
                ROUTING_KEY
        );
    }
}