package com.igloo_club.nungil_v3.service;

import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.domain.events.FcmEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmEventService {
    private final ApplicationEventPublisher publisher;

    public void sendMessageTo(String title, String body, Member member) {
        publisher.publishEvent(new FcmEvent(title, body, member));
    }
}
