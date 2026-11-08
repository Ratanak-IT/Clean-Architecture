@param kh.edu.istad.platform.authorization.web.i18n.I18nHelper msg = null
@param String currentLang = "en"
@param String successMessage = null
@param String errorMessage = null
@param String username = null
@param org.springframework.security.web.csrf.CsrfToken _csrf = null

@template.layout(
    title = (msg != null ? msg.get("changePassword.title") : "Change Password"),
    msg = msg,
    currentLang = currentLang,
    username = username,
    activeTab = "password",
    content = @`
        <div>
            <!-- Navigation Tabs -->
            <div class="tabs">
                <a href="/" class="tab-item">${msg != null ? msg.get("profile.title") : "Overview"}</a>
                <a href="/profile/edit" class="tab-item">${msg != null ? msg.get("profile.editProfile") : "Edit Profile"}</a>
                <a href="/profile/password" class="tab-item active">${msg != null ? msg.get("profile.changePassword") : "Change Password"}</a>
                <a href="/profile/accounts" class="tab-item">${msg != null ? msg.get("profile.linkedAccounts") : "Linked Accounts"}</a>
                <a href="/profile/apps" class="tab-item">${msg != null ? msg.get("profile.connectedApps") : "Connected Apps"}</a>
            </div>

            <div class="card" style="max-width: 550px; margin: 0 auto;">
                <div style="margin-bottom: 1.5rem;">
                    <h2 style="font-size: 1.35rem; font-weight: 700; margin-bottom: 0.35rem;">${msg != null ? msg.get("changePassword.title") : "Change Password"}</h2>
                    <p style="color: var(--text-muted); font-size: 0.9rem;">${msg != null ? msg.get("changePassword.subtitle") : "Ensure your account is using a strong password"}</p>
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

                <form action="/profile/password" method="post">
                    @if(_csrf != null)
                        <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}"/>
                    @endif

                    <div class="form-group">
                        <label class="form-label" for="currentPassword">${msg != null ? msg.get("changePassword.currentPassword") : "Current Password"}</label>
                        <input type="password" id="currentPassword" name="currentPassword" class="form-control" required/>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="newPassword">${msg != null ? msg.get("changePassword.newPassword") : "New Password"}</label>
                        <input type="password" id="newPassword" name="newPassword" class="form-control" minlength="8" required/>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="confirmPassword">${msg != null ? msg.get("changePassword.confirmPassword") : "Confirm New Password"}</label>
                        <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" minlength="8" required/>
                    </div>

                    <div style="display: flex; justify-content: flex-end; gap: 0.75rem; margin-top: 1.5rem;">
                        <a href="/" class="btn btn-secondary">${msg != null ? msg.get("common.cancel") : "Cancel"}</a>
                        <button type="submit" class="btn btn-primary">${msg != null ? msg.get("changePassword.submitButton") : "Update Password"}</button>
                    </div>
                </form>
            </div>
        </div>
    `
)

