package com.arthur.tasktrackerapi.email;

public interface EmailService {
    void send (String to, String subject, String text);
}
