package org.example;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class App {

    public static final String NASA_API_URI = "https://api.nasa.gov/planetary/apod?api_key=CYNIa7fe2N6eWRgbspE8fYeX3PY4G2yRZzAKKis6";
    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setUserAgent("My Service")
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet request = new HttpGet(NASA_API_URI);
        CloseableHttpResponse response = httpClient.execute(request);

//        Arrays.stream(response.getAllHeaders()).forEach(System.out::println);

        NasaResponse nasaResponse = mapper.readValue(response.getEntity().getContent(), new TypeReference<>() {
        });

        HttpGet requestForNasaUrl = new HttpGet(nasaResponse.getUrl());
        CloseableHttpResponse response1 = httpClient.execute(requestForNasaUrl);

        byte[] bytes = response1.getEntity().getContent().readAllBytes();
        
        String fileName = new File(nasaResponse.getUrl()).getName();

        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(bytes);
        }
    }
}
