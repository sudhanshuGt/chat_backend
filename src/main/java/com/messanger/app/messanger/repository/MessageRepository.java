package com.messanger.app.messanger.repository;


 
import com.messanger.app.messanger.entity.Message;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

 @Query("SELECT m FROM Message m " +
       "WHERE (m.sender.username = :me AND m.receiver.username = :other) " +
       "   OR (m.sender.username = :other AND m.receiver.username = :me) " +
       "ORDER BY m.timestamp")
List<Message> findChatHistory(
    @Param("me") String me,
    @Param("other") String other);


    @Query(value = """
    SELECT m.* FROM MESSAGE m
    INNER JOIN (
      SELECT
        CASE
          WHEN sender_id = :myId THEN receiver_id
          ELSE sender_id
        END AS chat_partner,
        MAX(timestamp) AS last_time
      FROM MESSAGE
      WHERE sender_id = :myId OR receiver_id = :myId
      GROUP BY chat_partner
    ) grp ON 
      ((m.sender_id = :myId AND m.receiver_id = grp.chat_partner)
       OR (m.receiver_id = :myId AND m.sender_id = grp.chat_partner))
      AND m.timestamp = grp.last_time
    ORDER BY m.timestamp DESC
    """, nativeQuery = true)
List<Message> findLatestPerConversation(@Param("myId") Long myId);

}

