package hello.corenoref.service.member;

import hello.corenoref.member.Member;

public interface MemberService {
    void join(Member member);

    Member findMember(Long memberId);
}
