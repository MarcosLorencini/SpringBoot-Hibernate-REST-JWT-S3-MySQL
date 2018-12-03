package com.nelioalves.cursomc.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class SmtpEmailService extends AbstractEmailService  {
	
	// o spring vai instanciar o mailSender como todos os dados do application-dev.properties
	//#implementando smtpEmailService do Goggle
	@Autowired
	private MailSender mailSender;
	
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

}
