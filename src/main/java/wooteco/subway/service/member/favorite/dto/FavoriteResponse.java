package wooteco.subway.service.member.favorite.dto;

import wooteco.subway.domain.member.favorite.Favorite;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FavoriteResponse {
    private String source;
    private String target;

    private FavoriteResponse() {
    }

    public FavoriteResponse(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getSource(), favorite.getTarget());
    }

    public static List<FavoriteResponse> listOf(Set<Favorite> favorites) {
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}
