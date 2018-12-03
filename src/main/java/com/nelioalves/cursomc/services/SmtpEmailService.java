package com.nelioalves.cursomc.services;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class SmtpEmailService extends AbstractEmailService  {
	
	// o spring vai instanciar o mailSender como todos os dados do application-dev.properties
	//#implementando smtpEmailService do Goggle
	@Autowired
	private MailSender mailSender;
	

	@Autowired
	private JavaMailSender javaMailSender;
	
	//mostra no log do servidor
	//Logger referente a esta classe
	//static para ser usado um para todos
	private static final Logger LOG = LoggerFactory.getLogger(SmtpEmailService.class);

	@Override
	public void sendEmail(SimpleMailMessage msg) {
		LOG.info("Enviando email.....");
		mailSender.send(msg);
		LOG.info("Email envidado");
		
	}

	@Override
	public void sendHtmlEmail(MimeMessage msg) {
		LOG.info("Enviando email.....");
		javaMailSender.send(msg);
		LOG.info("Email envidado");
		
	}

}
