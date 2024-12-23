package com.igloo_club.nungil_v3.listener;

import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.domain.events.FcmEvent;
import com.igloo_club.nungil_v3.dto.FCMSendDTO;
import com.igloo_club.nungil_v3.service.FCMService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class FcmEventListener {

    private final FCMService fcmService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(FcmEvent event) {
        Member member = event.getMember();

        try {
            fcmService.sendMessageTo(FCMSendDTO.builder().title(event.getTitle()).body(event.getBody()).build(), member);
        } catch (Exception e) {
            // do nothing
        }
    }
}
