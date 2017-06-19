package my.simple.bruteforce.app.service;

import my.simple.bruteforce.app.utils.RequestSender;
import my.simple.bruteforce.app.utils.RequestSenderThread;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BruteForceService {

    @Autowired
    RequestSender requestSender;

    @Autowired
    RequestSenderThread requestSenderThread;

    private static final Logger log = Logger.getLogger(BruteForceService.class);

    public void doBruteForce() {
        System.out.println("+++++++++++++++++++++++++++НАЧАЛИ БРУТФОРС!!!++++++++++++++++++++++++++++++");
        log.info("+++++++++++++++++++++++++++НАЧАЛИ БРУТФОРС!!!++++++++++++++++++++++++++++++");
        int passwordLength = 1;

        while(true) {
            System.out.println("+++++++++ПРОБУЕМ ПАРОЛИ ИЗ " + passwordLength + " СИМВОЛОВ...+++++++++++++");
            log.info("+++++++++ПРОБУЕМ ПАРОЛИ ИЗ " + passwordLength + " СИМВОЛОВ...+++++++++++++");
            requestSender.sendRequest(passwordLength);
            passwordLength++;
        }

        /*new RequestSenderThread(3).start();
        new RequestSenderThread(4).start();
        new RequestSenderThread(5).start();
        new RequestSenderThread(6).start();
        new RequestSenderThread(7).start();*/

    }

}
