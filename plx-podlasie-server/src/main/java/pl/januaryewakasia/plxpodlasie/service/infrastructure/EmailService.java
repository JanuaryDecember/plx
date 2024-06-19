package pl.januaryewakasia.plxpodlasie.service.infrastructure;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.januaryewakasia.plxpodlasie.model.Listing;

import java.util.List;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendEmail(String to, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }

    @Async
    public void sendCategoryRemovalEmail(List<Listing> listings) throws MessagingException {
        for (Listing listing : listings) {
          sendEmail(listing.getUser().getEmail(), "Category removal", String.format("Your listing with id %d, was removed due to category changes.\nHere is data that we gathered from your listing:\nName: %s\nDesc: %s\nImage: %s", listing.getId(), listing.getName(), listing.getDescription(), listing.getImage()));
        }
    }
}
