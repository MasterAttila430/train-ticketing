package com.siemens.train.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Sends booking confirmation to customer
    public void sendBookingConfirmation(String to, String trainName,
                                        String departure, String arrival,
                                        int seats) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Booking Confirmation - " + trainName);
        message.setText(
                "Dear Customer,\n\n"
                        + "Your booking has been confirmed!\n\n"
                        + "Train: " + trainName + "\n"
                        + "From: " + departure + "\n"
                        + "To: " + arrival + "\n"
                        + "Seats: " + seats + "\n\n"
                        + "Thank you for travelling with us!"
        );
        send(message);
    }

    // Sends delay notification to affected passengers
    public void sendDelayNotification(String to, String trainName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Delay Notification - " + trainName);
        message.setText(
                "Dear Customer,\n\n"
                        + "We regret to inform you that train "
                        + trainName + " has been delayed.\n\n"
                        + "We apologize for the inconvenience."
        );
        send(message);
    }

    // Helper: sends the message and logs any errors
    private void send(SimpleMailMessage message) {
        try {
            mailSender.send(message);
            LOG.info("Email sent to {}", message.getTo());
        } catch (Exception e) {
            LOG.error("Failed to send email to {}: {}", message.getTo(), e.getMessage());
        }
    }
}