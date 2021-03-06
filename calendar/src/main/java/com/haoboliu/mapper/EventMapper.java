package com.haoboliu.mapper;

import com.haoboliu.bean.Event;
import com.haoboliu.bean.SharedEvent;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public interface EventMapper extends MyBatisBaseDao<Event, Integer> {

    List<Event> getEventsByAccountId(String id);

    List<Event> selectByKeyWords(String accountId, String keywords);

    Integer addEventToReceiver(Integer ownerId, Integer eventId, Integer receiverId);

    List<SharedEvent> getSharedEventByReceiver(String id);
}
