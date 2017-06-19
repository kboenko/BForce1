package my.simple.bruteforce.app.utils;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

@Service
public class RequestSenderThread extends Thread{

    private int length;
    private static HttpURLConnection httpConn;
    private static final String wrongAnswer="WRONG =(";
    private static final Logger log = Logger.getLogger(RequestSenderThread.class);

    public RequestSenderThread() {

    }

    public RequestSenderThread(int length) {
        this.length = length;
    }

    public interface PermuteCallback{
        void handle(Object[] snapshot);
    }

    private void permute(Object[] symbols, int permuteLength, PermuteCallback callback) {
        int n = symbols.length;

        int[] indexes = new int[permuteLength];
        int total = (int) Math.pow(n, permuteLength);

        Object[] snapshot = new Object[permuteLength];
        while (total-- > 0) {
            for (int i = 0; i < permuteLength; i++){
                snapshot[i] = symbols[indexes[i]];
            }
            callback.handle(snapshot);

            for (int i = 0; i < permuteLength; i++) {
                if (indexes[i] >= n - 1) {
                    indexes[i] = 0;
                } else {
                    indexes[i]++;
                    break;
                }
            }
        }
    }

    private static HttpURLConnection sendPostRequest(String requestURL, Map<String, String> params) throws IOException {
        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);

        httpConn.setDoInput(true);

        StringBuffer requestParams = new StringBuffer();

        if (params != null && params.size() > 0) {

            httpConn.setDoOutput(true);

            Iterator<String> paramIterator = params.keySet().iterator();
            while (paramIterator.hasNext()) {
                String key = paramIterator.next();
                String value = params.get(key);
                requestParams.append(URLEncoder.encode(key, "UTF-8"));
                requestParams.append("=").append(URLEncoder.encode(value, "UTF-8"));
            }

            OutputStreamWriter writer = new OutputStreamWriter(
                    httpConn.getOutputStream());
            writer.write(requestParams.toString());
            writer.flush();
        }

        return httpConn;
    }

    private static synchronized String[] readMultipleLinesRespone() throws IOException {
        InputStream inputStream = null;
        if (httpConn != null) {
            inputStream = httpConn.getInputStream();
        } else {
            throw new IOException("Connection is not established.");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        List<String> response = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            response.add(line);
        }
        reader.close();

        return response.toArray(new String[0]);
    }

    private static void pritnResponse(String[] response, String password) {
        for (String str : response) {
            log.debug(str);
        }
    }

    private static boolean checkResponse(String[] response) {
        for (String str : response) {
            if (str.contains(wrongAnswer))
                return true;
        }
        return false;
    }

    public void sendRequest(int n) {
        Object[] chars = { '1', '2', '3', '4' , '5', '6', '7', '8', '9', '0'};
        Map<String, String> params = new HashMap<>();
        String requestURL = "http://www.rollshop.co.il/test.php";
        PermuteCallback callback = new PermuteCallback() {

            @Override
            public void handle(Object[] snapshot) {
                String password = "";
                for(int i = 0; i < snapshot.length; i++){
                    password = password + snapshot[i].toString();
                }
                System.out.println();
                params.put("code", password);
                try {
                    log.debug("++++++++++++++++++++++Пробуем заслать запрос на проверку комбинации: " + password);
                    sendPostRequest(requestURL, params);
                    String[] response = readMultipleLinesRespone();
                    log.debug("+++++++++++++++++++Получили ответ на комбинацию " + password);
                    pritnResponse(response, password);
                    if(!checkResponse(response)) {
                        System.out.println("Нашли пароль!");
                        log.debug("Right password: " + password);
                        pritnResponse(response, password);
                        log.debug("Answer (wiki link) is: " + response[7]);
                        log.debug("End of password bruteforcing.");
                        System.exit(0);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        permute(chars, n, callback);
    }

    public void run() {
        log.info("+++++++++ПРОБУЕМ ПАРОЛИ ИЗ " + length + " СИМВОЛОВ...+++++++++++++");
        sendRequest(length);
    }

}

