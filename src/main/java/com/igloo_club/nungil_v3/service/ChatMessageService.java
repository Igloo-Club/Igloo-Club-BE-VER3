package com.igloo_club.nungil_v3.service;

import com.igloo_club.nungil_v3.domain.*;
import com.igloo_club.nungil_v3.dto.ChatMessageResponse;
import com.igloo_club.nungil_v3.dto.ChatRoomCreateResponse;
import com.igloo_club.nungil_v3.dto.ChatRoomDetailResponse;
import com.igloo_club.nungil_v3.dto.ChatRoomListResponse;
import com.igloo_club.nungil_v3.exception.ChatMessageErrorResult;
import com.igloo_club.nungil_v3.exception.ChatRoomErrorResult;
import com.igloo_club.nungil_v3.exception.GeneralException;
import com.igloo_club.nungil_v3.repository.ChatMessageRepository;
import com.igloo_club.nungil_v3.repository.ChatRoomRepository;
import com.igloo_club.nungil_v3.repository.MemberChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;

    private final ChatMessageRepository chatMessageRepository;

    private final PresignedUrlService presignedUrlService;

    private final MemberChatRoomRepository memberChatRoomRepository;

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

        // 본인 혹은 상대방이 나간 채팅방이면 예외 발생
        if (chatRoom.isInactiveChatRoom()) {
            throw new GeneralException(ChatRoomErrorResult.INACTIVE_CHATROOM);
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
        Slice<ChatMessageResponse> messageSlice = getMessageSlice(chatRoom, member, pageRequest);

        // 1-1. 메시지 목록은 스크롤 시에 아래에서 위로 가므로, 가장 최근 채팅이 가장 뒤로 가도록 뒤집음
        List<ChatMessageResponse> reversedContent = new ArrayList<>(messageSlice.getContent());
        Collections.reverse(reversedContent);

        // 1-2. 뒤집은 목록으로 새로운 Slice 생성
        Slice<ChatMessageResponse> reversedMessageSlice = new SliceImpl<>(reversedContent, pageRequest, messageSlice.hasNext());

        // 2. 채팅 상대방 탐색
        Member opponent = getOpponent(chatRoom, member);

        // 3. 채팅방의 상세 정보 반환
        String imageUrl = presignedUrlService.generatePresignedDownloadUrl(opponent.getRepresentativeImageFilename());
        return ChatRoomDetailResponse.create(opponent.getNickname(), imageUrl, chatRoomId, reversedMessageSlice);
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
    public Slice<ChatMessageResponse> getMessageSlice(ChatRoom chatRoom, Member member, PageRequest pageRequest) {

        // 메시지 발행자(member)가 해당 채팅방의 일원이 아니면 예외 발생
        if (isOutsider(chatRoom, member)) {
            throw new GeneralException(ChatRoomErrorResult.NOT_MEMBER);
        }

        // 주어진 채팅방의 메시지들을 pageRequest에 맞추어 조회
        Slice<ChatMessage> messageSlice = chatMessageRepository.findByChatRoom(pageRequest, chatRoom);

        // 메시지들을 DTO 리스트로 변환
        List<ChatMessageResponse> responseList = messageSlice.getContent().stream()
                .map(chatMessage -> {
                    Member sender = chatMessage.getMember();
                    return ChatMessageResponse.create(sender, chatMessage, member.isAuthor(chatMessage));
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
        Slice<ChatRoom> chatRoomSlice = chatRoomRepository.findActiveChatRoomByMember(member, member, pageRequest);

        return chatRoomSlice.map(chatRoom -> {
            Member opponent = getOpponent(chatRoom, member);
            String imageUrl = presignedUrlService.generatePresignedDownloadUrl(opponent.getRepresentativeImageFilename());
            ChatMessage lastMessage = chatMessageRepository.findTop1ByChatRoomOrderByCreatedAtDesc(chatRoom);

            return ChatRoomListResponse.create(chatRoom, lastMessage, opponent, imageUrl);
        });
    }

    /**
     * 주어진 채팅방을 삭제하는 메서드입니다.
     * @param chatRoomId 삭제할 채팅방 id
     * @param member 삭제를 요청한 사용자
     */
    @Transactional
    public void deleteChatRoom(Long chatRoomId, Member member) {
        ChatRoom chatRoom = getChatRoom(chatRoomId);

        // MemberChatRoom 엔티티를 찾지 못한 경우
        MemberChatRoom memberChatRoom = memberChatRoomRepository.findByMemberAndChatRoom(member, chatRoom)
                .orElseThrow(() -> new GeneralException(ChatRoomErrorResult.MEMBER_CHATROOM_NOT_FOUND));

        // 이미 사용자에 의해 삭제 처리가 된 채팅방인 경우
        if (memberChatRoom.isDeleted()) {
            throw new GeneralException(ChatRoomErrorResult.CHATROOM_ALREADY_DELETED);
        }

        memberChatRoom.setAsDeleted();
        memberChatRoomRepository.save(memberChatRoom);
    }


    /**
     * 특정 채팅 메시지를 삭제 상태로 변경하는 메서드입니다.
     * @param chatRoomId 채팅방 id
     * @param chatMessageId 채팅 메시지 id
     * @param member 삭제를 요청한 사용자
     */
    @Transactional
    public ChatMessageResponse deleteMessage(Long chatRoomId, Long chatMessageId, Member member) {
        ChatRoom chatRoom = getChatRoom(chatRoomId);
        if (isOutsider(chatRoom, member)) {
            throw new GeneralException(ChatRoomErrorResult.NOT_MEMBER);
        }

        ChatMessage chatMessage = chatMessageRepository.findTop1ByIdAndChatRoom(chatMessageId, chatRoom)
                .orElseThrow(() -> new GeneralException(ChatMessageErrorResult.CHAT_MESSAGE_NOT_FOUND));

        // 본인이 작성한 메시지만 삭제 가능
        boolean isAuthor = member.isAuthor(chatMessage);
        if (!isAuthor) {
            throw new GeneralException(ChatMessageErrorResult.ONLY_MESSAGE_AUTHOR_CAN_DELETE);
        }

        // DB에 있는 메시지 내용을 '삭제된 메시지입니다.'로 변경
        chatMessage.updateAsDeleted();
        chatMessageRepository.save(chatMessage);

        return ChatMessageResponse.create(chatMessage.getMember(), chatMessage, isAuthor);
    }

    /**
     * 일대일 채팅방을 생성하는 메서드입니다.
     * @param receiver 채팅방 사용자 1
     * @param sender 채팅방 사용자 2
     * @return 채팅방 id를 포함하는 응답 DTO
     */
    @Transactional
    public ChatRoomCreateResponse createChatRoom(Member receiver, Member sender) {

        ChatRoom chatRoom = chatRoomRepository.findChatRoomBetweenMembers(receiver, sender)
                .orElse(ChatRoom.create(receiver, sender));

        createMemberChatRoomIfNotExist(receiver, chatRoom);
        createMemberChatRoomIfNotExist(sender, chatRoom);

        return ChatRoomCreateResponse.create(chatRoomRepository.save(chatRoom));
    }

    private void createMemberChatRoomIfNotExist(Member member, ChatRoom chatRoom) {
        MemberChatRoom memberChatRoom = memberChatRoomRepository.findByMemberAndChatRoom(member, chatRoom)
                .orElse(MemberChatRoom.create(member, chatRoom));
        memberChatRoomRepository.save(memberChatRoom);
    }
}
