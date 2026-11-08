package kh.edu.istad.platform.authorization.domain.dto.user.command;

import java.util.UUID;

public record UpdateUserProfileCommand(
        UUID userUuid,
        String givenName,
        String familyName,
        String nickname,
        String phoneNumber,
        String locale
) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID userUuid;
        private String givenName;
        private String familyName;
        private String nickname;
        private String phoneNumber;
        private String locale;

        public Builder userUuid(UUID userUuid) {
            this.userUuid = userUuid;
            return this;
        }

        public Builder givenName(String givenName) {
            this.givenName = givenName;
            return this;
        }

        public Builder familyName(String familyName) {
            this.familyName = familyName;
            return this;
        }

        public Builder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder locale(String locale) {
            this.locale = locale;
            return this;
        }

        public UpdateUserProfileCommand build() {
            return new UpdateUserProfileCommand(userUuid, givenName, familyName, nickname, phoneNumber, locale);
        }
    }
}
