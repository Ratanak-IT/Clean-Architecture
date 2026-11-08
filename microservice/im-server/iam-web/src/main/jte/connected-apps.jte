@param kh.edu.istad.platform.authorization.web.i18n.I18nHelper msg = null
@param String currentLang = "en"
@param java.util.List<kh.edu.istad.platform.authorization.web.dto.ConnectedAppView> apps = null
@param String successMessage = null
@param String errorMessage = null
@param String username = null
@param org.springframework.security.web.csrf.CsrfToken _csrf = null

@template.layout(
    title = (msg != null ? msg.get("connectedApps.title") : "Connected Applications"),
    msg = msg,
    currentLang = currentLang,
    username = username,
    activeTab = "apps",
    content = @`
        <div>
            <!-- Navigation Tabs -->
            <div class="tabs">
                <a href="/" class="tab-item">${msg != null ? msg.get("profile.title") : "Overview"}</a>
                <a href="/profile/edit" class="tab-item">${msg != null ? msg.get("profile.editProfile") : "Edit Profile"}</a>
                <a href="/profile/password" class="tab-item">${msg != null ? msg.get("profile.changePassword") : "Change Password"}</a>
                <a href="/profile/accounts" class="tab-item">${msg != null ? msg.get("profile.linkedAccounts") : "Linked Accounts"}</a>
                <a href="/profile/apps" class="tab-item active">${msg != null ? msg.get("profile.connectedApps") : "Connected Apps"}</a>
            </div>

            <div class="card" style="max-width: 750px; margin: 0 auto;">
                <div style="margin-bottom: 1.5rem;">
                    <h2 style="font-size: 1.35rem; font-weight: 700; margin-bottom: 0.35rem;">${msg != null ? msg.get("connectedApps.title") : "Connected Applications"}</h2>
                    <p style="color: var(--text-muted); font-size: 0.9rem;">${msg != null ? msg.get("connectedApps.subtitle") : "Applications authorized to access your data"}</p>
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

                @if(apps != null && !apps.isEmpty())
                    <div style="display: flex; flex-direction: column; gap: 0.85rem;">
                        @for(var app : apps)
                            <div style="display: flex; align-items: center; justify-content: space-between; padding: 1.15rem 1.25rem; background: #f8fafc; border: 1px solid var(--border-color); border-radius: 10px; flex-wrap: wrap; gap: 1rem;">
                                <div>
                                    <div style="display: flex; align-items: center; gap: 0.65rem;">
                                        <h3 style="font-size: 1rem; font-weight: 700; color: var(--text-main);">${app.clientName()}</h3>
                                        <span class="badge" style="font-size: 0.75rem; font-family: monospace;">${app.clientId()}</span>
                                    </div>
                                    <div style="display: flex; flex-wrap: wrap; gap: 0.35rem; margin-top: 0.5rem;">
                                        @if(app.scopes() != null)
                                            @for(var sc : app.scopes())
                                                <span class="badge badge-primary" style="font-size: 0.75rem;">${sc}</span>
                                            @endfor
                                        @endif
                                    </div>
                                </div>

                                <form action="/profile/apps/revoke" method="post" onsubmit="return confirm('${msg != null ? msg.get("connectedApps.confirmRevoke") : "Revoke access for this application?"}');">
                                    @if(_csrf != null)
                                        <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}"/>
                                    @endif
                                    <input type="hidden" name="clientId" value="${app.registeredClientId()}"/>
                                    <button type="submit" class="btn btn-danger" style="font-size: 0.825rem; padding: 0.4rem 0.85rem;">
                                        ${msg != null ? msg.get("connectedApps.revoke") : "Revoke Access"}
                                    </button>
                                </form>
                            </div>
                        @endfor
                    </div>
                @else
                    <div style="text-align: center; padding: 2.5rem 1rem; color: var(--text-dim);">
                        <svg width="40" height="40" fill="none" stroke="currentColor" viewBox="0 0 24 24" style="margin: 0 auto 0.75rem auto; opacity: 0.5;"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/></svg>
                        <p>${msg != null ? msg.get("connectedApps.noApps") : "No applications currently authorized"}</p>
                    </div>
                @endif
            </div>
        </div>
    `
)

