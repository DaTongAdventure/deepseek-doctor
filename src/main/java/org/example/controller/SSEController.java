package org.example.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.utils.SSEMsgType;
import org.example.utils.SSEServer;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/sse")
public class SSEController {

    @Resource
    private DeepSeekChatModel chatModel;


    @GetMapping(path = "/connect", produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public SseEmitter deepseekChat(@RequestParam String userId) {
        return SSEServer.connect(userId);
    }


    @GetMapping(path = "/sendMessage", produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public Object deepseekChat(@RequestParam String userId, @RequestParam String message) {
        SSEServer.sendMessage(userId, message, SSEMsgType.MESSAGE);
        return "ok";
    }

    @GetMapping(path = "/sendMessageAll", produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public Object sendMessageAll(@RequestParam String message) {
        SSEServer.sendMessageToAllUsers(message);
        return "ok";
    }


    @GetMapping(path = "/sendMessageAdd", produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public Object sendMessageAdd(@RequestParam String userId, @RequestParam String message) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            Thread.sleep(300);
            SSEServer.sendMessage(userId, message + "-" + i, SSEMsgType.ADD);
        }

        SSEServer.sendMessageToAllUsers(message);
        return "ok";
    }

    @GetMapping(path = "/stop", produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public Object stop(@RequestParam String userId) {
        SSEServer.stopServer(userId);
        return "ok";
    }

    @GetMapping(path = "/getOnlineCounts")
    public Object getOnlineCounts() {
        return SSEServer.getOnlineCounts();
    }


    @GetMapping("/ai/stream1")
    public List<String> deepseekStream1(@RequestParam String msg) {
        Prompt prompt = new Prompt(msg);
        Flux<ChatResponse> stream = chatModel.stream(prompt);
        List<String> list = stream.toStream().map(chatResponse ->
        {
            String text = chatResponse.getResult().getOutput().getText();
            log.info(text);
            return text;
        }).toList();
        return list;
    }

}
