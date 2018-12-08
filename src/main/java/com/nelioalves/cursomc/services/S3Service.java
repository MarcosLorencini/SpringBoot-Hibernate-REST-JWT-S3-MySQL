package com.nelioalves.cursomc.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.nelioalves.cursomc.service.exception.FileException;


//acessa os servicos do S3 da Amazon

@Service
public class S3Service {
	
	private Logger LOG = LoggerFactory.getLogger(S3Service.class);
	
	@Autowired
	AmazonS3 s3client;
	
	@Value("${s3.bucket}")
	private String bucketName;
	
	//bucketName nome o bucket no S3
	//"teste" nome do arquivo no S3
	//file caminho do aqruivo local
	
	//faz o upload(enviar) de arquivo para o S3
	//multipartFile este tipo de arquivo que o endpoint vai receber na requisicao 
	//URI retorna o endereco web do novo recurso que foi gerado
	public URI uploadFile(MultipartFile multipartFile) {
		try {
				//pega o nome do arquivo
				String fileName = multipartFile.getOriginalFilename();
				//encapsula a partir de uma origem(arquivo)
				InputStream is = multipartFile.getInputStream();
				//string do tipo do arquivo que foi enviado
				String contentType = multipartFile.getContentType();
				return uploadFile(is, fileName, contentType);
			} catch (IOException e) {
				throw new FileException("Erro de IO: " + e.getMessage());
			}
			
	}
			
		
	//sobrecarga do metodo acima
	//retorna a URI no objeto que foi gravado no S3
	public URI uploadFile(InputStream is, String fileName, String contentType) {
		try {
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentType(contentType);
			LOG.info("Iniciando upload");
			s3client.putObject(bucketName, fileName, is, meta);
			LOG.info("Upload finalizado");
			//retorna um objeto URL do java, chama .toURI() para converter para URL
			return s3client.getUrl(bucketName, fileName).toURI();
		} catch (URISyntaxException e) {
			throw new FileException("Erro ao converter URL para URI");
		}
	}
	
		
	

}
