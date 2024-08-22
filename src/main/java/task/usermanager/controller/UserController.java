package task.usermanager.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import task.usermanager.dto.DeleteRangeDTO;
import task.usermanager.dto.MessageDTO;
import task.usermanager.dto.PageRequestDTO;
import task.usermanager.dto.UserActionDTO;
import task.usermanager.dto.UserDTO;
import task.usermanager.service.UserService;

import static task.usermanager.config.WebSocketConstant.TOPIC_USERS;

@Controller
@MessageMapping("/v1")
@RequiredArgsConstructor
public class UserController {

    private final SimpMessagingTemplate messagingTemplate;

    private final UserService userService;

    @MessageMapping("/create")
    @SendTo(TOPIC_USERS + "/create")
    public UserDTO create(@Validated(UserDTO.Create.class) UserDTO user) {
        return userService.create(user);
    }

    @MessageMapping("/delete")
    @SendTo(TOPIC_USERS + "/delete")
    public MessageDTO<Long> delete(@Validated @Min(1) Long id, StompHeaderAccessor stompHeaderAccessor) {
        userService.delete(id, stompHeaderAccessor.getUser().getName());
        return new MessageDTO<>(id);
    }

    @MessageMapping("/update")
    @SendTo(TOPIC_USERS + "/update")
    public UserDTO update(@Validated(UserDTO.Update.class) UserDTO user) {
        return userService.update(user);
    }

    @MessageMapping("/find")
    @SendTo(TOPIC_USERS + "/find")
    public Page<UserDTO> find(PageRequestDTO pageRequestDTO, StompHeaderAccessor stompHeaderAccessor) {
        messagingTemplate.convertAndSend(TOPIC_USERS + "/action", UserActionDTO.builder()
                .user(stompHeaderAccessor.getUser().getName())
                .action("use request GET /users")
                .build());
        return userService.find(pageRequestDTO);
    }

    @MessageMapping("/deleteRange")
    @SendTo(TOPIC_USERS + "/deleteRange")
    public DeleteRangeDTO deleteRange(@Validated DeleteRangeDTO deleteRangeDTO, StompHeaderAccessor stompHeaderAccessor) {
        userService.deleteRange(deleteRangeDTO.getStartId(), deleteRangeDTO.getEndId(), stompHeaderAccessor.getUser().getName());
        return deleteRangeDTO;
    }
}
