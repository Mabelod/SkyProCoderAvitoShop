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
    @Mapping(target = "image", expression = "java(user.getAvatar() != null ? \"http://localhost:8080/user/\" + user.getAvatar().getId() + \"/avatar\" : null)")
    UserRecord toUserDto(User user);

    @Mapping(target = "regDate", source = "regDate", dateFormat = "dd.MM.yyyy HH:mm:ss")
    User toUserEntity(UserRecord userRecord);

    RegisterReq toRegisterReq(User user);

    @Mapping(target = "author", expression = "java(ads.getUser() != null ? (int) ads.getUser().getId() : null)")
    @Mapping(target = "image", expression = "java(ads.getImage() != null ? \"/ads/\" + ads.getImage().getId() + \"/image\" : null)")
    @Mapping(target = "pk", source = "id")
    AdsRecord toAdsDto(Ads ads);

    CreateAds toCreateAds(Ads ads);

    @Mapping(target = "authorFirstName", expression = "java(ads.getUser() != null ? ads.getUser().getFirstName() : null)")
    @Mapping(target = "authorLastName", expression = "java(ads.getUser() != null ? ads.getUser().getLastName() : null)")
    @Mapping(target = "email", expression = "java(ads.getUser() != null ? ads.getUser().getEmail() : null)")
    @Mapping(target = "image", expression = "java(ads.getImage() != null ? \"/ads/\" + ads.getImage().getId() + \"/image\" : null)")
    @Mapping(target = "phone", expression = "java(ads.getUser() != null ? ads.getUser().getPhone() : null)")
    @Mapping(target = "pk", source = "id")
    FullAds toFullAds(Ads ads);

    @Mapping(target = "author", expression = "java(comment.getUser() != null ? (int) comment.getUser().getId() : null)")
    @Mapping(target = "createdAt", source = "createdAt", dateFormat = "dd.MM.yyyy HH:mm:ss")
    @Mapping(target = "pk", source = "id")
    CommentRecord toCommentDto(Comment comment);

    @Mapping(target = "createdAt", source = "createdAt", dateFormat = "dd.MM.yyyy HH:mm:ss")
    Comment toCommentEntity(CommentRecord commentRecord);

    default ResponseWrapperAds toResponseWrapperAds(List<Ads> results){
        return toResponseWrapperAds(results.size(), results);
    }
    ResponseWrapperAds toResponseWrapperAds(int count, List<Ads> results);

    default ResponseWrapperComment toResponseWrapperComment(List<Comment> results){
        return toResponseWrapperComment(results.size(), results);
    }
    ResponseWrapperComment toResponseWrapperComment(int count, List<Comment> results);
}
