package ru.skypro.homework.component;

import org.mapstruct.*;

import ru.skypro.homework.dto.*;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DtoMapper {

    @Mapping(target = "regDate", source = "regDate", dateFormat = "dd.MM.yyyy HH:mm:ss")
    @Mapping(target = "image", expression = "java(user.getAvatar() != null ? \"/users/\" + user.getAvatar().getId() + \"/avatar\" : null)")
    UserRecord toUserDto(User user);

    @Mapping(target = "regDate", source = "regDate", dateFormat = "dd.MM.yyyy HH:mm:ss")
    User toUserEntity(UserRecord userRecord);

    @Mapping(target = "author", expression = "java(ads.getUser() != null ? (int) ads.getUser().getId() : null)")
    @Mapping(target = "image", expression = "java(ads.getImage() != null ? \"/ads/\" + ads.getImage().getId() + \"/image\" : null)")
    @Mapping(target = "pk", source = "id")
    AdsRecord toAdsDto(Ads ads);

    CreateAds toCreateAdsDto(Ads ads);

    Ads toCreateAdsEntity(CreateAds createAds);

    @Mapping(target = "authorFirstName", expression = "java(ads.getUser() != null ? ads.getUser().getFirstName() : null)")
    @Mapping(target = "authorLastName", expression = "java(ads.getUser() != null ? ads.getUser().getLastName() : null)")
    @Mapping(target = "email", expression = "java(ads.getUser() != null ? ads.getUser().getEmail() : null)")
    @Mapping(target = "image", expression = "java(ads.getImage() != null ? \"/ads/\" + ads.getImage().getId() + \"/image\" : null)")
    @Mapping(target = "phone", expression = "java(ads.getUser() != null ? ads.getUser().getPhone() : null)")
    @Mapping(target = "pk", source = "id")
    FullAds toFullAds(Ads ads);

    @Mapping(target = "author", expression = "java(comment.getUser() != null ? (int) comment.getUser().getId() : null)")
    @Mapping(target = "authorImage", expression = "java(comment.getUser().getAvatar() != null ? \"/users/\" + comment.getUser().getAvatar().getId() + \"/avatar\" : null)")
    @Mapping(target = "authorFirstName", expression = "java(comment.getUser() != null ? comment.getUser().getFirstName() : null)")
    @Mapping(target = "createdAt", expression = "java(comment.getCreatedAt() != null ? comment.getCreatedAt().toInstant(java.time.ZoneOffset.of(\"+03:00:00\")).toEpochMilli() : null)")
    @Mapping(target = "pk", source = "id")
    CommentRecord toCommentDto(Comment comment);

    default ResponseWrapperAds toResponseWrapperAds(List<Ads> results){
        return toResponseWrapperAds(results.size(), results);
    }
    ResponseWrapperAds toResponseWrapperAds(int count, List<Ads> results);

    default ResponseWrapperComment toResponseWrapperComment(List<Comment> results){
        return toResponseWrapperComment(results.size(), results);
    }
    ResponseWrapperComment toResponseWrapperComment(int count, List<Comment> results);
}
