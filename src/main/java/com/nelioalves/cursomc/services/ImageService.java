package com.nelioalves.cursomc.services;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nelioalves.cursomc.service.exception.FileException;

//servico responsavel por fornecer funcionalidades de imagem

@Service
public class ImageService {
	//converte para um .jpg
	public BufferedImage getJpgImageFromFile(MultipartFile uploadedFile) {
		//pega a extensao do arquivo
		String ext = FilenameUtils.getExtension(uploadedFile.getOriginalFilename());
		if(!"png".equals(ext) && !"jpg".equals(ext)) {
			throw new FileException("somente imagens PNG e JPG são permitidas");
		}
		try {
			//obtem o BufferedImage a partir de MultipartFile
			BufferedImage img = ImageIO.read(uploadedFile.getInputStream());
			//se for um arquivo png converte em jpg
			if("png".equals(ext)) {
				img = pngToJpg(img);
			}
			return img;
		} catch (IOException e) {
			throw new FileException("Erro ao ler arquivo");
		} 
		
		
		
	}

	public BufferedImage pngToJpg(BufferedImage img) {
		BufferedImage jpgImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		jpgImage.createGraphics().drawImage(img, 0, 0, Color.WHITE, null);
		return jpgImage;
	}
	
	//retorn um inputStream a partir de um BufferedImage
	//o metodo que faz o upload para o S3Amazon recebe um ImputStream
	public InputStream getInputStream(BufferedImage img, String extension) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(img, extension, os);
			return new ByteArrayInputStream(os.toByteArray());
			
		}catch (IOException e) {
			throw new FileException("Erro ao ler arquivo");
		}
	}
	

}
