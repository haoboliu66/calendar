package com.haoboliu.mapper;

import com.haoboliu.bean.Event;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EventMapper extends MyBatisBaseDao<Event, Integer> {

    List<Event> getEventsByAccountId(String id);

    List<Event> selectByKeyWords(String keywords);
}