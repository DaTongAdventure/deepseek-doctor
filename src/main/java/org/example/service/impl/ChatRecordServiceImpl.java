package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.example.ChatTypeEnum;
import org.example.bean.ChatRecord;
import org.example.mapper.ChatRecordMapper;
import org.example.service.ChatRecordService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatRecordServiceImpl implements ChatRecordService {

    @Resource
    private ChatRecordMapper chatRecordMapper;

    @Override
    public void saveChatRecord(String userName, String message, ChatTypeEnum chatType) {

        ChatRecord chatRecord = new ChatRecord();
        chatRecord.setFamilyMember(userName);
        chatRecord.setContent(message);
        chatRecord.setChatType(chatType.type);
        chatRecord.setChatTime(LocalDateTime.now());

        chatRecordMapper.insert(chatRecord);
    }

    @Override
    public List<ChatRecord> getChatRecordList(String who) {
        QueryWrapper<ChatRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("family_member", who);

        return chatRecordMapper.selectList(queryWrapper);
    }
}
