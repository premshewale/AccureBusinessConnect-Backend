package com.accuresoftech.abc.chatbot;



import org.springframework.stereotype.Service;

@Service
public class FAQService {

    public String aboutCRM() {

        return """
Accure Business Connect (ABC CRM) is a modern Customer Relationship Management platform that helps businesses manage customers, track leads, assign tasks, handle support tickets, and monitor sales performance from a single dashboard.
""";
    }

    public String crmFeatures() {

        return """
ABC CRM provides:

• Customer & Contact Management
• Sales Pipeline & Lead Tracking
• Tasks & Reminders
• Support Tickets
• Invoices & Payments
• Reports & Dashboards
• Role-based Access Control
""";
    }

    public String pricing() {

        return """
ABC CRM pricing depends on organization size and required features.

Please contact our sales team for detailed pricing.
""";
    }

    public String contactInfo() {

        return """
Accure Softech Pvt. Ltd.

USA Office
8 The Green, Suite B
City of Dover, State of Delaware
Zip Code 19901

India Office
905, 9th Floor, Gera Imperium Rise
Wipro Circle, Rajiv Gandhi InfoTech Park – Phase 2
Hinjawadi, Pune 411057

Email: info@accuresoftech.com

HR: +91 8625044606
Sales: +91 8855991643
""";
    }

}