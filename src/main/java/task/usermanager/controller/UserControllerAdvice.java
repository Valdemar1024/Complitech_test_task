package task.usermanager.controller;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import task.usermanager.dto.MessageDTO;

import static task.usermanager.config.WebSocketConstant.TOPIC_USERS;

@ControllerAdvice
@Slf4j
public class UserControllerAdvice {

    @MessageExceptionHandler(value = {ValidationException.class, EntityExistsException.class, IllegalArgumentException.class})
    @SendTo(TOPIC_USERS + "/error")
    public MessageDTO<String> handleValidationException(RuntimeException ex) {
        return new MessageDTO<>(ex.getMessage());
    }

    @MessageExceptionHandler(value = {UsernameNotFoundException.class, EntityNotFoundException.class})
    @SendTo(TOPIC_USERS + "/error")
    public MessageDTO<String> handleUserNotFoundException(Exception ex) {
        return new MessageDTO<>("User not found");
    }

    @MessageExceptionHandler(value = {MessageConversionException.class})
    @SendTo(TOPIC_USERS + "/error")
    public MessageDTO<String> handleMessageConversionException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new MessageDTO<>("Invalid message format");
    }

    @MessageExceptionHandler(value = {Exception.class})
    @SendTo(TOPIC_USERS + "/error")
    public MessageDTO<String> handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new MessageDTO<>("Error occurred on server side");
    }
}
