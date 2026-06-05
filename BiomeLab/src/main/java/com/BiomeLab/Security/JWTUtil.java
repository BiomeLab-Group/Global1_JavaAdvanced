package com.BiomeLab.Security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

@Component
public class JWTUtil {
	
	//chave pública
	private final SecretKey CHAVE = Jwts.SIG.HS256.key().build();
	
	//geração de token
	public String gerarToken(String username, Integer duracao) {
		
		Date data_atual = new Date();
		
		JwtBuilder builder = Jwts.builder()
								 .subject(username)
								 .issuedAt(data_atual)
								 .expiration(
								  new Date(data_atual.getTime() + (1000 * 60 * 60 * duracao)))
								 .signWith(CHAVE);
								 
		return builder.compact();
	}
	
	//extrair um usuário de um token
	public String extrairUsername(String token) {
		
		try {
			JwtParser parser = Jwts.parser().verifyWith(CHAVE).build();
			
			return parser.parseSignedClaims(token).getPayload().getSubject();
		} catch(Exception e) {
			return null;
		}
		
	}
	
	//validar token (com base na chave pública)
	public boolean validarToken(String token) {
		try {
			JwtParser parser = Jwts.parser().verifyWith(CHAVE).build();
			parser.parseSignedClaims(token);
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	
}
