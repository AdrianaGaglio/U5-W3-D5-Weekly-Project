package epicode.it.events.utils.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class EmailSvc {

    @Autowired
    private JavaMailSender mailSender;

    public String sendEmail(@Valid EmailRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(request.getFrom());
        message.setTo(request.getTo());
        if (request.getCc() != null)
            message.setCc(request.getCc());

        if (request.getReplyTo() != null)
            message.setReplyTo(request.getReplyTo());

        message.setSubject(request.getSubject());
        message.setText(request.getBody());
        mailSender.send(message);
        return "Message sent successfully";
    }

    public String sendEmailHtml(@Valid EmailRequest request) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            //SimpleMailMessage message = new SimpleMailMessage();

            helper.setFrom(request.getFrom());
            helper.setTo(request.getTo());
            if (request.getCc() != null)
                helper.setCc(request.getCc());

            if (request.getReplyTo() != null)
                helper.setReplyTo(request.getReplyTo());

            helper.setSubject(request.getSubject());
            helper.setText(request.getBody(), true);

            mailSender.send(message);
            return "Message sent successfully";

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
}
