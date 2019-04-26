package com.nelioalves.cursomc.services;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
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
	
	//função para "cropar" uma imagem para que fique quadrada
	//retorna uma imagem recortada
	public BufferedImage cropSquare(BufferedImage sourceImg) {
		//descobri o minimo, se é a largura ou a altura
		int min = (sourceImg.getHeight() <= sourceImg.getWidth()) ? sourceImg.getHeight() : sourceImg.getWidth();
		return Scalr.crop(sourceImg,
				(sourceImg.getWidth()/2) - (min/2),//metade da largura menos a metade o minimo
				(sourceImg.getHeight()/2) - (min/2),//metade da altura menos a metade o minimo
				min,//quanto quer recortar na largura
				min);//quanto quer recortar na altura
	}
	
	//função para redimensionar uma imagem
	//size tamanho que quer que seja recortada
	// Scalr.Method.ULTRA_QUALITY evitar a perda de qualidade
	public BufferedImage resize(BufferedImage sourceImg, int size) {
		return Scalr.resize(sourceImg, Scalr.Method.ULTRA_QUALITY, size);
	}
	
	
	

}
