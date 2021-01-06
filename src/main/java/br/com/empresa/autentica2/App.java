package br.com.empresa.autentica2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


import com.mifmif.common.regex.Generex;//com.github.mifmif

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )  {
    	//depois de autenticar é necessário guardar o token
    	Auth auth = new Auth();
    	String token = auth.geraToken();
		System.out.println("access_token="+token);
    	//fazer cobrança
		try {
			Generex generex = new Generex("[a-zA-Z0-9]{26,35}");
			String txid = generex.random();			
			URL url = new URL ("https://api-pix-h.gerencianet.com.br/v2/cob/"+txid);
			String payload="{\r\n  \"calendario\": {\r\n    \"expiracao\": 3600\r\n  },\r\n  \"devedor\": {\r\n    \"cpf\": \"10567056635\",\r\n    \"nome\": \"Gorbadock Oldbuck\"\r\n  },\r\n  \"valor\": {\r\n    \"original\": \"0.01\"\r\n  },\r\n  \"chave\": \"2c5c7441-a91e-4982-8c25-6105581e18ae\",\r\n  \"solicitacaoPagador\": \"Cobrança dos serviços prestados.\"\r\n}";
			HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
	        conn.setDoOutput(true);
	        conn.setRequestMethod("PUT");
	        conn.setRequestProperty("Content-Type", "application/json");
	        conn.setRequestProperty("Authorization", "Bearer "+ token);
	        
	        OutputStream  os = conn.getOutputStream();
	        os.write(payload.getBytes());
	        os.flush(); 
	        
	        InputStreamReader reader = new InputStreamReader(conn.getInputStream());
	        BufferedReader br = new BufferedReader(reader);

	        String response;
	        System.out.println("Envio "+payload);
	        while ((response = br.readLine()) != null) {
	          System.out.println("Resposta "+response);
	        }
	        
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Erro na cobrança");
		}
        
    }
}
