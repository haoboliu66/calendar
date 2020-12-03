package com.haoboliu.controller;


import com.alibaba.fastjson.JSONObject;
import com.haoboliu.bean.Account;
import com.haoboliu.bean.Event;
import com.haoboliu.returnjson.ReturnObject;
import com.haoboliu.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {}, allowCredentials = "true")
public class EventController {

    @Autowired
    private EventService eventService;

    /**
     * add a new event or modify an old event
     * @param event
     * @return
     */
    @RequestMapping(value = "/addEvent", method = RequestMethod.POST)
    public String addEvent(Event event) {
        System.out.println("event: " + event);
        Event res = eventService.selectByEvent(event);
        ReturnObject returnObject = new ReturnObject(res).setCode(200);
        return JSONObject.toJSONString(returnObject);
    }

    /**
     * delete an event by event id
     * @param id
     * @return
     */
    @PostMapping("/deleteEvent")
    public String deleteEvent(String id) {
        System.out.println("del: " + id);
        int res = eventService.deleteEventByKey(id);
        ReturnObject returnObject = new ReturnObject();
        return res > 0? JSONObject.toJSONString(returnObject): JSONObject.toJSONString(returnObject.setCode(-1));
    }

    @PostMapping("/listEvent")
    public String listEvent(String id) {
        System.out.println("account id: " + id);
        List<Event> list = eventService.getEventsByUserId(id);
        list.forEach(System.out::println);
        ReturnObject returnObject = new ReturnObject(list);
        return JSONObject.toJSONString(returnObject);
    }

    @GetMapping("/searchEvent")
    public String searchEvent(String keywords) {
        System.out.println("keywords: " + keywords);
        List<Event> list = eventService.selectByKeyWords(keywords);
        list.forEach(System.out::println);
        ReturnObject returnObject = new ReturnObject(list);
        return JSONObject.toJSONString(returnObject);
    }



}
