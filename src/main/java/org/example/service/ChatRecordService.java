package org.example.service;

import org.example.bean.ChatRecord;
import org.example.ChatTypeEnum;

import java.util.List;

public interface ChatRecordService {

    /**
     * @Description: 保存用户和AI的聊天记录
     * @Author 风间影月
     * @param userName
     * @param message
     * @param chatType
     */
    public void saveChatRecord(String userName, String message, ChatTypeEnum chatType);

    /**
     * @Description: 查询用户和AI的历史聊天记录
     * @Author 风间影月
     * @param userName
     * @return List<ChatRecord>
     */
    public List<ChatRecord> getChatRecordList(String who);

}
