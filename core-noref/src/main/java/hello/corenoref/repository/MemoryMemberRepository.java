package hello.corenoref.repository;

import hello.corenoref.member.Member;

import java.util.HashMap;
import java.util.Map;

public class MemoryMemberRepository implements MemberRepository{

    private final static Map<Long, Member> memoryDB = new HashMap<>();

    @Override
    public void Save(Member member) {
        memoryDB.put(member.getId(), member);
    }

    @Override
    public Member findById(Long id) {
        return memoryDB.get(id);
    }
}
