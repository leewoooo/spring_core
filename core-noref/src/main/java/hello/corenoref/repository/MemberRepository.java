package hello.corenoref.repository;

import hello.corenoref.member.Member;

public interface MemberRepository {
    void Save(Member member);

    Member findById(Long id);
}
