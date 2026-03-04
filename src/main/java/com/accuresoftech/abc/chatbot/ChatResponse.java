package com.accuresoftech.abc.chatbot;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatResponse {

    private String intent;

    private String reply;

    private Object data;

}