package com.nelioalves.cursomc.services;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {
	
	@Autowired
	private TemplateEngine templateEngine;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	//pega o email de envio padrão do arquivo application.properties
	@Value("${default.sender}")
	private String sender;
	
	//metodo da interface
	@Override
	public void sendOrderConfirmationEmail(Pedido obj) {
		SimpleMailMessage sm = prepareSimpleMailMessageFromPedido(obj);
		sendEmail(sm);//metodo da interface / padrão template/vai ser implementado
		//pelas implementações da interface AbstractEmailService que é MockEmailService
	}
	//pode ser acessados por subclasses
	protected SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido obj) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(obj.getCliente().getEmail());//p cliente que fez o pedido
		sm.setFrom(sender);//remetente do email
		sm.setSubject("Pedido confirmado! Código: " + obj.getId());//assunto do email
		sm.setSentDate(new Date(System.currentTimeMillis()));// pega data do servidor
		sm.setText(obj.toString());//corpo do email do metodo toString na classe Pedido
		return sm;
	}
	
	//responsável por retornar o HTML preenchido com os dados de um pedido, a partir do template Thymeleaf
	//confirmacaoPedido.html
	protected String htmlFromTemplatePedido(Pedido obj) {
		//é o context do thymeleaf
		Context context = new Context();
		//vai enviar o objeto Pedido para o html para popula-lo
		context.setVariable("pedido", obj);
		//processa o template para retornar o html
		return templateEngine.process("email/confirmacaoPedido", context);
		
	}
	
	@Override
	public void sendOrderConfirmationHtmlEmail(Pedido obj) {
		MimeMessage mm;
		try {
			//tenta enviar o email via html e caso cair no catch  envia o email normal
			mm = prepareSimpleMimeMessageFromPedido(obj);
			sendHtmlEmail(mm);
		} catch (MessagingException e) {
			sendOrderConfirmationEmail(obj);
		}
		
	}
	protected MimeMessage prepareSimpleMimeMessageFromPedido(Pedido obj) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mmh = new MimeMessageHelper(mimeMessage, true);
		mmh.setTo(obj.getCliente().getEmail());//p cliente que fez o pedido
		mmh.setFrom(sender);//remetente do email
		mmh.setSubject("Pedido confirmado! Código: " + obj.getId());//assunto do email
		mmh.setSentDate(new Date(System.currentTimeMillis()));// pega data do servidor
		mmh.setText(htmlFromTemplatePedido(obj),true);//corpo do emai processado a partir do tamplate thymeleaf, true = é um html
		return mimeMessage;
		
	}
	
	//envia a nova senha para o cliente
	@Override
	public void sendNewPasswordEmail(Cliente cliente, String newPass) {
		SimpleMailMessage sm = prepareNewPasswordEmail(cliente, newPass );
		sendEmail(sm);
		
	}
	
	//prepara o email da nova senha
	private SimpleMailMessage prepareNewPasswordEmail(Cliente cliente, String newPass) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(cliente.getEmail());//p cliente que fez o pedido
		sm.setFrom(sender);//remetente do email
		sm.setSubject("Solicitação de nova senha");//assunto do email
		sm.setSentDate(new Date(System.currentTimeMillis()));// pega data do servidor
		sm.setText("Nova senha: "+ newPass);
		return sm;
	}
	

}
