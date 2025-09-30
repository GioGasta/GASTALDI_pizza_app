import com.google.gson.Gson;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App
{
    public OkHttpClient client;

    public App(){
        client = new OkHttpClient();
    }

    public void doGet() throws IOException
    {
        Request request = new Request.Builder().url("https://crudcrud.com/api/5a052ff685f6421a940a96b1a03da3b8/pizze").build();

        try (Response response = client.newCall(request).execute())
        {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            Headers responseHeaders = response.headers();
            for (int i = 0; i < responseHeaders.size(); i++)
            {
                System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
            }

            Gson gson = new Gson();

            Pizza[] pizze = gson.fromJson(response.body().string(),  Pizza[].class);

            for (Pizza pizza : pizze){
                System.out.println(pizza);
            }
        }
    }

    public void run(){
        System.out.println("Welcome to the Pizza App!");

        try  {
            doGet();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
