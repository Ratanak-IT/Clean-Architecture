@param kh.edu.istad.platform.authorization.web.i18n.I18nHelper msg = null
@param String currentLang = "en"
@param java.util.List<kh.edu.istad.platform.authorization.domain.dto.identity.result.IdentityResult> identities = null
@param String successMessage = null
@param String errorMessage = null
@param String username = null
@param org.springframework.security.web.csrf.CsrfToken _csrf = null

@template.layout(
    title = (msg != null ? msg.get("linkedAccounts.title") : "Linked Accounts"),
    msg = msg,
    currentLang = currentLang,
    username = username,
    activeTab = "accounts",
    content = @`
        <div>
            <!-- Navigation Tabs -->
            <div class="tabs">
                <a href="/" class="tab-item">${msg != null ? msg.get("profile.title") : "Overview"}</a>
                <a href="/profile/edit" class="tab-item">${msg != null ? msg.get("profile.editProfile") : "Edit Profile"}</a>
                <a href="/profile/password" class="tab-item">${msg != null ? msg.get("profile.changePassword") : "Change Password"}</a>
                <a href="/profile/accounts" class="tab-item active">${msg != null ? msg.get("profile.linkedAccounts") : "Linked Accounts"}</a>
                <a href="/profile/apps" class="tab-item">${msg != null ? msg.get("profile.connectedApps") : "Connected Apps"}</a>
            </div>

            <div class="card" style="max-width: 750px; margin: 0 auto;">
                <div style="margin-bottom: 1.5rem;">
                    <h2 style="font-size: 1.35rem; font-weight: 700; margin-bottom: 0.35rem;">${msg != null ? msg.get("linkedAccounts.title") : "Linked Accounts"}</h2>
                    <p style="color: var(--text-muted); font-size: 0.9rem;">${msg != null ? msg.get("linkedAccounts.subtitle") : "Manage external login providers linked to your account"}</p>
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

                @if(identities != null && !identities.isEmpty())
                    <div style="display: flex; flex-direction: column; gap: 0.85rem;">
                        @for(var ident : identities)
                            <div style="display: flex; align-items: center; justify-content: space-between; padding: 1rem 1.25rem; background: #f8fafc; border: 1px solid var(--border-color); border-radius: 10px;">
                                <div style="display: flex; align-items: center; gap: 1rem;">
                                    <div style="width: 40px; height: 40px; border-radius: 8px; background: #eef2ff; display: flex; align-items: center; justify-content: center; font-weight: bold; text-transform: uppercase; color: #4f46e5;">
                                        ${ident.provider() != null && !ident.provider().isEmpty() ? ident.provider().substring(0, 1).toUpperCase() : "P"}
                                    </div>
                                    <div>
                                        <div style="font-weight: 600; font-size: 0.95rem; text-transform: capitalize;">
                                            ${ident.provider()}
                                        </div>
                                        <div style="color: var(--text-muted); font-size: 0.825rem; margin-top: 0.15rem;">
                                            ${ident.displayName() != null ? ident.displayName() : (ident.email() != null ? ident.email() : "Connected")}
                                        </div>
                                    </div>
                                </div>

                                <form action="/profile/accounts/${String.valueOf(ident.uuid())}/unlink" method="post" onsubmit="return confirm('${msg != null ? msg.get("linkedAccounts.confirmUnlink") : "Are you sure you want to unlink this account?"}');">
                                    @if(_csrf != null)
                                        <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}"/>
                                    @endif
                                    <button type="submit" class="btn btn-danger" style="font-size: 0.825rem; padding: 0.4rem 0.85rem;">
                                        ${msg != null ? msg.get("linkedAccounts.unlink") : "Unlink"}
                                    </button>
                                </form>
                            </div>
                        @endfor
                    </div>
                @else
                    <div style="text-align: center; padding: 2.5rem 1rem; color: var(--text-dim);">
                        <p>${msg != null ? msg.get("linkedAccounts.noAccounts") : "No external accounts linked"}</p>
                    </div>
                @endif

                <div class="divider" style="margin-top: 2rem;">
                    <span>Connect More Providers</span>
                </div>

                <div style="display: flex; gap: 0.75rem; flex-wrap: wrap; justify-content: center;">
                    <a href="/oauth2/authorization/google" class="btn btn-secondary" style="font-size: 0.85rem;">
                        Google
                    </a>
                    <a href="/oauth2/authorization/github" class="btn btn-secondary" style="font-size: 0.85rem;">
                        GitHub
                    </a>
                    <a href="/oauth2/authorization/facebook" class="btn btn-secondary" style="font-size: 0.85rem;">
                        Facebook
                    </a>
                </div>
            </div>
        </div>
    `
)

