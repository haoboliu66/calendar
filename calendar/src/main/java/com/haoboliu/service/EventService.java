package com.haoboliu.service;

import com.haoboliu.bean.Account;
import com.haoboliu.bean.Event;
import com.haoboliu.bean.SharedEvent;
import com.haoboliu.mapper.EventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    @Autowired
    EventMapper eventMapper;

    @Autowired
    AccountService accountService;

    // slf4j
    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    public Event selectByEvent(Event event) {
        int res = 0;
        if (event.getId() == null) {
            // new event
            res = insertEvent(event);
        } else {
            // old event
            res = eventMapper.updateByPrimaryKey(event);
        }
        logger.info("After insert/update operation, event :" + event);

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

    /**
     * add events records with receiver id
     *
     * @param receiverId
     * @param events
     * @return
     */
    public List<SharedEvent> addEventsToReceiver(Integer receiverId, ArrayList<Event> events) {
        logger.info("Start inserting share records");
        ArrayList<Integer> eventIds = new ArrayList<>();
        // collect all shared event id
        for (Event e : events) {
            eventIds.add(e.getId());
        }

        // owner account
        int owner = events.get(0).getAccountId();
        Account ownerAccount = accountService.selectByUserId(owner);
        logger.info("Events from: " + ownerAccount);
        logger.info("eids: " + eventIds);

        ArrayList<SharedEvent> list = new ArrayList<>();
        int res = 0;
        // insert record (ownerId, eventId, receiverId)
        for (int eid : eventIds) {
            res += eventMapper.addEventToReceiver(owner, eid, receiverId);
            Event e = eventMapper.selectByPrimaryKey(eid);
            list.add(new SharedEvent(e,ownerAccount));
        }
        logger.info("record lines inserted: " + res);

        return list;
    }
}
