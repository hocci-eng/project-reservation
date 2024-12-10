package zerobase.projectreservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.projectreservation.domain.Member;
import zerobase.projectreservation.domain.Reservation;
import zerobase.projectreservation.domain.type.ArriveStatus;
import zerobase.projectreservation.dto.ReservationDto;
import zerobase.projectreservation.exception.impl.NotRegisteredException;
import zerobase.projectreservation.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class ArrivalService {

    private final MemberRepository memberRepository;

    /**
     * 사용자 도착 (키오스크에서 서버에 전달)
     * member의 휴대폰번호를 기반으로 멤버에서 예약조회
     */
    @Transactional
    public ReservationDto markArrivalByPhoneNumber(String phoneNumber) {

        Member member = memberRepository.findMemberByPhoneNumber(phoneNumber)
                .orElseThrow(NotRegisteredException::new);

        Reservation reservation = member.getReservation();
        reservation.setArriveStatus(ArriveStatus.ARRIVED);

        return reservation.toReservationDto();
    }
}
