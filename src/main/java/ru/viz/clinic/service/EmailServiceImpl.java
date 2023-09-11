package ru.viz.clinic.service;

import com.vaadin.flow.component.UI;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.viz.clinic.data.entity.Engineer;
import ru.viz.clinic.help.Helper;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class EmailServiceImpl {
    private final JavaMailSender emailSender;
    private final String fromMailAddress;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static final String NEW_ORDER_MESSAGE = "<h2><span style=\"font-size:14px\"><strong>Уважаемый %s,&nbsp;</strong></span></h2>\n" +
            "\n" +
            "<p><span style=\"font-size:14px\">сообщаем вам о поступлении новой заявки на ваше имя</span></p>\n" +
            "\n" +
            "<p><span style=\"font-size:14px\"><em>С уважением </em></span></p>\n" +
            "\n" +
            "<p><span style=\"font-size:14px\"><em>Сергей Вицентович</em></span></p>\n" +
            "\n" +
            "<p>&nbsp;</p>";
    private static final String SUCCESS_SEND_EMAIL_MESSAGE = "сообщение отправлено на адрес %s";
    private static final String ERROR_SEND_EMAIL_MESSAGE = "ошибка при отправки сообщения на адрес %s";
    private static final String NEW_ORDER_SUBJECT = "Новая заявка";

    public EmailServiceImpl(
            final JavaMailSender emailSender,
            @Value("${viz-clinic.mail-user}") final String fromMailAddress
    ) {
        this.emailSender = emailSender;
        this.fromMailAddress = fromMailAddress;
    }

    public void sendOrderCreatedMessages(
            final Set<Engineer> engineerList,
            final UI ui
    ) {
        engineerList.forEach(engineer -> executorService.submit(() -> sendOrderCreatedMessage(engineer, ui)));
    }

    private void sendOrderCreatedMessage(
            @NotNull final Engineer engineer,
            @NotNull final UI ui
    ) {
        final MimeMessage message = emailSender.createMimeMessage();
        try {
            message.setFrom(new InternetAddress(fromMailAddress));
            message.setRecipients(MimeMessage.RecipientType.TO, engineer.getEmail());
            message.setSubject(NEW_ORDER_SUBJECT);
            message.setContent(String.format(NEW_ORDER_MESSAGE, engineer.getEntityName()), "text/html; charset=utf-8");
            emailSender.send(message);
            ui.access(() -> Helper.showSuccessNotification(String.format(SUCCESS_SEND_EMAIL_MESSAGE, engineer.getEmail())));
            log.info("send order create to email  {}", engineer.getEmail());
        } catch (final MailException | MessagingException e) {
            ui.access(() -> Helper.showErrorNotification(String.format(ERROR_SEND_EMAIL_MESSAGE, engineer.getEmail())));
            log.error("error sending engineer id {} with email {}", engineer.getId(), engineer.getEmail(), e);
        }
    }
}