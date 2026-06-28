package com.modsProject.modsProject.server.controllers.creators;

import com.modsProject.modsProject.server.database.models.Creator;
import com.modsProject.modsProject.server.database.models.Message;
import com.modsProject.modsProject.server.database.models.Source;
import com.modsProject.modsProject.server.database.repositories.CreatorRepository;
import com.modsProject.modsProject.server.database.repositories.MessageRepository;
import com.modsProject.modsProject.server.database.repositories.SourceRepository;
import com.modsProject.modsProject.server.dto.MessageRequestDto;
import com.modsProject.modsProject.server.dto.MessageResponseDto;
import com.modsProject.modsProject.utils.web.HtmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class CreatorMessagesController {
    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @Autowired
    CreatorRepository creatorRepository;

    @Autowired
    SourceRepository sourceRepository;

    @Autowired
    MessageRepository messageRepository;

    @GetMapping("/creator/{id}/comments")
    public String getCreatorComments(@PathVariable(value = "id") Long id, Model model, Principal principal) {
        HtmlUtils.loadHeaderParams(model, principal, creatorRepository);
        HtmlUtils.loadCreatorParams(model, principal, id);

        Optional<Creator> creator = creatorRepository.findById(id);

        if (creator.isPresent()) {
            model.addAttribute("creator", creator.get());
        }
        else {
            model.addAttribute("title", "404 Not Found");
            model.addAttribute("message", "Error 404: Not Found");
            model.addAttribute("description", "Creator with id " + id + " is not found.");

            return "statuses/status";
        }

        List<Source> sources = sourceRepository.findByCreatorId(id);

        model.addAttribute("sources", sources);

        return "creatorComments";
    }

    @MessageMapping("/creator/{id}/comments.send")
    public void send(@DestinationVariable Long id, MessageRequestDto request, Principal principal) {
        Long sender_id = principal != null ? Long.parseLong(principal.getName()) : null;

        if (sender_id != null) {
            sender_id = sender_id >= 0 ? sender_id : null;
        }

        Message saved_message = messageRepository.save(new Message(request.getMessage(), sender_id, "creator", id));

        MessageResponseDto response = new MessageResponseDto(request.getMessage(), "Анонiм", "anonymous.png", null, LocalDateTime.now());

        if (saved_message.getSenderId() != null) {
            Optional<Creator> raw_creator = creatorRepository.findById(saved_message.getSenderId());

            if (raw_creator.isPresent()) {
                response.setSenderName(raw_creator.get().getName());
                response.setSenderImage(raw_creator.get().getImagePath());
                response.setSenderId(raw_creator.get().getId());
            }
        }

        messagingTemplate.convertAndSend("/topic/creator/"+id+"/comments", response);
    }

    @MessageMapping("/creator/{id}/{personal_id}/comments.join")
    public void join(@DestinationVariable Long id, @DestinationVariable String personal_id) {
        List<Message> messages = messageRepository.findByContextIdAndContextType(id, "creator");

        List<MessageResponseDto> responses = new ArrayList<>();

        for (Message message : messages) {
            MessageResponseDto response = new MessageResponseDto(message.getMessage(), "Анонiм", "anonymous.png", null, message.getDate());

            if (message.getSenderId() != null) {
                Optional<Creator> raw_creator = creatorRepository.findById(message.getSenderId());

                if (raw_creator.isPresent()) {
                    response.setSenderName(raw_creator.get().getName());
                    response.setSenderImage(raw_creator.get().getImagePath());
                    response.setSenderId(raw_creator.get().getId());
                }
            }

            responses.add(response);
        }

        messagingTemplate.convertAndSend("/queue/creator/"+id+"/comments/"+personal_id+"/history", responses);
    }
}
