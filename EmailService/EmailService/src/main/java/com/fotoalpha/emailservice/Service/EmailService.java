package com.fotoalpha.emailservice.Service;

import com.fotoalpha.emailservice.KafkaEvents.AppointmentCreatedEvent;
import com.fotoalpha.emailservice.KafkaEvents.GalleryUpdatedEvent;
import com.fotoalpha.emailservice.KafkaEvents.SendMailEvent;
import com.fotoalpha.emailservice.SendMeEmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${mail.sender.private.email}")
    String mailSenderPrivateEmail;

    public String sendMeMail(SendMeEmailRequest sendMeEmailRequest) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        String htmlContent = """
                                <html>
                                        <head>
                                            <meta charset="UTF-8">
                                        </head>
                                        <body style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; color: #333; background-color: #f9f9f9; padding: 20px;">
                                               <div style="max-width: 600px; margin: 0 auto; background: #ffffff; border-radius: 10px; overflow: hidden; border: 1px solid #eee; box-shadow: 0 4px 10px rgba(0,0,0,0.05);">
                                               		<div style="background-color: #2c3e50; color: #ffffff; padding: 30px; text-align: center;">
                                                   	 	<h1 style="margin: 0; font-size: 24px; letter-spacing: 2px; text-transform: uppercase;">FotoAlpha</h1>
                                                    	<p style="margin: 10px 0 0 0; opacity: 0.8;">Üzenet</p>
                                               		</div>
                                                    <div style="padding: 30px">
                                                        <p><strong>Kedves</strong> Martin!</p>
                                                        <p><strong>%s</strong> küldött egy üzenetet:</p>
                                                        <div style="background-color: whitesmoke; width: 100%%; height: 150px; overflow: hidden; padding: px; border-radius:10px; word-wrap:break-word;overflow-wrap: break-word;">
                                                        	<p style="margin:10px; display:block; "><i>%s</i></p>
                                                        </div>
                                                        <p><strong>%s</strong> emailcíme: %s</p>
                                                    </div>
                                               </div>
                                        </body>
                                        </html>
        """.formatted(sendMeEmailRequest.getFirstName(), sendMeEmailRequest.getMessage(), sendMeEmailRequest.getFirstName(),sendMeEmailRequest.getUserEmail());
        mimeMessageHelper.setTo(mailSenderPrivateEmail);
        mimeMessageHelper.setText( htmlContent, true);
        mimeMessageHelper.setSubject("Egy felhasználó üzenetet küldött! - FotoAlpha");

        mailSender.send(mimeMessage);
        return "Email sikeresen elküldve!";

    }

    public String sendAppointmentCreatedEmail(AppointmentCreatedEvent event) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        String finalLocation = (event.pairLocations() == null || event.pairLocations().isEmpty())
                ? "Budapest " + event.postalCode() + " " + event.streetName() + " " + event.streetType() + " " + event.houseNumber()
                : event.pairLocations();
        String htmlContent = """
                <!DOCTYPE html>
                     <html>
                     <head>
                         <meta charset="UTF-8">
                     </head>
                     <body style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; color: #333; background-color: #f9f9f9; padding: 20px;">
                         <div style="max-width: 600px; margin: 0 auto; background: #ffffff; border-radius: 10px; overflow: hidden; border: 1px solid #eee; box-shadow: 0 4px 10px rgba(0,0,0,0.05);">
                             <div style="background-color: #2c3e50; color: #ffffff; padding: 30px; text-align: center;">
                                 <h1 style="margin: 0; font-size: 24px; letter-spacing: 2px; text-transform: uppercase;">FotoAlpha</h1>
                                 <p style="margin: 10px 0 0 0; opacity: 0.8;">Időpontfoglalásod rögzítve</p>
                             </div>
                             <div style="padding: 30px;">
                                 <p>Kedves <strong>Martin</strong>!</p>
                                 <p><strong>%s %s</strong> imént adott le egy új foglalást!</p>
                                 <h3 style="border-bottom: 2px solid #f1c40f; padding-bottom: 10px; color: #2c3e50;">Foglalási adatok</h3>
                                 <table style="width:100%%; border-collapse: collapse; margin-top: 20px; justify-self: center;">
                                     <tr style="background-color: #f8f9fa;">
                                         <td style="padding: 12px; border: 1px solid #eee; font-weight: bold; width: 40%%;">Foglalás azonosító:</td>
                                         <td style="padding: 12px; border: 1px solid #eee;">%s</td>
                                     </tr>
                                     <tr style="background-color: #f8f9fa;">
                                         <td style="padding: 12px; border: 1px solid #eee; font-weight: bold;">Időpont:</td>
                                         <td style="padding: 12px; border: 1px solid #eee;">%s, %s</td>
                                     </tr>
                                     <tr>
                                         <td style="padding: 12px; border: 1px solid #eee; font-weight: bold;">Helyszín:</td>
                                         <td style="padding: 12px; border: 1px solid #eee;">%s</td>
                                     </tr>
                                     <tr style="background-color: #f8f9fa;">
                                         <td style="padding: 12px; border: 1px solid #eee; font-weight: bold;">Csomag:</td>
                                         <td style="padding: 12px; border: 1px solid #eee;">%s</td>
                                     </tr>
                                     <tr>
                                         <td style="padding: 12px; border: 1px solid #eee; font-weight: bold; color: #e67e22;">Ár:</td>
                                         <td style="padding: 12px; border: 1px solid #eee; font-weight: bold; color: #e67e22;">%s Ft</td>
                                     </tr>
                                     <table style="width:100%%; max-width:100%%; border-collapse: collapse; table-layout: fixed; margin-top: 20px;">
                                         <tr style="background-color: #f8f9fa;">
                                             <td style="padding: 12px; border: 1px solid #eee; font-weight: bold; width: 40%%;">Teljes név:</td>
                                             <td style="padding: 12px; border: 1px solid #eee;">%s %s</td>
                                         </tr>
                                         <tr>
                                             <td style="padding: 12px; border: 1px solid #eee; font-weight: bold;">Telefonszám:</td>
                                             <td style="padding: 12px; border: 1px solid #eee;">%s</td>
                                         </tr>
                                         <tr style="background-color: #f8f9fa;">
                                             <td style="padding: 12px; border: 1px solid #eee; font-weight: bold;">Email:</td>
                                             <td style="padding: 12px; border: 1px solid #eee; word-break: break-word; overflow-wrap: break-word;">%s</td>
                                         </tr>
                                         <tr>
                                             <td style="padding: 12px; border: 1px solid #eee; font-weight: bold;">Foglalás:</td>
                                             <td style="padding: 12px; border: 1px solid #eee;">%s</td>
                                         </tr>
                                     </table>
                             </div>
                             <div style="background-color: #f4f4f4; padding: 20px; text-align: center; font-size: 12px; color: #777;">
                                 <p>© 2026 FotoAlpha - Csihar Martin</p>
                             </div>
                         </div>
                     </body>
                     </html>
                """.formatted(
                event.lastName(), event.firstName(),
                event.appointmentId(),
                event.appointmentDate(), event.appointmentTime(),
                finalLocation,
                event.bundleName(),
                event.price(),
                event.lastName(), event.firstName(),
                event.phoneNumber(),
                event.userEmail(),
                event.orderDate()
        );
        helper.setTo(event.userEmail());
        helper.setText(htmlContent, true);
        helper.setSubject("Sikeres foglalás - FotoAlpha");
        mailSender.send(mimeMessage);
        sendAppointmentCreatedForAdmin(event);
        return "OK";
    }

    private void sendAppointmentCreatedForAdmin(AppointmentCreatedEvent event) throws MessagingException {
        MimeMessage mimeMessageForAdmin = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessageForAdmin, true, "UTF-8");

        String finalLocation = (event.pairLocations() == null || event.pairLocations().isEmpty())
                ? "Budapest " + event.postalCode() + " " + event.streetName() + " " + event.streetType() + " " + event.houseNumber()
                : event.pairLocations();
        String htmlContent = """
                                <!DOCTYPE html>
                     <html>
                     <head>
                         <meta charset="UTF-8">
                     </head>
                     <body style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; color: #333; background-color: #f9f9f9; padding: 20px;">
                         <div style="max-width: 600px; margin: 0 auto; background: #ffffff; border-radius: 10px; overflow: hidden; border: 1px solid #eee; box-shadow: 0 4px 10px rgba(0,0,0,0.05);">
                             <div style="background-color: #2c3e50; color: #ffffff; padding: 30px; text-align: center;">
                                 <h1 style="margin: 0; font-size: 24px; letter-spacing: 2px; text-transform: uppercase;">FotoAlpha</h1>
                                 <p style="margin: 10px 0 0 0; opacity: 0.8;">Időpontfoglalásod rögzítve</p>
                             </div>
                             <div style="padding: 30px;">
                                 <p>Kedves <strong>Martin</strong>!</p>
                                 <p><strong>%s %s</strong> imént adott le egy új foglalást!</p>
                                 <h3 style="border-bottom: 2px solid #f1c40f; padding-bottom: 10px; color: #2c3e50;">Foglalási adatok</h3>
                                 <table style="width:100%%; border-collapse: collapse; margin-top: 20px; justify-self: center;">
                                     <tr style="background-color: #f8f9fa;">
                                         <td style="padding: 12px; border: 1px solid #eee; font-weight: bold; width: 40%%;">Foglalás azonosító:</td>
                                         <td style="padding: 12px; border: 1px solid #eee;">%s</td>
                                     </tr>
                                     <tr style="background-color: #f8f9fa;">
                                         <td style="padding: 12px; border: 1px solid #eee; font-weight: bold;">Időpont:</td>
                                         <td style="padding: 12px; border: 1px solid #eee;">%s, %s</td>
                                     </tr>
                                     <tr>
                                         <td style="padding: 12px; border: 1px solid #eee; font-weight: bold;">Helyszín:</td>
                                         <td style="padding: 12px; border: 1px solid #eee;">%s</td>
                                     </tr>
                                     <tr style="background-color: #f8f9fa;">
                                         <td style="padding: 12px; border: 1px solid #eee; font-weight: bold;">Csomag:</td>
                                         <td style="padding: 12px; border: 1px solid #eee;">%s</td>
                                     </tr>
                                     <tr>
                                         <td style="padding: 12px; border: 1px solid #eee; font-weight: bold; color: #e67e22;">Ár:</td>
                                         <td style="padding: 12px; border: 1px solid #eee; font-weight: bold; color: #e67e22;">%s Ft</td>
                                     </tr>
                                     <table style="width:100%%; max-width:100%%; border-collapse: collapse; table-layout: fixed; margin-top: 20px;">
                                         <tr style="background-color: #f8f9fa;">
                                             <td style="padding: 12px; border: 1px solid #eee; font-weight: bold; width: 40%%;">Teljes név:</td>
                                             <td style="padding: 12px; border: 1px solid #eee;">%s %s</td>
                                         </tr>
                                         <tr>
                                             <td style="padding: 12px; border: 1px solid #eee; font-weight: bold;">Telefonszám:</td>
                                             <td style="padding: 12px; border: 1px solid #eee;">%s</td>
                                         </tr>
                                         <tr style="background-color: #f8f9fa;">
                                             <td style="padding: 12px; border: 1px solid #eee; font-weight: bold;">Email:</td>
                                             <td style="padding: 12px; border: 1px solid #eee; word-break: break-word; overflow-wrap: break-word;">%s</td>
                                         </tr>
                                         <tr>
                                             <td style="padding: 12px; border: 1px solid #eee; font-weight: bold;">Foglalás:</td>
                                             <td style="padding: 12px; border: 1px solid #eee;">%s</td>
                                         </tr>
                                     </table>
                             </div>
                             <div style="background-color: #f4f4f4; padding: 20px; text-align: center; font-size: 12px; color: #777;">
                                 <p>© 2026 FotoAlpha - Csihar Martin</p>
                             </div>
                         </div>
                     </body>
                     </html>
                """.formatted(
                event.lastName(), event.firstName(),
                event.appointmentId(),
                event.appointmentDate(), event.appointmentTime(),
                finalLocation,
                event.bundleName(),
                event.price(),
                event.lastName(), event.firstName(),
                event.phoneNumber(),
                event.userEmail(),
                event.orderDate()
        );
        helper.setTo(mailSenderPrivateEmail);
        helper.setText(htmlContent, true);
        helper.setSubject("Új foglalás érkezett! - FotoAlpha");
        mailSender.send(mimeMessageForAdmin);
    }

    public void sendPwResetEmail(SendMailEvent event) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        String passwordResetUrl = "https://www.fotoalpha.hu/passwordReset?token="+event.resetID()+"&email="+event.email();
        String htmlContent = """
                <html>
                <head>
                    <meta charset="UTF-8">
                </head>
                <body style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; color: #333; background-color: #f9f9f9; padding: 20px;">
                       <div style="max-width: 600px; margin: 0 auto; background: #ffffff; border-radius: 10px; overflow: hidden; border: 1px solid #eee; box-shadow: 0 4px 10px rgba(0,0,0,0.05);">
                       		<div style="background-color: #2c3e50; color: #ffffff; padding: 30px; text-align: center;">
                           	 	<h1 style="margin: 0; font-size: 24px; letter-spacing: 2px; text-transform: uppercase;">FotoAlpha</h1>
                            	<p style="margin: 10px 0 0 0; opacity: 0.8;">Elfelejtett jelszó</p>
                       		</div>
                            <div style="padding: 30px">
                                <p><strong>Elfelejtetted</strong> a jelszavad? Sebaj, erre a linkre kattintva azonnal megváltoztathatod!</p>
                                <div style="padding: 4px; border-radius:20px; background-color: whitesmoke; width: 100%%; height: 100%%; text-align: center; ">
                                	<p style="color: blue;">%s</p>
                                </div>
                            </div>
                       </div>
                </body>
                </html>
                """.formatted(passwordResetUrl);

        helper.setTo(event.email());
        helper.setSubject("Jelszó visszaállítás - FotoAlpha");
        helper.setText(htmlContent, true);
        mailSender.send(mimeMessage);
    }

    public void sendGalleryUpdatedEvent(GalleryUpdatedEvent event) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        String htmlContent = """
                                <html>
                                        <head>
                                            <meta charset="UTF-8">
                                        </head>
                                        <body style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; color: #333; background-color: #f9f9f9; padding: 20px;">
                                               <div style="max-width: 600px; margin: 0 auto; background: #ffffff; border-radius: 10px; overflow: hidden; border: 1px solid #eee; box-shadow: 0 4px 10px rgba(0,0,0,0.05);">
                                               		<div style="background-color: #2c3e50; color: #ffffff; padding: 30px; text-align: center;">
                                                   	 	<h1 style="margin: 0; font-size: 24px; letter-spacing: 2px; text-transform: uppercase;">FotoAlpha</h1>
                                                    	<p style="margin: 10px 0 0 0; opacity: 0.8;">Galéria állapotváltozás</p>
                                               		</div>
                                                    <div style="padding: 30px">
                                                        <p><strong>Kedves</strong> %s!</p>
                                                        <p><strong>Martin</strong> az imént töltötte fel galériád <strong>%s db</strong> képpel, és <strong>%s db</strong> videóval!</p>
                                                    </div>
                                               </div>
                                        </body>
                                        </html>
        """.formatted(event.firstName(),event.photoCount(),event.videoCount());
        helper.setSubject("Frissült a galériád! - FotoAlpha");
        helper.setTo(event.email());
        helper.setText(htmlContent, true);
        mailSender.send(mimeMessage);
    }
}
