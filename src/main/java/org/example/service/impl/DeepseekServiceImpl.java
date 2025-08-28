package org.example.service.impl;

import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.ChatTypeEnum;
import org.example.service.ChatRecordService;
import org.example.service.DeepseekService;
import org.example.utils.SSEMsgType;
import org.example.utils.SSEServer;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


@Slf4j
@Service
public class DeepseekServiceImpl implements DeepseekService {
    @Resource
    private ChatClient chatClient;

    @Resource
    private ChatRecordService chatRecordService;

    @Resource
    private DeepSeekChatModel chatModel;
    @Override
    public void deepseekv3DoctorStream3(String userName, String message) {
        chatRecordService.saveChatRecord(userName,message, ChatTypeEnum.USER);
        ChatClient.ChatClientRequestSpec prompt = chatClient.prompt();
        List<String> list = prompt.user(message).stream().chatClientResponse().toStream().map(chatClientResponse -> {
            String text = chatClientResponse.chatResponse().getResult().getOutput().getText();
            SSEServer.sendMessage(userName, text, SSEMsgType.ADD);
            return text;
        }).toList();
        System.out.println("list = " + JSON.toJSONString(list));

        // 保存AI回复的记录到数据库
        String htmlResult = "";
        for (String s : list) {
            htmlResult += s;
        }
        chatRecordService.saveChatRecord(userName,htmlResult, ChatTypeEnum.BOT);
/*        Flux<ChatResponse> chatResponseFlux = prompt.user(message).stream().chatResponse();
        List<String> list1 = chatResponseFlux.toStream().map(chatResponse -> {

            String text = chatResponse.getResult().getOutput().getText();
            log.info(text);
            SSEServer.sendMessage(userName, text, SSEMsgType.ADD);
            return text;
        }).toList();*/
        SSEServer.sendMessage(userName, "GG", SSEMsgType.ADD);
    }
}
