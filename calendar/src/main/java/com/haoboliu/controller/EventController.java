package com.haoboliu.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haoboliu.bean.Account;
import com.haoboliu.bean.Event;
import com.haoboliu.bean.SharedEvent;
import com.haoboliu.returnjson.ReturnObject;
import com.haoboliu.service.AccountService;
import com.haoboliu.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/event")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {}, allowCredentials = "true")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private AccountService accountService;

    // slf4j
    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    /**
     * add a new event or modify an old event
     *
     * @param event
     * @return
     */
    @RequestMapping(value = "/addEvent", method = RequestMethod.POST)
    public String addEvent(Event event) {
        logger.info("New Event: " + event);
        Event res = eventService.selectByEvent(event);
        ReturnObject returnObject = new ReturnObject(res).setCode(200);
        return JSONObject.toJSONString(returnObject);
    }

    /**
     * delete an event by event id
     *
     * @param id
     * @return
     */
    @PostMapping("/deleteEvent")
    public String deleteEvent(String id) {
        logger.info("del event id: " + id);
        int res = eventService.deleteEventByKey(id);
        ReturnObject returnObject = new ReturnObject();
        return res > 0 ? JSONObject.toJSONString(returnObject) : JSONObject.toJSONString(returnObject.setCode(-1));
    }

    @PostMapping("/listEvent")
    public String listEvent(String id) {
        logger.info("List Events From Account: " + id);
        List<Event> list = eventService.getEventsByUserId(id);
        ReturnObject returnObject = new ReturnObject(list);
        return JSONObject.toJSONString(returnObject);
    }

    @GetMapping("/searchEvent")
    public String searchEvent(@RequestParam("keywords") String keywords, @RequestParam("accountId") String accountId) {
        logger.info("AccountId: " + accountId);
        logger.info("Keywords: " + keywords);
        List<Event> list = eventService.selectByKeyWords(accountId, keywords);
        ReturnObject returnObject = new ReturnObject(list);
        return JSONObject.toJSONString(returnObject);
    }

    /**
     * share events to a receiver
     *
     * @param map
     * @return
     */
    @PostMapping("/shareEvent")
    public String shareEvent(@RequestBody Map<String, Object> map) {
        logger.info("Share events!");
        Assert.isInstanceOf(String.class, map.get("receiver"), "Incorrect type");
        String receiverMsg = (String) map.get("receiver");
        logger.info("receiverMsg: " + receiverMsg);
        logger.info("Convert Entity Type in event list Started");
        ArrayList<Event> list = (ArrayList<Event>) (map.get("events"));
        ArrayList<Event> events = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String str = JSONObject.toJSONString(list.get(i));
            Event e = JSONObject.parseObject(str, Event.class);
            events.add(e);
        }
        logger.info("Covert Entity Type Done");
       // check validity of receiver
        Account account = validateReceiver(receiverMsg);
        if (account == null) {
            ReturnObject returnObject = new ReturnObject();
            return JSONObject.toJSONString(returnObject.setCode(-1).setMessage("No such receiver"));
        }
        // receiver exists
        logger.info("Receiver Validated");
        Integer receiverId = account.getId();
        List<SharedEvent> sharedEvents = eventService.addEventsToReceiver(receiverId, events);
        ReturnObject returnObject = new ReturnObject(sharedEvents);
        return JSONObject.toJSONString(returnObject.setMessage("Share Calendar Success"));
    }

    @PostMapping("/getSharedEvent")
    public String getSharedEvent(@RequestParam String id) {
        logger.info("Receiver Id: " + id);
        List<SharedEvent> sharedEvents = eventService.getSharedEventByReceiver(id);
        ReturnObject returnObject = new ReturnObject(sharedEvents);
        logger.info("sharedEvent Size: " + sharedEvents.size());
        return JSONObject.toJSONString(returnObject);
    }

    /**
     * check if such receiver exits
     *
     * @param receiver can be either username or email
     * @return
     */
    private Account validateReceiver(String receiver) {
        return accountService.selectByUsernameAndEmail(receiver, receiver);
    }

}
