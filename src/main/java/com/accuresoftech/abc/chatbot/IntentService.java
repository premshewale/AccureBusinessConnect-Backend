package com.accuresoftech.abc.chatbot;


import org.springframework.stereotype.Service;

import com.accuresoftech.abc.chatbot.ChatIntent;

@Service
public class IntentService {

    public ChatIntent detectIntent(String message) {

        String msg = message.toLowerCase();

        if (msg.contains("what is crm") || msg.contains("about crm"))
            return ChatIntent.ABOUT_CRM;

        if (msg.contains("features"))
            return ChatIntent.CRM_FEATURES;

        if (msg.contains("price") || msg.contains("cost") || msg.contains("pricing"))
            return ChatIntent.CRM_PRICING;

        if (msg.contains("contact") || msg.contains("support"))
            return ChatIntent.CONTACT_INFO;

        if (msg.contains("lead"))
            return ChatIntent.SHOW_LEADS;

        if (msg.contains("task"))
            return ChatIntent.SHOW_TASKS;

        if (msg.contains("customer"))
            return ChatIntent.SHOW_CUSTOMERS;

        return ChatIntent.UNKNOWN;
    }

}