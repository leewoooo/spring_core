package hello.corenoref.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Member {
    private Long id;
    private String name;
    private Grade grade;
}
