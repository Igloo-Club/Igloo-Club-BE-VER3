package com.igloo_club.nungil_v3.service;

import com.igloo_club.nungil_v3.domain.ChatDTO;
import com.igloo_club.nungil_v3.domain.ChatMessage;
import com.igloo_club.nungil_v3.domain.ChatRoom;
import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.dto.ChatMessageListResponse;
import com.igloo_club.nungil_v3.dto.ChatRoomDetailResponse;
import com.igloo_club.nungil_v3.dto.ChatRoomListResponse;
import com.igloo_club.nungil_v3.exception.ChatRoomErrorResult;
import com.igloo_club.nungil_v3.exception.GeneralException;
import com.igloo_club.nungil_v3.repository.ChatMessageRepository;
import com.igloo_club.nungil_v3.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;

    private final ChatMessageRepository chatMessageRepository;

    private final PresignedUrlService presignedUrlService;

    /**
     * 채팅 메시지를 데이터베이스에 저장하는 메서드입니다.
     * @param chatDTO 발행된 채팅 메시지 DTO
     * @param member 메시지 발행자 정보
     * @return 기존의 발행된 메시지에서 데이터가 추가된 DTO
     */
    @Transactional
    public ChatDTO save(ChatDTO chatDTO, Member member) {

        ChatRoom chatRoom = getChatRoom(chatDTO.getChatRoomId());

        // 메시지 발행자(member)가 해당 채팅방의 일원이 아니면 예외 발생
        if (isOutsider(chatRoom, member)) {
            throw new GeneralException(ChatRoomErrorResult.NOT_MEMBER);
        }

        // 데이터베이스에 채팅 메시지 저장
        ChatMessage chatMessage = ChatMessage.create(chatRoom, member, chatDTO.getContent());
        chatMessageRepository.save(chatMessage);

        return ChatDTO.of(chatDTO.getChatRoomId(), member, chatMessage);
    }

    /**
     * 채팅방의 메시지 목록과 상대방 정보를 반환하는 메서드이다.
     * @param chatRoomId 채팅방 ID
     * @param member 조회를 요청한 회원 엔티티
     * @param pageRequest 조회되는 페이지 번호, 갯수, 정렬 방식(최근순)
     * @return 채팅방 상세 정보
     */
    public ChatRoomDetailResponse getChatRoomDetail(Long chatRoomId, Member member, PageRequest pageRequest) {

        ChatRoom chatRoom = getChatRoom(chatRoomId);

        // 1. 채팅방의 메시지 목록을 최근순으로 조회
        Slice<ChatMessageListResponse> messageSlice = getMessageSlice(chatRoom, member, pageRequest);

        // 1-1. 가장 최근 채팅이 가장 뒤로 가도록 뒤집음
        List<ChatMessageListResponse> reversedContent = new ArrayList<>(messageSlice.getContent());
        Collections.reverse(reversedContent);

        // 뒤집은 목록으로 새로운 Slice 생성
        Slice<ChatMessageListResponse> reversedMessageSlice = new SliceImpl<>(reversedContent, pageRequest, messageSlice.hasNext());

        // 2. 채팅 상대방 탐색
        Member opponent = getOpponent(chatRoom, member);

        // 3. 채팅방의 상세 정보 반환
        return ChatRoomDetailResponse.create(member, opponent, reversedMessageSlice);
    }

    private Member getOpponent(ChatRoom chatRoom, Member member) {
        return chatRoom.getSender().equals(member) ? chatRoom.getReceiver() : chatRoom.getSender();
    }

    /**
     * 주어진 채팅방의 메시지 목록을 Slice 형식으로 조회하는 메서드입니다.
     * @param chatRoom 채팅방 엔티티
     * @param member 조회를 요청한 회원의 엔티티
     * @param pageRequest 조회되는 페이지 번호, 갯수, 정렬 방식(최근순)
     * @return Slice 형식의 채팅 메시지 목록
     */
    public Slice<ChatMessageListResponse> getMessageSlice(ChatRoom chatRoom, Member member, PageRequest pageRequest) {

        // 메시지 발행자(member)가 해당 채팅방의 일원이 아니면 예외 발생
        if (isOutsider(chatRoom, member)) {
            throw new GeneralException(ChatRoomErrorResult.NOT_MEMBER);
        }

        // 주어진 채팅방의 메시지들을 pageRequest에 맞추어 조회
        Slice<ChatMessage> messageSlice = chatMessageRepository.findByChatRoom(pageRequest, chatRoom);

        // 메시지들을 DTO 리스트로 변환
        List<ChatMessageListResponse> responseList = messageSlice.getContent().stream()
                .map(chatMessage -> {
                    Member sender = chatMessage.getMember();
                    Boolean isSender = member.getId().equals(sender.getId());

                    return ChatMessageListResponse.create(sender, chatMessage, isSender);
                }).collect(Collectors.toList());

        // 변환된 DTO 리스트와 함께 새로운 Slice 객체를 생성하여 반환
        return new SliceImpl<>(responseList, pageRequest, messageSlice.hasNext());
    }

    private ChatRoom getChatRoom(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new GeneralException(ChatRoomErrorResult.CHAT_ROOM_NOT_FOUND));
    }

    /**
     * 사용자가 주어진 채팅방의 속하지 않은 외부인인지 확인한다.
     * @param chatRoom 채팅방
     * @param member 확인할 사용자
     * @return 채팅방에 속해있지 않으면 true 반환
     */
    private boolean isOutsider(ChatRoom chatRoom, Member member) {
        Long memberId = member.getId();
        return !memberId.equals(chatRoom.getReceiver().getId()) && !memberId.equals(chatRoom.getSender().getId());
    }

    /**
     * 사용자의 채팅방 목록을 Slice 형식으로 조회하는 메서드입니다.
     * @param member 조회를 요청한 회원의 엔티티
     * @param pageRequest 조회되는 페이지 번호, 갯수, 정렬 방식(최근순)
     * @return Slice 형식의 채팅 메시지 목록
     */
    public Slice<ChatRoomListResponse> getChatRoomSlice(Member member, PageRequest pageRequest){
        Slice<ChatRoom> chatRoomSlice = chatRoomRepository.findBySenderOrReceiver(member, member, pageRequest);

        return chatRoomSlice.map(chatRoom -> {
            Member opponent = getOpponent(chatRoom, member);
            String imageUrl = presignedUrlService.generatePresignedDownloadUrl(opponent.getRepresentativeImageFilename());
            ChatMessage lastMessage = chatMessageRepository.findTop1ByChatRoomOrderByCreatedAtDesc(chatRoom);

            return ChatRoomListResponse.create(chatRoom, lastMessage, opponent, imageUrl);
        });
    }


}
