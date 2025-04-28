package com.example.workerManagers.domain.chat.controller;

import com.example.workerManagers.domain.chat.dto.ChatMessageDto;
import com.example.workerManagers.domain.chat.dto.ChatRoomCreateDto;
import com.example.workerManagers.domain.chat.dto.ChatRoomDto;
import com.example.workerManagers.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessageDto chatMessage, 
                          Authentication authentication) {
        ChatMessageDto savedMessage = chatService.saveAndSendMessage(chatMessage, authentication.getName());
        messagingTemplate.convertAndSend("/topic/chat." + chatMessage.getChatRoomId(), savedMessage);
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomDto>> getMyChatRooms(Authentication authentication) {
        return ResponseEntity.ok(chatService.getMyChatRooms(authentication.getName()));
    }

    @GetMapping("/rooms/recruiter")
    public ResponseEntity<List<ChatRoomDto>> getRecruiterChatRooms(Authentication authentication) {
        return ResponseEntity.ok(chatService.getRecruiterChatRooms(authentication.getName()));
    }

    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<?> getChatMessages(
            @PathVariable(required = false) Long roomId,
            Authentication authentication) {
        if (roomId == null || roomId <= 0) {
            return ResponseEntity.ok(Map.of(
                "messages", List.of(),
                "message", "채팅방을 선택해주세요."
            ));
        }

        List<ChatMessageDto> messages = chatService.getChatMessages(roomId, authentication.getName());
        
        if (messages.isEmpty()) {
            return ResponseEntity.ok(Map.of(
                "messages", messages,
                "message", "메시지가 없습니다."
            ));
        }
        
        return ResponseEntity.ok(Map.of(
            "messages", messages
        ));
    }

    @PostMapping("/rooms")
    public ResponseEntity<ChatRoomDto> createChatRoom(
            @RequestBody ChatRoomCreateDto createDto,
            Authentication authentication) {
        return ResponseEntity.ok(chatService.createChatRoom(createDto, authentication.getName()));
    }

    @PutMapping("/rooms/{roomId}/read")
    public ResponseEntity<Void> markMessagesAsRead(
            @PathVariable Long roomId,
            Authentication authentication) {
        chatService.markMessagesAsRead(roomId, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<Void> leaveChatRoom(
            @PathVariable Long roomId,
            Authentication authentication) {
        chatService.leaveChatRoom(roomId, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Void> deleteMessage(
            @PathVariable Long messageId,
            Authentication authentication) {
        chatService.deleteMessage(messageId, authentication.getName());
        return ResponseEntity.ok().build();
    }
} 