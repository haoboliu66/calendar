package com.haoboliu.service;

import com.haoboliu.bean.Event;
import com.haoboliu.mapper.EventMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    EventMapper eventMapper;


    public Event selectByEvent(Event event) {
        int res = 0;
        if (event.getId() == null) {
            // new event
            res = insertEvent(event);
        } else {
            // old event
            res = eventMapper.updateByPrimaryKey(event);
        }
        System.out.println("after operation, event :" + event);

        return event;
    }

    public int insertEvent(Event event) {
        return eventMapper.insert(event);
    }

    public List<Event> getEventsByUserId(String id) {
        return eventMapper.getEventsByAccountId(id);
    }

    public int deleteEventByKey(String eventId) {
        return eventMapper.deleteByPrimaryKey(Integer.parseInt(eventId));
    }

    public List<Event> selectByKeyWords(String keywords) {
        return eventMapper.selectByKeyWords(keywords);
    }
}
