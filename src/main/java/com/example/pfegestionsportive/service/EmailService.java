package com.example.pfegestionsportive.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String code) {
        String subject = " Vérification de votre email - PFE Sportive";
        String message = String.join("\n",
                "Bonjour,",
                "",
                "Merci de vous être inscrit sur PFE Gestion Sportive.",
                "",
                "Votre code de vérification est:",
                "==============================",
                "        " + code,
                "==============================",
                "",
                "Ce code expire dans 24 heures.",
                "",
                "Si vous n'avez pas créé de compte, ignorez cet email.",
                "",
                "Cordialement,",
                "L'équipe PFE Gestion Sportive"
        );

        sendEmail(to, subject, message);
    }

    public void sendPasswordResetEmail(String to, String code) {
        String subject = " Réinitialisation de votre mot de passe - PFE Sportive";
        String message = String.join("\n",
                "Bonjour,",
                "",
                "Vous avez demandé une réinitialisation de mot de passe.",
                "",
                "Votre code de réinitialisation est:",
                "==============================",
                "        " + code,
                "==============================",
                "",
                "Ce code expire dans 1 heure.",
                "",
                "Si vous n'avez pas demandé cette réinitialisation,",
                "ignorez cet email et votre mot de passe restera inchangé.",
                "",
                "Cordialement,",
                "L'équipe PFE Gestion Sportive"
        );

        sendEmail(to, subject, message);
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("bakiryasmine2@gmail.com");

        mailSender.send(message);
    }
}