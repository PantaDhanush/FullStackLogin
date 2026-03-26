package com.example.login.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendWelcomeEmail(String toEmail, String name) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("🎉 Welcome to Our Platform!");

            String content = """
                <html>
                <body style="font-family: Arial, sans-serif; background-color:#f4f4f4; padding:20px;">
                    <div style="max-width:600px; margin:auto; background:white; padding:20px; border-radius:10px;">
                        <h2 style="color:#4CAF50;">Welcome, %s! 🎉</h2>
                        <p>We're excited to have you on board.</p>
                        
                        <p>Thank you for registering with us. You can now explore all features of our platform.</p>
                        
                        <br>
                        <p style="color:#555;">Regards,<br><b>Auth Team</b></p>
                    </div>
                </body>
                </html>
                """.formatted(name);

            helper.setText(content, true); // ✅ true = HTML

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Unable to send welcome email", e);
        }
    }

    public void sendResetOtpEmail(String toEmail, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Password Reset OTP");

            String content = """
                <html>
                <body style="font-family: Arial, sans-serif; background-color:#f4f4f4; padding:20px;">
                    <div style="max-width:600px; margin:auto; background:white; padding:20px; border-radius:10px; text-align:center;">
                        
                        <h2 style="color:#333;">Reset Your Password</h2>
                        <p>Use the OTP below to reset your password:</p>
                        
                        <div style="font-size:28px; font-weight:bold; color:#4CAF50; margin:20px 0;">
                            %s
                        </div>

                        <p style="color:#888;">This OTP is valid for <b>5 minutes</b>.</p>

                        <br>
                        <p style="font-size:12px; color:#aaa;">
                            If you didn’t request this, please ignore this email.
                        </p>

                        <p style="color:#555;">Regards,<br><b>Auth Team</b></p>
                    </div>
                </body>
                </html>
                """.formatted(otp);

            helper.setText(content, true);

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Unable to send OTP email", e);
        }
    }

    public void sendOtpEmail(String toEmail, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Account Verification OTP");

            String content = """
            <html>
            <body style="font-family: Arial, sans-serif; background-color:#f4f4f4; padding:20px;">
                <div style="max-width:600px; margin:auto; background:white; padding:20px; border-radius:10px; text-align:center;">
                    
                    <h2 style="color:#333;">Verify Your Account</h2>
                    <p>Use the OTP below to verify your email address:</p>
                    
                    <div style="font-size:28px; font-weight:bold; color:#2196F3; margin:20px 0;">
                        %s
                    </div>

                    <p style="color:#888;">This OTP is valid for <b>5 minutes</b>.</p>

                    <br>
                    <p style="font-size:12px; color:#aaa;">
                        If you didn’t create this account, please ignore this email.
                    </p>

                    <p style="color:#555;">Regards,<br><b>Auth Team</b></p>
                </div>
            </body>
            </html>
            """.formatted(otp);

            helper.setText(content, true); // HTML enabled

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Unable to send verification OTP email", e);
        }
    }
}