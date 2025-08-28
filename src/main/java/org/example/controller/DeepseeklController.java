package org.example.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.bean.ChatEntity;
import org.example.bean.ChatRecord;
import org.example.service.ChatRecordService;
import org.example.service.DeepseekService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/deepseek")
public class DeepseeklController {

    @Resource
    private DeepSeekChatModel chatModel;
    // 聊天客户端实例，用于与AI模型进行交互
    @Resource
    private ChatClient chatClient;

    @Resource
    private DeepseekService service;
    @Resource
    private ChatRecordService chatRecordService;
/*    DeepseeklController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }*/

    /**
     * 构造函数，通过依赖注入获取ChatClient.Builder
     * 并配置默认系统提示词后构建ChatClient实例
     *
     * @param builder ChatClient的构建器对象，由Spring容器自动注入
     */
/*    public DeepseeklController(ChatClient.Builder builder) {
        // 配置AI的系统角色提示词："你是一个AI智能应用"
        // 系统提示词用于定义AI的行为模式和角色定位
        this.chatClient = builder.defaultSystem("你是一个AI智能应用").build();
    }*/
    @GetMapping("/ai/chat")
    public Object deepseekChat(@RequestParam String msg) {
        String call = chatModel.call(msg);
        return call;
    }


    @GetMapping("/ai/stream")
    public Flux<ChatResponse> deepseekStream(@RequestParam String msg) {
        Prompt prompt = new Prompt(msg);
        Flux<ChatResponse> stream = chatModel.stream(prompt);
        return stream;
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

    @GetMapping("/ai/stream2")
    public String deepseekStream2(@RequestParam String msg) {
        String content = chatClient.prompt()
                .system("你的名字叫电风扇，是全世界最有名的医生，看病无数，阅人无数，知道什么病该怎么治，也知道病人向你提出任何身体异象你所能推断的病是什么。")
                .call()
                .content();

        return content;
    }


    @PostMapping("/ai/v3/doctor/stream")
    public List<String> deepseekv3DoctorStream3(@RequestBody ChatEntity chatEntity) {
        service.deepseekv3DoctorStream3(chatEntity.getCurrentUserName(), chatEntity.getMessage());
        return null;
    }


    @GetMapping("/getRecords")
    public List<ChatRecord> getRecords(@RequestParam String who) {
        return chatRecordService.getChatRecordList(who);
    }

}
