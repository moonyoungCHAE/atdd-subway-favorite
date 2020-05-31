package wooteco.subway.service.member;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;

import java.util.List;

@Service
public class FavoriteService {
    private final MemberRepository memberRepository;

    public FavoriteService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void createFavorite(Member member, FavoriteRequest request) {
        Favorite favorite = request.toFavorite();
        member.addFavorite(favorite);
        memberRepository.save(member);
    }

    public List<FavoriteResponse> findFavorites(Member member) {
        return FavoriteResponse.listOf(member.getFavorites());
    }

    public void deleteFavorite(Member member, FavoriteRequest request) {
        member.removeFavorite(request.toFavorite());
        memberRepository.save(member);
    }
}
