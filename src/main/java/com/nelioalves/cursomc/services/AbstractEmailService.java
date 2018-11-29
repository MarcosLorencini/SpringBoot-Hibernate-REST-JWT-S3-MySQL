package com.nelioalves.cursomc.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import com.nelioalves.cursomc.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {
	
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

}
