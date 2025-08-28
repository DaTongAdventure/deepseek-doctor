package org.example;

public enum ChatTypeEnum {
    USER("user", "用户发送的内容"),
    BOT("bot", "AI回复的内容");


    public final String type;
    public final String value;


    ChatTypeEnum(String type, String value) {
        this.type = type;
        this.value = value;

    }

}
