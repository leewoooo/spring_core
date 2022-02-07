package hello.corenoref.service.member;

import hello.corenoref.member.Member;
import hello.corenoref.repository.MemberRepository;
import hello.corenoref.repository.MemoryMemberRepository;

public class MemberServiceImpl implements MemberService {

    // MemberService가 DIP를 위반하고 있다. 추상화에 의존하는 것이 아닌 구현체를 직접 의존하고 있다.
    // OCP 또한 위반되고 있다. MemberRepository의 구현체가 변경되었을 경우 직접 코드를 수정해줘야 한다.
    private final MemberRepository memberRepository = new MemoryMemberRepository();

    @Override
    public void join(Member member) {
        memberRepository.Save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

}
