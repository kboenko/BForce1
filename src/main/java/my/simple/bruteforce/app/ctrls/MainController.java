package my.simple.bruteforce.app.ctrls;

import my.simple.bruteforce.app.service.BruteForceService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {

    @Autowired
    BruteForceService bruteForceService;

    private static final Logger log = Logger.getLogger(MainController.class);

    @RequestMapping("/")
    public String home() {
        return "index";
    }

    @RequestMapping(value = "/bf", method = RequestMethod.GET)
    public void bruteForce() {
        log.info("+++++++++++++++++++++++++++НАЧИНАЕМ БРУТФОРС!!!++++++++++++++++++++++++++++++");
        bruteForceService.doBruteForce();
    }
}
