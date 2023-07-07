package com.courses;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GatosService {

    public static void verGatos() throws IOException {
        // 1. Vamos a traer los datos de la API
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/images/search")
                .get()
                .build();
        Response response = client.newCall(request).execute();

        String jsonResponse = response.body().string();

        // Quitar corchetes
        jsonResponse = jsonResponse.substring(1, jsonResponse.length());
        jsonResponse = jsonResponse.substring(0, jsonResponse.length() - 1);

        // Crear objecto de la clase Gson
        Gson gson = new Gson();
        Gatos gatos = gson.fromJson(jsonResponse, Gatos.class);

        // Redimensionar la imagen
        BufferedImage image = null;
        try {
            URL url = new URL(gatos.getUrl());
            image = ImageIO.read(url);

            ImageIcon fondoGato = new ImageIcon(image);

            if (fondoGato.getIconWidth() > 800) {
                Image fondo = fondoGato.getImage();
                Image modificada = fondo.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                fondoGato = new ImageIcon(modificada);
            }

            String menu = "Opciones: \n" +
                    "1. Ver otra imagen.\n" +
                    "2. Favorito.\n" +
                    "3. Volver.";

            String[] botones = { "ver otra imagen", "favorito", "volver" };
            String idGato = gatos.getId();
            String opcion = (String) JOptionPane.showInputDialog(null, menu, idGato, JOptionPane.INFORMATION_MESSAGE,
                    fondoGato, botones, botones[0]);

            int seleccion = -1;
            for (int i = 0; i < botones.length; i++) {
                if (opcion.equals(botones[i])) {
                    seleccion = i;
                }

                switch (seleccion) {
                    case 0:
                        verGatos();
                        break;
                    case 1:
                        favoritoGato(gatos);
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void favoritoGato(Gatos gato) throws IOException {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n    \"image_id\" : \"" + gato.getId() + "\"\r\n}");
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites")
                    .method("POST", body)
                    .addHeader("x-api-key", gato.getApiKey())
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
