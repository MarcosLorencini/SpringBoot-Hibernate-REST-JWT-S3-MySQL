package com.nelioalves.cursomc.services;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;


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
	public void uploadFile(String localFilePath) {
		try {
			File file = new File(localFilePath);
			LOG.info("Iniciando upload");
			s3client.putObject(new PutObjectRequest(bucketName, "teste.jpg", file));
			LOG.info("Upload finalizado");
		}catch(AmazonServiceException e) {
			LOG.info("AmazonServiceException: "+e.getErrorMessage());
			LOG.info("Status code: "+e.getErrorCode());
		}catch(AmazonClientException e) {
			LOG.info("AmazonClientException: "+e.getMessage());
			
		}
		
	}

}
