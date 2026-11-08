@param kh.edu.istad.platform.authorization.web.i18n.I18nHelper msg = null
@param String currentLang = "en"
@param kh.edu.istad.platform.authorization.web.dto.UserProfileView user = null
@param String successMessage = null
@param String errorMessage = null
@param String username = null
@param org.springframework.security.web.csrf.CsrfToken _csrf = null

@template.layout(
    title = (msg != null ? msg.get("profileEdit.title") : "Edit Profile"),
    msg = msg,
    currentLang = currentLang,
    username = username,
    activeTab = "edit",
    content = @`
        <div>
            <!-- Navigation Tabs -->
            <div class="tabs">
                <a href="/" class="tab-item">${msg != null ? msg.get("profile.title") : "Overview"}</a>
                <a href="/profile/edit" class="tab-item active">${msg != null ? msg.get("profile.editProfile") : "Edit Profile"}</a>
                <a href="/profile/password" class="tab-item">${msg != null ? msg.get("profile.changePassword") : "Change Password"}</a>
                <a href="/profile/accounts" class="tab-item">${msg != null ? msg.get("profile.linkedAccounts") : "Linked Accounts"}</a>
                <a href="/profile/apps" class="tab-item">${msg != null ? msg.get("profile.connectedApps") : "Connected Apps"}</a>
            </div>

            <div class="card" style="max-width: 650px; margin: 0 auto;">
                <div style="margin-bottom: 1.5rem;">
                    <h2 style="font-size: 1.35rem; font-weight: 700; margin-bottom: 0.35rem;">${msg != null ? msg.get("profileEdit.title") : "Edit Profile"}</h2>
                    <p style="color: var(--text-muted); font-size: 0.9rem;">${msg != null ? msg.get("profileEdit.subtitle") : "Update your personal details"}</p>
                </div>

                @if(successMessage != null)
                    <div class="alert alert-success">
                        ${successMessage}
                    </div>
                @endif

                @if(errorMessage != null)
                    <div class="alert alert-error">
                        ${errorMessage}
                    </div>
                @endif

                <form action="/profile/edit" method="post">
                    @if(_csrf != null)
                        <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}"/>
                    @endif

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                        <div class="form-group">
                            <label class="form-label" for="givenName">${msg != null ? msg.get("profileEdit.givenName") : "First Name"}</label>
                            <input type="text" id="givenName" name="givenName" class="form-control" value="${user != null && user.givenName() != null ? user.givenName() : ""}"/>
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="familyName">${msg != null ? msg.get("profileEdit.familyName") : "Last Name"}</label>
                            <input type="text" id="familyName" name="familyName" class="form-control" value="${user != null && user.familyName() != null ? user.familyName() : ""}"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="nickname">Nickname</label>
                        <input type="text" id="nickname" name="nickname" class="form-control" value="${user != null && user.nickname() != null ? user.nickname() : ""}"/>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="phoneNumber">${msg != null ? msg.get("profileEdit.phoneNumber") : "Phone Number"}</label>
                        <input type="text" id="phoneNumber" name="phoneNumber" class="form-control" placeholder="+85512345678" value="${user != null && user.phoneNumber() != null ? user.phoneNumber() : ""}"/>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="locale">${msg != null ? msg.get("common.language") : "Preferred Language"}</label>
                        <select id="locale" name="locale" class="form-control">
                            <option value="en" selected="${user != null && "en".equals(user.locale())}">English</option>
                            <option value="km" selected="${user != null && "km".equals(user.locale())}">ខ្មែរ (Khmer)</option>
                        </select>
                    </div>

                    <div style="display: flex; justify-content: flex-end; gap: 0.75rem; margin-top: 1.5rem;">
                        <a href="/" class="btn btn-secondary">${msg != null ? msg.get("common.cancel") : "Cancel"}</a>
                        <button type="submit" class="btn btn-primary">${msg != null ? msg.get("common.save") : "Save Changes"}</button>
                    </div>
                </form>
            </div>
        </div>
    `
)
