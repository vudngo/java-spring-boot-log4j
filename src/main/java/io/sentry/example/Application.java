package io.sentry.example;

import io.sentry.Sentry;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin
@EnableAutoConfiguration
@SpringBootApplication
public class Application {

    private static final Logger logger = LogManager.getLogger("example.Application");

    private static Map<String, Integer> inventory = new HashMap<>();

    private void checkout(List<Item> cart) {
        Map<String, Integer> tempInventory = inventory;

        for (Item item : cart) {
            int currentInventory = tempInventory.get(item.getId());
            if (currentInventory <= 0) {
                throw new RuntimeException("No inventory for " + item.getId());
            }

            tempInventory.put(item.getId(), currentInventory-1);
        }
        inventory = tempInventory;
    }

    @PostMapping(value="/checkout", consumes = "application/json")
    @ResponseBody
    public ResponseEntity checkoutCart(@RequestHeader(name = "X-Session-ID", required = true) String sessionId,
                                       @RequestHeader(name = "X-Transaction-ID", required = true) String transactionId,
                                       @RequestBody Order order) {
        try {
            // set session and transaction id as tags
            ThreadContext.put("session_id", sessionId);
            ThreadContext.put("transaction_id", transactionId);

            // perform checkout
            logger.info("Processing order for: " + order.getEmail());
            checkout(order.getCart());
        } catch (Exception e) {
            // log error + return 500, if exception
            logger.error("Error while checking out", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Checkout error");
        }

        // return 200 if checkout was successful
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

    @RequestMapping("/capture-message")
    @ResponseBody
    String captureMessage() {
        String someLocalVariable = "stack locals";
        ThreadContext.put("customKey1", "customValue1");

        logger.debug("Debug message");
        logger.info("Info message");
        logger.warn("Warn message"); // warning message that will be sent to Sentry
        return "Success";
    }

    @RequestMapping("/handled")
    @ResponseBody
    String handledError() {
        String someLocalVariable = "stack locals";
        try {
            int example = 1 / 0;
        } catch (Exception e) {
            // caught exception that will be sent to Sentry
            logger.error("Caught exception!", e);
        }
        return "Success";
    }

    @RequestMapping("/unhandled")
    @ResponseBody
    String unhandledError() {
        throw new RuntimeException("Unhandled exception!");
    }

    public static void main(String[] args) {
        Sentry.init();
        Sentry.getStoredClient().setServerName("fe1");
        inventory.put("wrench", 0);
        inventory.put("nails", 0);
        inventory.put("hammer", 1);
        SpringApplication.run(Application.class, args);
    }
}
