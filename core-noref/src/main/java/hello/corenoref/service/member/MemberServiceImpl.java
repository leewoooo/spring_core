package hello.corenoref.service.member;

import hello.corenoref.member.Member;
import hello.corenoref.repository.MemberRepository;

public class MemberServiceImpl implements MemberService {

    // MemberService가 DIP를 위반하고 있다. 추상화에 의존하는 것이 아닌 구현체를 직접 의존하고 있다.
    // OCP 또한 위반되고 있다. MemberRepository의 구현체가 변경되었을 경우 직접 코드를 수정해줘야 한다.
//    private final MemberRepository memberRepository = new MemoryMemberRepository();

    // interface에만 의존을 하고 구현 클래스는 외부에서 주입을 받음으로 DIP를 지킬 수 있다.
    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void join(Member member) {
        memberRepository.Save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

}
