package com.accuresoftech.abc.chatbot;



import org.springframework.stereotype.Service;

import com.accuresoftech.abc.chatbot.ChatResponse;
import com.accuresoftech.abc.chatbot.ChatIntent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final IntentService intentService;
    private final FAQService faqService;
    private final AIService aiService;

    public ChatResponse handleMessage(String message) {

        ChatIntent intent = intentService.detectIntent(message);

        switch (intent) {

            case ABOUT_CRM:
                return ChatResponse.builder()
                        .intent(intent.name())
                        .reply(faqService.aboutCRM())
                        .build();

            case CRM_FEATURES:
                return ChatResponse.builder()
                        .intent(intent.name())
                        .reply(faqService.crmFeatures())
                        .build();

            case CRM_PRICING:
                return ChatResponse.builder()
                        .intent(intent.name())
                        .reply(faqService.pricing())
                        .build();

            case CONTACT_INFO:
                return ChatResponse.builder()
                        .intent(intent.name())
                        .reply(faqService.contactInfo())
                        .build();

            case SHOW_LEADS:
                return ChatResponse.builder()
                        .intent(intent.name())
                        .reply("You can manage leads from the Leads module in the CRM dashboard.")
                        .build();

            case SHOW_TASKS:
                return ChatResponse.builder()
                        .intent(intent.name())
                        .reply("Tasks are available in the Tasks module of the CRM.")
                        .build();

            case SHOW_CUSTOMERS:
                return ChatResponse.builder()
                        .intent(intent.name())
                        .reply("Customer records are available in the Customers module.")
                        .build();

            default:

                String aiReply = aiService.askAI(message);

                return ChatResponse.builder()
                        .intent("AI_RESPONSE")
                        .reply(aiReply)
                        .build();
        }
    }

}